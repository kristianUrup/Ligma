package com.example.ligma.LOGIC;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.example.ligma.BE.Card;
import com.example.ligma.BE.Player;
import com.example.ligma.GUI.MainActivity;
import com.example.ligma.R;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Card> {

    ArrayList<Player> mPlayers;

    /**
     * initializes mPlayers, context and resource.
     * @param context
     * @param resource
     * @param players
     */
    public CustomAdapter(@NonNull Context context, int resource, ArrayList<Player> players) {
        super(context, resource);
        this.mPlayers = players;
    }
    @Override
    /**
     * Gets the mPlayers count.
     */
    public int getCount() {
        return mPlayers.size();
    }

    /**
     * gets the layoutInflater from context.
     * @return
     */
    LayoutInflater getLayoutInflater(){
        return (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * If the view does not exist, then sets the layoutInflater to null.
     * @param position
     * @param view
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View view, ViewGroup parent){
        if(view == null){
            view = getLayoutInflater().inflate(R.layout.list_player, null);
        }
        TextView txtName = view.findViewById(R.id.txt_name_list);
        ImageView playerImg = view.findViewById(R.id.image_profile);
        Button btnDelete = view.findViewById(R.id.button_delete);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayers.remove(position);
                notifyDataSetChanged();
            }
        });

        int defaultHeight = 200;
        int imageWidth = defaultHeight;
        int imageHeight = defaultHeight;
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(imageWidth,imageHeight);
        int txtViewWidth = 900;
        int txtViewHeight = defaultHeight;
        LinearLayout.LayoutParams txtViewParams = new LinearLayout.LayoutParams(txtViewWidth, txtViewHeight);

        Player player = mPlayers.get(position);

        txtName.setText(player.getName());

        Bitmap bitmap = decodeBase64(player.getImage());
        playerImg.setImageBitmap(bitmap);
        playerImg.setLayoutParams(imageParams);
        txtName.setLayoutParams(txtViewParams);

        return view;
    }

    /**
     * Decodes a string to base64 and returns bitmap
     * @param input
     * @return
     */
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length );
        return bitmap;
    }


}
