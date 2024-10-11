package com.example.filestorage.model.entity;

import android.net.Uri;

import java.io.File;

public class StorageFileLinker {
    private String fileName;
    private String filePath;
    private Uri fileUri;
    private double fileSize;

    public StorageFileLinker(String fileName, String filePath, Uri fileUri , double fileSize) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileUri = fileUri;
        this.fileSize = fileSize;
    }

    public StorageFileLinker() {
        this.fileName = "No Data";
        this.filePath = "No Data";
    }

    public File getFile() {
        return new File(this.filePath);
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    public Uri getFileUri() {
        return fileUri;
    }
}
