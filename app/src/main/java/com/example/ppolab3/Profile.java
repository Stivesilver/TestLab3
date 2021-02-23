package com.example.ppolab3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {
    ImageView avatarImage;
    TextView victoryView, defeatView;
    EditText nameEdit, passwdEdit;
    Button changeAvatarButton, saveButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri avatarPath;
    String username, passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        if (avatarPath != null){
            Picasso.get().load(avatarPath).into(avatarImage);
        }

        avatarImage = (ImageView) findViewById(R.id.avatar_img);
        victoryView = (TextView) findViewById(R.id.victory_view);
        defeatView = (TextView) findViewById(R.id.defeat_view);
        nameEdit = (EditText) findViewById(R.id.name_edit);
        passwdEdit = (EditText) findViewById(R.id.passwd_edit);
        changeAvatarButton = (Button) findViewById(R.id.change_avatar_bt);
        saveButton = (Button) findViewById(R.id.save_bt);

        victoryView.setText("Победы: " + PlayerStat.victory);
        defeatView.setText("Поражения: " + PlayerStat.defeat);

        nameEdit.setText(PlayerStat.username);
        passwdEdit.setText(PlayerStat.passwd);

        changeAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);

                DatabaseReference reference = FirebaseDatabase.getInstance("https://lab3-b9e76-default-rtdb.firebaseio.com/")
                        .getReference("users");
                reference.child(PlayerStat.username).child("avatarPath").setValue(avatarPath.toString());
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = nameEdit.getText().toString();
                passwd = passwdEdit.getText().toString();
                if (!username.isEmpty() && !passwd.isEmpty()){
                    username = username.trim();
                    passwd = passwd.trim();

                    DatabaseReference reference = FirebaseDatabase.getInstance("https://lab3-b9e76-default-rtdb.firebaseio.com/").getReference("users");

                    reference.child(PlayerStat.username).removeValue();

                    PlayerStat playerStat = new PlayerStat(username, passwd);
                    reference.child(username).setValue(playerStat);

                    finish();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            avatarPath = data.getData();
            Picasso.get().load(avatarPath).into(avatarImage);
        }
    }
}
