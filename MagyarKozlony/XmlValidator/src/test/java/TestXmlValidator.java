import com.validation.xml.FileProcessor;
import com.validation.xml.XmlValidator;
import com.validation.xml.exceptions.DirectoryNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class TestXmlValidator {
    private static final String XML_CONSTANTS = XMLConstants.W3C_XML_SCHEMA_NS_URI;
    private static final String XSD_FILE_PATH = "document.xsd";
    private static final String TXT_FILE_PATH = "document.txt";
    private static final String XSD_FILE_EXTENSION = "xsd";
    private static final String XML_FILE_EXTENSION = "xml";
    private static final String TXT_FILE_EXTENSION = "txt";
    private static final String INVALID_DIRECTORY_PATH = "../directory/not/exists/path";
    private static final String INVALID_FILE_PATH = "../directory/not/exists/path/document.xsd";
    private static final String INVALID = "invalid";
    private static final String VALID = "valid";
    private static final String VALIDATION_RESULT_VALID = "  => validation result: valid!";
    private static final String VALIDATION_RESULT_INVALID = "  => validation result: invalid!  Cause: ";
    private static final String FILE_NOT_FOUND_ON_PATH = "File not found on path: ";

    private static final int NUMBER_OF_XML_FILES = 2;

    private File xsdFile;
    private ClassLoader classLoader;

    @Before
    public void init(){
        classLoader = getClass().getClassLoader();
        xsdFile = new File(classLoader.getResource(XSD_FILE_PATH).getFile());
    }

    @Test
    public void testXmlValidatorConstructor_ShouldRunWithoutExceptions() throws FileNotFoundException, SAXException {
        new XmlValidator(XML_CONSTANTS, xsdFile.getPath());
    }

    @Test(expected = FileNotFoundException.class)
    public void testXmlValidatorConstructor_ShouldThrowExceptionBecauseFileNotExists() throws FileNotFoundException, SAXException {
        new XmlValidator(XML_CONSTANTS, INVALID_FILE_PATH);
    }

    @Test
    public void testValidate_ShouldValidate() throws FileNotFoundException, DirectoryNotFoundException, SAXException {
        FileProcessor fileProcessor = new FileProcessor(xsdFile.getParent(), xsdFile.getPath(), XSD_FILE_EXTENSION);
        Set<String> xmlFileList = fileProcessor.getFilesWithExtensionFromDirectory(XML_FILE_EXTENSION);
        xmlFileList.add(INVALID_FILE_PATH);
        XmlValidator xmlValidator = new XmlValidator(XML_CONSTANTS, xsdFile.getPath());
        xmlFileList.forEach(file -> {
            String result = xmlValidator.validate(file);
            if(file.contains(INVALID)){
                assertTrue(result.contains(VALIDATION_RESULT_INVALID));
            }
            else
                if(file.contains(VALID)){
                    assertTrue(result.contains(VALIDATION_RESULT_VALID));
                }
                else {
                    assertTrue(result.contains(FILE_NOT_FOUND_ON_PATH));
                }
        });
    }

    @Test
    public void testValidateXmlList_ShouldValidateXmlListAndCreateFileForResult() throws IOException, DirectoryNotFoundException, SAXException {
        FileProcessor fileProcessor = new FileProcessor(xsdFile.getParent(), xsdFile.getPath(), XSD_FILE_EXTENSION);
        Set<String> xmlFileList = fileProcessor.getFilesWithExtensionFromDirectory(XML_FILE_EXTENSION);
        XmlValidator xmlValidator = new XmlValidator(XML_CONSTANTS, xsdFile.getPath());

        List<String> xmlFileListToCheck = new ArrayList<>(xmlFileList);

        String resultFilename = xsdFile.getParent() + File.separator + "result.txt";
        System.out.println(resultFilename);

        xmlValidator.validateXmlList(xmlFileListToCheck, resultFilename);

        File file = new File(resultFilename);
        assertTrue(file.exists());
        assertTrue(file.length() != 0);
        file.delete();
    }
}
