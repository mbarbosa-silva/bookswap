package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.model.File;
import com.app.repository.FileRepository;

import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.Optional;

@Service
public class FileService {
	
	@Autowired
    private FileRepository FileRepository;

    public File storeFile(MultipartFile file) throws Exception {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if(fileName.contains("..")) {
                throw new Exception("Sorry! Filename contains invalid path sequence " + fileName);
            }

            File dbFile = new File(fileName, file.getContentType(), file.getBytes());

            return FileRepository.save(dbFile);
        } catch (IOException ex) {
            throw ex;
        }
    }

    public Optional<File> getFile(String fileId) throws Exception {
    	try {
    		return FileRepository.findById(fileId);
    	} catch(Exception ex) {
    		throw new Exception("File not found with id " + fileId);
    	}
    }
}
