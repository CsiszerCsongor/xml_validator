package com.validation.xml;

import com.validation.xml.exceptions.DirectoryNotFoundException;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd_HH-mm-ss";
    private static final String RESULT_FILENAME_EXTENSION = ".txt";
    //private static final String RESULT_FILE_DIRECTORY = "../";

    public static void main(String[] args) {
        Main main = new Main();

        try {
            String xmlFilesDirectory = "../../";
            String xsdFilePath = args[1] = "../../document.xsd";

            String xsdExtension = "xsd";
            String xmlExtension = "xml";

            FileProcessor fileProcessor = new FileProcessor(xmlFilesDirectory, xsdFilePath, xsdExtension);

            List<String> listOfXmlFiles = new ArrayList<>(fileProcessor.getFilesWithExtensionFromDirectory(xmlExtension));

            String resultFilename = main.createFileName();

            System.out.println();
            System.out.println("Filename : " + resultFilename);
            System.out.println();

            XmlValidator xmlValidator = new XmlValidator(XMLConstants.W3C_XML_SCHEMA_NS_URI.toString(), xsdFilePath);
            xmlValidator.validateXmlList(listOfXmlFiles, resultFilename);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            System.out.println();
            System.out.println("Usage: <program> <directory_of_xmls> <xsd_filename>");
        }
    }

    private String createFileName(){
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.now().format(formatter) + RESULT_FILENAME_EXTENSION;
    }

}

