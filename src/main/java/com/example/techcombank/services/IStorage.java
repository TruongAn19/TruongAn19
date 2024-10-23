package com.example.techcombank.services;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IStorage {
    public byte[] storeFile(MultipartFile file);
    public Stream<Path> loadAll();
    public byte[] readFile(Long userId);
    public void deleteAllFiles();
}
