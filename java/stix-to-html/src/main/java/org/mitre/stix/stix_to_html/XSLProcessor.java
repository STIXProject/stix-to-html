package org.mitre.stix.stix_to_html;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author BAKERJ
 */
public class XSLProcessor {

    /** The singleton instance variable. */
    private static XSLProcessor instance = null;

    /**
     * This class is a singelton. 
     * 
     * @return the singleton instance.
     */
    public static XSLProcessor Instance() {

        if (XSLProcessor.instance == null) {
            XSLProcessor.instance = new XSLProcessor();
        }

        return XSLProcessor.instance;
    }
    /** 
     * A map of cached stylesheets 
     * key is xsl file name
     * value is the complied template
     */
    private HashMap<String, Templates> templateCache = null;

    /** Creates a new instance of ProcessXML */
    private TransformerFactory factory;

    /** Creates a new instance of XSLProcessor */
    private XSLProcessor() {
        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");

        factory = TransformerFactory.newInstance();

        templateCache = new HashMap<String, Templates>();
    }

    /** Transform an XML and XSL document as <code>Reader</code>s,
     *  placing the resulting transformed document in a 
     *  <code>Writer</code>. Convenient for handling an XML 
     *  document as a String (<code>StringReader</code>) residing
     * in memory, not on disk. The output document could easily be
     *  handled as a String (<code>StringWriter</code>) or as a
     *  <code>JSPWriter</code> in a JavaServer page.
     */
    public void process(Reader xmlFile, Reader xslFile, Writer output) throws TransformerException {

        process(new StreamSource(xmlFile), new StreamSource(xslFile), new StreamResult(output));
    }

    /** Transform an XML and XSL document as <code>File</code>s,
     *  placing the resulting transformed document in a 
     *  <code>Writer</code>. The output document could easily 
     *  be handled as a String (<code>StringWriter</code)> or as 
     *  a <code>JSPWriter</code> in a JavaServer page.
     */
    public void processWithCache(Reader xmlFile, File xslFile, Writer output, Map<String, Object> parameters) throws TransformerException, IOException {

        process(new StreamSource(xmlFile), this.getTemplatesForXslFile(xslFile), new StreamResult(output), parameters);
    }

    /** Transform an XML and XSL document as <code>File</code>s,
     *  placing the resulting transformed document in a 
     *  <code>Writer</code>. The output document could easily 
     *  be handled as a String (<code>StringWriter</code)> or as 
     *  a <code>JSPWriter</code> in a JavaServer page.
     */
    public void process(File xmlFile, File xslFile, Writer output) throws TransformerException {
        process(new StreamSource(xmlFile), new StreamSource(xslFile), new StreamResult(output));
    }

    /** Transform an XML <code>File</code> based on an XSL 
     *  <code>File</code>, placing the resulting transformed 
     *  document in a <code>OutputStream</code>. Convenient for 
     *  handling the result as a <code>FileOutputStream</code> or 
     *  <code>ByteArrayOutputStream</code>.
     */
    public void process(File xmlFile, File xslFile, OutputStream out) throws TransformerException {
        process(new StreamSource(xmlFile), new StreamSource(xslFile), new StreamResult(out));
    }

    /** Transform an XML source using XSLT based on a new template
     *  for the source XSL document. The resulting transformed 
     *  document is placed in the passed in <code>Result</code> 
     *  object.
     */
    public void process(Source xml, Source xsl, Result result) throws TransformerException {
        try {
            Templates template = factory.newTemplates(xsl);
            Transformer transformer = template.newTransformer();
            transformer.transform(xml, result);
        } catch (TransformerConfigurationException tce) {
            throw new TransformerException(tce.getMessageAndLocation());
        } catch (TransformerException te) {
            throw new TransformerException(te.getMessageAndLocation());
        }
    }

    /** Transform an XML source using XSLT based on a new template
     *  for the source XSL document. The resulting transformed 
     *  document is placed in the passed in <code>Result</code> 
     *  object.
     */
    private void process(Source xml, Templates template, Result result, Map<String, Object> parameters) throws TransformerException {
        try {
            Transformer transformer = template.newTransformer();

            for (String name : parameters.keySet()) {
                transformer.setParameter(name, parameters.get(name));
            }

            transformer.transform(xml, result);
        } catch (TransformerConfigurationException tce) {
            throw new TransformerException(tce.getMessageAndLocation());
        } catch (TransformerException te) {
            throw new TransformerException(te.getMessageAndLocation());
        }
    }

    /**
     * Check the cache for templates for the input file. if not found create 
     * and cache new templaes.
     * 
     * @param xslFile
     * @return
     */
    private Templates getTemplatesForXslFile(File xslFile) throws IOException, TransformerConfigurationException {

        Templates templates = null;

        if (templateCache.containsKey(xslFile.getCanonicalPath())) {
            templates = templateCache.get(xslFile.getCanonicalPath());
        } else {
            TransformerFactory tfactory = TransformerFactory.newInstance();
            templates = tfactory.newTemplates(new StreamSource(xslFile));
            templateCache.put(xslFile.getCanonicalPath(), templates);
        }
        return templates;
    }
}