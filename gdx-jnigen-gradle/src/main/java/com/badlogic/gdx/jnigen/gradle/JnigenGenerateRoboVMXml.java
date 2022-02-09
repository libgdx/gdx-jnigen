package com.badlogic.gdx.jnigen.gradle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

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
import org.gradle.internal.xml.XmlTransformer;
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
	private final XmlTransformer xmlTransformer = new XmlTransformer();

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
		
		File oldRobovmXml = new File(ext.subProjectDir + ext.jniDir + File.separatorChar + "robovm.xml");
		if(oldRobovmXml.exists()) {
			throw new RuntimeException("Legacy " + oldRobovmXml.getPath() + " file exists, please migrate to gradle declaration and delete this file.");
		}

		File buildDir = getProject().getBuildDir();
		if(!buildDir.exists())
			buildDir.mkdirs();
		
		File robovmXml = new File(buildDir, "robovm.xml");

		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element config = doc.createElement("config");
			doc.appendChild(config);

			Element libs = doc.createElement("libs");
			config.appendChild(libs);

			// Default assumes we only care about one .a
			Element lib = doc.createElement("lib");
			lib.setTextContent("libs/" + target.getSharedLibFilename(ext.sharedLibName));
			libs.appendChild(lib);

			if (!ext.robovm.extraLibs.isEmpty()) {
				for (RoboVMXmlLib l : ext.robovm.extraLibs) {
					lib = doc.createElement("lib");
					if (l.variant != null)
						lib.setAttribute("variant", l.variant);
					lib.setTextContent(l.path);
					libs.appendChild(lib);
				}
			}

			if (!ext.robovm.forceLinkClasses.isEmpty()) {
				Element forceLinkClasses = doc.createElement("forceLinkClasses");

				for (String p : ext.robovm.forceLinkClasses) {
					Element pattern = doc.createElement("pattern");
					pattern.setTextContent(p);
					forceLinkClasses.appendChild(pattern);
				}

				config.appendChild(forceLinkClasses);
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);

			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			transformer.setOutputProperty("omit-xml-declaration", "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.transform(source, result);

			FileOutputStream fos = new FileOutputStream(robovmXml);
			xmlTransformer.addAction(ext.robovm.getXmlAction());
			xmlTransformer.transform(writer.toString(), fos);
			fos.close();
		} catch (IOException | ParserConfigurationException | TransformerException e) {
			throw new RuntimeException("Unable to create temporary robovm.xml file", e);
		}
	}
}
