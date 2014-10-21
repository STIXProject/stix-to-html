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
import org.mitre.stix.stix_to_html.XSLProcessor;


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
    private static void transform(String in, String out, boolean printDebug) throws Exception {
        InputStream xsl = App.class.getClassLoader().getResourceAsStream("stix_to_html.xsl");
        
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("debug", printDebug);
        
        XSLProcessor processor = XSLProcessor.Instance();
        processor.process(
            new InputStreamReader(new FileInputStream(new File(in)), "UTF-8"), 
            new InputStreamReader(xsl), 
            new OutputStreamWriter(new FileOutputStream(new File(out)), "UTF-8"),
            parameters
        );
    }


    /**
     * Builds command line options.
     * @return Instance of Options containing command-line options.
     */
    private static Options buildOptions(){
        Option help = OptionBuilder.withLongOpt("help")
                                   .withDescription("print this message")
                                   .create("h");

        Option inFile = OptionBuilder.withLongOpt("infile")
                                     .withArgName("FILE")
                                     .withDescription("input xml filename")
                                     .hasArg()
                                     .create("i");

        Option outFile =  OptionBuilder.withLongOpt("outfile")
                                       .withArgName("FILE")
                                       .withDescription("output html filename")
                                       .hasArg()
                                       .create("o");
        
        Option debug = OptionBuilder.withLongOpt("debug")
                                    .withDescription("print debug transform information")
                                    .hasArg(false)
                                    .create("d");
        
        Options options = new Options();
        options.addOption(help);
        options.addOption(inFile);
        options.addOption(outFile);
        options.addOption(debug);

        return options;
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
    private static void validateArgs(CommandLine line) throws InvalidArgumentException {
        boolean hasInput = line.hasOption('i') || line.hasOption("input");
        boolean hasOutput = line.hasOption('o') || line.hasOption("output");

        if(false == (hasInput && hasOutput)){
            throw new InvalidArgumentException("Must supply both an input and output filename");
        }

        String inFile = getInputFilename(line);
        if(false == inFile.toLowerCase().endsWith(".xml")) {
            throw new InvalidArgumentException("Input must end with '.xml'");
        }
    }


    /**
     * Returns the input filename argument value.
     * @param line The command line arguments
     * @return The input filename value
     */
    private static String getInputFilename(CommandLine line) {
        String path =  line.hasOption('i') ? line.getOptionValue('i') : line.getOptionValue("input");
        path = path.replace("~", System.getProperty("user.home"));
        return path;
    }


    /**
     * Returns the output filename argument value.
     * @param line The command line arguments
     * @return The output filename.
     */
    private static String getOutputFilename(CommandLine line) {
        String path = line.hasOption('o') ? line.getOptionValue('o') : line.getOptionValue("output");
        path = path.replace("~", System.getProperty("user.home"));
        return path;
    }

    /**
     * Returns true if the user passed in a -d or --debug flag.
     * @param line The command line arguments
     * @return True if the user passed in -d or --debug flag.
     */
    private static boolean getDebug(CommandLine line) {
        return (line.hasOption('d') || line.hasOption("debug"));
    }

    
    /**
     * Displays command line help and exits with the provided exit status.
     * @param options Command line options (used to print help message).
     * @param status The exit status.
     */
    private static void showHelpAndExit(Options options, int status) {
        String version = getVersion();
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
    private static String getVersion(){
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
     * Entry point for application.
     * @param args
     */
    public static void main( String[] args ) {	
        CommandLineParser parser = new GnuParser();
        Options options = buildOptions();

        try {            
            CommandLine line = parser.parse(options, args);

            if(line.hasOption('h') || line.hasOption("help")){
                showHelpAndExit(options, EXIT_SUCCESS);
            }

            validateArgs(line);

            String inFile = getInputFilename(line);
            String outFile = getOutputFilename(line);
            boolean printDebug = getDebug(line);
            transform(inFile, outFile, printDebug);
            
        } catch(ParseException ex) {
            System.err.println("[!] Invalid arguments: " + ex.getMessage() + "\n");
            showHelpAndExit(options, EXIT_FAILURE);
        } catch(InvalidArgumentException ex) {
            System.err.println("[!] Invalid arguments: " + ex.getMessage() + "\n");
            showHelpAndExit(options, EXIT_FAILURE);
        } catch (Exception ex ){
            System.err.println("[!] Fatal error: " + ex.getMessage());
        }

        System.exit(EXIT_SUCCESS);
    }
}
