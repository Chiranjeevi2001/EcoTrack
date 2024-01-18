package com.example.ecotrack_v1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class ReportActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private final int FINE_PERMISSION_CODE = 1;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button back;
    ImageView trashImg;
    FloatingActionButton addTrashBtn;
    ActivityResultLauncher<Intent> imagePickLauncher;
    TextView location;
    LinearLayout cb_kitchenWaste;
    ImageButton kitchenWaste;
    TextView txt_kitchenWaste;
    boolean flg_kitchenWaste = false;

    LinearLayout cb_plasticWaste;
    ImageButton plasticWaste;
    TextView txt_plasticWaste;
    boolean flg_plasticWaste = false;

    LinearLayout cb_eWaste;
    ImageButton eWaste;
    TextView txt_eWaste;
    boolean flg_eWaste = false;

    LinearLayout cb_biomedicalWaste;
    ImageButton biomedicalWaste;
    TextView txt_biomedicalWaste;
    boolean flg_biomedicalWaste = false;

    LinearLayout cb_constructionWaste;
    ImageButton constructionWaste;
    TextView txt_constructionWaste;
    boolean flg_constructionWaste = false;

    LinearLayout cb_organicWaste;
    ImageButton organicWaste;
    TextView txt_organicWaste;
    boolean flg_organicWaste = false;

    LinearLayout cb_glassWaste;
    ImageButton glassWaste;
    TextView txt_glassWaste;
    boolean flg_glassWaste = false;

    LinearLayout cb_hazardousWaste;
    ImageButton hazardousWaste;
    TextView txt_hazardousWaste;
    boolean flg_hazardousWaste = false;

    LinearLayout cb_sanitaryWaste;
    ImageButton sanitaryWaste;
    TextView txt_sanitaryWaste;
    boolean flg_sanitaryWaste = false;
    Uri selectedImageUri;
    double latitude, longitude;
    String place;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report2);
        back = findViewById(R.id.back_button);
        trashImg = findViewById(R.id.img_trash);
        addTrashBtn = findViewById(R.id.btn_add_trash_img);
        location = findViewById(R.id.txt_location);


        cb_kitchenWaste = findViewById(R.id.cb_kitchen_waste);
        kitchenWaste = cb_kitchenWaste.findViewById(R.id.trash_icon);
        kitchenWaste.setBackgroundResource(R.drawable.kitchen_waste);
        txt_kitchenWaste = cb_kitchenWaste.findViewById(R.id.trash_description);
        txt_kitchenWaste.setText("Kitchen Waste");

        cb_plasticWaste = findViewById(R.id.cb_plastic_waste);
        plasticWaste = cb_plasticWaste.findViewById(R.id.trash_icon);
        plasticWaste.setBackgroundResource(R.drawable.plastic_waste);
        txt_plasticWaste = cb_plasticWaste.findViewById(R.id.trash_description);
        txt_plasticWaste.setText("Plastic Waste");

        cb_eWaste = findViewById(R.id.cb_e_waste);
        eWaste = cb_eWaste.findViewById(R.id.trash_icon);
        eWaste.setBackgroundResource(R.drawable.electronic_waste);
        txt_eWaste = cb_eWaste.findViewById(R.id.trash_description);
        txt_eWaste.setText("E Waste");

        cb_biomedicalWaste = findViewById(R.id.cb_biomedical_waste);
        biomedicalWaste = cb_biomedicalWaste.findViewById(R.id.trash_icon);
        biomedicalWaste.setBackgroundResource(R.drawable.biomedical_waste);
        txt_biomedicalWaste = cb_biomedicalWaste.findViewById(R.id.trash_description);
        txt_biomedicalWaste.setText("Biomedical Waste");

        cb_constructionWaste = findViewById(R.id.cb_construction_and_demolition_waste);
        constructionWaste = cb_constructionWaste.findViewById(R.id.trash_icon);
        constructionWaste.setBackgroundResource(R.drawable.construction_demolition_waste);
        txt_constructionWaste = cb_constructionWaste.findViewById(R.id.trash_description);
        txt_constructionWaste.setText("Construction and Demolition Waste");

        cb_organicWaste = findViewById(R.id.cb_organic_waste);
        organicWaste = cb_organicWaste.findViewById(R.id.trash_icon);
        organicWaste.setBackgroundResource(R.drawable.organic_waste);
        txt_organicWaste = cb_organicWaste.findViewById(R.id.trash_description);
        txt_organicWaste.setText("Organic Waste");

        cb_glassWaste = findViewById(R.id.cb_glass_Waste);
        glassWaste = cb_glassWaste.findViewById(R.id.trash_icon);
        glassWaste.setBackgroundResource(R.drawable.glass_waste);
        txt_glassWaste = cb_glassWaste.findViewById(R.id.trash_description);
        txt_glassWaste.setText("Glass Waste");

        cb_hazardousWaste = findViewById(R.id.cb_hazardous_waste);
        hazardousWaste = cb_hazardousWaste.findViewById(R.id.trash_icon);
        hazardousWaste.setBackgroundResource(R.drawable.hazardous_waste);
        txt_hazardousWaste = cb_hazardousWaste.findViewById(R.id.trash_description);
        txt_hazardousWaste.setText("Hazardous Waste");

        cb_sanitaryWaste = findViewById(R.id.cb_sanitary_waste);
        sanitaryWaste = cb_sanitaryWaste.findViewById(R.id.trash_icon);
        sanitaryWaste.setBackgroundResource(R.drawable.sanitary_waste);
        txt_sanitaryWaste = cb_sanitaryWaste.findViewById(R.id.trash_description);
        txt_sanitaryWaste.setText("Sanitary Waste");


        kitchenWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flg_kitchenWaste)
                {
                    flg_kitchenWaste = true;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.appGreen);
                    kitchenWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
                else {
                    flg_kitchenWaste = false;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.black);
                    kitchenWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
            }
        });
        plasticWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flg_plasticWaste)
                {
                    flg_plasticWaste = true;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.appGreen);
                    plasticWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
                else {
                    flg_plasticWaste = false;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.black);
                    plasticWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
            }
        });
        eWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flg_eWaste)
                {
                    flg_eWaste = true;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.appGreen);
                    eWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
                else {
                    flg_eWaste = false;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.black);
                    eWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
            }
        });
        biomedicalWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flg_biomedicalWaste)
                {
                    flg_biomedicalWaste = true;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.appGreen);
                    biomedicalWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
                else {
                    flg_biomedicalWaste = false;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.black);
                    biomedicalWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
            }
        });
        constructionWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flg_constructionWaste)
                {
                    flg_constructionWaste = true;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.appGreen);
                    constructionWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
                else {
                    flg_constructionWaste = false;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.black);
                    constructionWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
            }
        });
        organicWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flg_organicWaste)
                {
                    flg_organicWaste = true;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.appGreen);
                    organicWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
                else {
                    flg_organicWaste = false;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.black);
                    organicWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
            }
        });
        glassWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flg_glassWaste)
                {
                    flg_glassWaste = true;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.appGreen);
                    glassWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
                else {
                    flg_glassWaste = false;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.black);
                    glassWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
            }
        });
        hazardousWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flg_hazardousWaste)
                {
                    flg_hazardousWaste = true;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.appGreen);
                    hazardousWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
                else {
                    flg_hazardousWaste = false;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.black);
                    hazardousWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
            }
        });
        sanitaryWaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flg_sanitaryWaste)
                {
                    flg_sanitaryWaste = true;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.appGreen);
                    sanitaryWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
                else {
                    flg_sanitaryWaste = false;
                    int newTintColor = ContextCompat.getColor(ReportActivity.this, R.color.black);
                    sanitaryWaste.setColorFilter(newTintColor, PorterDuff.Mode.SRC_IN);
                }
            }
        });


        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null && data.getData()!=null){
                            selectedImageUri = data.getData();
                            AndroidUtil.setProfilePic(ReportActivity.this,selectedImageUri,trashImg);
                        }
                    }
                }
        );

        addTrashBtn.setOnClickListener(v-> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportActivity.this, HomeActivity.class));
                finish();
            }
        });
;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

    }
    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
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
                    Toast.makeText(getBaseContext(), "Getting your location", Toast.LENGTH_SHORT).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    try {
                        getPlaceName(latitude, longitude);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    mapFragment.getMapAsync(ReportActivity.this);

                }
            }
        });
    }

    private void getPlaceName(double latitude, double longitude) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
        place = addresses.get(0).getAddressLine(0);
        location.setText("Current Location:"+place);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_PERMISSION_CODE)
        {
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getLastLocation();
            }
            else {
                Toast.makeText(ReportActivity.this,"Location Permission is denied", Toast.LENGTH_SHORT).show();
            }
        }


    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(place).draggable(true);
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,16f));
        mMap.addCircle(new CircleOptions().center(latLng)
                .radius(500)
                .fillColor(Color.TRANSPARENT)
                .strokeColor(Color.DKGRAY));
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }
}