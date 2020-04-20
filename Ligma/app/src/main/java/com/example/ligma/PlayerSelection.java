package com.example.ligma;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PlayerSelection extends Activity {
    ListView listView;
    ArrayList<String> list;
    Button addBtn;
    EditText editText;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);

        String[] friends;
        listView = findViewById(R.id.LviewPlayers);
        addBtn = findViewById(R.id.btnAddNewPlayer);
        editText = findViewById(R.id.txtNewPlayer);

        list = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, list);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String names = editText.getText().toString();

                list.add(names);
                listView.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
            }
        });


    }

    public void onClickAddPlayer(View view){

    }

    public void onClickStart(View view){
        Intent startGameIntent = new Intent(this, TheGame.class);
        startActivity(startGameIntent);
    }
}
