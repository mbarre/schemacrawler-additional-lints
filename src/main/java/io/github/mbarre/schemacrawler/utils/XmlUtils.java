package io.github.mbarre.schemacrawler.utils;

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
