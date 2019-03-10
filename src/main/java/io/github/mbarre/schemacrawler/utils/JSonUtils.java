package io.github.mbarre.schemacrawler.utils;

/*
 * #%L
 * Additional SchemaCrawler Lints
 * %%
 * Copyright (C) 2015 - 2016 github
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.google.gson.Gson;
import org.slf4j.LoggerFactory;

/**
 * Provides some tools around JSon to share them on the whole project.
 * @author adriens
 */
public class JSonUtils {

	private JSonUtils(){
		throw new IllegalAccessError("Utility class.");
	}
    
    /**
     * Test if a string is a valid json
     * @param content content
     * @return is the string's content is a valid json or not
     */
    public static boolean isJsonContent(String content){
		if (content == null)
			return false;
		
		Gson gson = new Gson();
		try {
		     gson.fromJson(content, Object.class);
		    return true;
		} catch (Exception e) {
			LoggerFactory.getLogger(JSonUtils.class).info("String is not JSON.", e);
			return false;
		}
		
	}
	
}
