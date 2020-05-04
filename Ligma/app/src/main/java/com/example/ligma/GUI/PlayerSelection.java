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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
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
    ArrayList<String> images;
    String imgDefaultString = "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";
    String imgCameraString;
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE_BY_BITMAP = 101;
    static int PERMISSION_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);

        imgCameraString = imgDefaultString;
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
            if(list.contains(editText.getText().toString())) {
                AddNewPlayer.setVisibility(View.INVISIBLE);
                String noDuplicateNameErrorText = "No duplicate names!";
                editText.setError(noDuplicateNameErrorText);
            }
            else {
                AddNewPlayer.setVisibility(View.VISIBLE);
                AddNewPlayer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(editText.getText().toString())) {
                            addToListView();
                            txtTip.setVisibility(View.INVISIBLE);
                            images.add(imgCameraString);
                            imgCameraString = imgDefaultString;


                        }
                        if (listView.getCount() > 1) {
                            AddBtn.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }else
            {
            String noNameErrorText = "You must add a name";
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
        imgCamera.setImageResource(R.drawable.defaultpicture);
    }

    public void onClickStart(View view){
        if(list.isEmpty()){
            String startGameWithoutPlayers = "You cannot start a game with less than 2 players";
            errorText.setText(startGameWithoutPlayers);
        }else {
            Intent startGameIntent = new Intent(this, TheGame.class);
            startGameIntent.putStringArrayListExtra("player_list", list);
            startGameIntent.putStringArrayListExtra("image_list", images);
            startActivity(startGameIntent);
        }
    }
}
