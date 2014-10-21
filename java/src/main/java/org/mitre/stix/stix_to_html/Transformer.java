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
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.mitre.stix.stix_to_html.XSLProcessor;

public class Transformer {
        
    private XSLProcessor _processor = null;
    
    /**
     * Returns an instance of <code>Transformer</code>.
     * @param xsl The XSLT file to apply to XML documents.
     * @param parameters XSL parameter map.
     * @throws TransformerConfigurationException
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Transformer(Reader xsl, Map<String, Object> parameters)
        throws  TransformerConfigurationException, UnsupportedEncodingException, 
                FileNotFoundException, IOException {
        
        _processor = new XSLProcessor(xsl, parameters);
    }
    
    /**
     * Returns an instance of <code>Transformer</code>.
     * @param xslFilename The XSLT file to apply to XML documents.
     * @param parameters XSL parameter map.
     * @throws TransformerConfigurationException
     * @throws UnsupportedEncodingException
     * @throws FileNotFoundException
     * @throws IOException
     */
    public Transformer(String xslFilename, Map<String, Object> parameters)
        throws  TransformerConfigurationException, UnsupportedEncodingException, 
                FileNotFoundException, IOException {
        
        this(new InputStreamReader(new FileInputStream(xslFilename), "UTF-8"), parameters);
    }
       
    
    /**
     * Transforms the XML file <code>inFile</code>, writing the results to <code>outFile</code>.
     * @param inFile
     * @param outFile
     * @throws TransformerException
     */
    public void transformFile(String inFile, String outFile) 
        throws  FileNotFoundException, UnsupportedEncodingException, TransformerException {
        
        System.out.println("[-] Transforming " + inFile + " into " + outFile);
        transformFile(
            new InputStreamReader(new FileInputStream(new File(inFile)), "UTF-8"), 
            new OutputStreamWriter(new FileOutputStream(new File(outFile)), "UTF-8")
        );
    }
    
    
    /**
     * Transforms the XML file <code>inFile</code>, writing the results to <code>outFile</code>.
     * @param inFile
     * @param outFile
     * @throws TransformerException
     */
    public void transformFile(Reader inFile, Writer outFile) throws TransformerException {
        _processor.process(inFile, outFile);
    }
    
    
    /**
     * Transforms the XML files found in <code>inDir</code> into HTML documents, writing the
     * results to <code>outDir</code>.
     * @param inDir
     * @param outDir
     * @throws Exception
     */
    public void transformDirectory(String inDir, String outDir) throws Exception {
        transformDirectory(inDir, outDir, ".html");
    }
    
    
    /**
     * Checks that <code>dir</code> is a directory.
     * @param dir
     * @throws Exception
     */
    private void _checkIsDir(File dir) throws Exception { 
        if(false == dir.isDirectory()){
            throw new Exception(
                "Directory name is not a directory or cannot be found: " + dir.getAbsolutePath()
            );
        }
    }
    
    
    /**
     * Returns a file path for the resulting document. 
     * 
     * This takes the input filename and appends @suffix to it, and appends that filename
     * to the @outDir path.
     */
    private String _getOutputFilePath(File inFile, File outDir, String suffix) {
        String inFilename = inFile.getName();
        String outFilename = (
            outDir.getAbsolutePath() + File.separator + inFilename + suffix
        );
        return outFilename;
    }
    
    /**
     * Runs the stix-to-html transform against each of the files in <code>files</code>.
     * Results are written to <code>outDir</code> with an extension of <code>suffix</code>.
     * @param files A list of XML files
     * @param outDir An output directory.
     * @param suffix An extension to put on each transformed result file.
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * @throws TransformerException
     */
    private void _transformFiles(List<File> files, File outDir, String suffix) 
        throws  FileNotFoundException, UnsupportedEncodingException, TransformerException {
        
        for (File file : files) {
          String outFilePath = _getOutputFilePath(file, outDir, suffix);
          transformFile(file.getAbsolutePath(), outFilePath);
        }   
    }
    
    
    /**
     * Runs the stix-to-html transform against each of the files in <code>inDir</code>.
     * Results are written to <code>outDir</code> with an extension of <code>suffix</code>.
     * @param inDir
     * @param outDir
     * @param suffix
     * @throws Exception
     */
    public void transformDirectory(String inDir, String outDir, String suffix)
        throws Exception {
        
        File outFolder = new File(outDir);
        List<File> files = _getXmlFiles(inDir);
        
        _checkIsDir(outFolder);
        _transformFiles(files, outFolder, suffix);
    }
    
   
    /**
     * Returns a list of XML files found in <code>dir</code>.
     * @param dir
     * @return
     * @throws Exception
     */
    private List<File> _getXmlFiles(String dir) throws Exception {
        List<File> files = new LinkedList<File>();            
        File folder = new File(dir);
        
        _checkIsDir(folder);
        
        for (File file : folder.listFiles()) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".xml")) {
                files.add(file);
            }
        }
        
        return files;
    }
}

