package com.validation.xml;

import org.xml.sax.SAXException;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.List;

public class XmlValidator {
    private File xsdFile;
    private Validator validator;
    private static final String FILE_NOT_FOUND_ON_PATH = "File not found on path: ";
    private static final String VALIDATION_RESULT_VALID = "  => validation result: valid!";
    private static final String VALIDATION_RESULT_INVALID = "  => validation result: invalid!  Cause: ";

    public XmlValidator(){}

    public XmlValidator(String xmlConstants, String xsdFilePath) throws FileNotFoundException, SAXException {
        checkAndSetFileExists(xsdFilePath);

        SchemaFactory factory = SchemaFactory.newInstance(xmlConstants);
        Schema schema = factory.newSchema(this.xsdFile);
        validator = schema.newValidator();
    }

    public String validate(String xmlFilePath){
        try {
            validator.validate(new StreamSource(new File(xmlFilePath)));
            String result = xmlFilePath + VALIDATION_RESULT_VALID;
            return result;
        } catch (SAXException e) {
            String result = xmlFilePath + VALIDATION_RESULT_INVALID + e.getMessage();
            return result;
        } catch (IOException ioException) {
            String result = FILE_NOT_FOUND_ON_PATH + xmlFilePath;
            return result;
        }
    }

    public void validateXmlList(List<String> xmlFileList, String resultFilePath){
        if(xmlFileList == null || xmlFileList.isEmpty()){
            return;
        }

        try {
            FileOutputStream resultFileOutputStream = new FileOutputStream(resultFilePath);
            BufferedOutputStream bouf = new BufferedOutputStream(resultFileOutputStream);
            xmlFileList.forEach(xmlFile -> {
                String result = validate(xmlFile);
                try {
                    bouf.write(result.getBytes());
                    bouf.write('\n');
                    bouf.flush();
                } catch (IOException ioException) {
                    System.out.println("I/O Exception occured: " + ioException.getMessage());
                }
            });
            bouf.close();
            resultFileOutputStream.close();

        } catch (IOException ioException) {
            System.out.println("Error at writing into file: " + resultFilePath);
            System.out.println("Error message: " + ioException.getMessage());
        }

    }

    private void checkAndSetFileExists(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        if(!file.isFile()){
            throw new FileNotFoundException(FILE_NOT_FOUND_ON_PATH + filePath);
        }

        this.xsdFile = file;
    }
}
