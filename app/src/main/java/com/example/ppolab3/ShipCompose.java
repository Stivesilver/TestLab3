package com.example.ppolab3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShipCompose extends AppCompatActivity implements View.OnClickListener {
    private Button[][] buttons = new Button[Game.LineWidth][Game.LineWidth];
    Button buttonGo, buttonBig, buttonMedium, buttonSmall, buttonSingle;
    CheckBox checkVertical;
    ShipTypes selectedShip = null;
    int singleShips = 4, smallShips = 3, mediumShips = 2, bigShips = 1;
    FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://lab3-b9e76-default-rtdb.firebaseio.com/");
    ;

    DatabaseReference reference = rootNode.getReference("games");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare);

        for (int i = 0; i < Game.LineWidth; i++) {
            for (int j = 0; j < Game.LineWidth; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setBackgroundColor(Color.LTGRAY);
                buttons[i][j].setText(buttonID);
                buttons[i][j].setTextColor(Color.LTGRAY);
                Game.fieldMy[i][j] = false;
            }
        }

        buttonSingle = findViewById(R.id.single_ship);
        buttonSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedShip = ShipTypes.SINGLE;
                buttonMedium.setEnabled(false);
                buttonBig.setEnabled(false);
            }
        });

        buttonSmall = findViewById(R.id.small_ship);
        buttonSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedShip = ShipTypes.SMALL;
                buttonMedium.setEnabled(false);
                buttonBig.setEnabled(false);
            }
        });

        buttonMedium = findViewById(R.id.medium_ship);
        buttonMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedShip = ShipTypes.MEDIUM;
                buttonBig.setEnabled(false);
                buttonSmall.setEnabled(false);

            }
        });

        buttonBig = findViewById(R.id.big_ship);
        buttonBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedShip = ShipTypes.BIG;
                buttonMedium.setEnabled(false);
                buttonSmall.setEnabled(false);

            }
        });

        buttonGo = findViewById(R.id.btn_go);
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.isReadyMe = !Game.isReadyMe;
                String ready;
                if (Game.isReadyMe) {
                    ready = "Ожидание";
                    buttonGo.setText(ready);
                    ready = "true";
                } else {
                    ready = "Вперед";
                    buttonGo.setText(ready);
                    ready = "false new";
                }

                reference.child(Game.Id).child(Game.myUserPath).child("ready").setValue(ready);
                if (Game.isReadyMe && Game.isReadySecond) {
                    Game.isReadyMe = false;
                    Game.isReadySecond = false;
                    reference.child(Game.Id).child(Game.myUserPath).child("ready").setValue("false");

                    Intent intent = new Intent(getApplicationContext(), FightActivity.class);
                    startActivity(intent);
                }
            }
        });
        checkShips();
        ReadyEventListener();
    }

    @Override
    public void onClick(View v) {
        if (((Button) v).getText().toString().equals("")) {
            return;
        }

        if (selectedShip == ShipTypes.SINGLE) {
            placeShip(((Button) v).getText().toString(), 1);
        } else if (selectedShip == ShipTypes.SMALL) {
            placeShip(((Button) v).getText().toString(), 2);
        } else if (selectedShip == ShipTypes.MEDIUM) {
            placeShip(((Button) v).getText().toString(), 3);
        } else if (selectedShip == ShipTypes.BIG)  {
            placeShip(((Button) v).getText().toString(), 4);
        }
        checkShips();
    }

    public void checkShips() {
        buttonBig.setEnabled(bigShips != 0);
        buttonMedium.setEnabled(mediumShips != 0);
        buttonSmall.setEnabled(smallShips != 0);
        buttonSingle.setEnabled(singleShips != 0);
        buttonGo.setEnabled(singleShips == 0 && smallShips == 0 && mediumShips == 0 && bigShips == 0);
        selectedShip = null;
        buttonSingle.setText("1x: " + singleShips);
        buttonSmall.setText("2x: " + smallShips);
        buttonMedium.setText("3x: " + mediumShips);
        buttonBig.setText("4x: " + bigShips);
    }

    public void checkField() {
        for (int i = 0; i < Game.LineWidth; i++) {
            for (int j = 0; j < Game.LineWidth; j++) {
                if (Game.fieldMy[i][j]) {
                    buttons[i][j].setBackgroundColor(Color.LTGRAY);
                } else {
                    buttons[i][j].setBackgroundColor(Color.LTGRAY);
                }
            }
        }
    }

    public void placeShip(String buttonName, int size) {
        String subStr = buttonName.substring(7, 9);
        char c = subStr.charAt(0);
        int Y = Character.getNumericValue(c);
        c = subStr.charAt(1);
        int X = Character.getNumericValue(c);

        checkVertical = findViewById(R.id.verticalShip);
        if (checkVertical.isChecked()) {
            for (int i = 0; i < size; i++) {
                if (Game.fieldMy[Y][X] != Game.fieldMy[Y + i][X] || Y + size > Game.LineWidth) {
                    return;
                }
            }
            for (int i = 0; i < size; i++) {
                Game.fieldMy[Y + i][X] = true;
                buttons[Y + i][X].setBackgroundColor(Color.BLACK);
                buttons[Y + i][X].setTextColor(Color.BLACK);
                String yx = String.valueOf(Y + i) + String.valueOf(X);
                reference.child(Game.Id).child(Game.myUserPath).child("field").child(yx).setValue("true");
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (Game.fieldMy[Y][X] != Game.fieldMy[Y][X + i] || X + size > Game.LineWidth) {
                    return;
                }
            }
            for (int i = 0; i < size; i++) {
                Game.fieldMy[Y][X + i] = true;
                buttons[Y][X + i].setBackgroundColor(Color.BLACK);
                buttons[Y][X + i].setTextColor(Color.BLACK);
                String yx = String.valueOf(Y) + String.valueOf(X + i);
                reference.child(Game.Id).child(Game.myUserPath).child("field").child(yx).setValue("true");
            }
        }

        if (selectedShip == ShipTypes.MEDIUM) {
            mediumShips--;
        } else if (selectedShip == ShipTypes.BIG) {
            bigShips--;
        } else if (selectedShip == ShipTypes.SMALL) {
            smallShips--;
        } else if (selectedShip == ShipTypes.SINGLE) {
            singleShips--;
        }
    }

    public void ReadyEventListener() {
        DatabaseReference userRef = rootNode.getReference("games/" + Game.Id + "/" + Game.userPath + "/ready");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(String.class).equals("true")) {
                    Game.isReadySecond = true;
                } else if (snapshot.getValue(String.class).equals("false new")) {
                    Game.isReadySecond = false;
                } else {
                    Game.isReadyMe = false;
                    Game.isReadySecond = false;
                    reference.child(Game.Id).child(Game.myUserPath).child("ready").setValue("false");
                    Intent intent = new Intent(getApplicationContext(), FightActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
