package com.example.ligma;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PlayerSelection extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);

    }

    public void onClickStart(View view){
        Intent startGameIntent = new Intent(this, TheGame.class);
        startActivity(startGameIntent);
    }
}
