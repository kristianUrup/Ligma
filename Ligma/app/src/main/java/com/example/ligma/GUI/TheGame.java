package com.example.ligma.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ligma.BE.Card;
import com.example.ligma.BE.CardType;
import com.example.ligma.R;

public class TheGame extends AppCompatActivity {

    ArrayList<String> playerList;
    ArrayList<Card> deck;
    TextView cardDesc;
    TextView playerName;
    TextView cardExp;
    TextView cardType;
    LinearLayout inventory;
    FrameLayout cardLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_game);

        Intent intent = getIntent();
        playerList = intent.getStringArrayListExtra("player_list");
        deck = new ArrayList<>();
        initDeck();
        Log.d("CREATION", "player list: " + playerList.toString());
        cardDesc = findViewById(R.id.card_description);
        cardExp = findViewById(R.id.cardExp);
        cardType = findViewById(R.id.cardType);
        playerName = findViewById(R.id.player_name);
        inventory = findViewById(R.id.inventory_layout);
        cardLayout = findViewById(R.id.cardLayout);

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        cardLayout.setOnTouchListener(new OnSwipeTouchListener());


        cardDesc.setText("hello officer, drink 2 shots, but Frederik 400 shots");
        playerName.setText("Mathias");
        TextView tvToInsert = new TextView(this);
        tvToInsert.setLayoutParams(lparams);
        tvToInsert.setText("TOILET MANNER");
        this.inventory.addView(tvToInsert);
    }

    private void initDeck() {
        Card card1 = new Card(1, CardType.DRINK, "goof goof 2 drinks goof goof");
        Card card2 = new Card(2, CardType.DRINK, "bla bla bla boog boog");
        Card card3 = new Card(3, CardType.DRINK, "poopeee stinkyyyyy");
        Card card4 = new Card(4, CardType.DRINK, "argh argh argh argh 3 drink yes");

        deck.add(card1);
        deck.add(card2);
        deck.add(card3);
        deck.add(card4);
    }
}
