package com.centric.infomodel.structure;

import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonArray;
import javax.json.Json;
import javax.json.JsonValue;

import java.util.List;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;

import org.apache.commons.validator.routines.UrlValidator;

public abstract class ElementAbstract {

	public String id;
	public String parentRefId;
	public String name;
	public String documentation;
	public String visibility;
	
	public static final String EMPTY_STRING = "";
	public static final String UNKNOWN_STRING = "Unknown";
	public static final String MULTIPLICITY_ONE = "1";
	
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
	

	public static String getRef(JsonObject object, String refLabel)
	{
		return getRef(object, refLabel, ElementAbstract.EMPTY_STRING);
	}
	
	public static String getRef(JsonObject object, String refLabel, String defaultRef)
	{
		try
		{
			JsonObject RefJsonObject = object.getJsonObject(refLabel);	
		
			return getRefValue(RefJsonObject, defaultRef);
		
		} catch (Exception e)
		{
			return defaultRef;
		}
		
	}
	
	public static String getRefValue(JsonObject object)
	{
		return getRefValue(object, ElementAbstract.EMPTY_STRING);
	}
	
	public static String getRefValue(JsonObject object, String defaultRef)
	{
		try
		{
		
			if (object != null)
			{
				return object.getString("$ref", defaultRef);
			} else {
				return defaultRef;
			}
		
		} catch (Exception e)
		{
			return defaultRef;
		}
		
	}

}
