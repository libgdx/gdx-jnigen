package com.badlogic.gdx.jnigen.gradle;

import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.commons.AndroidABI;
import com.badlogic.gdx.jnigen.commons.Platform;
import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.invocation.Gradle;
import org.gradle.api.publish.PublicationContainer;
import org.gradle.api.publish.PublishingExtension;
import org.gradle.api.publish.maven.MavenArtifact;
import org.gradle.api.publish.maven.MavenPublication;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Desu
 */
public class JnigenPlugin implements Plugin<Project> {

    @Override
    public void apply (Project project) {
        JnigenExtension ext = project.getExtensions().create("jnigen", JnigenExtension.class, project);
        JnigenTask jnigen = project.getTasks().create("jnigen", JnigenTask.class, ext);
        jnigen.dependsOn(project.getTasks().getByName("classes"));

        project.getTasks().create("jnigenGHA", JnigenGithubActionBuilderTask.class, ext);

        project.getGradle().projectsEvaluated(new Action<Gradle>() {
            @Override
            public void execute (Gradle gradle) {
                Set<Platform> platformsConfigured = new HashSet<>();

                for (BuildTarget target : ext.targets) {
                    platformsConfigured.add(target.os.getPlatform());
                }

                int size = platformsConfigured.size();
                Platform[] platformsToBuild = new Platform[size];
                int index = 0;
                for (Platform platform : platformsConfigured) {
                    platformsToBuild[index++] = platform;
                }

                JnigenPackageTask jnigenPackageTask = project.getTasks().create("jnigenPackageAll", JnigenPackageTask.class, ext);
                jnigenPackageTask.configure(null, platformsToBuild);


                //Maven publish

                if (!project.getPlugins().hasPlugin("maven-publish")) {
                    return;
                }

                project.getPlugins().apply("maven-publish");
                project.getExtensions().configure(PublishingExtension.class, new Action<PublishingExtension>() {
                    @Override
                    public void execute (PublishingExtension publishingExtension) {
                        publishingExtension.publications(new Action<PublicationContainer>() {
                            @Override
                            public void execute (PublicationContainer publications) {

                                publications.create("jniPlatform", MavenPublication.class, new Action<MavenPublication>() {
                                    @Override
                                    public void execute (MavenPublication mavenPublication) {
                                        String archiveBaseName = jnigenPackageTask.getSharedLibName();
                                        mavenPublication.setArtifactId(archiveBaseName + "-platform");

                                        safeAddMavenPublication("jnigenPackageAllDesktop", project, mavenPublication, "natives-desktop");

                                        for (AndroidABI abi : AndroidABI.values()) {
                                            safeAddMavenPublication("jnigenPackageAndroid_" + abi.getAbiString(), project, mavenPublication, "natives-" + abi.getAbiString());
                                        }

                                        safeAddMavenPublication("jnigenPackageAllIOS", project, mavenPublication, "natives-ios");

                                    }
                                });

                            }
                        });
                    }
                });
            }
        });
    }

    private void safeAddMavenPublication (String jniPackageTask, Project project, MavenPublication mavenPublication, String classifier) {
        try {
            JnigenPackageTask packageByName = (JnigenPackageTask)project.getTasks().getByName(jniPackageTask);
            mavenPublication.artifact(packageByName.getOutputs().getFiles().getSingleFile(), new Action<MavenArtifact>() {
                @Override
                public void execute (MavenArtifact mavenArtifact) {
                    mavenArtifact.setClassifier(classifier);
                    mavenArtifact.builtBy(packageByName);
                }
            });
        } catch (Exception e) {
        }

    }


}
