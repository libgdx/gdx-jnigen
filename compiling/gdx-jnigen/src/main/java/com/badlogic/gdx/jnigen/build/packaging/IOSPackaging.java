package com.badlogic.gdx.jnigen.build.packaging;

import com.badlogic.gdx.jnigen.FileDescriptor;
import com.badlogic.gdx.jnigen.RobovmBuildConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.stream.Stream;

public class IOSPackaging extends PlatformPackager {

    private static final Logger logger = LoggerFactory.getLogger(IOSPackaging.class);

    @Override
    protected void packagePlatformImpl () {
        File buildDir = buildConfig.buildDir.file();
        buildDir.mkdirs();

        File outputJar = new File(buildConfig.libsDir.file().getAbsoluteFile(), buildConfig.targetJarBaseName + "-natives-ios.jar");

        //single input file for the framework we built with jnigen
        FileDescriptor frameworkFolder = buildConfig.libsDir.child(buildConfig.sharedLibName + ".xcframework");

        File robovmXml = new File(buildDir, "robovm.xml");
        generateRobovmXML(robovmXml);

        try {
            try (FileOutputStream fileOutputStream = new FileOutputStream(outputJar)) {
                try (JarOutputStream jarOutputStream = new JarOutputStream(fileOutputStream)) {
                    bundleRobovmXML(jarOutputStream, robovmXml);
                    bundleBuiltFrameworkIntoJar(jarOutputStream, frameworkFolder);

                    //Bundle any extra frameworks also. Fail oon not found
                    RobovmBuildConfig robovmBuildConfig = buildConfig.robovmBuildConfig;
                    if (!robovmBuildConfig.getExtraXCFrameworks().isEmpty()) {
                        for (String extraXCFramework : robovmBuildConfig.getExtraXCFrameworks()) {
                            bundleExtraFrameworkIntoJar(jarOutputStream, extraXCFramework);
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Exception when packing", e);
            throw new RuntimeException(e);
        }

        logger.info("Packed ios platform to {}", outputJar);
    }

    private void bundleRobovmXML(JarOutputStream jarOutputStream, File robovmXml) throws IOException {
        JarEntry robovmEntry = new JarEntry("META-INF/robovm/ios/robovm.xml");

        jarOutputStream.putNextEntry(robovmEntry);
        Files.copy(robovmXml.toPath(), jarOutputStream);
        jarOutputStream.closeEntry();
    }

    private void bundleBuiltFrameworkIntoJar (JarOutputStream jarOutputStream, FileDescriptor frameworkFolder) throws IOException {
        //add the framework
        if (frameworkFolder.file().exists()) {
            Path sourcePath = frameworkFolder.file().toPath();
            try (Stream<Path> stream = Files.walk(sourcePath)) {
                stream.forEach(path -> {
                    try {
                        String entryName = sourcePath.relativize(path).toString().replace("\\", "/");
                        JarEntry entry = new JarEntry("META-INF/robovm/ios/libs/" + buildConfig.sharedLibName + ".xcframework/" + entryName + (Files.isDirectory(path) ? "/" : ""));
                        jarOutputStream.putNextEntry(entry);

                        // Only copy file contents for regular files
                        if (Files.isRegularFile(path)) {
                            Files.copy(path, jarOutputStream);
                        }

                        jarOutputStream.closeEntry();
                    } catch (IOException e) {
                        logger.error("Exception when bundling build xcframework into jar", e);
                        throw new RuntimeException(e);
                    }
                });
            }
        } else {
            if (buildConfig.errorOnPackageMissingNative) {
                throw new RuntimeException("Framework folder does not exist: " + frameworkFolder);
            } else {
                logger.error("Framework folder does not exist: {}", frameworkFolder);
            }
        }
    }

    private void bundleExtraFrameworkIntoJar (JarOutputStream jarOutputStream, String extraXCFrameworkPath) throws IOException {
        FileDescriptor frameworkFolder = buildConfig.libsDir.parent().child(extraXCFrameworkPath);
        //add the framework
        if (frameworkFolder.file().exists()) {
            Path sourcePath = frameworkFolder.file().toPath();
            try (Stream<Path> stream = Files.walk(sourcePath)) {
                stream.forEach(path -> {
                    try {
                        String xcFrameworkName = extraXCFrameworkPath.substring(extraXCFrameworkPath.lastIndexOf('/') + 1);
                        xcFrameworkName = xcFrameworkName.substring(0, xcFrameworkName.lastIndexOf('.'));

                        String entryName = sourcePath.relativize(path).toString().replace("\\", "/");
                        JarEntry entry = new JarEntry("META-INF/robovm/ios/libs/" + xcFrameworkName + ".xcframework/" + entryName + (Files.isDirectory(path) ? "/" : ""));
                        jarOutputStream.putNextEntry(entry);

                        // Only copy file contents for regular files
                        if (Files.isRegularFile(path)) {
                            Files.copy(path, jarOutputStream);
                        }

                        jarOutputStream.closeEntry();
                    } catch (IOException e) {
                        logger.error("Exception when bundling extra xcframework into jar", e);
                        throw new RuntimeException(e);
                    }
                });
            }
        } else {
            throw new RuntimeException("Extra Framework folder does not exist: " + frameworkFolder);
        }
    }

    private void generateRobovmXML (File robovmXml) {
        robovmXml.getParentFile().mkdirs();

        RobovmBuildConfig robovmBuildConfig = buildConfig.robovmBuildConfig;

        if (robovmBuildConfig.getManualFile() != null) {
            File manualFile = robovmBuildConfig.getManualFile();
            if (!manualFile.exists()) {
                throw new RuntimeException("Manual robovm.xml file does not exist: " + manualFile.getPath());
            }

            try {
                Files.copy(manualFile.toPath(), robovmXml.toPath(), StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.COPY_ATTRIBUTES);
            } catch (IOException e) {
                throw new RuntimeException("Unable to copy manual robovm.xml file to temporary robovm.xml file", e);
            }
            return;
        }

        File oldRobovmXml = new File(buildConfig.jniDir.file(), "robovm.xml");
        if (oldRobovmXml.exists()) {
            throw new RuntimeException("Legacy " + oldRobovmXml.getPath()
                    + " file exists, please define `jnigen.robovm.manualFile = file('jni/robovm.xml')` or migrate to gradle declaration and delete this file.");
        }

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element config = doc.createElement("config");
            doc.appendChild(config);

            Element frameworkPaths = doc.createElement("frameworkPaths");
            config.appendChild(frameworkPaths);

            Element xcFrameworkPath = doc.createElement("path");
            xcFrameworkPath.setTextContent("libs");

            frameworkPaths.appendChild(xcFrameworkPath);

            Element frameworks = doc.createElement("frameworks");
            config.appendChild(frameworks);

            Element framework = doc.createElement("framework");
            framework.setTextContent(buildConfig.sharedLibName);
            frameworks.appendChild(framework);

            // Add any extra libraries we have declared
            if (!robovmBuildConfig.getExtraLibs().isEmpty()) {
                Element libs = doc.createElement("libs");
                for (RobovmBuildConfig.RoboVMXmlLib l : robovmBuildConfig.getExtraLibs()) {
                    Element lib = doc.createElement("lib");
                    if (l.variant != null)
                        lib.setAttribute("variant", l.variant);
                    lib.setTextContent(l.path);
                    libs.appendChild(lib);
                }
                config.appendChild(libs);
            }

            // Add any extra xcFramework we have declared
            // TODO: 03.05.23 Rework at somepoint, if xcframeworks tag merging works
            if (!robovmBuildConfig.getExtraXCFrameworks().isEmpty()) {
                for (String path : robovmBuildConfig.getExtraXCFrameworks()) {
                    String xcFrameworkName = path.substring(path.lastIndexOf('/') + 1);
                    xcFrameworkName = xcFrameworkName.substring(0, xcFrameworkName.lastIndexOf('.'));
                    Element frameworkEl = doc.createElement("framework");
                    frameworkEl.setTextContent(xcFrameworkName);
                    frameworks.appendChild(frameworkEl);
                }
            }

            // Add any forceLinkClasses definitions we have declared
            if (!robovmBuildConfig.getForceLinkClasses().isEmpty()) {
                Element forceLinkClasses = doc.createElement("forceLinkClasses");

                for (String p : robovmBuildConfig.getForceLinkClasses()) {
                    Element pattern = doc.createElement("pattern");
                    pattern.setTextContent(p);
                    forceLinkClasses.appendChild(pattern);
                }

                config.appendChild(forceLinkClasses);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            FileOutputStream fos = new FileOutputStream(robovmXml);
            StreamResult result = new StreamResult(fos);
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(source, result);
            fos.close();
        } catch (IOException | ParserConfigurationException | TransformerException e) {
            throw new RuntimeException("Unable to create temporary robovm.xml file", e);
        }
    }
}
