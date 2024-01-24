package com.example.ecotrack_v1;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class SocialWorkerProfileActivity extends AppCompatActivity {
    BottomNavigationView bnView;
    EditText fullName;
    TextView email;
    TextView profileType;
    Button logout, updateProfile;

    FirebaseAuth auth;
    FirebaseFirestore fStore;
    ImageView profilePic;

    UserModel currentUserModel;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;


    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_worker_profile);
        bnView = (BottomNavigationView) findViewById(R.id.bnView_sw);
        fullName =findViewById(R.id.txt_profile_fullname);
        email =  findViewById(R.id.txt_profile_email);
        profileType =findViewById(R.id.txt_profileType);
        logout = (Button) findViewById(R.id.btn_profile_logout);
        profilePic = findViewById(R.id.img_profile_pic);
        updateProfile = findViewById(R.id.btn_update_profile);
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        getUserData();

        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data!=null && data.getData()!=null){
                            selectedImageUri = data.getData();
                            AndroidUtil.setProfilePic(SocialWorkerProfileActivity.this,selectedImageUri,profilePic);
                        }
                    }
                }
        );
        DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value!=null && value.exists()) {
                    fullName.setText(value.getString("fullname"));
                    email.setText(value.getString("email"));
                    profileType.setText("You are logged in as a " + value.getString("profileType"));
                }
            }
        });
        profilePic.setOnClickListener((v)->{
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateBtnClick();
            }
        });
        Menu menu = bnView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Toast.makeText(SocialWorkerProfileActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SocialWorkerProfileActivity.this, StartActivity.class));
                finish();
            }
        });
        bnView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_home_sw)
                {
                    startActivity(new Intent(SocialWorkerProfileActivity.this, SocialWorkerHomeActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_profile_sw)
                {
                    //startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                }
                else if(id == R.id.nav_green_points_sw)
                {
                    startActivity(new Intent(SocialWorkerProfileActivity.this, SocialWorkerGreenPointsActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_report_info_sw)
                {
                    startActivity(new Intent(SocialWorkerProfileActivity.this, SocialWorkerStatusActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
    void getUserData(){

        FireBaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Uri uri  = task.getResult();
                        AndroidUtil.setProfilePic(SocialWorkerProfileActivity.this,uri,profilePic);
                    }
                });

        FireBaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            currentUserModel = task.getResult().toObject(UserModel.class);
            fullName.setText(currentUserModel.getFullname());
            email.setText(currentUserModel.getEmail());
        });
    }

    private void updateBtnClick() {
        String newUsername = fullName.getText().toString();
        if(newUsername.isEmpty() || newUsername.length()<3){
            fullName.setError("Username length should be at least 3 chars");
            return;
        }

        currentUserModel.setFullname(newUsername);

        if(selectedImageUri!=null){
            FireBaseUtil.getCurrentProfilePicStorageRef().putFile(selectedImageUri)
                    .addOnCompleteListener(task -> {
                        updateToFirestore();
                    });
        }else{
            updateToFirestore();
        }
    }

    private void updateToFirestore() {
        FireBaseUtil.currentUserDetails().set(currentUserModel)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        AndroidUtil.showToast(SocialWorkerProfileActivity.this,"Updated successfully");
                    }else{
                        AndroidUtil.showToast(SocialWorkerProfileActivity.this,"Updated failed");
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SocialWorkerProfileActivity.this, SocialWorkerHomeActivity.class));
        super.onBackPressed();
    }
}