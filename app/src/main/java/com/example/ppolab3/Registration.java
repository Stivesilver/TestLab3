package com.example.ppolab3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Registration extends AppCompatActivity {
    EditText nameEdit, emailEdit, passwdEdit;
    Button regButton;
    String username, passwd, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        nameEdit = (EditText) findViewById(R.id.name_edit);
        emailEdit = (EditText) findViewById(R.id.email_edit);
        passwdEdit = (EditText) findViewById(R.id.passwd_edit);
        regButton = (Button) findViewById(R.id.reg_bt);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = nameEdit.getText().toString();
                email = emailEdit.getText().toString();
                passwd = passwdEdit.getText().toString();

                if (!username.isEmpty() && !passwd.isEmpty() && !email.isEmpty()){
                    username = username.trim();
                    passwd = passwd.trim();
                    email = email.trim();

                    DatabaseReference reference = FirebaseDatabase.getInstance("https://lab3-b9e76-default-rtdb.firebaseio.com/").getReference("users");
                    PlayerStat playerStat = new PlayerStat(username, email, passwd, "0", "0", "");
                    reference.child(username).setValue(playerStat);

                    Intent intent = new Intent(getApplicationContext(), Playground.class);
                    startActivity(intent);
                }
                else {
                    nameEdit.setError("Имя, емэил или пароль пустые");
                }
            }
        });
    }
}
