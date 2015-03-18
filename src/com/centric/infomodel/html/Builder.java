package com.centric.infomodel.html;

import java.io.File;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

public class Builder {

	public static void  transformXml(Document xmlDocument, String xsltTemplateFilePath, String targetHtmlFilePath) throws TransformerException
	{
				
        // reference the xslt source
        Source xsltStreamSource = new StreamSource(new File(xsltTemplateFilePath));
                
        // reference the xml source file
        DOMSource xmlSource = new DOMSource(xmlDocument);
        // Source xmlStreamSource = new StreamSource(xmlStringReader);
        
        // perform the transformation
        TransformerFactory factory = TransformerFactory.newInstance();                
		
		Transformer transformer = factory.newTransformer(xsltStreamSource);
		transformer.transform(xmlSource, new StreamResult(new File(targetHtmlFilePath)));
	
	}
	
	public static void saveXml(Document xmlDocument, String targetXmlFilePath) throws TransformerException
	{
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		
		DOMSource xmlSource = new DOMSource(xmlDocument);
		StreamResult streamResult =  new StreamResult(new File(targetXmlFilePath));
		
       // adds new line between each element
       transformer.setOutputProperty(OutputKeys.INDENT, "yes");
       
       // adds indenting based on xml structure
       transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
		
		transformer.transform(xmlSource, streamResult);
		
	}
	
}
