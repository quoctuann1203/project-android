package com.example.learnenglish2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CorrectWordActivity extends AppCompatActivity {


    List<CorrectWord> questions = new ArrayList<CorrectWord>();
    String[] quizzes;
    // Declare all other variables

    String day;
    Random random;
    Integer score = 0;
    Integer star = 0;
    Integer wrongAns = 0;
    Integer levelQuestion = 3;

    TextView txtCorrectAnswer, txtRightAnswer, txtQuestionContainer, txtScore, timeCountDown;
    EditText etUserInput;
    Button btnCheck, btnShow, btnNext;

    MediaPlayer correctAnswerMp3;
    MediaPlayer wrongAnswerMp3;
    MediaPlayer backgroundMp3;

    CountDownTimer countDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_word);

        // init UI
        initUI();

        // GET DRAW QUESTION
        getRawQuestionList();

//        startTimer();
        // initialize the random variable

        random = new Random();
        quizzes = getLevelQuestion(3);
        day = quizzes[random.nextInt(quizzes.length)];

        txtQuestionContainer.setText(mixWords(day));

        backgroundMp3 = MediaPlayer.create(this, R.raw.wii);
        backgroundMp3.start();

        timeCountDown = findViewById(R.id.timeCountDown);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startTimer();
                if(etUserInput.getText().toString().equalsIgnoreCase(day)){
                    score = score + 1;
                    if(score % 3 == 0 ) {
                        star += 1;
                        txtScore.setText("" + star.toString());
                        playCorrectSound();
                    }
                    levelQuestion += 1;
                    quizzes = getLevelQuestion(levelQuestion);
                    wrongAns = 0;
                    Dialog dialog = new Dialog(CorrectWordActivity.this);
                    dialog.setContentView(R.layout.correct_dialog);
                    Button btnHide = dialog.findViewById(R.id.btnContinue);
                    dialog.show();

                    btnHide.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etUserInput.setText("");
                            changeQuestion();
//                            day = quizzes[random.nextInt(quizzes.length)];
//                            txtQuestionContainer.setText(mixWords(day));
                            dialog.dismiss();
                        }
                    });


                }else {
                    wrongAns += 1;
                    score = 0;
                    levelQuestion -= 1;
                    quizzes = getLevelQuestion(levelQuestion);

                    Toast.makeText(CorrectWordActivity.this, "You are wrong", Toast.LENGTH_SHORT).show();

                    if (wrongAns == 3){
                        if(star > 0) {
                            star -= 1;
                            txtScore.setText("" + star.toString());
                            playIncorrectSound();
                        }else {
                            txtScore.setText("end game!!!");
                            Intent intent = new Intent(CorrectWordActivity.this,ScoreCorrectWordActivity.class);
                            intent.putExtra("SCORE",String.valueOf(star));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                        wrongAns = 0;

                    }
                    etUserInput.setText("");
                    changeQuestion();
//                    day = quizzes[random.nextInt(quizzes.length)];
//                    txtQuestionContainer.setText(mixWords(day));
                }
            }
        });

        //BTN NEXT
//        btnNext.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                startTimer();
//                if(etUserInput.getText().toString().equalsIgnoreCase(day)){
//                    score = score + 1;
//                    if(score % 3 == 0 ) {
//                        star += 1;
//                        txtScore.setText("" + star.toString());
//                        playCorrectSound();
//                    }
//                    Dialog dialog = new Dialog(CorrectWordActivity.this);
//                    dialog.setContentView(R.layout.correct_dialog);
//                    Button btnHide = dialog.findViewById(R.id.btnContinue);
//                    dialog.show();
//
//                    btnHide.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            etUserInput.setText("");
//                            changeQuestion();
////                            day = quizzes[random.nextInt(quizzes.length)];
////                            txtQuestionContainer.setText(mixWords(day));
//                            dialog.dismiss();
//                        }
//                    });
//                }else {
//                    wrongAns += 1;
//                    Toast.makeText(CorrectWordActivity.this, "You are wrong", Toast.LENGTH_SHORT).show();
//
//                    if (wrongAns == 3){
//                        if(star > 0) {
//                            star -= 1;
//                            txtScore.setText("" + star.toString());
//                            playIncorrectSound();
//                        }else {
//                            txtScore.setText("end game!!!");
//                            Intent intent = new Intent(CorrectWordActivity.this,ScoreCorrectWordActivity.class);
//                            intent.putExtra("SCORE",String.valueOf(star));
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                        }
//                        wrongAns = 0;
//
//                    }
//                    etUserInput.setText("");
//                    changeQuestion();
////                    day = quizzes[random.nextInt(quizzes.length)];
////                    txtQuestionContainer.setText(mixWords(day));
//
//                }
//
//                day = quizzes[random.nextInt(quizzes.length)];
//
//                txtQuestionContainer.setText(mixWords(day));
//
//                etUserInput.setText("");
//                txtRightAnswer.setVisibility(View.INVISIBLE);
//                txtCorrectAnswer.setVisibility(View.INVISIBLE);
//            }
//        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCorrectAnswer.setVisibility(View.VISIBLE);
                txtRightAnswer.setVisibility(View.VISIBLE);

                txtRightAnswer.setText(day);
            }
        });
    }

    public void changeQuestion() {
        day = quizzes[random.nextInt(quizzes.length)];
        txtQuestionContainer.setText(mixWords(day));
    }
    private void startTimer() {
        countDown = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long mil) {
                if(mil < 20000)
                    timeCountDown.setText(String.valueOf(mil / 1000));
            }

            @Override
            public void onFinish() {
                changeQuestion();
            }
        };
        countDown.start();
    }

    private void playCorrectSound() {
        initMp3();
        correctAnswerMp3.start();
    }

    private void playIncorrectSound() {
        initMp3();
        wrongAnswerMp3.start();
    }

    private void initMp3() {
        try {
            correctAnswerMp3.reset();
            wrongAnswerMp3.reset();
        } catch (Exception e) {
        }
        correctAnswerMp3 = MediaPlayer.create(this, R.raw.correct);
        wrongAnswerMp3 = MediaPlayer.create(this, R.raw.wrong);
    }

    private String mixWords(String word) {
        List<String> words = Arrays.asList(word.split(""));

        Collections.shuffle(words);
        String mixed = "";

        for(String i: words) {
            mixed += i;
        }
        return mixed;
    }

    private void initUI () {
        txtCorrectAnswer = findViewById(R.id.txtCorrectAnswer);
        txtRightAnswer = findViewById(R.id.txtRightAnswer);
        txtQuestionContainer = findViewById(R.id.txtQuestionContainer);
        txtScore = findViewById(R.id.txtScore);

        etUserInput = findViewById(R.id.etUserInput);
        btnCheck = findViewById(R.id.btnCheck);
        btnShow = findViewById(R.id.btnShow);
//        btnNext = findViewById(R.id.btnNext);
    }

    private String[] getLevelQuestion(int level) {
        questions.forEach(question -> {
            if(level == question.level){
                quizzes = question.question;
            }
        });
        return quizzes;
    }

    private void getRawQuestionList() {
        //        Question question = new Question(1, new String[]{"Hi","Hello"});
        CorrectWord question2 = new CorrectWord(2, new String[]{"psyche", "stimulate"});
        CorrectWord question3 = new CorrectWord(3, new String[]{"play", "opportunity"});
        CorrectWord question4 = new CorrectWord(4, new String[]{"revolutionalise", "instinctively"});
        CorrectWord question5 = new CorrectWord(5, new String[]{"overbearing", "interchange"});
        CorrectWord question6 = new CorrectWord(6, new String[]{"Accomplishment", "psychological"});

        questions.add(new CorrectWord(3, new String[]{"hi","hello"}));
        questions.add(question2);
        questions.add(question3);
        questions.add(question4);
        questions.add(question5);
        questions.add(question6);
    }
}
