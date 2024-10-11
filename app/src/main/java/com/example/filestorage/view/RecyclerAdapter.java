package com.example.filestorage.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filestorage.R;
import com.example.filestorage.databinding.RowItemBinding;
import com.example.filestorage.model.entity.StorageFileLinker;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static final String TAG = "RecyclerAdapter";
    private Context context;
    private ArrayList<StorageFileLinker> dataList;

    public RecyclerAdapter(Context context, ArrayList<StorageFileLinker> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.row_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        holder.viewBinding.textView.setTag(position);
        if (dataList.get(position) == null) {
            holder.viewBinding.textView.setText(R.string.no_data);;
        } else {
            holder.viewBinding.textView.setText(dataList.get(position).getFileName());
        }


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private RowItemBinding viewBinding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewBinding = RowItemBinding.bind(itemView);
        }

    }
}
