package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.app.model.Ad;
import com.app.model.File;
import com.app.model.User;
import com.app.repository.FileRepository;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;
	
    public File storeNewPhoto(MultipartFile newFile) throws Exception {
        try {

        	String fileName = StringUtils.cleanPath(newFile.getOriginalFilename());
        	
            if(fileName.contains("..")) {
                throw new Exception("File name is not correct" + fileName);
            }

            File file = new File(fileName, newFile.getContentType(), newFile.getBytes());

            return fileRepository.save(file);
        } catch (Exception ex) {
            throw ex;
        }
    }
    
	public void deleteFile(Long id, Object obj) throws Exception {
    	
		File file;
		
		if(obj instanceof User) {
    		file = fileRepository.findFileByUserId(((User) obj).getId());
    	} else if (obj instanceof Ad) {
    		file = fileRepository.findFileByProductId(((Ad) obj).getProduct().getId());
    	} else {
    		throw new Exception("wrong object class type");
    	}
    	
		fileRepository.delete(file);
    	
    }
    
}
