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


import org.junit.Assert;
import org.junit.Test;

/**
 * @author barmi83
 * @since
 */
public class XmlUtilsTest {
    
    
    public XmlUtilsTest(){
        XmlUtils test = new XmlUtils();
        Assert.assertTrue(true);
    }
    @Test
    public void testUtils_success() throws Exception {
        
        String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><note><to>Tove</to><from>Jani</from><heading>Reminder</heading><body>Don't forget me this weekend!</body></note>";
        
        Assert.assertTrue(XmlUtils.isXmlContent(data));
        
    }
    
    /**
     * Tests wether even a malformed (ie. missing prolog) xml document is
     * detected as xml
     *
     * @throws Exception
     */
    @Test
    public void testXmlEvenWithMissingProlog() throws Exception {
        String data = "<note><to>Tove</to><from>Jani</from><heading>Reminder</heading><body>Don't forget me this weekend!</body></note>";
        Assert.assertTrue(XmlUtils.isXmlContent(data));
    }
    
    @Test
    public void testUtils_fails() throws Exception {
        
        String data = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
                + " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure "
                + "dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non "
                + "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
        
        Assert.assertFalse(XmlUtils.isXmlContent(data));
        
        Assert.assertFalse(XmlUtils.isXmlContent(null));
        
    }
}
