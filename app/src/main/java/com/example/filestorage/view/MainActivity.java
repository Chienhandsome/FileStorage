package com.example.filestorage.view;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.filestorage.R;
import com.example.filestorage.databinding.ActivityMainBinding;
import com.example.filestorage.model.entity.DataListManager;
import com.example.filestorage.model.entity.StorageFileLinker;
import com.example.filestorage.presenter.IMainPresenter;
import com.example.filestorage.presenter.MainPresenter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IMainView {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding viewBinding;
    private RecyclerAdapter recyclerAdapter;
    private IMainPresenter presenter;

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    accessStorageToPick();
                }
            }
    );

    private final ActivityResultLauncher<Intent> pickFileLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    if (data.getClipData() != null) {
                        ArrayList<Uri> uris = new ArrayList<>();
                        for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                            uris.add(data.getClipData().getItemAt(i).getUri());
                        }
                        takeAndDisplayFileInfoOnClipBoard(uris);
                    } else if (data.getData() != null) {
                        ArrayList<Uri> uris = new ArrayList<>();
                        uris.add(data.getData());
                        takeAndDisplayFileInfoOnClipBoard(uris);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initSetting();
    }

    //khởi tạo các thiết lập ban đầu
    private void initSetting() {
        presenter = new MainPresenter(this);
        recyclerViewSetup();
        setUpViewEvents();
    }

    //cài đặt sự kiện cho view
    private void setUpViewEvents() {
        viewBinding.addImgButton.setOnClickListener(v -> {
            accessStorageToPick();
        });
    }

    //cài đặt cho recycler view
    private void recyclerViewSetup() {
        recyclerAdapter = new RecyclerAdapter(this, DataListManager.getInstance().getDataList());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        viewBinding.recyclerView.setLayoutManager(linearLayoutManager);
        viewBinding.recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        viewBinding.recyclerView.setAdapter(recyclerAdapter);
    }


    private void takeAndDisplayFileInfoOnClipBoard(ArrayList<Uri> uriArrayList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (uriArrayList == null) {
                Toast.makeText(this, "Uri is null", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this,"uriList size: " + uriArrayList.size(), Toast.LENGTH_SHORT).show();
            DocumentFile file;

            for (int i = 0; i < uriArrayList.size(); i++) {
                Uri uri = uriArrayList.get(i);
                file = DocumentFile.fromSingleUri(this, uri);

                if (file != null && file.exists()){
                    StorageFileLinker storageFileLinker = new StorageFileLinker(file.getName(), file.getUri().toString(), file.getUri(), file.length());
                    DataListManager.getInstance().addData(storageFileLinker);
                    recyclerAdapter.notifyItemChanged(DataListManager.getInstance().getSize() - 1);

                    //yêu cầu presenter upload file lên storage
                    presenter.uploadFileToStorage(file);
                } else {
                    Log.d("onDisPlay", "displayImageFromStorage: file not found");
                }
            }
        }
    }

    //kiểm tra quyền và yêu cầu mở thư viện
    private void accessStorageToPick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            checkAndRequestPermission(READ_MEDIA_IMAGES, this::openFilePicker);
        } else {
            checkAndRequestPermission(READ_EXTERNAL_STORAGE, this::openFilePicker);
        }
    }

    //kiểm tra quyền và yêu cầu cấp quyền nếu quyền chưa được cấp, sau đó mở thu viện
    private void checkAndRequestPermission(String permission, Runnable onGranted) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            onGranted.run();
        } else {
            if (shouldShowRequestPermissionRationale(permission)) {
                showExplanationDialog();
            } else {
                Toast.makeText(this, "Media access permission not granted.", Toast.LENGTH_SHORT).show();
                requestPermissionLauncher.launch(permission);
            }
        }
    }

    //mở thư viện của điện thoại(màn hình chọn file)
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("*/*");
        pickFileLauncher.launch(intent);
    }

    //hiển thị dialog yêu cầu cấp quyền và giải thích tại sao cần cấp quyền
    private void showExplanationDialog() {
        new AlertDialog.Builder(this).setTitle("Permission Needed")
                .setMessage("This app needs to access your media files to display images.")
                .setPositiveButton("Go to setting", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create().show();
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}