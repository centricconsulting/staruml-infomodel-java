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
				
		for(int n = 0; n < JsonResults.size(); n++)
		{
			JsonObject JsonResult = JsonResults.getJsonObject(n);
			
			if(JsonResult.getString("_type").equals("UMLClassView"))
			{
				this.ContainedClassIds.add(ElementAbstract.getRef(JsonResult, "model"));
			}
		}
			
	}
	
	public void populateXmlElement(Element parentElement)
	{

		Document doc = parentElement.getOwnerDocument();
		
		// spawn the top element
		Element childElement = doc.createElement("diagram");
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
		
		// add element
		newElement2 = doc.createElement("is-default");
		newElement2.appendChild(doc.createTextNode(ElementAbstract.getBooleanString(this.isDefault)));
		childElement.appendChild(newElement2);		
		
		// add element
		newElement2 = doc.createElement("is-visible");
		newElement2.appendChild(doc.createTextNode(ElementAbstract.getBooleanString(this.isVisible)));
		childElement.appendChild(newElement2);		
		
		Element newElementX;
		
		for(int n = 0; n < this.ContainedClassIds.size(); n++)
		{
			newElementX = doc.createElement("diagram-class");
			newElementX.setAttribute("class-id", this.ContainedClassIds.get(n));	
			childElement.appendChild(newElementX);
		}
		
		
		parentElement.appendChild(childElement);
	}
	
}
