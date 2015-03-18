package com.centric.infomodel.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;

import com.centric.infomodel.structure.Project;

public class Application {

    public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException  {
		
    	String JsonFilePath, HtmlFilePath, XsltFilePath;
    	
    	if(args.length <3)
    	{
    		throw new IllegalArgumentException("Insufficient argements were provided on the command line.");
    	}
    	    	
    	// argument inputs
    	try
    	{
    		JsonFilePath = normalizePath(args[0]);
    		File f = new File(JsonFilePath);
    		
    		if(!f.exists())
    		{
    			throw new IllegalArgumentException("The source file \"" + args[0] + "\" does not exist.");
    		} else if (f.isDirectory()) {
    			throw new IllegalArgumentException("The source file \"" + args[0] + "\" is not a valid file.");
    		}
    	} catch (Exception e) {
    		throw new IllegalArgumentException("The source file \"" + args[0] + "\" is invalid. " + e.getMessage());
    	}
		
    	
    	// argument inputs
    	try
    	{
    		HtmlFilePath = normalizePath(args[1]);
    		File f = new File(HtmlFilePath);
    		
    		if(f.isDirectory())
    		{
    			throw new IllegalArgumentException("The target file \"" + args[1] + "\" is not a valid file.");
    		} 
    		
    	} catch (Exception e) {
    		throw new IllegalArgumentException("The target file \"" + args[1] + "\" is invalid. " + e.getMessage());
    	}
    	
    	
    	// argument inputs
    	try
    	{
    		XsltFilePath = normalizePath(args[2]);
    		File f = new File(XsltFilePath);
    		
    		if(!f.exists())
    		{
    			throw new IllegalArgumentException("The transform (xslt) file \"" + args[2] + "\" does not exist.");
    		} else if (f.isDirectory()) {
    			throw new IllegalArgumentException("The transform (xslt) file \"" + args[2] + "\" is not a valid file.");
    		}
    		
    	} catch (Exception e) {
    		throw new IllegalArgumentException("The transform file \"" + args[2] + "\" is invalid. " + e.getMessage());
    	}
 
		// build xml file path
		String HtmlFileBaseName = FilenameUtils.getBaseName(HtmlFilePath);
		String HtmlFolderPath = FilenameUtils.getFullPath(HtmlFilePath);
		String XmlFileName = HtmlFileBaseName + ".xml";		
		String XmlFilePath = FilenameUtils.concat(HtmlFolderPath, XmlFileName);

		
		// populate the project structure
		Project project = new Project(getDocumentJsonObject(JsonFilePath), JsonFilePath);
		
		// retrieve the xml document
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();		
		
		project.populateXmlElement(doc);
		
		// generate the html document
		Builder.transformXml(doc, XsltFilePath, HtmlFilePath);
		
		// generate the xml document
		Builder.saveXml(doc, XmlFilePath);
		
		
		
	}

    private static String normalizePath(String path)
    {
    	if (System.getProperty("os.name").startsWith("Windows"))
    	{
    		return path.replace("/", "\\");
    	} else {
    		return path;
    	}
    }

    
    public static JsonObject getDocumentJsonObject(String JsonFilePath) throws IOException
    {
		InputStream fis = new FileInputStream(JsonFilePath);       
        JsonReader jsonReader = Json.createReader(fis);        
        JsonObject ProjectJsonObject = jsonReader.readObject();
		jsonReader.close();
		fis.close();
		
		return ProjectJsonObject;
    }
	
	public static String getXmlFromDocument(Document doc) throws TransformerException
	{
		
		   DOMSource domSource = new DOMSource(doc.getDocumentElement());		  
		   
	       StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
	       
	       // adds new line between each element
	       transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	       
	       // adds indenting based on xml structure
	       transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	       
	       transformer.transform(domSource, result);
	       
	       return writer.toString();
	       
	}
    
}
