package com.centric.infomodel.structure;
import javax.json.JsonObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Attribute extends ElementAbstract {

	public String stereotypeName;
	public String stereotypeId;
	public boolean isUnique;
	public String multiplicity;
	public String typeId;
	public String typeName;
	public boolean isID;
	public boolean isDerived;
	public boolean isLeaf;
	
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
		this.isID= json.getBoolean("isID", false);
		this.isDerived= json.getBoolean("isDerived", false);
		this.isLeaf= json.getBoolean("isLeaf", false);
		this.multiplicity = json.getString("multiplicity",ElementAbstract.EMPTY_STRING);
		this.stereotypeName = json.getString("sterotype",ElementAbstract.EMPTY_STRING);
		this.stereotypeId = ElementAbstract.getRef(json, "stereotype");
		this.typeName = json.getString("type",ElementAbstract.EMPTY_STRING);
		this.typeId = ElementAbstract.getRef(json, "type");
			
	}
	
	public void populateXmlElement(Element parentElement)
	{	
			
		Document doc = parentElement.getOwnerDocument();
		
		
		// spawn the top element
		Element childElement = doc.createElement("attribute");
		childElement.setAttribute("id",this.id);

		// flags
		if(this.isUnique) childElement.setAttribute("is-unique",ElementAbstract.getBooleanString(this.isUnique));
		//childElement.setAttribute("is-identifier",ElementAbstract.getBooleanString(this.isID));
		if(this.isDerived) childElement.setAttribute("is-derived",ElementAbstract.getBooleanString(this.isDerived));
		if(this.isLeaf) childElement.setAttribute("is-operational",ElementAbstract.getBooleanString(this.isLeaf));

		// other id references
		childElement.setAttribute("parent-object-id",this.parentRefId);

		
		if(this.stereotypeId.length() > 0)
		{
			childElement.setAttribute("reference-object-id",this.stereotypeId);
		}

		if(this.typeId.length() > 0)
		{
			childElement.setAttribute("type-object-id",this.typeId);
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
		// exclude the standard case where multiplicity is 1
		if(!this.multiplicity.equals("1") && this.multiplicity.length() > 0)
		{
			Element newElement6 = doc.createElement("multiplicity");
			newElement6.appendChild(doc.createTextNode(this.multiplicity));
			childElement.appendChild(newElement6);
		}
		
		parentElement.appendChild(childElement);
		
		
	}	
}
