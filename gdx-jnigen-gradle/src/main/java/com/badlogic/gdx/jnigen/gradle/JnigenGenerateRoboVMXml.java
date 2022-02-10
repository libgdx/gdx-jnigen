package com.badlogic.gdx.jnigen.gradle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.BuildTarget.TargetOs;
import com.badlogic.gdx.jnigen.gradle.JnigenExtension.RoboVMXml.RoboVMXmlLib;

/**
 * @author Desu
 */
public class JnigenGenerateRoboVMXml extends DefaultTask {
	private static final Logger log = LoggerFactory.getLogger(JnigenGenerateRoboVMXml.class);

	JnigenExtension ext;

	@Inject
	public JnigenGenerateRoboVMXml(JnigenExtension ext) {
		this.ext = ext;

		setGroup("jnigen");
		setDescription("Generates robovm.xml file");
	}

	@TaskAction
	public void run() {
		BuildTarget target = ext.get(TargetOs.IOS);
		if (target == null) {
			log.info("Nothing to do because no IOS BuildTarget");
			return;
		}

		File buildDir = getProject().getBuildDir();
		if (!buildDir.exists())
			buildDir.mkdirs();

		File robovmXml = new File(buildDir, "robovm.xml");

		if (ext.robovm.getManualFile() != null) {
			File manualFile = ext.robovm.getManualFile();
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

		File oldRobovmXml = new File(ext.subProjectDir + ext.jniDir + File.separatorChar + "robovm.xml");
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

			Element libs = doc.createElement("libs");
			config.appendChild(libs);

			// Add the base library we compiled
			Element lib = doc.createElement("lib");
			lib.setTextContent("libs/" + target.getSharedLibFilename(ext.sharedLibName));
			libs.appendChild(lib);

			// Add any extra libraries we have declared
			if (!ext.robovm.getExtraLibs().isEmpty()) {
				for (RoboVMXmlLib l : ext.robovm.getExtraLibs()) {
					lib = doc.createElement("lib");
					if (l.variant != null)
						lib.setAttribute("variant", l.variant);
					lib.setTextContent(l.path);
					libs.appendChild(lib);
				}
			}

			// Add any forceLinkClasses definitions we have declared
			if (!ext.robovm.getForceLinkClasses().isEmpty()) {
				Element forceLinkClasses = doc.createElement("forceLinkClasses");

				for (String p : ext.robovm.getForceLinkClasses()) {
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
