package com.example.ligma.LOGIC;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;

import com.example.ligma.BE.Card;

public class StatusHandling {

    Context ctx;
    public StatusHandling(Context context){
        ctx = context;
    }

    public void showStatusPopUp(Card card) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };

        String statusText = popUpText(card);

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setMessage(statusText).setNeutralButton("Got it!", dialogClickListener).show();
    }

    public String popUpText(Card card){
        String statusText = "\nCard: " + card.getText()
                + "\n"
                + "\nEffect: " + card.getEffectExplanation()
                + "\n";

        return statusText;
    }



}
