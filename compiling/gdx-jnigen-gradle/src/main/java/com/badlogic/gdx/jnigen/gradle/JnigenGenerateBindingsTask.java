package com.badlogic.gdx.jnigen.gradle;

import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.generator.ParseTarget;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.InvalidUserCodeException;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.repositories.IvyArtifactRepository;
import org.gradle.api.file.Directory;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.RelativePath;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.Sync;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.TaskProvider;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class JnigenGenerateBindingsTask extends DefaultTask {

    private static final String ZIG_VERSION = "0.16.0";
    private static final String ZIG_MODULE = "zig-x86_64-windows";
    private static final String ZIG_REPOSITORY_URL = "https://ziglang.org/download";
    private static final String ZIG_DEPENDENCY = "org.ziglang:" + ZIG_MODULE + ":" + ZIG_VERSION + "@zip";
    private static final String ZIG_LIB_PREFIX = ZIG_MODULE + "-" + ZIG_VERSION + "/lib/";

    private final JnigenBindingGeneratorExtension generator;
    private final List<BuildTarget> buildTargets;
    private final Configuration configuration;
    private final Provider<Directory> sysrootDir;

    @Inject
    public JnigenGenerateBindingsTask(JnigenBindingGeneratorExtension generator, List<BuildTarget> buildTargets) {
        this.generator = generator;
        this.buildTargets = buildTargets;

        setGroup("jnigen");

        setDescription("Generates java jnigen binding code.");

        Configuration llvmConfiguration = getProject().getConfigurations().create("generatorLLVM", conf -> {
            conf.setVisible(false);
            conf.setCanBeResolved(true);
            conf.setCanBeConsumed(false);
            conf.setDescription("LLVM dependencies for generator");
        });

        getProject().getDependencies().add("generatorLLVM", "org.bytedeco:llvm-platform:19.1.3-1.5.11");

        this.configuration = llvmConfiguration;

        Project project = getProject();
        try {
            project.getRepositories().ivy(repo -> {
                repo.setName("ziglang");
                repo.setUrl(ZIG_REPOSITORY_URL);
                repo.patternLayout(layout -> layout.artifact("[revision]/[module]-[revision].[ext]"));
                repo.metadataSources(IvyArtifactRepository.MetadataSources::artifact);
                // Only ever ask ziglang.org for the zig distribution, never for other dependencies.
                repo.content(content -> content.includeGroup("org.ziglang"));
            });
        } catch (InvalidUserCodeException e) {
            getLogger().info("jnigen: project repositories are managed in settings; declare the ziglang ivy repository there (see the error printed if the Zig headers cannot be resolved)", e);
        }

        Configuration sysrootConfiguration = project.getConfigurations().create("generatorSysroot", conf -> {
            conf.setVisible(false);
            conf.setCanBeResolved(true);
            conf.setCanBeConsumed(false);
            conf.setDescription("Zig distribution providing the libc headers of every jnigen target for the generator");
        });

        project.getDependencies().add("generatorSysroot", ZIG_DEPENDENCY);

        sysrootDir = project.getLayout().getBuildDirectory().dir("jnigen/zig-sysroot");
        TaskProvider<Sync> extractSysroot = project.getTasks().register("jnigenExtractSysroot", Sync.class, sync -> {
            sync.setGroup("jnigen");
            sync.setDescription("Unpacks the Zig libc headers the jnigen generator parses against.");
            sync.from((Callable<Object>) () -> project.zipTree(sysrootConfiguration.getSingleFile()));
            sync.include(ZIG_LIB_PREFIX + "include/**", ZIG_LIB_PREFIX + "libc/include/**");
            sync.eachFile(file -> file.setPath(file.getPath().substring(ZIG_LIB_PREFIX.length())));
            sync.setIncludeEmptyDirs(false);
            sync.into(sysrootDir);
        });
        dependsOn(extractSysroot);
    }

    private static RelativePath dropFirstSegments(RelativePath path, int count) {
        String[] segments = path.getSegments();
        return new RelativePath(path.isFile(), Arrays.copyOfRange(segments, count, segments.length));
    }

    @Classpath
    public Configuration getConfiguration() {
        return configuration;
    }

    @InputDirectory
    public Provider<Directory> getSysrootDir() {
        return sysrootDir;
    }

    @Input
    public List<ParseTarget> getParseTargets() {
        EnumSet<ParseTarget> targets = EnumSet.noneOf(ParseTarget.class);
        for (BuildTarget target : buildTargets) {
            ParseTarget parseTarget = ParseTarget.of(target.os, target.architecture, target.bitness, target.getTargetAndroidABI());
            if (parseTarget == null)
                throw new IllegalArgumentException("No parse target for native build target " + target);

            targets.add(parseTarget);
        }
        return new ArrayList<>(targets);
    }

    @TaskAction
    public void run() {
        Objects.requireNonNull(generator.getOutputPath(), "jnigen.generator.outputPath not defined");
        Objects.requireNonNull(generator.getBasePackage(), "jnigen.generator.basePackage not defined");
        Objects.requireNonNull(generator.getFileToParse(), "jnigen.generator.fileToParse not defined");

        // Gradle is annoying and ships it's own javaparser, but doesn't relocate it
        FileCollection filteredClasspath = getProject().files(
                getProject().getBuildscript()
                        .getConfigurations().getByName("classpath")
                        .resolve().stream()
                        .filter(file -> !file.getAbsolutePath().contains("gradle-"))
                        .collect(Collectors.toList())
        );

        String[] options = generator.getOptions();
        if (options == null)
            options = new String[0];

        List<ParseTarget> targets = getParseTargets();
        if (targets == null || targets.isEmpty())
            throw new GradleException("No native build targets configured");

        ArrayList<String> args = new ArrayList<>();
        args.add(generator.getOutputPath().getAbsolutePath());
        args.add(generator.getBasePackage());
        args.add(generator.getFileToParse());
        args.add(sysrootDir.get().getAsFile().getAbsolutePath());
        args.add(targets.stream().map(ParseTarget::name).collect(Collectors.joining(",")));
        args.addAll(Arrays.asList(options));

        getProject().javaexec(spec -> {
            spec.environment("LIBCLANG_DISABLE_CRASH_RECOVERY", "1");
            spec.setClasspath(filteredClasspath.plus(configuration));
            spec.getMainClass().set("com.badlogic.gdx.jnigen.generator.Generator");
            spec.args(args);
        });
    }
}
