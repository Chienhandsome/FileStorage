package com.example.filestorage.presenter;

import android.net.Uri;

import androidx.documentfile.provider.DocumentFile;

import java.io.File;

public interface IMainPresenter {
    void uploadFileToStorage(DocumentFile documentFile);

}
