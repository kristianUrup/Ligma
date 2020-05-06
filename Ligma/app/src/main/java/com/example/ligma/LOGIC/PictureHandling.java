package com.example.ligma.LOGIC;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.example.ligma.GUI.PlayerSelection;

import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.startActivityForResult;
import static com.example.ligma.GUI.TheGame.TAG;

public class PictureHandling {

    Context ctx;
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP = 101;
    static int PERMISSION_REQUEST_CODE = 1;

    public PictureHandling(Context context){
        ctx = context;
    }

    public void takePicture(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(ctx.getPackageManager()) != null) {
            startActivityForResult(activity, intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP, null);

        } else
            Log.d(TAG, "camera app could NOT be started");
    }

    public void checkPermission(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return;
        }

        ArrayList<String> permissions = new ArrayList<String>();
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (permissions.size() > 0) {
            ActivityCompat.requestPermissions(activity, permissions.toArray(new String[permissions.size()]), PERMISSION_REQUEST_CODE);
        }
    }
}
