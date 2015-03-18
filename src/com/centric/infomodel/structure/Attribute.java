package com.centric.infomodel.structure;

import javax.json.JsonObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Attribute extends ElementAbstract {

	public String stereotypeName;
	public String stereotypeId;
	public boolean isUnique;
	public String multiplicity;
	public String dataType;
	
	public Attribute(JsonObject json)
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
		this.isUnique= json.getBoolean("isUnique", false);
		this.multiplicity = json.getString("multiplicity","1");
		this.stereotypeName = json.getString("sterotype",ElementAbstract.EMPTY_STRING);
		this.stereotypeId = ElementAbstract.getRef(json, "stereotype");
		this.dataType = json.getString("type",ElementAbstract.EMPTY_STRING);
		
	}
	
	public void populateXmlElement(Element parentElement)
	{	
		
		Document doc = parentElement.getOwnerDocument();
		
		
		// spawn the top element
		Element childElement = doc.createElement("attribute");
		childElement.setAttribute("id",this.id);
		childElement.setAttribute("class-id",this.parentRefId);
		childElement.setAttribute("parent-ref-id",this.parentRefId);
		childElement.setAttribute("stereotype-class-id",this.stereotypeId);
		

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
		Element newElement3 = doc.createElement("type");
		newElement3.appendChild(doc.createTextNode(this.dataType));
		childElement.appendChild(newElement3);		
		

		// add element
		Element newElement4 = doc.createElement("stereotype-name");
		newElement4.appendChild(doc.createTextNode(this.stereotypeName));
		childElement.appendChild(newElement4);

		
		// add element
		Element newElement6 = doc.createElement("multiplicity");
		newElement6.appendChild(doc.createTextNode(this.multiplicity));
		childElement.appendChild(newElement6);
		
		parentElement.appendChild(childElement);
		
		
	}	
}
