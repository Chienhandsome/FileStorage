package com.example.filestorage.model.api;

import android.util.Log;

import androidx.documentfile.provider.DocumentFile;

import com.example.filestorage.presenter.ICallback;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class FirebaseStorageManager implements IFirebaseStorageManager {
    private static final String TAG = "FirebaseStorageManager";
    private static final String STORAGE_ADDRESS = "gs://file-storage-16a58.appspot.com";
    private static FirebaseStorageManager instance;
    private FirebaseStorage firebaseStorage;

    private FirebaseStorageManager() {
        firebaseStorage = FirebaseStorage.getInstance(STORAGE_ADDRESS);
    }

    public synchronized static FirebaseStorageManager getInstance() {
        if (instance == null) {
            instance = new FirebaseStorageManager();
        }
        return instance;
    }

    //upload file lên storage
    @Override
    public void uploadFileToStorage(DocumentFile documentFile, ICallback callback) {
        if (documentFile != null) {
            //nen file truoc khi upload

            StorageReference storageReference = firebaseStorage.getReference().child(Objects.requireNonNull(documentFile.getName()));
            UploadTask uploadTask = storageReference.putFile(documentFile.getUri());
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                callback.onSuccess("File uploaded successfully");
            }).addOnFailureListener(exception -> {
                callback.onError("File upload failed! Cause by: " + exception.getMessage());
            });
        }
    }
}
