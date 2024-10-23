package com.example.techcombank.controller;

import com.example.techcombank.models.ResponseObject;
import com.example.techcombank.services.IStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/api/v1/FileUpload")
public class FileUploadController {
    //Inject Storage Service here
    @Autowired
    private IStorage storage;

    @PostMapping(path = "/upload")
    public ResponseEntity<ResponseObject> uploadFile(@RequestParam("image") MultipartFile file) {
        try {
            //Save files to a folder => user a service
            byte[] generateFilename = storage.storeFile(file);
            System.out.println("abc" + generateFilename);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("Oke", "Up load file success", generateFilename)
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("Failed", exception.getMessage(), "")
            );
        }
    }
    @GetMapping(path = "/{userId}")
    public  ResponseEntity<byte[]> readDetailFile(@PathVariable Long userId) {
        try {
            byte[] bytes = storage.readFile(userId);
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(bytes) ;
        } catch (Exception exception) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("")
    public  ResponseEntity<ResponseObject> getUploadFile() {
        try {
            List<String> urls = storage.loadAll()
                    .map(path -> {
                        String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "readDetailFile", path.getFileName().toString()).build().toUri().toString();
                        return urlPath;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("oke", "List file to success", urls)
            );
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("Failed", "List file to fail", new String[] {})
            );
        }
    }
}
