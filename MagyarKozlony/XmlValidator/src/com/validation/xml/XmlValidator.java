package com.validation.xml;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.List;

public class XmlValidator {
    private File xsdFile;
    private Schema schema;
    private Validator validator;

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
            String result = xmlFilePath + "  => validation result: valid!";
            return result;
        } catch (SAXException e) {
            String result = xmlFilePath + "  => validation result: invalid!  Cause: " + e.getMessage();
            return result;
        } catch (IOException ioException) {
            String result = "File not found: " + xmlFilePath;
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
                System.out.println(result);
            });
            bouf.close();
            resultFileOutputStream.close();

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    private void checkAndSetFileExists(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        if(!file.isFile()){
            throw new FileNotFoundException("File not found on path: " + filePath);
        }

        this.xsdFile = file;
    }
}
