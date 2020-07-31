package com.example.quizapp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {
    TextView myTxtQuestion;
    TextView txtTime;
    Button btnTrue;
    Button btnWrong;
    int myQuizQuestion;
    ProgressBar userpg;
    TextView s;
    int userScore;
    String Score_KEY="SCORE";
    String INDEX_KEY="INDEX";
    int seconds = 20;
    private int score = 0;
    private boolean stopTimer = false;
    private int questionindex;
    QuizModel [] questioncollection= new QuizModel[]{
            new QuizModel(R.string.q1,true),
            new QuizModel(R.string.q2,false),
            new QuizModel(R.string.q3,true),
            new QuizModel(R.string.q4,true),
            new QuizModel(R.string.q5,false),
            new QuizModel(R.string.q6,true),
            new QuizModel(R.string.q7,true),
            new QuizModel(R.string.q8,false),
            new QuizModel(R.string.q9,true),
            new QuizModel(R.string.q10,true)
    };
    final int USERFINAL=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        timer();
        myTxtQuestion=findViewById(R.id.textQuestion);
        txtTime=findViewById(R.id.timer);
        btnTrue=findViewById(R.id.btnTrue);
        btnWrong=findViewById(R.id.btnWrong);
        userpg=findViewById(R.id.quizPB);
        s=findViewById(R.id.textQuizStats);
        if(savedInstanceState!=null){
            userScore=savedInstanceState.getInt(Score_KEY);
            questionindex=savedInstanceState.getInt(INDEX_KEY);
            s.setText("your score is"+userScore);
        }
        else{
            userScore=0;
            questionindex=0;
        }
        QuizModel q1=questioncollection[0];
        myTxtQuestion.setText(q1.getmQuestion());
        btnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbacktouser(true);
                updatequestion();
            }
        });
        btnWrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbacktouser(false);
                updatequestion();
            }
        });
    }
    private void updatequestion(){
        questionindex=(questionindex+1)%10;
        if(questionindex==0){
            AlertDialog.Builder quizalert=new AlertDialog.Builder(this);
            quizalert.setCancelable(false);
            quizalert.setTitle("Quiz is Finished");
            quizalert.setMessage("your score is"+userScore);
            quizalert.setPositiveButton("endQuiz", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            quizalert.show();
        }
        myQuizQuestion=questioncollection[questionindex].getmQuestion();
        myTxtQuestion.setText(myQuizQuestion);
        userpg.incrementProgressBy(USERFINAL);
        s.setText("your score is "+userScore);
    }
    private void feedbacktouser(boolean ans){
        boolean ans1=questioncollection[questionindex].ismAnswer();
        if(ans==ans1){
            userScore=userScore+1;
            Toast.makeText(getApplicationContext(),"GOOD",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"NOT GOOD",Toast.LENGTH_SHORT).show();
            Vibrator vibrator=(Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(300);
        }
    }
    private void timer() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                txtTime.setText("TIME :" + seconds);
                seconds--;
                if (seconds <=0) {
                    Toast.makeText(getApplicationContext(),"TIME OUT",Toast.LENGTH_SHORT).show();
                    showalertmessage();
                    stopTimer = true;
                }
                if (stopTimer == false) {
                    handler.postDelayed(this, 1000);
                }
            }
        });
    }
    private void showalertmessage(){
        AlertDialog.Builder QuizAlert = new AlertDialog.Builder(this);
        QuizAlert.setCancelable(false);
        QuizAlert.setTitle("TIME OUT");
        QuizAlert.setMessage("your score is "+userScore);
        QuizAlert.setPositiveButton("endQuiz", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        QuizAlert.show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        stopTimer = false;
        finish();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Score_KEY,userScore);
        outState.putInt(INDEX_KEY,questionindex);

    }
}