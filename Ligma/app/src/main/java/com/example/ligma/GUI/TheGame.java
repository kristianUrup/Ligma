package com.example.ligma.GUI;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.stream.Collectors;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


import android.view.View;
import android.widget.ArrayAdapter;
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


        cardDesc.setText("hello officer, drink 2 shots, but Frederik 400 shots");
        playerName.setText("");
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
        Card card6 = new Card(6, CardType.FUNCTION, "TOILET", " remember to flush if you do the poo poo", "T");

        deck.add(card1);
        deck.add(card2);
        deck.add(card3);
        deck.add(card4);
        deck.add(card5);
        deck.add(card6);
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
        showPlayerInfo(playerList.get(currentPlayerIndex));
        Card startingCard = deck.remove();
        deckToShuffle.add(startingCard);

        cardType.setText(startingCard.getCardType().name());
        cardDesc.setText(startingCard.getText());
        cardExp.setText("");

        if (startingCard.getCardType() != CardType.DRINK) {
            cardExp.setText(startingCard.getEffectExplanation());
        }

        if(startingCard.getCardType()== CardType.FUNCTION){
            cardSym.setText(startingCard.getCardSymbol());
            addToInventory(startingCard);
        }
    }

    private void showPlayerInfo(Player player){
        playerName.setText(player.getName());

        ArrayList<Card> cards = player.getInventory().stream()
                .filter(card ->  card.getCardSymbol() != null && !card.getCardSymbol().isEmpty())
                .collect(Collectors.toCollection(ArrayList::new));

        CustomAdapter adapter = new CustomAdapter(getApplicationContext(),
                android.R.layout.simple_list_item_1,cards, player);

        final int adapterCount = adapter.getCount();

        for (int i = 0; i < adapterCount; i++) {
            View item = adapter.getView(i, null, null);
            inventory.addView(item);
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

}
