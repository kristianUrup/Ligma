package com.example.ligma.GUI;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ligma.BE.Card;
import com.example.ligma.BE.FunctionType;
import com.example.ligma.BE.Player;
import com.example.ligma.BE.CardType;
import com.example.ligma.DAL.CardDAO;
import com.example.ligma.DAL.FirestoreCallback;
import com.example.ligma.LOGIC.OnSwipeTouchListener;
import com.example.ligma.R;

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
    ArrayList<Player> playerList;
    ImageView imgPlayer;
    TextView cardSym;

    FrameLayout cardLayout;

    private int currentPlayerIndex = 0;

    CardDAO cDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_game);

        playerList = new ArrayList<>();
        deckToShuffle = new ArrayList<>();
        deck = new LinkedList<>();
        cardSym = findViewById(R.id.card_symbol);
        cardDesc = findViewById(R.id.card_description);
        cardExp = findViewById(R.id.cardExp);
        cardType = findViewById(R.id.cardType);
        playerName = findViewById(R.id.player_name);
        inventory = findViewById(R.id.inventory_layout);
        cardLayout = findViewById(R.id.cardLayout);
        imgPlayer = findViewById(R.id.imgPlayer);
        cDAO = new CardDAO();
        setPlayers();

        initViews();

        cardLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                nextTurn();
            }
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                nextTurn();
            }
        });

        startGame();
    }

    private void initViews() {
        cardSym = findViewById(R.id.card_symbol);
        cardDesc = findViewById(R.id.card_description);
        cardExp = findViewById(R.id.cardExp);
        cardType = findViewById(R.id.cardType);
        playerName = findViewById(R.id.player_name);
        inventory = findViewById(R.id.inventory_layout);
        cardLayout = findViewById(R.id.cardLayout);
        imgPlayer = findViewById(R.id.imgPlayer);
    }

    private void setPlayers() {
        Intent intent = getIntent();
        ArrayList<String> playerListAsString = intent.getStringArrayListExtra("player_list");

        Intent imgIntent = getIntent();
        ArrayList<String> playerImageListAsString = imgIntent.getStringArrayListExtra("image_list");

        for (int i = 0, j = 0; i < playerListAsString.size() && j < playerImageListAsString.size(); i++, j++) {
            Player playerToAdd = new Player(playerListAsString.get(i), new ArrayList<>(), playerImageListAsString.get(j));
            playerList.add(playerToAdd);
        }
    }

    private void initDeck() {
        if(deckToShuffle.isEmpty())
        {
            cardLayout.setVisibility(View.INVISIBLE);
        }
        cDAO.readCards(new FirestoreCallback() {
            @Override
            public void onCallBack(ArrayList<Card> deck) {
                cardLayout.setVisibility(View.VISIBLE);
                deckToShuffle = deck;
                shuffleDeck();
                setCurrentRoundInfo();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    private void startGame() {
        initDeck();
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

        if (startingCard.getCardType() != null) {
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
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertBox(v, player, card);
                }
            });
            inventory.addView(btn);

        }
        byte[] decodedBytes = Base64.decode(player.getImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        imgPlayer.setImageBitmap(bitmap);
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

    private void doCardFunction(FunctionType functionType) {
        switch(functionType) {
            case SKIP:
                nextTurn();
                break;
            case DOUBLE:
                int doubleValue = 2;

                String expText = cardExp.getText().toString();
                String descText = cardDesc.getText().toString();

                cardDesc.setText(multiplyDrinkValue(descText, doubleValue));
                cardExp.setText(multiplyDrinkValue(expText, doubleValue));
                break;
        }
    }

    private String multiplyDrinkValue(String text, int multiplyAmount) {
        String newText = "";
        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);
            if (Character.isDigit(character)) {
                int number = Character.getNumericValue(character) * multiplyAmount;
                newText = newText + number;
            }else {
                newText = newText + character;
            }
        }
        return newText;
    }

    private void showAlertBox(View view, Player player, Card card) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        inventory.removeView(view);
                        player.removeFromInventory(card);
                        doCardFunction(card.getFunctionType());
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        String alertMessage = "Card: " + card.getText()
                + "\n"
                + "\nEffect: " + card.getEffectExplanation()
                + "\n"
                + "\nOnce used it will be removed from your inventory!"
                + "\n"
                + "\nAre you sure you want to use this card?";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(alertMessage).setPositiveButton("Yes!", dialogClickListener)
                .setNegativeButton("No...", dialogClickListener).show();
    }
}
