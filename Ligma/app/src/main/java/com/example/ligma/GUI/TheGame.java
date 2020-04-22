package com.example.ligma.GUI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ligma.R;

public class TheGame extends AppCompatActivity {

    TextView cardDesc;
    TextView playerName;
    LinearLayout inventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_the_game);

        cardDesc = findViewById(R.id.card_description);
        playerName = findViewById(R.id.player_name);
        inventory = findViewById(R.id.inventory_layout);

        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        cardDesc.setText("hello officer, drink 2 shots, but Frederik 400 shots");
        playerName.setText("Mathias");
        TextView tvToInsert = new TextView(this);
        tvToInsert.setLayoutParams(lparams);
        tvToInsert.setText("TOILET MANNER");
        this.inventory.addView(tvToInsert);
    }
}
