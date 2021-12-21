package com.example.learnenglish2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button startQuiz, startWord, highScore, userGuide;
    List<CorrectWord> questions = new ArrayList<CorrectWord>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startQuiz = findViewById(R.id.btn_startquiz);
        highScore = findViewById(R.id.btn_highscore);
        startWord = findViewById(R.id.btn_startword);
        userGuide = findViewById(R.id.btn_userguide);

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });

        highScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HighScoreActivity.class);
                startActivity(intent);
            }
        });

        startWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, CorrectWordActivity.class);


                startActivity(intent);
            }
        });

        userGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromDB();
                Intent intent = new Intent(MainActivity.this, UserGuideActivity.class);
                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)questions);
                intent.putExtra("BUNDLE",args);
                Log.i("question", questions.toString());

                startActivity(intent);
            }
        });


    }
    private  void getDataFromDB() {
        // GET DATA FROM FIRESTORE
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://learnenglish-9dfdc-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("list");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot questionDb: dataSnapshot.getChildren()){
                    List<String> question = new ArrayList<>();

                    for (DataSnapshot questionChild: questionDb.child("question").getChildren()){
                        question.add(questionChild.getValue().toString());
                    }
                    int level = Integer.parseInt(questionDb.child("level").getValue().toString());

                    CorrectWord correctWord = new CorrectWord(level, question);
                    questions.add(correctWord);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, "Get list questions failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}