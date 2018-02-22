package com.example.up804392.nobrainer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private DatabaseAccess databaseAccess;
    private SQLiteDatabase db;
    private ArrayList<Integer> questions;
    private ArrayList<Button> answers;
    private Button finish;
    private TextView outCome;
    private TextView correctAnswer;
    private int score;
    private int quizID;
    private Cursor cursor;
    private int currentQuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Bundle b = getIntent().getExtras();
        quizID = b.getInt("1");
        currentQuest = 0;
        score = 0;
        questions = new ArrayList<Integer>();
        answers = new ArrayList<Button>();
        finish = findViewById(R.id.finish);
        outCome= findViewById(R.id.outCome);
        correctAnswer = findViewById(R.id.correctAnswer);
        final Button answer1 = findViewById(R.id.answer1);
        final Button answer2 = findViewById(R.id.answer2);
        final Button answer3 = findViewById(R.id.answer3);
        final Button answer4 = findViewById(R.id.answer4);
        answers.add(answer1);
        answers.add(answer2);
        answers.add(answer3);
        answers.add(answer4);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navBar);
        navigationView.setNavigationItemSelectedListener(this);

        databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        String query1 = "select QuestID from Question where QuizID =" + quizID;
        cursor = databaseAccess.getDatabase().rawQuery(query1, null);
        while(true) {
            if (cursor.moveToNext())
                questions.add(cursor.getInt(cursor.getColumnIndex("QuestID")));
            else
                break;
        }

        final TextView questText = findViewById(R.id.questText);
        String query2 = "select QuizName from Quiz where QuizID =" + quizID;
        cursor = databaseAccess.getDatabase().rawQuery(query2, null);
        if(cursor != null){
            cursor.moveToFirst();
            questText.setText(cursor.getString(cursor.getColumnIndex("QuizName")));
        }

        final TextView quest = findViewById(R.id.quest);
        quest.setText(databaseAccess.getQuestion(questions.get(0)));


        setAnswers(currentQuest);

        databaseAccess.close();

        final Button next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                nextQuestion(quest, next);
            }
        });

        answer1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkAnswer(answer1);
            }
        });

        answer2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkAnswer(answer2);
            }
        });

        answer3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkAnswer(answer3);
            }
        });

        answer4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkAnswer(answer4);
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.home)
            startActivity(new Intent(QuizActivity.this, HomeActivity.class));
        if(id == R.id.about) {
            startActivity(new Intent(QuizActivity.this, AboutActivity.class));
        }
        return false;
    }

    public void setAnswers(int id){
        Cursor c = databaseAccess.getAnswers(questions.get(id));
        for(int x = 0; x<4; x++){
            c.moveToNext();
            answers.get(x).setText(c.getString(c.getColumnIndex("AnswerText")));
        }
    }

    public void nextQuestion(TextView quest, Button next){
        currentQuest++;
        databaseAccess.open();
        quest.setText(databaseAccess.getQuestion(questions.get(currentQuest)));
        setAnswers(currentQuest);
        databaseAccess.close();
        if(currentQuest+1 == questions.size()){
            next.setVisibility(View.INVISIBLE);
            final TextView last = findViewById(R.id.lastText);
            last.setVisibility(View.VISIBLE);
            finish.setVisibility(View.VISIBLE);
        }
        for (int x = 0; x <4; x++){
            answers.get(x).setVisibility(View.VISIBLE);
        }
        outCome.setVisibility(View.INVISIBLE);
        correctAnswer.setVisibility(View.INVISIBLE);

    }

    public void checkAnswer(Button button){
        databaseAccess.open();
        Cursor c = databaseAccess.checkAnswer(questions.get(currentQuest), button.getText().toString());
        c.moveToNext();
        if(c.getString(c.getColumnIndex("AnswerCorrect")).equals("true")) {
            outCome.setText("Correct");
            score++;
        }
        else {
            outCome.setText("Incorrect");
            Cursor c2 = databaseAccess.correctAnswer((questions.get(currentQuest)));
            for (int x = 0; x <4; x++) {
                c2.moveToNext();
                if(c2.getString(c2.getColumnIndex("AnswerCorrect")).equals("true"))
                    correctAnswer.setText("The Correct Answer was \n" + c2.getString(c2.getColumnIndex("AnswerText")));
            }
            correctAnswer.setVisibility(View.VISIBLE);
        }
        for (int x = 0; x <4; x++){
            answers.get(x).setVisibility(View.INVISIBLE);
        }
        outCome.setVisibility(View.VISIBLE);


        databaseAccess.close();
    }

}
