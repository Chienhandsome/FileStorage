package com.example.filestorage.presenter;

import androidx.documentfile.provider.DocumentFile;

import com.example.filestorage.model.api.FirebaseStorageManager;
import com.example.filestorage.model.api.IFirebaseStorageManager;
import com.example.filestorage.view.IMainView;

public class MainPresenter implements IMainPresenter{
    private IMainView view;
    private ICallback callback = new ICallback() {
        @Override
        public void onSuccess(String message) {
            view.onSuccess(message);
        }

        @Override
        public void onError(String message) {
            view.onError(message);
        }
    };

    public MainPresenter(IMainView view) {
        this.view = view;
    }

    //yêu cầu upload file lên storage
    @Override
    public void uploadFileToStorage(DocumentFile documentFile) {
        IFirebaseStorageManager firebaseStorageManager = FirebaseStorageManager.getInstance();
        firebaseStorageManager.uploadFileToStorage(documentFile, callback);
    }
}
