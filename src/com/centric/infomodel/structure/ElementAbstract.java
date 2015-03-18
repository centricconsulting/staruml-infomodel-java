package com.centric.infomodel.structure;

import javax.json.JsonObject;

import org.apache.commons.validator.routines.UrlValidator;

public abstract class ElementAbstract {

	public String id;
	public String parentRefId;
	public String name;
	public String documentation;
	
	public static final String EMPTY_STRING = "";
	public static final String UNKNOWN_STRING = "Unknown";
	
	public static String getBooleanString(boolean value)
	{
		if(value == true)
		{
			return "true";
		} else {
			return "false";
		}
	}
	
	public static String isUrlString(String text)
	{
		final String[] schemes = {"http","https"};
		UrlValidator urlValidator = new UrlValidator(schemes);
		
		if (urlValidator.isValid(text))
		{
			 return "true";
		} else {
			return  "false";
		}
		
	}
	
	public static String getParentRef(JsonObject GenericJsonObject)
	{
		return getRef(GenericJsonObject, "_parent", ElementAbstract.EMPTY_STRING);
	}
	
	public static String getRef(JsonObject GenericJsonObject, String refLabel)
	{
		return getRef(GenericJsonObject, refLabel, ElementAbstract.EMPTY_STRING);
	}
	
	public static String getRef(JsonObject GenericJsonObject, String refLabel, String defaultRef)
	{
		try
		{
			JsonObject RefJsonObject = GenericJsonObject.getJsonObject(refLabel);	
		
			if (RefJsonObject != null)
			{
				return RefJsonObject.getString("$ref", defaultRef);
			} else {
				return defaultRef;
			}
		
		} catch (Exception e)
		{
			return defaultRef;
		}
		
		
		
	}
	

}
