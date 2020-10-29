package com.badlogic.gdx.jnigen.gradle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.badlogic.gdx.jnigen.BuildTarget;
import com.badlogic.gdx.jnigen.BuildTarget.TargetOs;

public class JnigenIOSJarTask extends JnigenJarTask {

	public JnigenIOSJarTask() {
		super(TargetOs.IOS);
	}
	
	private void generateXML(File robovmXml, String sharedLibName)
	{
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			Document doc = docBuilder.newDocument();
			Element config = doc.createElement("config");
			doc.appendChild(config);
			
			Element libs = doc.createElement("libs");
			config.appendChild(libs);
			
			//Default assumes we only care about one .a
			Element lib = doc.createElement("lib");
			lib.setTextContent("libs/" + sharedLibName);
			libs.appendChild(lib);
			
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
	
	public void add(BuildTarget target, JnigenExtension ext, String abi) {
		String targetFolder = target.getTargetFolder();
		
		String path = ext.subProjectDir + ext.libsDir + File.separatorChar + targetFolder;
		from(path, (copySpec) -> {
			copySpec.include("*.a");
			copySpec.into("META-INF/robovm/ios/libs");
		});
		
		from(path, (copySpec) -> {
			copySpec.include("*.a.tvos");
			copySpec.into("META-INF/robovm/tvos/libs");
			copySpec.rename(".tvos$", "");
		});
		
		File robovmXml = new File(ext.subProjectDir + ext.jniDir + File.separatorChar + "robovm.xml");
		if(!robovmXml.exists()) {
			generateXML(robovmXml, target.getSharedLibFilename(ext.sharedLibName));
		}
		
		from(robovmXml, (copySpec) -> {
			copySpec.into("META-INF/robovm/ios");
			copySpec.rename(".*", "robovm.xml");
		});
		from(robovmXml, (copySpec) -> {
			copySpec.into("META-INF/robovm/tvos");
			copySpec.rename(".*", "robovm.xml");
		});
	}
}
