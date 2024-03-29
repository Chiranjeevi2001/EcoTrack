package com.example.ecotrack_v1;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FireBaseUtil {
    public FireBaseUtil() {
    }

    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean isLoggedIn(){
        if(currentUserId()!=null){
            return true;
        }
        return false;
    }

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }
    public static CollectionReference reportCollection(){
        return FirebaseFirestore.getInstance().collection("reports");
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static StorageReference getCurrentProfilePicStorageRef()
    {
        return FirebaseStorage.getInstance().getReference().child("Profile_pic")
                .child(FireBaseUtil.currentUserId());
    }
    public static StorageReference getTrashImageStorageRef()
    {
        return FirebaseStorage.getInstance().getReference().child("Trash_Pics/"+currentUserId());
    }
}
