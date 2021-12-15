package com.example.learnenglish2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ScoreCorrectWordActivity extends AppCompatActivity {
    private TextView score;
    private Button scoredone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_correct_word);

        scoredone = findViewById(R.id.word_done);
        score = findViewById(R.id.sa_score1);

        String score_tr = getIntent().getStringExtra("SCORE");
        score.setText(score_tr);
        scoredone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreCorrectWordActivity.this,MainActivity.class);
                ScoreCorrectWordActivity.this.startActivity(intent);
                ScoreCorrectWordActivity.this.finish();
            }
        });
    }
}