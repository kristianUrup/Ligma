package com.example.ligma.DAL;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ligma.BE.Card;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CardDAO {

    FirebaseFirestore db;
    public static final String TAG = "TAG";

    public CardDAO(){
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Gets data from database as an async call.
     * The firetore callback parameter makes sure that the call is completed before anything that needs it can be called.
     * After getting the data, it adds it to the local variable deck list and returns the list.
     * @param callback The firestore callback
     * @return an ArrayList containing cards
     */
    public ArrayList<Card> readCards(FirestoreCallback callback){
        ArrayList<Card> deckToShuffle = new ArrayList<>();

        db.collection("cards")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            ArrayList<Card> deckToShuffle = new ArrayList<>();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                deckToShuffle.add(document.toObject(Card.class));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            callback.onCallBack(deckToShuffle);
                        } else {
                            Log.w(TAG, "Reading documents resulted in error.", task.getException());

                        }
                    }
                });
        return deckToShuffle;
    }
}
