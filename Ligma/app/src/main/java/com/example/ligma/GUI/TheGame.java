package com.example.ligma.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import com.example.ligma.R;

public class TheGame extends AppCompatActivity {

    ArrayList<String> playerList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_game);

        Intent intent = getIntent();
        playerList = intent.getStringArrayListExtra("player_list");
        Log.d("CREATION", "player list: " + playerList.toString());
    }
}
