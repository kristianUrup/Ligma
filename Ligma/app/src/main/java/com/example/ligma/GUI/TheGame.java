package com.example.ligma.GUI;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.example.ligma.BE.Card;
import com.example.ligma.BE.CardType;
import com.example.ligma.LOGIC.OnSwipeTouchListener;
import com.example.ligma.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class TheGame extends AppCompatActivity {

    ArrayList<String> playerList;
    ArrayList<Card> deck;
    TextView cardDesc;
    TextView playerName;
    TextView cardExp;
    TextView cardType;
    LinearLayout inventory;
    FrameLayout cardLayout;

    private int currentPlayerIndex = 0;
    private int currentDeckIndex = 0;

    FirebaseFirestore db;

    String TAG = "read";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_game);

        Intent intent = getIntent();
        playerList = intent.getStringArrayListExtra("player_list");
        deck = new ArrayList<>();
        Log.d("CREATION", "player list: " + playerList.toString());
        cardDesc = findViewById(R.id.card_description);
        cardExp = findViewById(R.id.cardExp);
        cardType = findViewById(R.id.cardType);
        playerName = findViewById(R.id.player_name);
        inventory = findViewById(R.id.inventory_layout);
        cardLayout = findViewById(R.id.cardLayout);

        db = FirebaseFirestore.getInstance();

        cardLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                Toast.makeText(TheGame.this, "Swipe to the left", Toast.LENGTH_SHORT).show();
                nextTurn();
            }
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                Toast.makeText(TheGame.this, "Swipe to the right", Toast.LENGTH_SHORT).show();
                nextTurn();
            }
        });

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tvToInsert = new TextView(this);
        tvToInsert.setLayoutParams(lparams);
        this.inventory.addView(tvToInsert);

        startGame();
    }

    private void initDeck() {
        Card card1 = new Card(1, CardType.DRINK, "goof goof 2 drinks goof goof");
        Card card2 = new Card(2, CardType.DRINK, "bla bla bla boog boog");
        Card card3 = new Card(3, CardType.DRINK, "poopeee stinkyyyyy");
        Card card4 = new Card(4, CardType.DRINK, "argh argh argh argh 3 drink yes");
        Card card5 = new Card(5, CardType.CHALLENGE, "DUEL", "jifjsdogjiogj sdogjsdfoig jdfsiogjiogjdfiog jdfogdjsfiog jsdfiog gdjsdjgiosdfjiojg iodfgjdiofsgjdios jsdg dsjgdiosfgj sd ");

        readCards();

        deck.add(card1);
        deck.add(card2);
        deck.add(card3);
        deck.add(card4);
        deck.add(card5);
    }

    private void startGame() {
        initDeck();
        setCurrentRoundInfo();
    }

    private void nextTurn() {
        if (currentPlayerIndex == playerList.size() - 1) {
            currentPlayerIndex = 0;
        }else {
            currentPlayerIndex++;
        }

        if (currentDeckIndex == deck.size() - 1) {
            currentDeckIndex = 0;
        }else {
            currentDeckIndex++;
        }

        setCurrentRoundInfo();

    }

    private void setCurrentRoundInfo() {
        playerName.setText(playerList.get(currentPlayerIndex));
        Card startingCard = deck.get(currentDeckIndex);

        cardType.setText(startingCard.getCardType().name());
        cardDesc.setText(startingCard.getText());
        cardExp.setText("");

        if (startingCard.getCardType() != CardType.DRINK) {
            cardExp.setText(startingCard.getEffectExplanation());
        }
    }

    public void readCards(){
        db.collection("cards")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()){
                                deck.add(document.toObject(Card.class));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Reading document resulted in error.", task.getException());
                        }
                    }
                });
    }
}
