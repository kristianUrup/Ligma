package com.example.ligma.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ligma.BE.Card;
import com.example.ligma.BE.Player;
import com.example.ligma.R;

public class TheGame extends AppCompatActivity {

    TextView cardDesc;
    TextView playerName;
    LinearLayout inventory;
    List<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_game);
        playerList = new ArrayList<>();

        Intent intent = getIntent();
        ArrayList<String> playerListAsString = intent.getStringArrayListExtra("player_list");

        for (String playerName: playerListAsString) {
            Player playerToAdd = new Player(playerName, new ArrayList<>());
            playerList.add(playerToAdd);
        }
        Log.d("CREATION", "player list: " + playerList.toString());

        cardDesc = findViewById(R.id.card_description);
        playerName = findViewById(R.id.player_name);
        inventory = findViewById(R.id.inventory_layout);

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        cardDesc.setText("hello officer, drink 2 shots, but Frederik 400 shots");
        playerName.setText("");
        TextView tvToInsert = new TextView(this);
        tvToInsert.setLayoutParams(lparams);
        tvToInsert.setText("TOILET MANNER");
        this.inventory.addView(tvToInsert);
    }

    private void showPlayerInfo(){

    }
}
