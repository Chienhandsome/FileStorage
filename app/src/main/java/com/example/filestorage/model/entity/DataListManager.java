package com.example.filestorage.model.entity;

import java.util.ArrayList;

public class DataListManager {
    private ArrayList<StorageFileLinker> dataList;
    private static DataListManager instance;

    public static DataListManager getInstance() {
        if (instance == null) {
            instance = new DataListManager();
        }
        return instance;
    }

    private DataListManager() {
        dataList = new ArrayList<>();
    }

    public void addData(StorageFileLinker data) {
        dataList.add(data);
    }

    public ArrayList<StorageFileLinker> getDataList() {
        return dataList;
    }

    public void removeData(int index) {
        dataList.remove(index);
    }

    public void removeData(StorageFileLinker data) {
        dataList.remove(data);
    }

    public void clearData() {
        dataList.clear();
    }

    public StorageFileLinker getData(int index) {
        return dataList.get(index);
    }

    public int getSize() {
        return dataList.size();
    }

    public void updateData(int index, StorageFileLinker data) {
        dataList.set(index, data);
    }

    public void setDataList(ArrayList<StorageFileLinker> dataList) {
        this.dataList = dataList;
    }
}
