package com.example.ecotrack_v1;

import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.startActivity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SocialWorkerReportRecyclerAdapter extends RecyclerView.Adapter<SocialWorkerReportRecyclerAdapter.SocialWorkerReportViewHolder> {

    private static final int FINE_PERMISSION_CODE = 1;
    UserModel currentUser;
    private OnItemClickListener onItemClickListener;
    Context context;
    public interface OnItemClickListener {
        void onRemoveItemClick(int position);
    }
    FusedLocationProviderClient fusedLocationProviderClient;

    ArrayList<ReportModel> reportModelArrayList;
    double latitude, longitude; //of the report location

    public SocialWorkerReportRecyclerAdapter(Context context, ArrayList<ReportModel> reportModelArrayList) {
        this.context = context;
        this.reportModelArrayList = reportModelArrayList;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @NonNull
    @Override
    public SocialWorkerReportRecyclerAdapter.SocialWorkerReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.report_status_recycler_view_sw, parent, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        return new SocialWorkerReportViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SocialWorkerReportRecyclerAdapter.SocialWorkerReportViewHolder holder, int position) {
        ReportModel reportModel = reportModelArrayList.get(position);
        getUserData();
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
        if (reportModel.isCleaned) {
            holder.txt_isCleaned.setText("Cleaned");
            holder.markAsDone.setVisibility(View.GONE);
        } else {
            holder.txt_isCleaned.setText("Not Cleaned");
        }

        holder.navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
                String fromPlace, toPlace;
                try {
                    toPlace = getPlaceName(reportModel.latitude, reportModel.longitude);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    fromPlace = getPlaceName(latitude, longitude);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Uri uri = Uri.parse("https://www.google.com/maps/dir/" + fromPlace + "/" + toPlace);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setPackage("com.google.android.apps.maps");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (ActivityNotFoundException exception) {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });

        holder.markAsDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want to mark this trash report as cleaned?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // User clicked Yes, perform the action
                            reportModel.isCleaned = true;
                            updateReportData(reportModel);

                            if(reportModel.trashSize.equals(context.getString(R.string.small)))
                            {
                                currentUser.addGreenPoints(50);
                            }
                            else if(reportModel.trashSize.equals(context.getString(R.string.medium)))
                            {
                                currentUser.addGreenPoints(75);
                            }
                            else if(reportModel.trashSize.equals(context.getString(R.string.large)))
                            {
                                currentUser.addGreenPoints(100);
                            }
                            updateUserCollection();
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // User clicked No, do nothing or handle accordingly
                        })
                        .show();
            }
        });
    }

    void getUserData(){

        FireBaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            currentUser = task.getResult().toObject(UserModel.class);

        });
    }

    private void updateUserCollection() {
        FireBaseUtil.currentUserDetails().set(currentUser)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        //AndroidUtil.showToast(ReportActivity.this,"Green Points Updated successfully");
                    }else{
                        AndroidUtil.showToast(context,"Green Points Update failed");
                    }
                });
    }

    private void updateReportData(ReportModel reportModel) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("reports");
        String fieldName = "imageUri";
        String targetValue = reportModel.imageUri;
        collectionRef.whereEqualTo(fieldName, targetValue)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String documentId = document.getId();
                        Map<String, Object> temp = new HashMap<>();
                        temp.put("imageUri", reportModel.imageUri);
                        temp.put("isCleaned", reportModel.isCleaned);
                        temp.put("latitude", reportModel.latitude);
                        temp.put("longitude", reportModel.longitude);
                        temp.put("place", reportModel.place);
                        temp.put("reportedUser", reportModel.reportedUser);
                        temp.put("trashSize", reportModel.trashSize);
                        temp.put("trashType", reportModel.trashType);

                        db.collection("reports")
                                .document(documentId)
                                .update(temp)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(context, "Marked as Cleaned", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firestore","Error submitting report", e);
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Collection Ref","SocialWorkerRecyclerView Error while getting reportID");
                });
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null)
                {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Toast.makeText(context, "Location Fetched", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private String getPlaceName(double latitude, double longitude) throws IOException {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
        String place = "";
        if(addresses.size()!=0) {
            place = addresses.get(0).getAddressLine(0);
        }
        return place;
    }

    @Override
    public int getItemCount() {
        return reportModelArrayList.size();
    }

    public class SocialWorkerReportViewHolder extends  RecyclerView.ViewHolder
    {
        TextView txt_location, txt_trashType, txt_trashSize, txt_isCleaned;
        Button navigate, markAsDone;
        ImageView img_trashImg;

        public SocialWorkerReportViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_location = itemView.findViewById(R.id.location_title);
            txt_trashType = itemView.findViewById(R.id.trash_types);
            txt_trashSize = itemView.findViewById(R.id.status_trash_size);
            txt_isCleaned = itemView.findViewById(R.id.status_report_cleaned);
            img_trashImg = itemView.findViewById(R.id.trash_image_view);
            navigate = itemView.findViewById(R.id.btn_navigate);
            markAsDone = itemView.findViewById(R.id.btn_mark_as_done);
            markAsDone.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onRemoveItemClick(position);
                    }
                }
            });

        }
    }
}
