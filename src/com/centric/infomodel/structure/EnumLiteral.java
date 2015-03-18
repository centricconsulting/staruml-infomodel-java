package com.centric.infomodel.structure;

import javax.json.JsonObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EnumLiteral extends ElementAbstract {

	public EnumLiteral(JsonObject json)
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
		
	}
	
	public void populateXmlElement(Element parentElement)
	{		
		
		Document doc = parentElement.getOwnerDocument();
		
		
		// spawn the top element
		Element childElement = doc.createElement("attribute");
		childElement.setAttribute("id",this.id);
		childElement.setAttribute("class-id",this.parentRefId);
		childElement.setAttribute("parent-ref-id",this.parentRefId);
		
		
		// add element
		Element newElement2 = doc.createElement("documentation");
		newElement2.setAttribute("is-url", ElementAbstract.isUrlString(this.documentation));
		newElement2.appendChild(doc.createTextNode(this.documentation));
		childElement.appendChild(newElement2);
		
		parentElement.appendChild(childElement);
	}
	
}
