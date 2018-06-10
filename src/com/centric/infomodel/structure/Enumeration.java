package com.centric.infomodel.structure;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Enumeration extends ElementAbstract {

	public String stereotypeId;
	
	public List<EnumLiteral> EnumLiterals = new ArrayList<EnumLiteral>();	
	
	public Enumeration(JsonObject json)
	{
		populate(json);  	
	}
	
	public void populate(JsonObject json)
	{
		
		// required
		this.name = json.getString("name", ElementAbstract.UNKNOWN_STRING);
		this.id = json.getString("_id", ElementAbstract.EMPTY_STRING);
		this.visibility = json.getString("visibility", ElementAbstract.EMPTY_STRING);
		
		// optional
		this.documentation = json.getString("documentation", ElementAbstract.EMPTY_STRING);
		this.parentRefId = ElementAbstract.getParentRef(json);
		
		this.stereotypeId = ElementAbstract.getRef(json, "stereotype");
		
		// populate associations
		JsonArray JsonResults = json.getJsonArray("literals");
				
		if (JsonResults != null)
		{		
			for(int n = 0; n < JsonResults.size(); n++)
			{
				JsonObject JsonResult = JsonResults.getJsonObject(n);
				
				if(JsonResult.getString("_type").equals("UMLEnumerationLiteral"))
				{
					this.EnumLiterals.add(new EnumLiteral(JsonResult));
				}
			}
		}
		
	}
	
	public void populateXmlElement(Element parentElement)
	{	
		Document doc = parentElement.getOwnerDocument();
		
		// spawn the top element
		Element childElement = doc.createElement("enum");
		childElement.setAttribute("id",this.id);
		childElement.setAttribute("parent-object-id",this.parentRefId);
		childElement.setAttribute("reference-object-id",this.stereotypeId);	
		childElement.setAttribute("visibility", this.visibility);

		// add element
		Element newElement1 = doc.createElement("name");
		newElement1.appendChild(doc.createCDATASection(this.name));
		childElement.appendChild(newElement1);
				
		// add element
		if(this.documentation.length() > 0)
		{
			Element newElement2 = doc.createElement("description");

			if(ElementAbstract.isUrlString(this.documentation).equals("true"))
			{
				newElement2.setAttribute("is-url", "true");
			}
			
			newElement2.appendChild(doc.createCDATASection(this.documentation));
			childElement.appendChild(newElement2);
		}
		
		// populate associations xml
		for(int n = 0; n < this.EnumLiterals.size(); n++)
		{
			this.EnumLiterals.get(n).populateXmlElement(childElement);
		}		
		
		parentElement.appendChild(childElement);
	}
	
}
