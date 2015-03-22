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
    	
    	/*
    	 * -p "{Source StarUML Project Json File Path}"
    	 * -t "{Target File Path}"
    	 * -x "{Xslt File Path}"
    	 * -g Inclusion will generate Xml file. Same name and folder path as target will be used unless explicit path is provided.
    	 * -m (optional) Explicit "{Xml File Path}"
    	 */
    	
    	String
    		JsonFilePath = null,
    		TargetFilePath = null,
    		XsltFilePath = null,
    		XmlFilePath = null;
    	
    	
    	boolean generateXml = false;
    	
    	for (int n = 0; n < args.length; n++)
    	{
    		
    		if(args[n].equals("-p"))
    		{
       			JsonFilePath = Application.getOptionFilePath("source", args[n+1], true);       			
    			n++;  // advance the arg counter
    			
    		} else if(args[n].equals("-t"))
    		{
    			TargetFilePath = Application.getOptionFilePath("target", args[n+1], false);
    			n++;  // advance the arg counter    			
    			
    		} else if(args[n].equals("-x"))
    		{
    			XsltFilePath = Application.getOptionFilePath("xsl template", args[n+1], true);
    			n++;  // advance the arg counter
    		    		
    		} else if(args[n].equals("-g"))
    		{
    			generateXml = true;
    			
    		} else if(args[n].equals("-m"))
    		{
    			XmlFilePath = Application.getOptionFilePath("xml file", args[n+1], false);    			
    			generateXml = true; // force the xml generation
    			n++;  // advance the arg counter
    		}
    		    		
    	}
		    					
		// populate the project structure
		Project project = new Project(getDocumentJsonObject(JsonFilePath), JsonFilePath);
		
		// retrieve the xml document
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();		
		
		project.populateXmlElement(doc);
		
		// generate the html document
		Builder.transformXml(doc, XsltFilePath, TargetFilePath);
		
		// generate the xml document
		if (generateXml == true)
		{
			// build xml file path			
			if(XmlFilePath == null)
			{
				String TargetFileBaseName = FilenameUtils.getBaseName(TargetFilePath);
				String TargetFolderPath = FilenameUtils.getFullPath(TargetFilePath);
				String XmlFileName = TargetFileBaseName + ".xml";	
				XmlFilePath = FilenameUtils.concat(TargetFolderPath, XmlFileName);
			}
			
			Builder.saveXml(doc, XmlFilePath);
		}	
	}
    
    private static String getOptionFilePath(String context, String path, boolean fileMustExist)
    {
    	
    	String resultPath = null;
    	
    	// argument inputs
    	try
    	{
    		resultPath = normalizePath(path);
    		File f = new File(resultPath);
    		
    		if(fileMustExist && !f.exists())
    		{
    			throw new IllegalArgumentException("The " + context + " file \"" + path + "\" does not exist.");
    		} else if (f.isDirectory()) {
    			throw new IllegalArgumentException("The " + context + " file \"" + path + "\" is not a valid file.");
    		} else {
    			return resultPath;
    		}
    	} catch (Exception e) {
    		throw new IllegalArgumentException("The " + context + " file \"" + path + "\" is invalid. " + e.getMessage());
    	}
    	
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
