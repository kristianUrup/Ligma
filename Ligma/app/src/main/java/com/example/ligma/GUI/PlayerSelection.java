package com.example.ligma.GUI;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.example.ligma.BE.Player;
import com.example.ligma.R;

public class PlayerSelection extends Activity {
    Player player;
    String TAG = TheGame.TAG;
    ListView listView;
    ArrayList<String> list;
    Button addBtn;
    EditText editText;
    ArrayAdapter<String> arrayAdapter;
    TextView errorText;
    ImageView imgCamera;
    String imgCameraString;
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP = 101;
    static int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);

        checkPermission();

        listView = findViewById(R.id.LviewPlayers);
        addBtn = findViewById(R.id.btnAddNewPlayer);
        editText = findViewById(R.id.txtNewPlayer);
        errorText = findViewById(R.id.txtViewErrorText);
        imgCamera = findViewById(R.id.img_camera);
        imgCamera.setOnClickListener(view -> takePicture());

        list = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, list);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAndAddPlayer();
            }
            @Override
            public void afterTextChanged(Editable s) {
                checkAndAddPlayer();
            }
        });
    }

    private void imageToString(Bitmap bitmap) {
        String encodedImage = encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 100);
        player.setImage(encodedImage);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Log.d(TAG, "Size of bitmap = " + imageBitmap.getByteCount());
                int height = imgCamera.getHeight();
                int width = imgCamera.getWidth();
                FrameLayout.LayoutParams params  = new FrameLayout.LayoutParams(width, height);
                imgCamera.setLayoutParams(params);
                imgCamera.setImageBitmap(imageBitmap);
                imageToString(imageBitmap);
            }
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private void checkPermission() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return;
        }

        ArrayList<String> permissions = new ArrayList<String>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (permissions.size() > 0) {
            ActivityCompat.requestPermissions(this, permissions.toArray(new String[permissions.size()]), PERMISSION_REQUEST_CODE);
        }
    }


    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP);
        } else
            Log.d(TAG, "camera app could NOT be started");
    }

    private void checkAndAddPlayer(){
        if(!TextUtils.isEmpty(editText.getText().toString())){
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    if(!TextUtils.isEmpty(editText.getText().toString())){
                        addToListView();
                    }
                }
            });
        } else {
            String noNameErrorText = "You must fill out a name before adding them.";
            editText.setError(noNameErrorText);
        }
    }

    private void addToListView(){
        String names = editText.getText().toString();

        list.add(names);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        errorText.setText("");
    }

    public void onClickStart(View view){
        if(list.isEmpty()){
            String startGameWithoutPlayers = "You cannot start a game with no players";
            errorText.setText(startGameWithoutPlayers);
        }else {
            Intent startGameIntent = new Intent(this, TheGame.class);
            startGameIntent.putStringArrayListExtra("player_list", list);
            startActivity(startGameIntent);
        }
    }


}
