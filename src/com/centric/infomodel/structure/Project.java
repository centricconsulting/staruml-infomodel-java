package com.centric.infomodel.structure;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Project  extends ElementAbstract {
	
	public Date modifiedDate;
	public String author;
	public String copyright;
	public String version;
	public String fileName;
	public String filePath;
	
	public List<Model> Models = new ArrayList<Model>();
	
	
	public Project(JsonObject json, String jsonFilePath) throws ParserConfigurationException
	{
		populate(json, jsonFilePath);	   			
	}
	
	public void populate(JsonObject json, String jsonFilePath) throws ParserConfigurationException
	{
		
		File jsonFile = new File(jsonFilePath);
		
		this.filePath = jsonFilePath;		
		this.modifiedDate = new Date(jsonFile.lastModified());
		this.fileName = jsonFile.getName();
		
		// Required
		this.name = json.getString("name", ElementAbstract.UNKNOWN_STRING);
		this.id = json.getString("_id", ElementAbstract.EMPTY_STRING);
	    
		// optional
		this.documentation = json.getString("documentation", ElementAbstract.EMPTY_STRING);
	    this.author = json.getString("author", ElementAbstract.EMPTY_STRING);
	    this.copyright = json.getString("copyright", ElementAbstract.EMPTY_STRING);	
	    this.version = json.getString("version", ElementAbstract.EMPTY_STRING);

		JsonArray JsonResults = json.getJsonArray("ownedElements");
		
		for(int n = 0; n < JsonResults.size(); n++)
		{
			JsonObject JsonResult = JsonResults.getJsonObject(n);
			
			if(JsonResult.getString("_type").equals("UMLModel"))
			{
				this.Models.add(new Model(JsonResult));
			}
		}
	}
	
	public void populateXmlElement(Document doc) throws ParserConfigurationException
	{
		
		// spawn the top element
		Element childElement = doc.createElement("project");
		
		// add properties
		childElement.setAttribute("id",this.id);
		
		// add element
		Element newElement1 = doc.createElement("name");
		newElement1.appendChild(doc.createTextNode(this.name));
		childElement.appendChild(newElement1);
		
		// add element
		Element newElementMD = doc.createElement("modified-date");
		newElementMD.appendChild(doc.createTextNode(new SimpleDateFormat("MMMM d, yyyy h:mma z").format(this.modifiedDate)));
		childElement.appendChild(newElementMD);		
		
		// add element
		Element newElement2 = doc.createElement("documentation");
		newElement2.setAttribute("is-url", ElementAbstract.isUrlString(this.documentation));
		newElement2.appendChild(doc.createTextNode(this.documentation));
		childElement.appendChild(newElement2);
		

		// add element
		Element newElement3 = doc.createElement("author");
		newElement3.appendChild(doc.createTextNode(this.author));
		childElement.appendChild(newElement3);
		

		// add element
		Element newElement4 = doc.createElement("copyright");
		newElement4.appendChild(doc.createTextNode(this.copyright));
		childElement.appendChild(newElement4);
		

		// add element
		Element newElement5 = doc.createElement("version");
		newElement5.appendChild(doc.createTextNode(this.version));
		childElement.appendChild(newElement5);
		
		// add element
		Element newElementFP = doc.createElement("file-path");
		newElementFP.appendChild(doc.createTextNode(this.filePath));
		childElement.appendChild(newElementFP);
		
		// add element
		Element newElementFN = doc.createElement("file-name");
		newElementFN.appendChild(doc.createTextNode(this.fileName));
		childElement.appendChild(newElementFN);		
				
		
		// append models
		for(int n = 0; n < this.Models.size(); n++)
		{
			this.Models.get(n).populateXmlElement(childElement);
		}
	
		doc.appendChild(childElement);
			
		
	}
	

	
}
