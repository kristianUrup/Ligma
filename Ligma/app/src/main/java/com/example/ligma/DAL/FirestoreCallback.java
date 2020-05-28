package com.example.ligma.DAL;

import com.example.ligma.BE.Card;

import java.util.ArrayList;

public interface FirestoreCallback {
    /**
     * Waits for the database call to arrive before continuing the code in this method.
     * @param deck
     */
    void onCallBack(ArrayList<Card> deck);
}
