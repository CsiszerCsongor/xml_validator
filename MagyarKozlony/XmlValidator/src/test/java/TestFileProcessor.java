import com.validation.xml.FileProcessor;
import com.validation.xml.exceptions.DirectoryNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;

public class TestFileProcessor {
    private static final String XSD_FILE_PATH = "document.xsd";
    private static final String TXT_FILE_PATH = "document.txt";
    private static final String XSD_FILE_EXTENSION = "xsd";
    private static final String XML_FILE_EXTENSION = "xml";
    private static final String TXT_FILE_EXTENSION = "txt";
    private static final String INVALID_DIRECTORY_PATH = "../directory/not/exists/path";
    private static final String INVALID_FILE_PATH = "../directory/not/exists/path/document.xsd";
    private static final int NUMBER_OF_XML_FILES = 2;

    private File xsdFile;
    private ClassLoader classLoader;

    @Before
    public void init(){
        classLoader = getClass().getClassLoader();
        xsdFile = new File(classLoader.getResource(XSD_FILE_PATH).getFile());
    }

    @Test
    public void testFileProcessorConstructor() throws IOException, DirectoryNotFoundException {
        FileProcessor fileProcessor = new FileProcessor(xsdFile.getParent(), xsdFile.getPath(), XSD_FILE_EXTENSION);
        assertTrue(fileProcessor.getDirectoryPath().equals(xsdFile.getParent() + File.separator));
        assertTrue(fileProcessor.getFileName().equals(xsdFile.getPath()));
        assertTrue(FilenameUtils.getExtension(fileProcessor.getFileName()).equals(XSD_FILE_EXTENSION));
    }

    @Test(expected = DirectoryNotFoundException.class)
    public void testFileProessorContructor_ShouldThrowExceptionBecauseInvalidDirectory() throws FileNotFoundException, DirectoryNotFoundException {
        new FileProcessor(INVALID_DIRECTORY_PATH, xsdFile.getPath(), XSD_FILE_EXTENSION);
    }

    @Test
    public void testFileProessorContructor_ShouldAddSlashSignIfNotExistsInPath() throws FileNotFoundException, DirectoryNotFoundException {
        assertFalse(xsdFile.getParent().endsWith(File.separator));
        FileProcessor fileProcessor = new FileProcessor(xsdFile.getParent(), xsdFile.getPath(), XSD_FILE_EXTENSION);
        assertTrue(fileProcessor.getDirectoryPath().endsWith(File.separator));
        assertTrue(fileProcessor.getDirectoryPath().equals(xsdFile.getParent() + File.separator));
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileProessorContructor_ShouldThrowExceptionBecauseFileNotExistsOnPath() throws FileNotFoundException, DirectoryNotFoundException {
        new FileProcessor(xsdFile.getParent(), INVALID_FILE_PATH, XSD_FILE_EXTENSION);
    }

    @Test(expected = FileNotFoundException.class)
    public void testFileProessorContructor_ShouldThrowExceptionBecauseFileHasNoExpectedExtension() throws FileNotFoundException, DirectoryNotFoundException {
        new FileProcessor(xsdFile.getParent(), xsdFile.getParent() + File.separator + TXT_FILE_PATH, XSD_FILE_EXTENSION);
    }

    @Test
    public void testGetFilesWithExtensionFromDirectory_ShouldReturnListOfXmlFiles() throws FileNotFoundException, DirectoryNotFoundException {
        FileProcessor fileProcessor = new FileProcessor(xsdFile.getParent(), xsdFile.getPath(), XSD_FILE_EXTENSION);
        Set<String> files = fileProcessor.getFilesWithExtensionFromDirectory(XML_FILE_EXTENSION);
        assertEquals(files.size(), NUMBER_OF_XML_FILES);
    }

    @Test
    public void testCheckFileExtension_ShouldReturnTrueBecauseMatchedFileExtension() throws FileNotFoundException, DirectoryNotFoundException {
        FileProcessor fileProcessor = new FileProcessor(xsdFile.getParent(), xsdFile.getPath(), XSD_FILE_EXTENSION);
        assertTrue(fileProcessor.checkFileExtension(XSD_FILE_EXTENSION));
    }

    @Test(expected = FileNotFoundException.class)
    public void testCheckFileExtension_ShouldThrowExceptionBecauseUnmatchedFileExtension() throws FileNotFoundException, DirectoryNotFoundException {
        File txtFile = new File(classLoader.getResource(TXT_FILE_PATH).getFile());
        FileProcessor fileProcessor = new FileProcessor(txtFile.getParent(), txtFile.getPath(), TXT_FILE_EXTENSION);
        assertTrue(fileProcessor.checkFileExtension(XSD_FILE_EXTENSION));
    }
}
