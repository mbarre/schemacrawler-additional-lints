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

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author salad74
 */
public class XmlUtils {
    
    /**
     * Tells wether a column contens XML data or not.
     * @param data the string to test
     * @return if the string is a valid xml (or not)
     */
    public static final boolean isXmlContent(String data) {
        
        if(data == null)
            return false;
        
        DocumentBuilder db;
        try {
            
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            db.setErrorHandler(new SimpleErrorHandler());
            
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(data));
            db.parse(is);
            
            return true;
            
        } catch (SAXException | IOException  | ParserConfigurationException e) {
            return false;
        }
    }
    
    
    private static class SimpleErrorHandler implements ErrorHandler {
        @Override
        public void warning(SAXParseException e) throws SAXException {
            // Do nothing
        }
        
        @Override
        public void error(SAXParseException e) throws SAXException {
            // Do nothing
        }
        
        @Override
        public void fatalError(SAXParseException e) throws SAXException {
            // Do nothing
        }
    }
    
}
