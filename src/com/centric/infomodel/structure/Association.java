package com.centric.infomodel.structure;

import javax.json.JsonObject;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Association extends ElementAbstract {

	public String end1Name;
	public String end1ClassId;
	public String end1Multiplicity;
	public String end1Aggregation;
	
	public String end2Name;
	public String end2ClassId;
	public String end2Multiplicity;
	public String end2Aggregation;	
	
	public Association(JsonObject json)
	{
		populate(json);  	
	}
	
	public void populate(JsonObject json)
	{
		// required
		// note: associations do not require names
		this.name = json.getString("name", ElementAbstract.EMPTY_STRING);
		this.id = json.getString("_id", ElementAbstract.EMPTY_STRING);
		
		// optional
		this.documentation = json.getString("documentation", ElementAbstract.EMPTY_STRING);
		this.parentRefId = ElementAbstract.getParentRef(json);
		
		
		// get end1 attributes
		JsonObject JsonResult;
		JsonResult = json.getJsonObject("end1");
		
		this.end1ClassId = ElementAbstract.getParentRef(JsonResult);
		this.end1Aggregation = JsonResult.getString("aggregation", ElementAbstract.EMPTY_STRING);
		this.end1Multiplicity = JsonResult.getString("multiplicity", "1");
		this.end1Name = JsonResult.getString("name", ElementAbstract.EMPTY_STRING);
		
		
		JsonResult = json.getJsonObject("end2");
		
		this.end2ClassId = ElementAbstract.getParentRef(JsonResult);
		this.end2Aggregation = JsonResult.getString("aggregation", ElementAbstract.EMPTY_STRING);
		this.end2Multiplicity = JsonResult.getString("multiplicity", "1");
		this.end2Name = JsonResult.getString("name", ElementAbstract.EMPTY_STRING);
		
	}
	
	public void populateXmlElement(Element parentElement)
	{	
		

		Document doc = parentElement.getOwnerDocument();
		
		
		// spawn the top element
		Element childElement = doc.createElement("association");
		childElement.setAttribute("id",this.id);
		childElement.setAttribute("class-id",this.parentRefId);
		childElement.setAttribute("parent-ref-id",this.parentRefId);
		

		// add element
		Element newElement1 = doc.createElement("name");
		newElement1.appendChild(doc.createTextNode(this.name));
		childElement.appendChild(newElement1);
				
		// add element
		Element newElement2 = doc.createElement("documentation");
		newElement2.appendChild(doc.createTextNode(this.documentation));
		childElement.appendChild(newElement2);
						
		
		// generic element variable
		Element ElementX;
		Element originationElement;
		Element destinationElement;
		
		// set direction orientations
		String end1Orientation, end2Orientation;
		
		if(this.end1ClassId.equals(this.parentRefId))
		{
			end1Orientation = "outbound";
		} else {
			end1Orientation = "inbound";			
		}
		
		if(this.end2ClassId.equals(this.parentRefId))
		{
			end2Orientation = "outbound";
		} else {
			end2Orientation = "inbound";			
		}
		
		
		// #############################################################
		// create a direction element 
		// #############################################################
		
		Element end1Element = doc.createElement("navigation");
		end1Element.setAttribute("orientation", end1Orientation);
		
		// ========== ORIGINATION ======================================		
		originationElement = doc.createElement("origination");
		originationElement.setAttribute("class-id", this.end1ClassId);
		end1Element.appendChild(originationElement);
		
		// add element
		ElementX = doc.createElement("name");
		ElementX.appendChild(doc.createTextNode(this.end1Name));
		originationElement.appendChild(ElementX);
		
		// add element
		ElementX = doc.createElement("multiplicity");
		ElementX.appendChild(doc.createTextNode(this.end1Multiplicity));
		originationElement.appendChild(ElementX);		
		
		// add element
		ElementX = doc.createElement("aggregation");
		ElementX.appendChild(doc.createTextNode(this.end1Aggregation));
		originationElement.appendChild(ElementX);
		
		// ========== DESTINATION =========================================
		destinationElement = doc.createElement("destination");
		end1Element.appendChild(destinationElement);
		destinationElement.setAttribute("class-id", this.end2ClassId);		
		
		// add element
		ElementX = doc.createElement("name");
		ElementX.appendChild(doc.createTextNode(this.end2Name));
		destinationElement.appendChild(ElementX);
		
		// add element
		ElementX = doc.createElement("multiplicity");
		ElementX.appendChild(doc.createTextNode(this.end2Multiplicity));
		destinationElement.appendChild(ElementX);		
		
		// add element
		ElementX = doc.createElement("aggregation");
		ElementX.appendChild(doc.createTextNode(this.end2Aggregation));
		destinationElement.appendChild(ElementX);	
		
		childElement.appendChild(end1Element);
		
		// #############################################################
		// create a direction element 
		// #############################################################
		Element end2Element = doc.createElement("navigation");
		end2Element.setAttribute("orientation", end2Orientation);
	
		// ========== ORIGINATION ======================================		
		
		originationElement = doc.createElement("origination");
		originationElement.setAttribute("class-id", this.end2ClassId);
		end2Element.appendChild(originationElement);		
		
		// add element
		ElementX = doc.createElement("name");
		ElementX.appendChild(doc.createTextNode(this.end2Name));
		originationElement.appendChild(ElementX);
		
		// add element
		ElementX = doc.createElement("multiplicity");
		ElementX.appendChild(doc.createTextNode(this.end2Multiplicity));
		originationElement.appendChild(ElementX);		
		
		// add element
		ElementX = doc.createElement("aggregation");
		ElementX.appendChild(doc.createTextNode(this.end2Aggregation));
		originationElement.appendChild(ElementX);
		
		// ========== DESTINATION ======================================
		
		destinationElement = doc.createElement("destination");
		destinationElement.setAttribute("class-id", this.end1ClassId);
		end2Element.appendChild(destinationElement);
		
		// add element
		ElementX = doc.createElement("name");
		ElementX.appendChild(doc.createTextNode(this.end1Name));
		destinationElement.appendChild(ElementX);
		
		// add element
		ElementX = doc.createElement("multiplicity");
		ElementX.appendChild(doc.createTextNode(this.end1Multiplicity));
		destinationElement.appendChild(ElementX);		
		
		// add element
		ElementX = doc.createElement("aggregation");
		ElementX.appendChild(doc.createTextNode(this.end1Aggregation));
		destinationElement.appendChild(ElementX);	
		
		childElement.appendChild(end2Element);
		
		// #############################################################
		// append parent elements
		// #############################################################		

		parentElement.appendChild(childElement);
		
	}
}
