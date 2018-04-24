package com.centric.infomodel.structure;

import java.util.ArrayList;
import java.util.List;

import javax.json.JsonArray;
import javax.json.JsonObject;

import org.apache.commons.validator.routines.UrlValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Operation extends ElementAbstract {

	public String stereotypeName;
	public String stereotypeId;
	public String specification;

	
	public Operation(JsonObject json)
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
		this.specification = json.getString("specification",ElementAbstract.EMPTY_STRING);
		this.stereotypeName = json.getString("sterotype",ElementAbstract.EMPTY_STRING);
		this.stereotypeId = ElementAbstract.getRef(json, "stereotype");

	
	}
	
	public void populateXmlElement(Element parentElement)
	{	
		
		Document doc = parentElement.getOwnerDocument();
		
		
		// spawn the top element
		Element childElement = doc.createElement("measure");
		childElement.setAttribute("id",this.id);
		childElement.setAttribute("parent-object-id",this.parentRefId);
		childElement.setAttribute("reference-object-id", this.stereotypeId);

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
			Element newElement6 = doc.createElement("specification");

			if(ElementAbstract.isUrlString(this.specification).equals("true"))
			{
				newElement6.setAttribute("is-url", "true");
			}			
			
			newElement6.appendChild(doc.createCDATASection(this.specification));
			childElement.appendChild(newElement6);
		}
		
		parentElement.appendChild(childElement);
		
		
	}	
}
