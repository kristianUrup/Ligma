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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.example.ligma.BE.Player;
import com.example.ligma.R;

public class PlayerSelection extends Activity {
    String TAG = TheGame.TAG;
    ListView listView;
    TextView txtTip;
    ArrayList<String> list;
    Button AddBtn;
    Button AddNewPlayer;
    EditText editText;
    ArrayAdapter<String> arrayAdapter;
    TextView errorText;
    ImageView imgCamera;
    String imgCameraString;
    ArrayList<String> images;
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP = 101;
    static int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);

        images = new ArrayList<>();
        checkPermission();
        txtTip = findViewById(R.id.txtTip);
        listView = findViewById(R.id.LviewPlayers);
        AddNewPlayer = findViewById(R.id.btnAddNewPlayer);
        AddBtn = findViewById(R.id.btnAdd);
        editText = findViewById(R.id.txtNewPlayer);
        errorText = findViewById(R.id.txtViewErrorText);
        imgCamera = findViewById(R.id.imgCamera);
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

    private String imageToString(Bitmap bitmap) {
        String encodedImage = encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 100);
        images.add(encodedImage);
        return encodedImage;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                Log.d(TAG, "Size of bitmap = " + imageBitmap.getByteCount());
                imgCamera.setImageBitmap(imageBitmap);
                imgCameraString = imageToString(imageBitmap);
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
        if(!TextUtils.isEmpty(editText.getText().toString())) {
            AddNewPlayer.setVisibility(View.VISIBLE);
            AddNewPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    if(!TextUtils.isEmpty(editText.getText().toString())){
                        addToListView();
                        txtTip.setVisibility(View.INVISIBLE);
                        imgCamera.setImageResource(R.drawable.defaultpicture);
                    }
                    if(listView.getCount() > 1)
                    {
                        AddBtn.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            String noNameErrorText = "You must fill out a name and add a photo before adding them.";
            editText.setError(noNameErrorText);
        }
    }

    private void addToListView(){
        String names = editText.getText().toString();
        AddNewPlayer.setVisibility(View.VISIBLE);
        list.add(names);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        errorText.setText("");
        editText.setText("");
    }

    public void onClickStart(View view){
        if(list.isEmpty()){
            String startGameWithoutPlayers = "You cannot start a game with no players";
            errorText.setText(startGameWithoutPlayers);
        }else {
            Intent startGameIntent = new Intent(this, TheGame.class);
            startGameIntent.putStringArrayListExtra("player_list", list);
            startGameIntent.putStringArrayListExtra("image_list", images);
            startActivity(startGameIntent);
        }
    }


}
