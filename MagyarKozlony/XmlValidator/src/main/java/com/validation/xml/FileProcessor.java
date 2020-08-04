package com.validation.xml;

import com.validation.xml.exceptions.DirectoryNotFoundException;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileProcessor {
    private String directoryPath;
    private String fileName;

    private static final String DIRECTORY_NOT_FOUN_ON_THIS_PATH = "Directory not found on this path: ";
    private static final String FILE_NOT_FOUN_ON_THIS_PATH = "File not found on this path: ";
    private static final String FILE_IS_REQUIRED_INSTEAD_OF = " file is required instead of: ";

    public FileProcessor(){}

    public FileProcessor(String directoryPath, String fileName, String expectedFileExtension) throws DirectoryNotFoundException, FileNotFoundException {
        this.directoryPath = directoryPath;
        this.fileName = fileName;

        File directory = new File(this.directoryPath);
        if(!directory.isDirectory()){
            throw new DirectoryNotFoundException(DIRECTORY_NOT_FOUN_ON_THIS_PATH + directory);
        }
        if(!directoryPath.endsWith("/")){
            this.directoryPath = this.directoryPath + "/";
        }

        File file = new File(this.fileName);
        if(!file.isFile()){
            throw new FileNotFoundException(FILE_NOT_FOUN_ON_THIS_PATH + this.fileName);
        }

        checkFileExtension(expectedFileExtension);
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Set<String> getFilesFromDirectory(){
        return Stream.of(new File(this.directoryPath).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getPath)
                .collect(Collectors.toSet());
    }

    public Set<String> getFilesWithExtensionFromDirectory(String extension){
        return Stream.of(new File(this.directoryPath).listFiles())
                .filter(file -> !file.isDirectory() && FilenameUtils.getExtension(file.getName()).equals(extension))
                .map(File::getPath)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public boolean checkFileExtension(String expectedExtension) throws FileNotFoundException {
        String extensionOfXSD = FilenameUtils.getExtension(this.fileName);
        if(!extensionOfXSD.equals(expectedExtension)){
            throw new FileNotFoundException(expectedExtension + FILE_IS_REQUIRED_INSTEAD_OF + this.fileName);
        }

        return true;
    }
}
