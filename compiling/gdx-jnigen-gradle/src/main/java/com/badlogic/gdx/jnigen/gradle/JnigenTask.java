package com.badlogic.gdx.jnigen.gradle;

import com.badlogic.gdx.jnigen.FileDescriptor;
import com.badlogic.gdx.jnigen.NativeCodeGenerator;
import com.badlogic.gdx.jnigen.build.PlatformBuilder;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Arrays;

/**
 * @author Desu
 */
public class JnigenTask extends DefaultTask {

    private static final Logger log = LoggerFactory.getLogger(JnigenTask.class);

    JnigenExtension ext;

    @Inject
    public JnigenTask (JnigenExtension ext) {
        this.ext = ext;

        setGroup("jnigen");
        setDescription("Generates jnigen native code files and build scripts.");
        getProject().afterEvaluate(project -> dependsOn(ext.nativeCodeGeneratorConfig.sourceSet.getRuntimeClasspath()));
    }

    @TaskAction
    public void run () {
        if (ext.sharedLibName == null) {
            log.error("sharedLibName must be defined");
            throw new RuntimeException("sharedLibName must be defined");
        }

        log.debug("subProjectDir {}", ext.subProjectDir);
        log.debug("sharedLibName {}", ext.sharedLibName);
        log.debug("nativeCodeGeneratorConfig {}", ext.nativeCodeGeneratorConfig);

        FileDescriptor headerDestination = new FileDescriptor(ext.subProjectDir).child(ext.jniDir);

        PlatformBuilder.copyHeaders(headerDestination);

        Arrays.stream(ext.nativeCodeGeneratorConfig.getSourceDirs())
                .map(s -> s.startsWith(ext.subProjectDir) ? s : ext.subProjectDir + s)
                .forEach(s -> {
                    try {
                        new NativeCodeGenerator().generate(s,
                                ext.nativeCodeGeneratorConfig.sourceSet.getRuntimeClasspath().getAsPath(), ext.subProjectDir + ext.jniDir,
                                ext.nativeCodeGeneratorConfig.includes, ext.nativeCodeGeneratorConfig.excludes);
                    } catch (Exception e) {
                        throw new RuntimeException("NativeCodeGenerator threw exception", e);
                    }
                });
    }
}
