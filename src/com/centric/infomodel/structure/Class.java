package com.centric.infomodel.structure;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Class extends ElementAbstract {
	
	public List<Attribute> Attributes = new ArrayList<Attribute>();
	public List<Operation> Operations = new ArrayList<Operation>();
	public List<Enum> Enums = new ArrayList<Enum>();
	public List<Association> Associations = new ArrayList<Association>();
	
	public Class(JsonObject json)
	{
		populate(json);  	
	}
	
	public void populate(JsonObject json)
	{
		
		// required
		this.name = json.getString("name", ElementAbstract.UNKNOWN_STRING);
		this.id = json.getString("_id", ElementAbstract.EMPTY_STRING);
		
		// optional
		this.documentation = json.getString("documentation", ElementAbstract.EMPTY_STRING);
		this.parentRefId = ElementAbstract.getParentRef(json);
		
		// populate attributes
		JsonArray JsonResults;
		int n;
		
		JsonResults = json.getJsonArray("attributes");
		
		if (JsonResults != null)
		{		
			for(n = 0; n < JsonResults.size(); n++)
			{
				JsonObject JsonResult = JsonResults.getJsonObject(n);
				
				if(JsonResult.getString("_type").equals("UMLAttribute"))
				{
					this.Attributes.add(new Attribute(JsonResult));
				}
			}
		}
		
		// populate operations
		JsonResults = json.getJsonArray("operations");

		if (JsonResults != null)
		{		
			for(n = 0; n < JsonResults.size(); n++)
			{
				JsonObject JsonResult = JsonResults.getJsonObject(n);
				
				if(JsonResult.getString("_type").equals("UMLOperation"))
				{
					this.Operations.add(new Operation(JsonResult));
				}
			}
		}
		
		// populate enumerations
		JsonResults = json.getJsonArray("enumerations");
				
		if (JsonResults != null)
		{		
			for(n = 0; n < JsonResults.size(); n++)
			{
				JsonObject JsonResult = JsonResults.getJsonObject(n);
				
				if(JsonResult.getString("_type").equals("UMLEnumeration"))
				{
					this.Enums.add(new Enum(JsonResult));
				}
			}
		}
		
		// populate associations
		JsonResults = json.getJsonArray("ownedElements");
				
		if (JsonResults != null)
		{		
			for(n = 0; n < JsonResults.size(); n++)
			{
				JsonObject JsonResult = JsonResults.getJsonObject(n);
				
				if(JsonResult.getString("_type").equals("UMLAssociation"))
				{
					this.Associations.add(new Association(JsonResult));
				}
			}
		}
						
	}
	
	public void populateXmlElement(Element parentElement)
	{

		Document doc = parentElement.getOwnerDocument();
		
		// spawn the top element
		Element childElement = doc.createElement("class");
		childElement.setAttribute("id",this.id);
		childElement.setAttribute("model-id",this.parentRefId);
		childElement.setAttribute("parent-ref-id",this.parentRefId);
		

		// add element
		Element newElement1 = doc.createElement("name");
		newElement1.appendChild(doc.createTextNode(this.name));
		childElement.appendChild(newElement1);
				
		// add element
		Element newElement2 = doc.createElement("documentation");
		newElement2.setAttribute("is-url", ElementAbstract.isUrlString(this.documentation));
		newElement2.appendChild(doc.createTextNode(this.documentation));
		childElement.appendChild(newElement2);
				
		int n;
		
		// populate attribute xml
		for(n = 0; n < this.Attributes.size(); n++)
		{
			this.Attributes.get(n).populateXmlElement(childElement);
		}
		
		// populate operations xml
		for(n = 0; n < this.Operations.size(); n++)
		{
			this.Operations.get(n).populateXmlElement(childElement);
		}		
		
		// populate enum xml
		for(n = 0; n < this.Enums.size(); n++)
		{
			this.Enums.get(n).populateXmlElement(childElement);
		}
		
		
		// populate associations xml
		for(n = 0; n < this.Associations.size(); n++)
		{
			this.Associations.get(n).populateXmlElement(childElement);
		}
		
		parentElement.appendChild(childElement);
	}
}
