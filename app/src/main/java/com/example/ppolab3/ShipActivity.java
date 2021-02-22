package com.example.ppolab3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShipActivity extends AppCompatActivity {
    TextView lblFirst, lblSecond, idtxt;
    ImageView avatarF, avatarS;
    Button readyBtn;
    FirebaseDatabase rootNode = FirebaseDatabase.getInstance("https://lab3-b9e76-default-rtdb.firebaseio.com/");;

    DatabaseReference reference = rootNode.getReference("games");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        lblFirst = findViewById(R.id.first_ready);
        lblSecond = findViewById(R.id.second_ready);
        avatarF = findViewById(R.id.img_first_user);
        avatarS = findViewById(R.id.img_second_user);
        idtxt = findViewById(R.id.id_room);
        readyBtn = findViewById(R.id.btn_ready);



        idtxt.setText(Game.Id);

        //Picasso.get().load(Uri.parse(Game.userFirst.getImageUrl())).into(avatarF);
        if(Game.userSecond!=null){
            //Picasso.get().load(Uri.parse(Game.userSecond.getImageUrl())).into(avatarS);
            Game.myUser = UserEnum.SECOND;
            Game.myUsername = Game.userSecond.getUsername();
        }
        else {
            Game.myUser = UserEnum.FIRST;
            Game.myUsername = Game.userFirst.getUsername();
        }

        if(Game.myUser == UserEnum.SECOND){
            Game.userPath = "userFirst";
            Game.myUserPath = "userSecond";
        }
        else {
            Game.userPath = "userSecond";
            Game.myUserPath = "userFirst";
        }



        readyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.isReadyMe = !Game.isReadyMe;
                checkReady();
                String ready;
                if (Game.isReadyMe){
                    ready = "true";
                }else {
                    ready = "false";
                }

                reference.child(Game.Id).child(Game.myUserPath).child("ready").setValue(ready);
                if(Game.isReadyMe && Game.isReadySecond){
                    Game.isReadyMe = false;
                    Game.isReadySecond = false;
                    reference.child(Game.Id).child(Game.myUserPath).child("ready").setValue("false new");
                    Intent intent = new Intent(getApplicationContext(), ShipCompose.class);
                    startActivity(intent);
                }
            }
        });

        addSecondUserEventListener();
        ReadyEventListener();
    }

    public void addSecondUserEventListener() {
        DatabaseReference userRef = rootNode.getReference("games/" + Game.Id + "/" + Game.userPath);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String userImage = snapshot.child("imageUrl").getValue(String.class);
                String userName = snapshot.child("username").getValue(String.class);
                if(Game.myUser == UserEnum.SECOND){
                    //Picasso.get().load(Uri.parse(userImage)).into(avatarF);
                }
                else {
                    //Picasso.get().load(Uri.parse(userImage)).into(avatarS);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void ReadyEventListener() {
        DatabaseReference userRef = rootNode.getReference("games/" + Game.Id + "/" + Game.userPath + "/ready");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(String.class).equals("true")){
                    Game.isReadySecond = true;
                }else if(snapshot.getValue(String.class).equals("false")){
                    Game.isReadySecond = false;
                }else {
                    Game.isReadyMe = false;
                    Game.isReadySecond = false;
                    reference.child(Game.Id).child(Game.myUserPath).child("ready").setValue("false new");
                    Intent intent = new Intent(getApplicationContext(), ShipCompose.class);
                    startActivity(intent);
                }
                checkReady();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void checkReady() {
        String ready = "Готов";
        String notReady = "Не готов";
        if (Game.isReadyMe) {
            lblFirst.setText(ready);
        } else {
            lblFirst.setText(notReady);
        }
        if (Game.isReadySecond) {
            lblSecond.setText(ready);
        } else {
            lblSecond.setText(notReady);
        }
    }
}