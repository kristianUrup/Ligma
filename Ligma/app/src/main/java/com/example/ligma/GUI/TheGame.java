package com.example.ligma.GUI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ligma.BE.Card;
import com.example.ligma.BE.CardType;
import com.example.ligma.BE.FunctionType;
import com.example.ligma.BE.Player;
import com.example.ligma.DAL.CardDAO;
import com.example.ligma.DAL.FirestoreCallback;
import com.example.ligma.LOGIC.Base64Decoder;
import com.example.ligma.LOGIC.OnSwipeTouchListener;
import com.example.ligma.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

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
    LinearLayout statutes;
    GifImageView loadingIcon;
    ArrayList<Player> players;
    ImageView imgPlayer;
    TextView cardSym;


    FrameLayout cardLayout;

    private int currentPlayerIndex = 0;

    CardDAO cDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_game);
        deckToShuffle = new ArrayList<>();

        deck = new LinkedList<>();
        cDAO = new CardDAO();

        setPlayers();

        initViews();

        Bundle bundleObject = getIntent().getExtras();
        players = (ArrayList<Player>) bundleObject.getSerializable("players");

        cardLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();

                Animation swipe = AnimationUtils.loadAnimation(TheGame.this,R.anim.lefttoright);
                cardLayout.startAnimation(swipe);
                nextTurn();
            }
        });

        cardExp.setAutoSizeTextTypeUniformWithConfiguration(
                1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);

        startGame();
    }

    private void initViews() {
        cardSym = findViewById(R.id.card_symbol);
        cardDesc = findViewById(R.id.card_description);
        cardExp = findViewById(R.id.cardExp);
        cardType = findViewById(R.id.cardType);
        playerName = findViewById(R.id.player_name);
        inventory = findViewById(R.id.inventory_layout);
        statutes = findViewById(R.id.status_layout);
        cardLayout = findViewById(R.id.cardLayout);
        imgPlayer = findViewById(R.id.imgPlayer);
        cardLayout = findViewById(R.id.cardLayout);
        loadingIcon = findViewById(R.id.loadingIcon);
    }

    private void setPlayers() {
    }

    private void initDeck() {
        if(deckToShuffle.isEmpty())
        {
            cardLayout.setVisibility(View.INVISIBLE);
            loadingIcon.setVisibility(View.VISIBLE);
            imgPlayer.setVisibility(View.INVISIBLE);

        }
        cDAO.readCards(new FirestoreCallback() {
            @Override
            public void onCallBack(ArrayList<Card> deck) {
                cardLayout.setVisibility(View.VISIBLE);
                loadingIcon.setVisibility(View.INVISIBLE);
                imgPlayer.setVisibility(View.VISIBLE);
                deckToShuffle = deck;
                shuffleDeck();
                setCurrentRoundInfo();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Going back is not an option", Toast.LENGTH_SHORT).show();
    }

    private void startGame() {
        initDeck();
    }

    private void nextTurn() {
        if (currentPlayerIndex == players.size() - 1) {
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

        switch (startingCard.getCardType()) {
            case FUNCTION:
                cardSym.setText(startingCard.getCardSymbol());
                addToInventory(startingCard);
                break;
            case STATUS:
                cardSym.setText("");
                addToStatuses(startingCard);
                break;
        }
        showPlayerInfo(players.get(currentPlayerIndex));
    }

    private void showPlayerInfo(Player player) {
        playerName.setText(player.getName());

        setPlayerInventory(player);
        setPlayerStatus(player);

        byte[] decodedBytes = Base64.decode(player.getImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        imgPlayer.setImageBitmap(bitmap);
    }

    private void setPlayerInventory(Player player) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        inventory.removeAllViews();
        for (Card card : player.getInventory()) {
            Button btn = new Button(this);

            String drinkText = card.getText().replace(' ', '\n');
            btn.setText(drinkText);
            btn.setTextColor(Color.WHITE);
            btn.setTypeface(Typeface.DEFAULT_BOLD);
            btn.setBackgroundResource(R.drawable.button_inventory);
            btn.setLayoutParams(lparams);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showInventoryBox(v, player, card);
                }
            });
            inventory.addView(btn);

        }
    }

    private void setPlayerStatus(Player player) {
        statutes.removeAllViews();
        for (Card card : player.getStatuses()) {
            Button btn = new Button(this);

            btn.setText(card.getText());
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStatusPopUp(card);
                }
            });
            statutes.addView(btn);
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

    private void imageToString(Bitmap bitmap) {
        String encodedImage = Base64Decoder.encodeToBase64(bitmap, Bitmap.CompressFormat.PNG, 100);
        player.setImage(encodedImage);
    }

    public void addToInventory(Card cardToAdd){
        Player player = players.get(currentPlayerIndex);
        player.addToInventory(cardToAdd);
    }

    public void addToStatuses(Card cardToAdd) {
        Player player = playerList.get(currentPlayerIndex);
        for (Card card : player.getStatuses()) {
            if (card.getText() == cardToAdd.getText()) {
                return;
            }
        }
        player.addToStatuses(cardToAdd);
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
            case REMOVESTATUS:
                removeAllStatusesFromPlayer();
                break;
            case REMOVEASTATUS:
                removeAStatusFromPLayer();
                break;
        }
    }

    private void removeAStatusFromPLayer() {
        for(int i = 0; i < statutes.getChildCount(); i++) {
            Button button = (Button) statutes.getChildAt(i);
            Card card = playerList.get(currentPlayerIndex).getStatuses().get(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showStatusRemovePopUp(button, card);
                }
            });
        }
    }

    private void removeAllStatusesFromPlayer() {
        Player player = playerList.get(currentPlayerIndex);
        for (Card card : player.getStatuses()) {
            player.removeFromStatuses(card);
        }
        statutes.removeAllViews();
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

    private void showInventoryBox(View view, Player player, Card card) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        inventory.removeView(view);
                        player.removeFromInventory(card);
                        doCardFunction(card.getFunctionType());
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

    private void showStatusPopUp(Card card) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };

        String statusText = "\nCard: " + card.getText()
                + "\n"
                + "\nEffect: " + card.getEffectExplanation()
                + "\n";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(statusText).setNeutralButton("Got it!", dialogClickListener).show();
    }

    private void showStatusRemovePopUp(Button button, Card card) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        playerList.get(currentPlayerIndex).removeFromStatuses(card);
                        statutes.removeView(button);
                        setPlayerStatus(playerList.get(currentPlayerIndex));
                        break;
                }
            }
        };

        String statusText = "\nCard: " + card.getText()
                + "\n"
                + "\nEffect: " + card.getEffectExplanation()
                + "\n"
                + "\nAre you sure you want to remove this status?"
                + "\n";


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(statusText).setPositiveButton("Yes!", dialogClickListener)
                .setNegativeButton("No...", dialogClickListener).show();
    }


}
