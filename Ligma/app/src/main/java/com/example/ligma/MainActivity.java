package com.example.ligma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClickStartGame(View view){
        Intent playerSelectionIntent = new Intent(this, PlayerSelection.class);
        startActivity(playerSelectionIntent);
    }

    public void onClickRules(View view){
        Intent rulesIntent = new Intent(this, Rules.class);
        startActivity(rulesIntent);
    }
}
