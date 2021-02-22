package com.example.ppolab3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FightActivity extends AppCompatActivity implements View.OnClickListener {
    private final Button[][] buttons = new Button[Game.LineWidth][Game.LineWidth];
    private final Button[][] ebuttons = new Button[Game.LineWidth][Game.LineWidth];
    private final boolean[][] isDisabled = new boolean[Game.LineWidth][Game.LineWidth];
    Button finish;
    FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://lab3-b9e76-default-rtdb.firebaseio.com/");
    DatabaseReference reference = rootNode.getReference("games");
    boolean isMyTurn = false;
    ArrayList<String> targets = new ArrayList<>();
    String sendMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);
        finish = findViewById(R.id.btn_battle);

        if(Game.myUser == UserEnum.SECOND){
            isMyTurn = false;
        }
        else {
            isMyTurn = true;
        }

        for (int i = 0; i < Game.LineWidth; i++) {
            for (int j = 0; j < Game.LineWidth; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                if(Game.fieldMy[i][j])
                {
                    buttons[i][j].setBackgroundColor(Color.BLACK);
                    buttons[i][j].setTextColor(Color.BLACK);
                }else {
                    buttons[i][j].setBackgroundColor(Color.LTGRAY);
                    buttons[i][j].setTextColor(Color.LTGRAY);
                }
                buttons[i][j].setText(buttonID);
            }
        }

        for (int i = 0; i < Game.LineWidth; i++) {
            for (int j = 0; j < Game.LineWidth; j++) {
                String buttonID = "ebutton_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                ebuttons[i][j] = findViewById(resID);
                ebuttons[i][j].setOnClickListener(this);
                ebuttons[i][j].setBackgroundColor(Color.LTGRAY);
                ebuttons[i][j].setText(buttonID);
                ebuttons[i][j].setTextColor(Color.LTGRAY);
                ebuttons[i][j].setEnabled(isMyTurn);
                Game.fieldEnemy[i][j] = false;
            }
        }

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Playground.class);
                startActivity(intent);
            }
        });

        getEnemyShips();
        ReadyEventListener();
    }

    public void getEnemyShips() {
        DatabaseReference ref = rootNode.getReference("games").child(Game.Id).child(Game.userPath).child("field");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    drawEnemy(postSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void drawEnemy(String cell){
        char c = cell.charAt(0);
        int Y = Character.getNumericValue(c);
        c = cell.charAt(1);
        int X = Character.getNumericValue(c);
        Game.fieldEnemy[Y][X] = true;
        targets.add(cell);

    }

    @Override
    public void onClick(View v) {
        if (((Button) v).getText().toString().equals("")) {
            return;
        }
        fire(((Button) v).getText().toString());
    }

    public void fire(String buttonName){
        String subStr = buttonName.substring(8, 10);
        char c = subStr.charAt(0);
        int Y = Character.getNumericValue(c);
        c = subStr.charAt(1);
        int X = Character.getNumericValue(c);
        if(isDisabled[Y][X]) {
            return;
        }
        if(Game.fieldEnemy[Y][X])
        {
            ebuttons[Y][X].setBackgroundColor(Color.RED);
            ebuttons[Y][X].setTextColor(Color.RED);
            Game.fieldEnemy[Y][X] = false;
            targets.remove(subStr);
        }else {
            ebuttons[Y][X].setBackgroundColor(Color.WHITE);
            ebuttons[Y][X].setTextColor(Color.WHITE);
        }
        isDisabled[Y][X]=true;
        reference.child(Game.Id).child(Game.myUserPath).child("fireAt").setValue(subStr);
        nextTurn();
        checkWin();
    }

    public void nextTurn(){
        isMyTurn = !isMyTurn;
        for (int i = 0; i < Game.LineWidth; i++) {
            for (int j = 0; j < Game.LineWidth; j++) {
                ebuttons[i][j].setEnabled(isMyTurn);
            }
        }
        String turn;
        if(isMyTurn){
            turn = "Наш ход";
        }else {
            turn = "Ход врага";
        }
        finish.setText(turn);
    }

    public void checkWin(){
        if(targets.isEmpty()){
            String result = "Победа!";
            finish.setText(result);
            finish.setEnabled(true);
            reference.child(Game.Id).child(Game.myUserPath).child("fireAt").setValue("victory");
            DatabaseReference refUsers = FirebaseDatabase.getInstance("https://lab3-b9e76-default-rtdb.firebaseio.com/").getReference("users");


            Query checkUser = refUsers.orderByChild("username").equalTo(Game.myUsername);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String victoryDB = dataSnapshot.child(Game.myUsername).child("victory").getValue(String.class);
                        int victory = Integer.parseInt(victoryDB);
                        victory++;
                        refUsers.child(Game.myUsername).child("victory").setValue(String.valueOf(victory));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void ReadyEventListener() {
        DatabaseReference userRef = rootNode.getReference("games/" + Game.Id + "/" + Game.userPath + "/fireAt");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String cell = snapshot.getValue(String.class);
                if(cell==null){
                    return;
                }
                if(cell.equals("victory")){
                    String result = "Вы проиграли";
                    finish.setText(result);
                    finish.setEnabled(true);
                    return;
                }
                char c = cell.charAt(0);
                int Y = Character.getNumericValue(c);
                c = cell.charAt(1);
                int X = Character.getNumericValue(c);

                if(Game.fieldMy[Y][X])
                {
                    buttons[Y][X].setBackgroundColor(Color.RED);
                    buttons[Y][X].setTextColor(Color.RED);
                }else {
                    buttons[Y][X].setBackgroundColor(Color.WHITE);
                    buttons[Y][X].setTextColor(Color.WHITE);
                }
                nextTurn();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
