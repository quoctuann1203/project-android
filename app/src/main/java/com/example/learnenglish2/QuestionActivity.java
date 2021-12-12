package com.example.learnenglish2;

import static com.example.learnenglish2.QuizNumberActivity.category_id;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView question,questionCount,timer;
    private Button option1,option2,option3,option4;
    private List<Question> questionList;
    private int questionNumber;
    private CountDownTimer countDown;
    private int score;
    private FirebaseFirestore firestore;
    private int setNo;
    private Dialog loadingDialog;
    private ImageView imv;
    MediaPlayer correctAnswerMp3;
    MediaPlayer wrongAnswerMp3;
    MediaPlayer backgroundMp3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        question = findViewById(R.id.question);
        questionCount = findViewById(R.id.question_number);
        timer = findViewById(R.id.countdown);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        imv = findViewById(R.id.imageURL);


        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);

        loadingDialog = new Dialog(QuestionActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        setNo = getIntent().getIntExtra("SETNO",5);

        firestore = FirebaseFirestore.getInstance();

        getQuestionKist();

        score = 0;

        backgroundMp3 = MediaPlayer.create(this, R.raw.wii);
        backgroundMp3.start();

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

    private void getQuestionKist() {
        questionList = new ArrayList<>();

        firestore.collection("QUIZ").document("CAT" + String.valueOf(category_id))
                .collection("SET" + String.valueOf(setNo))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {

                    QuerySnapshot questions = task.getResult();

                    for (QueryDocumentSnapshot doc : questions)
                    {
                        Question question = (new Question(doc.getString("QUESTION"),
                                doc.getString("A"),
                                doc.getString("B"),
                                doc.getString("C"),
                                doc.getString("D"),
                                Integer.valueOf(doc.getString("ANSWER")),
                                doc.getString("IMAGEURL")
                        ));
                        Log.i("aaa", question.imageUrl + "");
                        questionList.add(question);
                    }

                    Collections.shuffle(questionList);
                    setQuestion();
                }
                else
                {
                    Toast.makeText(QuestionActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

                loadingDialog.cancel();

            }
        });
    }

    private void setQuestion() {
        timer.setText(String.valueOf(20));
        question.setText(questionList.get(0).getQuestion());
        option1.setText(questionList.get(0).getOptionA());
        option2.setText(questionList.get(0).getOptionB());
        option3.setText(questionList.get(0).getOptionC());
        option4.setText(questionList.get(0).getOptionD());
        Picasso.with(getBaseContext())
                .load(questionList.get(0).getImageUrl())
                .into(imv);
        questionCount.setText(String.valueOf(1) + "/" + String.valueOf(questionList.size()));
        startTimer();
        questionNumber = 0;
    }

    private void startTimer() {
         countDown = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long mil) {
                if(mil < 20000)
                    timer.setText(String.valueOf(mil / 1000));
            }

            @Override
            public void onFinish() {
                changeQuestion();
            }
        };
        countDown.start();
    }

    @Override
    public void onClick(View view) {
        int selectedOption = 0;

        switch (view.getId()) {
            case R.id.option1:
                selectedOption = 1;
                break;
            case R.id.option2:
                selectedOption = 2;
                break;
            case R.id.option3:
                selectedOption = 3;
                break;
            case R.id.option4:
                selectedOption = 4;
                break;
            default:
        }
        countDown.cancel();

        checkAnswer(selectedOption, view);
    }

    private void checkAnswer(int selectedOption, View view) {
        if (selectedOption == questionList.get(questionNumber).getCorrectAnswer())
        {
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            playCorrectSound();
            score++;
        }
        else {
            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));
            switch (questionList.get(questionNumber).getCorrectAnswer())
            {
                case 1:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 2:
                    option2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 3:
                    option3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 4:
                    option4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }
            playIncorrectSound();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                changeQuestion();
            }
        },2000);


    }

    private void changeQuestion() {
        if(questionNumber < questionList.size()-1) {

            questionNumber++;
            playAnim(question,0,0);
            playAnim(option1,0,1);
            playAnim(option2,0,2);
            playAnim(option3,0,3);
            playAnim(option4,0,4);
            Picasso.with(getBaseContext())
                    .load(questionList.get(questionNumber).getImageUrl())
                    .into(imv);
            questionCount.setText(String.valueOf(questionNumber + 1) + "/" + String.valueOf(questionList.size()));

            timer.setText(String.valueOf(20));
            startTimer();
        }
        else {
            Intent intent = new Intent(QuestionActivity.this,ScoreActivity.class);
            intent.putExtra("SCORE",String.valueOf(score) + "/" + String.valueOf(questionList.size()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //QuestionActivity.this.finish();

        }
    }


    private void playAnim(final View view, final int value, final int viewNum) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (value == 0) {
                            switch (viewNum) {
                                case 0:
                                    ((TextView)view).setText(questionList.get(questionNumber).getQuestion());
                                    break;
                                case 1:
                                    ((Button)view).setText(questionList.get(questionNumber).getOptionA());
                                    break;
                                case 2:
                                    ((Button)view).setText(questionList.get(questionNumber).getOptionB());
                                    break;
                                case 3:
                                    ((Button)view).setText(questionList.get(questionNumber).getOptionC());
                                    break;
                                case 4:
                                    ((Button)view).setText(questionList.get(questionNumber).getOptionD());
                                    break;
                            }



                            if (viewNum != 0 ) {
                                ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E99C03")));
                            }
                            playAnim(view , 1, viewNum);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDown.cancel();
    }
}