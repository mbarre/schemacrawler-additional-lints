/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.mbarre.schemacrawler.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Provides some tools around JSon to share them on the whole project.
 * @author adriens
 */
public class JSonUtils {
    
    /**
     * Test if a string is a valid json
     * @param content content
     * @return is the string's content is a valid json or not
     */
    public static boolean isJsonContent(String content){
		if (content == null)
			return false;
		
		try{
			new JSONObject(content);	
			return true;
		}catch(JSONException e){
			return false;
		}
		
	}
	
}
