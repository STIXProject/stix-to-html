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

import java.lang.Exception;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.cli.*;
import org.mitre.stix.stix_to_html.Transformer;


/**
 * Thrown if invalid argument values or combinations are encountered
 * during command line argument processing.
 */
class InvalidArgumentException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidArgumentException(String message){
        super(message);
    }
}


/**
 * The stix-to-html application class. Contains the entry-point and transformation
 * logic.
 */
public class App {	
    public static final int EXIT_SUCCESS = 0;
    public static final int EXIT_FAILURE = 1;
    public static final String APPNAME = "stix-to-html";


    /**
     * Performs XSLT transformation against input file, writing results
     * to the output filename.
     * @param in Input XML filename
     * @param out Output HTML filename
     * @throws Exception
     */
    private static void _transformFile(String in, String out, boolean printDebug) throws Exception {
        InputStreamReader xsl = new InputStreamReader(
            App.class.getClassLoader().getResourceAsStream("stix_to_html.xsl"), "UTF-8"
        );
        
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("debug", printDebug);
        
        Transformer transformer = new Transformer(xsl, parameters);
        transformer.transformFile(in, out);
    }

    
    /**
     * Applies the stix-to-html XSL to each XML file within the input directory.
     * Resulting files are written to the output directory.
     * @param inDir
     * @param outDir
     * @param printDebug
     * @throws Exception
     */
    private static void _transformDirectory(String inDir, String outDir, boolean printDebug)
        throws Exception {
        
        InputStreamReader xsl = new InputStreamReader(
            App.class.getClassLoader().getResourceAsStream("stix_to_html.xsl"), "UTF-8"
        );
        
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("debug", printDebug);
        
        Transformer transformer = new Transformer(xsl, parameters);
        transformer.transformDirectory(inDir, outDir);
    }

    /**
     * Builds command line options.
     * @return Instance of Options containing command-line options.
     */
    private static Options _buildOptions(){
        Option help = OptionBuilder.withLongOpt("help")
                                   .withDescription("print this message")
                                   .create("h");

        Option inFile = OptionBuilder.withLongOpt("infile")
                                     .withArgName("FILE")
                                     .withDescription("input xml filename")
                                     .hasArg()
                                     .create("i");
        
        Option inDir = OptionBuilder.withLongOpt("indir")
                                    .withArgName("DIR")
                                    .withDescription("input directory (must be used with --outdir)")
                                    .hasArg()
                                    .create();

        Option outFile =  OptionBuilder.withLongOpt("outfile")
                                       .withArgName("FILE")
                                       .withDescription("output html filename")
                                       .hasArg()
                                       .create("o");
        
        Option outDir = OptionBuilder.withLongOpt("outdir")
                                     .withArgName("DIR")
                                     .withDescription("output directory (must be used with --indir)")
                                     .hasArg()
                                     .create();
        
        Option debug = OptionBuilder.withLongOpt("debug")
                                    .withDescription("print debug transform information")
                                    .hasArg(false)
                                    .create("d");
        
        Options options = new Options();
        options.addOption(help);
        options.addOption(inFile);
        options.addOption(outFile);
        options.addOption(inDir);
        options.addOption(outDir);
        options.addOption(debug);

        return options;
    }

    /**
     * Returns True if the command line arguments specify single-file
     * processing options.
     * @param line
     * @return
     */
    private static boolean _isProcessFile(CommandLine line) {
        boolean hasInputFile = line.hasOption('i') || line.hasOption("input");
        boolean hasOutputFile = line.hasOption('o') || line.hasOption("output");
        return (hasInputFile && hasOutputFile);
    }
    
    
    /**
     * Returns True if the command line arguments specify directory processing
     * options.
     * @param line
     * @return
     */
    private static boolean _isProcessDir(CommandLine line) {
        boolean hasInputDir = line.hasOption("indir");
        boolean hasOutputDir = line.hasOption("outdir");
        return (hasInputDir && hasOutputDir);
    }
    
   
    /**
     * Validates the arguments passed in via the command line.
     * <p>
     * This method makes sure that an input filename and output filename
     * are provided and that the input filename is an XML file by checking
     * its extension.
     * 
     * @param line The command line arguments.
     * @throws InvalidArgumentException
     */
    private static void _validateArgs(CommandLine line) throws InvalidArgumentException {
        boolean processFile = _isProcessFile(line);
        boolean processDir = _isProcessDir(line);
        
        if(false == (processFile || processDir)){
            throw new InvalidArgumentException("Must provide input and output options");  
        }
        
        if(processFile && processDir){
            throw new InvalidArgumentException("Cannot process both --infile and --indir");
        }
    }


    /**
     * Returns the input filename argument value.
     * @param line The command line arguments
     * @return The input filename value
     */
    private static String _getInputFilename(CommandLine line) {
        String path =  line.hasOption('i') ? line.getOptionValue('i') : line.getOptionValue("input");
        path = path.replace("~", System.getProperty("user.home"));
        return path;
    }


    /**
     * Returns the output filename argument value.
     * @param line The command line arguments
     * @return The output filename.
     */
    private static String _getOutputFilename(CommandLine line) {
        String path = line.hasOption('o') ? line.getOptionValue('o') : line.getOptionValue("output");
        path = path.replace("~", System.getProperty("user.home"));
        return path;
    }

    
    /**
     * Returns the output directory argument value
     * @param line
     * @return
     */
    private static String _getOutputDirectory(CommandLine line) {
        String path = line.getOptionValue("outdir");
        path = path.replace("~", System.getProperty("user.home"));
        return path;
    }
    
    
    /**
     * Returns the input directory argument value
     * @param line
     * @return
     */
    private static String _getInputDirectory(CommandLine line) {
        String path = line.getOptionValue("indir");
        path = path.replace("~", System.getProperty("user.home"));
        return path;
    }
    
    
    /**
     * Returns true if the user passed in a -d or --debug flag.
     * @param line The command line arguments
     * @return True if the user passed in -d or --debug flag.
     */
    private static boolean _isDebug(CommandLine line) {
        return (line.hasOption('d') || line.hasOption("debug"));
    }

    
    /**
     * Displays command line help and exits with the provided exit status.
     * @param options Command line options (used to print help message).
     * @param status The exit status.
     */
    private static void _showHelpAndExit(Options options, int status) {
        String version = _getVersion();
        String appName = APPNAME + " v" + version;
        HelpFormatter formatter = new HelpFormatter();
        
        formatter.printHelp(appName, options);   
        System.exit(status);
    }
    

    /**
     * Parses the embedded version.info.props file that is generated by 
     * Maven. Returns the value of the 'version' key.
     * @return The version of this application.
     */
    private static String _getVersion(){
        String version = null;
        InputStream versionInfo = App.class.getClassLoader().getResourceAsStream("version.info.props");
        Properties props = new Properties();
        
        try {
            props.load(versionInfo);
            version = props.getProperty("version", "Unknown Version");
        } catch(IOException ex) {
            version = "Unknown Version";
        }
        
        return version;
    }
    
    /**
     * Calls _transformDirectory() if the command line arguments specify directory
     * processing or _transformFile() otherwise.
     * @param line
     * @throws Exception
     */
    private static void _doTransform(CommandLine line) throws Exception {
        boolean isDebug = _isDebug(line);
        
        if(_isProcessFile(line)){
            String inFile = _getInputFilename(line);
            String outFile = _getOutputFilename(line);
            _transformFile(inFile, outFile, isDebug);
        }
        
        if(_isProcessDir(line)){
            String inDir = _getInputDirectory(line);
            String outDir = _getOutputDirectory(line);
            _transformDirectory(inDir, outDir, isDebug);
        }
    }
    
    /**
     * Entry point for application.
     * @param args
     */
    public static void main( String[] args ) {	
        CommandLineParser parser = new GnuParser();
        Options options = _buildOptions();

        try {            
            CommandLine line = parser.parse(options, args);

            if(line.hasOption('h') || line.hasOption("help")){
                _showHelpAndExit(options, EXIT_SUCCESS);
            }

            _validateArgs(line);
            _doTransform(line);
            
        } catch(ParseException ex) {
            System.err.println("[!] Invalid arguments: " + ex.getMessage() + "\n");
            _showHelpAndExit(options, EXIT_FAILURE);
        } catch(InvalidArgumentException ex) {
            System.err.println("[!] Invalid arguments: " + ex.getMessage() + "\n");
            _showHelpAndExit(options, EXIT_FAILURE);
        } catch (Exception ex ){
            System.err.println("[!] Fatal error: " + ex.getMessage());
        }

        System.exit(EXIT_SUCCESS);
    }
}
