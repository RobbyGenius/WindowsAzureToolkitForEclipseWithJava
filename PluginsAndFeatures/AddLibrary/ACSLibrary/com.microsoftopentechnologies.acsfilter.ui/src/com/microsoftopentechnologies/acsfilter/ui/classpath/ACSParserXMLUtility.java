/**
 * Copyright 2013 Persistent Systems Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.microsoftopentechnologies.acsfilter.ui.classpath;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.microsoftopentechnologies.acsfilter.ui.activator.Activator;

/**
 * Utility class used to parse and save xml files.
 */
final class ACSParserXMLUtility {

    private ACSParserXMLUtility() {

    }
    /** Parses XML file and returns XML document.
     * @param fileName .
     * @return XML document or <B>null</B> if error occured
     * @throws WindowsAzureInvalidProjectOperationException
     */
    protected static Document parseXMLFile(final String fileName)
    throws Exception {
        try {
            DocumentBuilder docBuilder;
            Document doc = null;
            DocumentBuilderFactory docBuilderFactory =
                DocumentBuilderFactory.newInstance();
            docBuilderFactory.setIgnoringElementContentWhitespace(true);
            docBuilder = docBuilderFactory.newDocumentBuilder();
            File xmlFile = new File(fileName);
            doc = docBuilder.parse(xmlFile);
            return doc;
        } catch (Exception e) {
        	Activator.getDefault().log(e.getMessage(), e);
        	throw new Exception(String.format("%s%s",
        			Messages.acsErrMsg, e.getMessage()));
        }

    }

    /** save XML file and saves XML document.
     * @param fileName
     * @param doc
     * @return XML document or <B>null</B> if error occured
     * @throws IOException
     * @throws WindowsAzureInvalidProjectOperationException
     */
    protected static boolean saveXMLFile(String fileName, Document doc)
    throws IOException, Exception {
        File xmlFile = null;
        FileOutputStream fos = null;
        Transformer transformer;
        try {
            xmlFile = new File(fileName);
            fos = new FileOutputStream(xmlFile);
            TransformerFactory transFactory =
                TransformerFactory.newInstance();
            transformer = transFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult destination = new StreamResult(fos);
            // transform source into result will do save
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source, destination);
        } catch (Exception excp) {
        	Activator.getDefault().log(excp.getMessage(), excp);
        	throw new Exception(String.format("%s%s",
        			Messages.saveErrMsg, excp.getMessage()));
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return true;
    }
}
