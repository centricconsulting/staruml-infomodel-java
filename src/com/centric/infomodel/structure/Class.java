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
	public List<Enumeration> Enumerations = new ArrayList<Enumeration>();
	public List<Constraint> Constraints = new ArrayList<Constraint>();
	
	public Class(JsonObject json)
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
		
		
		
		// populate associations
		JsonResults = json.getJsonArray("ownedElements");
				
		if (JsonResults != null)
		{		
			for(n = 0; n < JsonResults.size(); n++)
			{
				JsonObject JsonResult = JsonResults.getJsonObject(n);
				

				if(JsonResult.getString("_type").equals("UMLConstraint"))
				{
					this.Constraints.add(new Constraint(JsonResult));
				}
				
				if(JsonResult.getString("_type").equals("UMLEnumeration"))
				{
					this.Enumerations.add(new Enumeration(JsonResult));
				}				
				
			}
		}
						
	}
	
	public void populateXmlElement(Element parentElement)
	{

		Document doc = parentElement.getOwnerDocument();
		
		// spawn the top element
		Element childElement = doc.createElement("entity");
		childElement.setAttribute("id",this.id);
		childElement.setAttribute("parent-object-id",this.parentRefId);
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
		int n;
		

		// populate constraints xml
		for(n = 0; n < this.Constraints.size(); n++)
		{
			this.Constraints.get(n).populateXmlElement(childElement);
		}	
		
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
		
		
		// populate attribute xml
		for(n = 0; n < this.Enumerations.size(); n++)
		{
			this.Enumerations.get(n).populateXmlElement(childElement);
		}		


		
		parentElement.appendChild(childElement);
	}
}
