package com.example.ecotrack_v1;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class AndroidUtil {
    public static void setProfilePic(Context context, Uri imageUri, ImageView img)
    {
        Glide.with(context).load(imageUri).apply(RequestOptions.fitCenterTransform()).into(img);
    }
    public static  void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
