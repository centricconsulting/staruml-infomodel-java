package com.centric.infomodel.structure;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Constraint extends ElementAbstract {

	public List<String> ContainedReferenceIds = new ArrayList<String>();
	public String specification;
	
	public Constraint(JsonObject json)
	{
		populate(json);  	
	}
	
	public void populate(JsonObject json)
	{
		// required
		// note: associations do not require names
		this.name = json.getString("name", ElementAbstract.EMPTY_STRING);
		this.id = json.getString("_id", ElementAbstract.EMPTY_STRING);
		this.visibility = json.getString("visibility", ElementAbstract.VISIBILITY_PUBLIC);
		
		// optional
		this.documentation = json.getString("documentation", ElementAbstract.EMPTY_STRING);
		this.specification = json.getString("specification", ElementAbstract.EMPTY_STRING);
		this.parentRefId = ElementAbstract.getParentRef(json);
		
		
		// get end1 attributes
		JsonArray JsonResults = json.getJsonArray("constrainedElements");
		
		if (JsonResults != null)
		{		
			for(int n = 0; n < JsonResults.size(); n++)
			{
				JsonObject JsonResult = JsonResults.getJsonObject(n);				
				this.ContainedReferenceIds.add(ElementAbstract.getRefValue(JsonResult));
			}
		}
		
		
	}
	
	public void populateXmlElement(Element parentElement)
	{	
		

		Document doc = parentElement.getOwnerDocument();
		
		// spawn the top element
		Element childElement = doc.createElement("constraint");
		childElement.setAttribute("id",this.id);
		childElement.setAttribute("parent-object-id",this.parentRefId);
		childElement.setAttribute("visibility", this.visibility);

		if(this.specification.toLowerCase().contains("grain"))
		{
			childElement.setAttribute("is-grain","true");
		}
		
		if(this.specification.toLowerCase().contains("unique"))
		{
			childElement.setAttribute("is-unique","true");
		}
		
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
		
		// add element
		
		if(this.specification.length() > 0)
		{
			Element newElement3 = doc.createElement("specification");

			if(ElementAbstract.isUrlString(this.specification).equals("true"))
			{
				newElement3.setAttribute("is-url", "true");
			}
			
			newElement3.appendChild(doc.createCDATASection(this.specification));
			childElement.appendChild(newElement3);
		}
		
		Element newElementX;
		
		// NOTE: assumes that only entities have constraints referencing attributes
		for(int n = 0; n < this.ContainedReferenceIds.size(); n++)
		{
			newElementX = doc.createElement("constraint-attribute");
			String value = this.ContainedReferenceIds.get(n);
			newElementX.setAttribute("attribute-id", value);
			childElement.appendChild(newElementX);
		}
						
		
		// #############################################################
		// append parent elements
		// #############################################################		

		parentElement.appendChild(childElement);
		
	}
}
