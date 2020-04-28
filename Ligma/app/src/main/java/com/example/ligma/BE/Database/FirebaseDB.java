package com.example.ligma.BE.Database;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ligma.BE.Card;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirebaseDB {
    String TAG = "read";

    FirebaseFirestore db;

    public FirebaseDB(){
        db = FirebaseFirestore.getInstance();
    }


    public void readCards(){
        db.collection("cards")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()){
                                //deck.add(document.toObject(Card.class));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Reading document resulted in error.", task.getException());
                        }
                    }
                });
    }


}
