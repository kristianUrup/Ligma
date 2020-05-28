package com.example.ligma.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ligma.R;

public class MainActivity extends AppCompatActivity {


    /**
     * Sets the view to main activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Sets the view to playerSelection activity.
     * @param view
     */
    public void onClickStartGame(View view){
        Intent playerSelectionIntent = new Intent(this, PlayerSelection.class);
        startActivity(playerSelectionIntent);
    }

    /**
     * Sets the view to rules activity.
     * @param view
     */
    public void onClickRules(View view){
        Intent rulesIntent = new Intent(this, Rules.class);
        startActivity(rulesIntent);
    }
}
