package com.example.ligma.GUI;

import androidx.annotation.ColorRes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

    Player player;
    public static final String TAG = "TAG";
    ArrayList<Card> deckToShuffle;
    Queue<Card> deck;
    TextView cardDesc;
    TextView playerName;
    TextView cardExp;
    TextView cardType;
    LinearLayout inventory;
    ArrayList<String> playerImageList;
    ArrayList<Player> playerList;
    ImageView imgPlayer;
    TextView cardSym;

    FrameLayout cardLayout;

    private int currentPlayerIndex = 0;


    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_game);

        playerList = new ArrayList<>();
        playerImageList = new ArrayList<>();

        Intent intent = getIntent();
        ArrayList<String> playerListAsString = intent.getStringArrayListExtra("player_list");

        Intent imgIntent = getIntent();
        ArrayList<String> playerImageListAsString = imgIntent.getStringArrayListExtra("image_list");

        for (int i = 0, j = 0; i < playerListAsString.size() && j < playerImageListAsString.size(); i++, j++) {
            Player playerToAdd = new Player(playerListAsString.get(i), new ArrayList<>(), playerImageListAsString.get(j));
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
        imgPlayer = findViewById(R.id.imgPlayer);
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
        Card card1 = new Card("1", CardType.DRINK, "Take 2 drinks");
        Card card2 = new Card("2", CardType.DRINK, "Give 4 drinks out among the other players");
        Card card3 = new Card("3", CardType.DRINK, "You and the person to your right both take 2 drinks");
        Card card4 = new Card("4", CardType.DRINK, "Take 3 drinks. The person to your left takes double that");
        Card card5 = new Card("5", CardType.CHALLENGE, "DUEL", "The current player challenge another player for a shot of vodka. The one who grims the most has to take two drinks");
        Card card6 = new Card("6", CardType.FUNCTION, "SKIP", "You can skip a round", "S");
        Card card7 = new Card("7", CardType.FUNCTION, "BAILOUT", "You can bailout", "B");

        deckToShuffle.add(card1);
        deckToShuffle.add(card2);
        deckToShuffle.add(card3);
        deckToShuffle.add(card4);
        deckToShuffle.add(card5);
        deckToShuffle.add(card6);
        deckToShuffle.add(card7);

        //readCards();
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

    private void showPlayerInfo(Player player) {
        playerName.setText(player.getName());
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        inventory.removeAllViews();
        for (Card card : player.getInventory()) {
            Button btn = new Button(this);

            if(card.getCardSymbol() == "S"){
                Drawable s = getDrawable(R.drawable.skip_text);
                Drawable hotTub = getDrawable(R.drawable.ic_hot_tub_black_24dp);
                btn.setCompoundDrawablesWithIntrinsicBounds(null, s,null, hotTub);
            }
            if(card.getCardSymbol() == "B"){
                Drawable b = getDrawable(R.drawable.bail_out_text);
                Drawable bailOutText = getDrawable(R.drawable.bail_out);
                btn.setCompoundDrawablesWithIntrinsicBounds(null, b,null, bailOutText);
            }
            btn.setBackgroundResource(R.drawable.button_inventory);
            btn.setLayoutParams(lparams);
            inventory.addView(btn);

            byte[] decodedBytes = Base64.decode(player.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            imgPlayer.setImageBitmap(bitmap);

            Log.d("Testing1", "players: " + player.getName());
            Log.d("Testing2", "players: " + player.getImage());
            Log.d("Testing3", "player list: " + playerList.toString());
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

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private void imageToString(Bitmap bitmap) {
        String encodedImage = encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 100);
        player.setImage(encodedImage);
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
                                deckToShuffle.add(document.toObject(Card.class));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Reading documents resulted in error.", task.getException());
                        }
                    }
                });
    }
}
