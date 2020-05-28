package com.example.ligma.LOGIC;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Button;

import com.example.ligma.BE.Card;

public class StatusHandling {

    Context ctx;

    /**
     * initializes context.
     * @param context the current context
     */
    public StatusHandling(Context context){
        ctx = context;
    }

    /**
     * When clicking on a card an alertbox pops up to explain the card to you.
     * @param card the card to show pop from
     */
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

    /**
     * Sets the status text to the cards status.
     * @param card The card the status text is from
     * @return the string containing the status text
     */
    public String popUpText(Card card){
        String statusText = "\nCard: " + card.getText()
                + "\n"
                + "\nEffect: " + card.getEffectExplanation()
                + "\n";

        return statusText;
    }



}
