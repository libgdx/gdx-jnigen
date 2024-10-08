package com.badlogic.gdx.jnigen.gradle.gha;

import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.FileDescriptor;
import com.badlogic.gdx.jnigen.commons.CompilerABIType;
import com.badlogic.gdx.jnigen.commons.Os;
import com.badlogic.gdx.jnigen.gradle.JnigenExtension;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class GHABuilder {


    public GHABuilder (File outputFile, JnigenExtension ext) throws IOException {

        FileWriter writer = new FileWriter(outputFile);

        String template = new FileDescriptor("com.badlogic.gdx.jnigen.gradle/gha-template.yaml",
                FileDescriptor.FileType.Classpath).readString();

        boolean needsLinux = needsLinux(ext);
        boolean needsWindows = needsWindows(ext);
        boolean needsMac = needsMac(ext);

        List<String> jobsNeeded = new ArrayList<>();
        List<String> jobs = new ArrayList<>();
        List<String> artifactDownloads = new ArrayList<>();

        List<BuildTarget> allTargets = new ArrayList<>();
        allTargets.addAll(ext.targets);

        if (needsLinux) {
            String linuxTemplateString = new FileDescriptor("com.badlogic.gdx.jnigen.gradle/linux.yaml", FileDescriptor.FileType.Classpath).readString();
            linuxTemplateString = injectJobs(linuxTemplateString, Os.Linux, allTargets);
            jobs.add(linuxTemplateString);
            jobsNeeded.add("build-linux");

            String downloadArtifactTemplate = new FileDescriptor("com.badlogic.gdx.jnigen.gradle/artifactdownload.yaml", FileDescriptor.FileType.Classpath).readString();
            downloadArtifactTemplate = downloadArtifactTemplate.replace("%tasknameos%", "linux");
            artifactDownloads.add(downloadArtifactTemplate);
        }
        if (needsMac) {
            String macTemplateString = new FileDescriptor("com.badlogic.gdx.jnigen.gradle/mac.yaml", FileDescriptor.FileType.Classpath).readString();
            macTemplateString = injectJobs(macTemplateString, Os.MacOsX, allTargets);
            jobs.add(macTemplateString);
            jobsNeeded.add("build-mac");

            String downloadArtifactTemplate = new FileDescriptor("com.badlogic.gdx.jnigen.gradle/artifactdownload.yaml", FileDescriptor.FileType.Classpath).readString();
            downloadArtifactTemplate = downloadArtifactTemplate.replace("%tasknameos%", "mac");
            artifactDownloads.add(downloadArtifactTemplate);
        }
        if (needsWindows) {
            String windowsTemplateString = new FileDescriptor("com.badlogic.gdx.jnigen.gradle/windows.yaml", FileDescriptor.FileType.Classpath).readString();
            windowsTemplateString = injectJobs(windowsTemplateString, Os.Windows, allTargets);
            jobs.add(windowsTemplateString);
            jobsNeeded.add("build-windows");

            String downloadArtifactTemplate = new FileDescriptor("com.badlogic.gdx.jnigen.gradle/artifactdownload.yaml", FileDescriptor.FileType.Classpath).readString();
            downloadArtifactTemplate = downloadArtifactTemplate.replace("%tasknameos%", "windows");
            artifactDownloads.add(downloadArtifactTemplate);
        }

        if (!allTargets.isEmpty()) {
            throw new RuntimeException("Left over targets");
        }

        String jobsNeededString = "";
        for (int i = 0; i < jobsNeeded.size(); i++) {
            String item = jobsNeeded.get(i);
            if (i != 0) {
                jobsNeededString += ", " + item;
            } else {
                jobsNeededString += item;
            }
        }


        String buffer = "";
        for (String job : jobs) {
            buffer += job + "\n";
        }

        template = template.replace("%jobsMarker%", buffer);
        template = template.replace("%jobNeeds%", jobsNeededString);

        buffer = "";
        for (String downloadArtifact: artifactDownloads) {
            buffer += downloadArtifact + "\n";
        }

        template = template.replace("%downloadArtifacts%", buffer);

        writer.write(template);
        writer.close();
    }

    private String injectJobs (String template, Os os, List<BuildTarget> targets) {
        //Inject base job
        String jniGenTasks = "\n" +
                "      - name: Initialize jnigen\n" +
                "        run: ./gradlew jnigen";


        HashSet<Os> osToBuild = new HashSet<>();

        Iterator<BuildTarget> iterator = targets.iterator();
        while (iterator.hasNext()) {
            BuildTarget next = iterator.next();
            if (next.canBuildOnHost(os)) {
                osToBuild.add(next.os);
                iterator.remove();
            }
        }

        String buffer = "";
        for (Os targetOses : osToBuild) {
            buffer += " jnigenBuildAll" + targetOses;
        }

        jniGenTasks += "\n" +
                "      - name: Build natives \n" +
                "        run: ./gradlew" + buffer;

        return template.replace("%steps%", jniGenTasks);
    }

    private boolean needsMac (JnigenExtension ext) {
        return ext.targets.stream().anyMatch(it -> it.os == Os.MacOsX || it.os == Os.IOS);
    }

    private boolean needsWindows (JnigenExtension ext) {
        return ext.targets.stream().anyMatch(it -> it.os == Os.Windows && it.compilerABIType == CompilerABIType.MSVC);
    }

    private boolean needsLinux (JnigenExtension ext) {
        return ext.targets.stream().anyMatch(it -> it.os == Os.Android || it.os == Os.Linux || (it.os == Os.Windows && it.compilerABIType == CompilerABIType.GCC_CLANG));
    }

}
