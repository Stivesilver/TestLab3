package com.example.ppolab3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Playground extends AppCompatActivity {
    Button createButton, intoButton, profButton, exitButton;
    EditText codeEdit;
    FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://lab3-b9e76-default-rtdb.firebaseio.com/");;
    DatabaseReference reference;
    DatabaseReference referenceS;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playgraund);

        createButton = (Button) findViewById(R.id.create_bt);
        intoButton = (Button) findViewById(R.id.into_bt);
        profButton = (Button) findViewById(R.id.prof_bt);
        exitButton = (Button) findViewById(R.id.exit_bt);
        codeEdit = findViewById(R.id.code_edit);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference = rootNode.getReference("games");
                User user = new User(PlayerStat.username, PlayerStat.avatarPath);
                Game game = new Game(user);
                reference.child(Game.Id).child("id").setValue(Game.Id);
                reference.child(Game.Id).child("userFirst").setValue(user);
                reference.child(Game.Id).child("userSecond").child("username").setValue("");
                reference.child(Game.Id).child("userSecond").child("imageUrl").setValue("");
                reference.child(Game.Id).child("userSecond").child("ready").setValue("false");

                Intent intent = new Intent(getApplicationContext(), ShipActivity.class);
                startActivity(intent);
            }
        });

        intoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredKey = codeEdit.getText().toString().trim();
                DatabaseReference reference = FirebaseDatabase.getInstance("https://lab3-b9e76-default-rtdb.firebaseio.com/").getReference("games");
                Query checkUser = reference.orderByChild("id").equalTo(enteredKey);
                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            String usernameDB = dataSnapshot.child(enteredKey).child("userFirst").child("username").getValue(String.class);
                            String imageDB = dataSnapshot.child(enteredKey).child("userFirst").child("avatarPath").getValue(String.class);
                            String Id = dataSnapshot.child(enteredKey).child("id").getValue(String.class);
                            User userS = new User(PlayerStat.username, PlayerStat.avatarPath);
                            User userF = new User(usernameDB, imageDB);
                            referenceS = rootNode.getReference("games");
                            reference.child(enteredKey).child("userSecond").setValue(userS);
                            Game game = new Game(Id, userF, userS);

                            Intent intent = new Intent(getApplicationContext(), ShipActivity.class);
                            startActivity(intent);
                        } else {
                            codeEdit.setError("Нет такой комнаты");
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        profButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Profile.class);
                startActivity(intent);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
