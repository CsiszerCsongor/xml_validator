package com.validation.xml;

import javax.xml.XMLConstants;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    private static final String RESULT_FILENAME_EXTENSION = ".txt";
    private static final String XSD_FILE_EXTENSION = "xsd";
    private static final String XML_FILE_EXTENSION = "xml";
    private static final String PROGRAM_USAGE = "Usage: java -jar <program_name> <directory_of_xmls> <xsd_filename>\nDefault program name: XMLValidator.jar";
    private static final String VALIDATION_TERMINATED_SUCCESSFULLY = "Validation terminated successfully!";

    public static void main(String[] args) {
        if(args.length < 2){
            System.out.println(PROGRAM_USAGE);
            return;
        }
        Main main = new Main();

        try {
            String xmlFilesDirectory = args[0];
            String xsdFilePath = args[1];

            FileProcessor fileProcessor = new FileProcessor(xmlFilesDirectory, xsdFilePath, XSD_FILE_EXTENSION);
            List<String> listOfXmlFiles = new ArrayList<>(fileProcessor.getFilesWithExtensionFromDirectory(XML_FILE_EXTENSION));

            String resultFilename = main.createFileName();

            XmlValidator xmlValidator = new XmlValidator(XMLConstants.W3C_XML_SCHEMA_NS_URI, xsdFilePath);
            xmlValidator.validateXmlList(listOfXmlFiles, fileProcessor.getDirectoryPath() + resultFilename);

            System.out.println(VALIDATION_TERMINATED_SUCCESSFULLY);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            System.out.println();
            System.out.println(PROGRAM_USAGE);
        }
    }

    private String createFileName(){
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.now().format(formatter) + RESULT_FILENAME_EXTENSION;
    }

}

