/*
Copyright (c) 2014, The MITRE Corporation
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of The MITRE Corporation nor the 
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
package org.mitre.stix.stix_to_html;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.URIResolver;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.saxon.Configuration;
import net.sf.saxon.Controller;
import net.sf.saxon.lib.UnparsedTextURIResolver;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.jaxp.TransformerImpl;


/**
 * Resolves URIs from XSL import directives.
 *
 */
class ResourceURIResolver implements URIResolver {
    public Source resolve(String href, String base) throws TransformerException {
        return new StreamSource(getClass().getClassLoader().getResourceAsStream(href));
    }
}


/**
 * Resolves URIs found in unparsed-text() methods. This is a bit of a hack.
 *
 */
class ResourceUnparsedTextURIResolver implements UnparsedTextURIResolver {
    public Reader resolve(URI absoluteURI, String encoding, Configuration config) throws XPathException {
        String strURI = absoluteURI.toString();
        String filename = strURI.substring((strURI.lastIndexOf('/') + 1), strURI.length());
        return new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename.toString()));
    }
}


/**
 *
 * @author BAKERJ & bworrell
 */
public class XSLProcessor {
    private Transformer _transformer;
    
    /**
     * Constructor for XSLProcessor. Performs XSL transforms against XML documents.
     * @param xsl The XSL file to use when processing XML documents.
     * @throws IOException
     * @throws TransformerConfigurationException
     */
    public XSLProcessor(Reader xsl) throws IOException, TransformerConfigurationException {
        this(xsl, new HashMap<String, Object>());
    }
    
    
    /**
     * Constructor for XSLProcessor. Performs XSL transforms against XML documents.
     * @param xsl The XSL file to use when processing XML documents
     * @param parameters XSL parameter map
     * @throws IOException
     * @throws TransformerConfigurationException
     */
    public XSLProcessor(Reader xsl, Map<String, Object> parameters) 
        throws IOException, TransformerConfigurationException {
        
        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        _initTransformer(xsl, parameters);
       
    }


    /**
     * Initializes the private instance <code>_transfomer</code> attribute.
     * @param xsl
     * @param parameters
     * @throws IOException
     * @throws TransformerConfigurationException
     */
    private void _initTransformer(Reader xsl, Map<String, Object> parameters)
        throws IOException, TransformerConfigurationException {
        
        Templates template = _getNewTemplates(xsl);
        _transformer = template.newTransformer();
        _setUnparsedTextResolver(_transformer);
        _setParameters(_transformer, parameters);
    }
    
    
    /**
     * Returns a <code>Tempaltes</code> instance for the <code>xsl</code> stylesheet.
     * Called by <code>_initTransformer()</code>.
     * @param xsl
     * @return
     * @throws IOException
     * @throws TransformerConfigurationException
     */
    private Templates _getNewTemplates(Reader xsl) 
        throws IOException, TransformerConfigurationException {
        
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setURIResolver(new ResourceURIResolver());
        Templates template = factory.newTemplates(new StreamSource(xsl));
        
        return template;
    }
    

    /**
     * Sets the <code>unparsed-text()</code> uri resolver for the <code>transformer</code>.
     * This is needed when operating from within a jar file.
     * @param transformer
     */
    private void _setUnparsedTextResolver(Transformer transformer){
        TransformerImpl impl = (TransformerImpl)transformer;
        Controller controller = impl.getUnderlyingController();
        controller.setUnparsedTextURIResolver(new ResourceUnparsedTextURIResolver());
    }
    
    
    /**
     * Attaches XSL parameters to the <code>transformer</code>.
     * @param transformer
     * @param parameters
     */
    private void _setParameters(Transformer transformer, Map<String, Object> parameters) {
        for (String name : parameters.keySet()) {
            transformer.setParameter(name, parameters.get(name));
        }
    }
    
    
    /**
     * Runs the XSL transform against the <code>xmlFile</code>. Results are written to 
     * <code>output</code>.
     * @param xmlFile
     * @param output
     * @throws TransformerException
     */
    public void process(Reader xmlFile, Writer output) throws TransformerException {
        process(new StreamSource(xmlFile), new StreamResult(output));
    }

    
    /**
     * Runs the XSL transform against <code>xml</code>. Results are written to <code>result</code>.
     * @param xml
     * @param result
     * @throws TransformerException
     */
    public void process(Source xml, Result result) throws TransformerException {
        try {
            _transformer.transform(xml, result);
        } catch (TransformerConfigurationException tce) {
            throw new TransformerException(tce.getMessageAndLocation());
        } catch (TransformerException te) {
            throw new TransformerException(te.getMessageAndLocation());
        }
    }
}
