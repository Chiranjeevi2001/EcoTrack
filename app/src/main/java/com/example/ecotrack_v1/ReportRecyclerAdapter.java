package com.example.ecotrack_v1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ReportRecyclerAdapter extends RecyclerView.Adapter<ReportRecyclerAdapter.ReportViewHolder> {

    Context context;

    ArrayList<ReportModel> reportModelArrayList;

    public ReportRecyclerAdapter(Context context, ArrayList<ReportModel> reportModelArrayList) {
        this.context = context;
        this.reportModelArrayList = reportModelArrayList;
    }

    @NonNull
    @Override
    public ReportRecyclerAdapter.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.report_status_recycler_view, parent, false);

        return new ReportViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportRecyclerAdapter.ReportViewHolder holder, int position) {
        ReportModel reportModel = reportModelArrayList.get(position);
        String imagePath = reportModel.imageUri;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference imageRef = storageRef.child(imagePath);



        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context)
                .load(imageRef)
                .apply(requestOptions)
                .into(holder.img_trashImg);
            holder.txt_location.setText(reportModel.place);
            String trashTypes = "";
            for (int i = 0; i < reportModel.trashType.size()-1; i++) {
                trashTypes = trashTypes + reportModel.trashType.get(i) + ", ";
            }
            trashTypes += reportModel.trashType.get(reportModel.trashType.size()-1);
            holder.txt_trashType.setText(trashTypes);
            holder.txt_trashSize.setText(reportModel.trashSize);
            if (reportModel.isCleaned == true) {
                holder.txt_isCleaned.setText("Cleaned");
            } else {
                holder.txt_isCleaned.setText("Not Cleaned");
            }
    }

    @Override
    public int getItemCount() {
        return reportModelArrayList.size();
    }

    public static class ReportViewHolder extends  RecyclerView.ViewHolder
    {
        TextView txt_location, txt_trashType, txt_trashSize, txt_isCleaned;
        ImageView img_trashImg;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_location = itemView.findViewById(R.id.location_title);
            txt_trashType = itemView.findViewById(R.id.trash_types);
            txt_trashSize = itemView.findViewById(R.id.status_trash_size);
            txt_isCleaned = itemView.findViewById(R.id.status_report_cleaned);
            img_trashImg = itemView.findViewById(R.id.trash_image_view);

        }
    }
}
