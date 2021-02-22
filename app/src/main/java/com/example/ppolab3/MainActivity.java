package com.example.ppolab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText nameEdit, passwdEdit;
    Button logButton, regButton;
    String username, passwd, usernameDB, passwdDB, emailDB, victoryDB, defeatDB, coefficient, avatarPathDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEdit = (EditText) findViewById(R.id.name_edit);
        passwdEdit = (EditText) findViewById(R.id.passwd_edit);
        logButton = (Button) findViewById(R.id.log_bt);
        regButton = (Button) findViewById(R.id.reg_bt);

        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = nameEdit.getText().toString();
                passwd = passwdEdit.getText().toString();

                if (!username.isEmpty() && !passwd.isEmpty()){
                    username = username.trim();
                    passwd = passwd.trim();

                    DatabaseReference reference = FirebaseDatabase.getInstance("https://lab3-b9e76-default-rtdb.firebaseio.com/").getReference("users");
                    Query checkUser = reference.orderByChild("username").equalTo(username);
                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                passwdDB = dataSnapshot.child(username).child("passwd").getValue(String.class);
                                Log.d("DOGG", username + passwdDB);
                                if (passwdDB.equals(passwd)) {

                                    usernameDB = dataSnapshot.child(username).child("username").getValue(String.class);
                                    emailDB = dataSnapshot.child(username).child("email").getValue(String.class);
                                    victoryDB = dataSnapshot.child(username).child("victory").getValue(String.class);
                                    defeatDB = dataSnapshot.child(username).child("defeat").getValue(String.class);
                                    //coefficient = Integer.toString(Integer.parseInt(victoryDB)/(Integer.parseInt(victoryDB) + Integer.parseInt(defeatDB)));
                                    avatarPathDB = dataSnapshot.child(username).child("avatarPath").getValue(String.class);

                                    PlayerStat playerStat = new PlayerStat(usernameDB, emailDB, passwdDB, victoryDB, defeatDB, avatarPathDB);

                                    Intent intent = new Intent(getApplicationContext(), Playground.class);
                                    startActivity(intent);
                                }
                                else {
                                    passwdEdit.setError("Неправильный пароль");
                                }
                            }
                            else {
                                nameEdit.setError("Неправильный пользователь");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else{
                    nameEdit.setError("Имя или пароль пустые");
                }
            }
        });
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Registration.class);
                startActivity(intent);
            }
        });
    }
}