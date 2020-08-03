package com.validation.xml;

import com.validation.xml.exceptions.DirectoryNotFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileProcessor {
    private String directoryPath;
    private String fileName;

    public FileProcessor(){}

    public FileProcessor(String directoryPath, String fileName, String expectedFileExtension) throws DirectoryNotFoundException, FileNotFoundException {
        this.directoryPath = directoryPath;
        this.fileName = fileName;

        File directory = new File(this.directoryPath);
        if(!directory.isDirectory()){
            throw new DirectoryNotFoundException("Directory not found at this path: " + directory);
        }
        if(!directoryPath.endsWith("/")){
            this.directoryPath = this.directoryPath + "/";
        }

        File file = new File(this.fileName);
        if(!file.isFile()){
            throw new FileNotFoundException("File not found at this path: " + this.fileName);
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
                .filter(file -> !file.isDirectory() && getFileExtension(file.getName()).equals(extension))
                .map(File::getPath)
                .collect(Collectors.toSet());
    }

    public boolean checkFileExtension(String expectedExtension) throws FileNotFoundException {
        String extensionOfXSD = getFileExtension(this.fileName);
        if(!extensionOfXSD.equals(expectedExtension)){
            throw new FileNotFoundException(expectedExtension + " file is required instead of: " + this.fileName);
        }

        return true;
    }
    private String getFileExtension(String filename) {
        if(filename != null && filename.contains(".")){
            return filename.substring(filename.lastIndexOf(".") + 1);
        }
        return "";
    }
}
