package com.example.ligma.GUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.stream.Collectors;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ligma.BE.Card;
import com.example.ligma.BE.Player;
import com.example.ligma.BE.CardType;
import com.example.ligma.LOGIC.CustomAdapter;
import com.example.ligma.LOGIC.OnSwipeTouchListener;
import com.example.ligma.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class TheGame extends AppCompatActivity {

    private static final String TAG = "TAG";
    ArrayList<Card> deckToShuffle;
    Queue<Card> deck;
    TextView cardDesc;
    TextView playerName;
    TextView cardExp;
    TextView cardType;
    LinearLayout inventory;
    ArrayList<Player> playerList;
    TextView cardSym;

    FrameLayout cardLayout;

    private int currentPlayerIndex = 0;


    FirebaseFirestore db;


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

        deckToShuffle = new ArrayList<>();
        deck = new LinkedList<>();
        Log.d("CREATION", "player list: " + playerList.toString());

        cardSym = findViewById(R.id.card_symbol);
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
        Card card1 = new Card(1, CardType.DRINK, "Take 2 drinks");
        Card card2 = new Card(2, CardType.DRINK, "Give 4 drinks out among the other players");
        Card card3 = new Card(3, CardType.DRINK, "You and the person to your right both take 2 drinks");
        Card card4 = new Card(4, CardType.DRINK, "Take 3 drinks. The person to your left takes double that");
        Card card5 = new Card(5, CardType.CHALLENGE, "DUEL", "The current player challenge another player for a shot of vodka. The one who grims the most has to take two drinks");
        Card card6 = new Card(6, CardType.FUNCTION, "TOILET", "You are allowed to go to the toilet. Also skips your turn", "T");

        deckToShuffle.add(card1);
        deckToShuffle.add(card2);
        deckToShuffle.add(card3);
        deckToShuffle.add(card4);
        deckToShuffle.add(card5);
        deckToShuffle.add(card6);

        readCards();

        deck.add(card1);
        deck.add(card2);
        deck.add(card3);
        deck.add(card4);
        deck.add(card5);
    }

    private void startGame() {
        initDeck();
        shuffleDeck();
        setCurrentRoundInfo();
    }

    private void nextTurn() {
        if (currentPlayerIndex == playerList.size() - 1) {
            currentPlayerIndex = 0;
        }else {
            currentPlayerIndex++;
        }

        if (deck.size() == 0) {
            shuffleDeck();
        }
        setCurrentRoundInfo();
    }

    private void setCurrentRoundInfo() {
        Card startingCard = deck.remove();
        deckToShuffle.add(startingCard);

        cardType.setText(startingCard.getCardType().name());
        cardDesc.setText(startingCard.getText());

        if (startingCard.getCardType() != CardType.DRINK) {
            cardExp.setText(startingCard.getEffectExplanation());
        }else {
            cardExp.setText("");
        }

        if(startingCard.getCardType()== CardType.FUNCTION){
            cardSym.setText(startingCard.getCardSymbol());
            addToInventory(startingCard);
        }else {
            cardSym.setText("");
        }
        showPlayerInfo(playerList.get(currentPlayerIndex));
    }

    private void showPlayerInfo(Player player){
        playerName.setText(player.getName());

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        inventory.removeAllViews();
        for (Card card : player.getInventory()) {
            Button btn = new Button(this);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inventory.removeView(v);
                    player.removeFromInventory(card);
                }
            });
            btn.setText(card.getCardSymbol() + "\n" + card.getText());
            btn.setLayoutParams(lparams);
            inventory.addView(btn);
        }
    }

    private void shuffleDeck() {
        Random random = new Random();

        for (int i = deckToShuffle.size() - 1; i > 0; i--) {
            int randomPos = random.nextInt(i);

            Card temp = deckToShuffle.get(i);
            deckToShuffle.set(i, deckToShuffle.get(randomPos));
            deckToShuffle.set(randomPos, temp);
        }

        for (Card card : deckToShuffle) {
            deck.add(card);
        }

        deckToShuffle.clear();
    }

    public void addToInventory(Card cardToAdd){
        Player player = playerList.get(currentPlayerIndex);
        player.addToInventory(cardToAdd);
        Log.d(TAG, "Added card to inventory");
        for (Card card : player.getInventory()) {
            Log.d(TAG, "Card in inventory: " + card.getText());
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
                            Log.w(TAG, "Reading documents resulted in error.", task.getException());
                        }
                    }
                });
    }
}
