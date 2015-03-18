package com.centric.infomodel.structure;

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
		this.specification = json.getString("specification","1");
		this.stereotypeName = json.getString("sterotype",ElementAbstract.EMPTY_STRING);
		this.stereotypeId = ElementAbstract.getRef(json, "stereotype");
		
	}
	
	public void populateXmlElement(Element parentElement)
	{	
		
		Document doc = parentElement.getOwnerDocument();
		
		
		// spawn the top element
		Element childElement = doc.createElement("operation");
		childElement.setAttribute("id",this.id);
		childElement.setAttribute("class-id",this.parentRefId);
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
		Element newElementST = doc.createElement("stereotype");
		newElementST.setAttribute("class-id", this.stereotypeId);
		
		Element newElementName = doc.createElement("name");
		newElementST.appendChild(newElementName);
		newElementName .appendChild(doc.createTextNode(this.stereotypeName));
		
		childElement.appendChild(newElementST);

		String isUrlString;
		String[] schemes = {"http","https"};
		UrlValidator urlValidator = new UrlValidator(schemes);
		if (urlValidator.isValid(this.specification))
		{
			isUrlString = "true";
		} else {
			isUrlString = "false";
		}
		
		// add element
		Element newElement6 = doc.createElement("specification");
		newElement6.setAttribute("is-url", isUrlString);
		newElement6.appendChild(doc.createTextNode(this.specification));
		childElement.appendChild(newElement6);
		
		parentElement.appendChild(childElement);
		
		
	}	
}
