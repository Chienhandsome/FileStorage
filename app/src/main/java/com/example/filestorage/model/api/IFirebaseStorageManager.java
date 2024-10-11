package com.example.filestorage.model.api;

import androidx.documentfile.provider.DocumentFile;

import com.example.filestorage.presenter.ICallback;

public interface IFirebaseStorageManager {
    void uploadFileToStorage(DocumentFile documentFile, ICallback callback);
}
