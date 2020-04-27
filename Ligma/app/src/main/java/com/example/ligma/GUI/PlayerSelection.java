package com.example.ligma.GUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.ligma.BE.Player;
import com.example.ligma.R;

public class PlayerSelection extends Activity {
    ListView listView;
    ArrayList<String> list;
    Button addBtn;
    EditText editText;
    ArrayAdapter<String> arrayAdapter;
    TextView errorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);

        listView = findViewById(R.id.LviewPlayers);
        addBtn = findViewById(R.id.btnAddNewPlayer);
        editText = findViewById(R.id.txtNewPlayer);
        errorText = findViewById(R.id.txtViewErrorText);

        list = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, list);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAndAddPlayer();
            }
            @Override
            public void afterTextChanged(Editable s) {
                checkAndAddPlayer();
            }
        });
    }

    private void checkAndAddPlayer(){
        if(!TextUtils.isEmpty(editText.getText().toString())){
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    if(!TextUtils.isEmpty(editText.getText().toString())){
                        addToListView();
                    }
                }
            });
        } else {
            String noNameErrorText = "You must fill out a name before adding them.";
            editText.setError(noNameErrorText);
        }
    }

    private void addToListView(){
        String names = editText.getText().toString();

        list.add(names);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

        errorText.setText("");
    }

    public void onClickStart(View view){
        if(list.isEmpty()){
            String startGameWithoutPlayers = "You cannot start a game with no players";
            errorText.setText(startGameWithoutPlayers);
        }else {
            Intent startGameIntent = new Intent(this, TheGame.class);
            startGameIntent.putStringArrayListExtra("player_list", list);
            startActivity(startGameIntent);
        }
    }
}
