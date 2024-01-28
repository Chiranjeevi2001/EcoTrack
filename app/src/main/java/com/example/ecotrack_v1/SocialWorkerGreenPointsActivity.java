package com.example.ecotrack_v1;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ecotrack_v1.leaderboard.LeaderBoardActivity;
import com.example.ecotrack_v1.leaderboard.SocialWorkerLeaderBoardActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class SocialWorkerGreenPointsActivity extends AppCompatActivity {
    BottomNavigationView bnView;
    UserModel currentUser;
    TextView txt;
    Button share, leaderboard;
    ImageView img_greenPoints;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_worker_green_points);
        txt = (TextView) findViewById(R.id.txt_green_points);
        bnView = (BottomNavigationView) findViewById(R.id.bnView_sw);
        share = findViewById(R.id.btn_share_green_points);
        leaderboard = findViewById(R.id.btn_leaderboard);
        img_greenPoints = findViewById(R.id.img_green_points);

        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SocialWorkerGreenPointsActivity.this, SocialWorkerLeaderBoardActivity.class));

            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareGreenPoints(view);
            }
        });
        getUserGreenPoints();
        Menu menu = bnView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bnView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.nav_home_sw)
                {
                    startActivity(new Intent(SocialWorkerGreenPointsActivity.this, SocialWorkerHomeActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_profile_sw)
                {
                    startActivity(new Intent(SocialWorkerGreenPointsActivity.this, SocialWorkerProfileActivity.class));
                    finish();
                    return true;
                }
                else if(id == R.id.nav_green_points_sw)
                {
                    //startActivity(new Intent(GreenPointsActivity.this, GreenPointsActivity.class));
                }
                else if(id == R.id.nav_report_info_sw)
                {
                    startActivity(new Intent(SocialWorkerGreenPointsActivity.this, SocialWorkerStatusActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    private void getUserGreenPoints() {
        FireBaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            currentUser = task.getResult().toObject(UserModel.class);
            String greenPoints_txt = "You currently have "+currentUser.getGreenPoints()+" Green Points!";
            txt.setText(greenPoints_txt);
        });
    }

    public void shareGreenPoints(View view) {
        FireBaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            currentUser = task.getResult().toObject(UserModel.class);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SocialWorkerGreenPointsActivity.this, "Could not load the user details", Toast.LENGTH_SHORT).show();
            }
        });
        if (currentUser != null) {
            Bitmap imageBitmap = createImageWithGreenPoints(currentUser.getGreenPoints());
            String imagePath = saveImageToExternalStorage(imageBitmap);
            File imageFile = new File(imagePath);
            Uri imageUri = FileProvider.getUriForFile(SocialWorkerGreenPointsActivity.this, getApplicationContext().getPackageName() + ".provider", imageFile);
            Glide.with(SocialWorkerGreenPointsActivity.this)
                    .load(imageUri)
                    .into(img_greenPoints);
            String message = "I earned " + currentUser.getGreenPoints() + " Green Points in EcoTrack App! #EcoTrack";

            if (imagePath != null) {

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_TEXT, message);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

                if (isInstagramInstalled()) {
                    shareIntent.setPackage("com.instagram.android");
                }
                if (isWhatsAppInstalled()) {
                    shareIntent.setPackage("com.whatsapp");
                }

                startActivity(Intent.createChooser(shareIntent, "Share Green Points"));
            } else {
                Toast.makeText(this, "Could not load image", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Log.e("User Fetch Error","Could not fetch user");
        }
    }
    private boolean isWhatsAppInstalled() {
        try {
            getPackageManager().getPackageInfo("com.whatsapp", 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private boolean isInstagramInstalled() {
        try {
            getPackageManager().getPackageInfo("com.instagram.android", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private Bitmap createImageWithGreenPoints(int greenPoints) {
        int width = 500;
        int height = 200;
        Bitmap imageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);


        Canvas canvas = new Canvas(imageBitmap);

        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(15);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);
        String text = "Check it out! I have earned " + greenPoints + " Green Points on EcoTrack!";
        float textWidth = paint.measureText(text);

        float x = (canvas.getWidth() - textWidth) / 2;
        float y = (float) canvas.getHeight() / 2;

        canvas.drawColor(Color.BLACK);
        canvas.drawText(text, 250, y, paint);
        Bitmap logoBitmap = getBitmapFromDrawable(SocialWorkerGreenPointsActivity.this, R.drawable.ecotracklogo);
        canvas.drawBitmap(logoBitmap, x, y/2, null);

        return imageBitmap;
    }

    public static Bitmap getBitmapFromDrawable(Context context, @DrawableRes int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // If the drawable is not a BitmapDrawable, create a new Bitmap and draw the drawable onto it
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private String saveImageToExternalStorage(Bitmap bitmap) {
        String fileName = "green_points_" + System.currentTimeMillis() + ".png";
        String relativeLocation = Environment.DIRECTORY_PICTURES + File.separator + "EcoTrack";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // For Android 10 and above, use MediaStore to save the image
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, relativeLocation);

            Uri contentUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            if (contentUri != null) {
                try (OutputStream out = getContentResolver().openOutputStream(contentUri)) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                return getFilePathFromContentUri(SocialWorkerGreenPointsActivity.this, contentUri);
            } else {
                return null;
            }
        } else {
            // For versions before Android 10, use the old approach
            File externalStorage = new File(Environment.getExternalStoragePublicDirectory(relativeLocation), fileName);

            try (OutputStream out = new FileOutputStream(externalStorage)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return externalStorage.getAbsolutePath();
        }
    }

    private static String getFilePathFromContentUri(Context context, Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                return cursor.getString(columnIndex);
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SocialWorkerGreenPointsActivity.this, SocialWorkerHomeActivity.class));
        super.onBackPressed();
    }
}