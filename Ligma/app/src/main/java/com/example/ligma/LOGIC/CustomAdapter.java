package com.example.ligma.LOGIC;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ligma.BE.Card;
import com.example.ligma.BE.Player;
import com.example.ligma.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Card> {

    ArrayList<Card> sortedCards;
    Player player;
    public CustomAdapter(@NonNull Context context, int resource, ArrayList<Card> cards, Player playerTurn) {
        super(context, resource);
        sortedCards = cards;
        player = playerTurn;
    }
    @Override
    public int getCount() {
        return sortedCards.size();
    }

    LayoutInflater getLayoutInflater(){
        return (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){

        LinearLayout llayout = view.findViewById(R.id.inventory_layout);

        int width = 30;
        int height = width;

        LinearLayout.LayoutParams txtViewParams = new LinearLayout.LayoutParams(height, width);

        Card card = sortedCards.get(position);

        String cardSymbol = card.getCardSymbol();
        TextView cardTxtView = new TextView(llayout.getContext());
        cardTxtView.setText(cardSymbol);

        cardTxtView.setLayoutParams(txtViewParams);


        return view;
    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input,Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length );
        return bitmap;
    }
}
