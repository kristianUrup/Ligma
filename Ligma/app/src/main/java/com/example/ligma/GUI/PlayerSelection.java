package com.example.ligma.GUI;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.widget.ButtonBarLayout;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.example.ligma.LOGIC.Base64Decoder;

import com.example.ligma.BE.Card;
import com.example.ligma.BE.Player;
import com.example.ligma.LOGIC.CustomAdapter;

import com.example.ligma.R;

public class PlayerSelection extends Activity {
    String TAG = TheGame.TAG;
    ListView listView;
    TextView txtTip;
    ArrayList<String> list;
    Button AddBtn;
    Button AddNewPlayer;
    EditText editText;
    ArrayList<Player> players;
    Button deletePlayerBtn;
    CustomAdapter customAdapter;
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
        players = new ArrayList<>();

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
        String encodedImage = Base64Decoder.encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 100);
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
        AddNewPlayer.setVisibility(View.VISIBLE);
        String name = editText.getText().toString();
        Player playerToAdd = new Player(name, new ArrayList<>(), imgCameraString);

        imgCameraString = imgDefaultString;
        imgCamera.setImageResource(R.drawable.defaultpicture);
        editText.setText("");

        players.add(playerToAdd);
        customAdapter = new CustomAdapter(this, android.R.layout.simple_list_item_1, players);
        listView.setAdapter(customAdapter);
        customAdapter.notifyDataSetChanged();
    }

    public void onClickStart(View view){
        if(players.isEmpty()){
            String startGameWithoutPlayers = "You cannot start a game with less than 2 players";
            errorText.setText(startGameWithoutPlayers);
        }else {
            Intent startGameIntent = new Intent(this, TheGame.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("players", players);
            startGameIntent.putExtras(bundle);
            startActivity(startGameIntent);
        }
    }

    private void deletePlayer(int position){

    }
}
