package com.example.ligma.DAL;

import com.example.ligma.BE.Card;

import java.util.ArrayList;

public interface FirestoreCallback {
    void onCallBack(ArrayList<Card> deck);
}
