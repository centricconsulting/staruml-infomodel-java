package com.centric.infomodel.structure;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Diagram extends ElementAbstract {

	public boolean isDefault = false;
	public boolean isVisible = false;
	
	public List<String> ContainedClassIds = new ArrayList<String>();
	public List<String> ContainedEnumIds = new ArrayList<String>();
	
	public Diagram(JsonObject json)
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
		this.isDefault= json.getBoolean("defaultDiagram", false);
		this.isVisible = json.getBoolean("visible", false);
		this.parentRefId = ElementAbstract.getParentRef(json);

		
		JsonArray JsonResults = json.getJsonArray("ownedViews");
		
		if (JsonResults != null)
		{		
			for(int n = 0; n < JsonResults.size(); n++)
			{
				JsonObject JsonResult = JsonResults.getJsonObject(n);
				
				if(JsonResult.getString("_type").equals("UMLClassView"))
				{
					this.ContainedClassIds.add(ElementAbstract.getRef(JsonResult, "model"));					
				}
				else if(JsonResult.getString("_type").equals("UMLEnumerationView"))
				{
					this.ContainedEnumIds.add(ElementAbstract.getRef(JsonResult, "model"));
				}
			}
		}
			
	}
	
	public void populateXmlElement(Element parentElement)
	{

		Document doc = parentElement.getOwnerDocument();
		
		// spawn the top element
		Element childElement = doc.createElement("diagram");
		childElement.setAttribute("id",this.id);
		
		// flags
		childElement.setAttribute("is-default", ElementAbstract.getBooleanString(this.isDefault));
		childElement.setAttribute("is-visible", ElementAbstract.getBooleanString(this.isVisible));
		
		childElement.setAttribute("parent-object-id",this.parentRefId);
		

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
		

		Element newElementX;
		
		for(int n = 0; n < this.ContainedClassIds.size(); n++)
		{
			newElementX = doc.createElement("diagram-entity");
			newElementX.setAttribute("entity-id", this.ContainedClassIds.get(n));	
			childElement.appendChild(newElementX);
		}
				
		
		for(int n = 0; n < this.ContainedEnumIds.size(); n++)
		{
			newElementX = doc.createElement("diagram-enum");
			newElementX.setAttribute("enum-id", this.ContainedEnumIds.get(n));	
			childElement.appendChild(newElementX);
		}
		
		parentElement.appendChild(childElement);
		
	}
	
}
