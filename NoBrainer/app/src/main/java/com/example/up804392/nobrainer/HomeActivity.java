package com.example.up804392.nobrainer;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private DatabaseAccess databaseAccess;
    private SQLiteDatabase db;
    private Toast quizNotFound;
    private Toast quizFound;
    private SharedPreferences pin;
    public static final String prefName = "lastPinUsed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        quizNotFound = Toast.makeText(this, "no quiz found", Toast.LENGTH_SHORT);
        quizFound = Toast.makeText(this, "quiz found", Toast.LENGTH_SHORT);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navBar);
        navigationView.setNavigationItemSelectedListener(this);

        databaseAccess = DatabaseAccess.getInstance(this);
        pin = getSharedPreferences(prefName, MODE_PRIVATE);
        final EditText quizPin = findViewById(R.id.editText);

        final Button startQuiz = findViewById(R.id.button3);
        startQuiz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    databaseAccess.open();
                    db = databaseAccess.getDatabase();
                    String query = "select QuizID from Quiz where QuizPin =" + quizPin.getText().toString();
                    Cursor cursor = db.rawQuery(query, null);
                    if (cursor.moveToFirst()) {
                        SharedPreferences.Editor editor = pin.edit();
                        editor.putInt("key", cursor.getInt(cursor.getColumnIndex("QuizID")));
                        editor.commit();
                        Intent i = new Intent(HomeActivity.this, QuizActivity.class);
                        i.putExtra("1", cursor.getInt(cursor.getColumnIndex("QuizID")));
                        databaseAccess.close();
                        startActivity(i);
                        quizFound.show();
                    } else {
                        quizNotFound.show();
                        databaseAccess.close();
                    }
                }
                catch (Exception e){
                    databaseAccess.close();
                    quizNotFound.show();
                }
            }
        });
        final Button lastQuiz = findViewById(R.id.button4);
        lastQuiz.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    int value = pin.getInt("key", 0);
                    if(value != 0) {
                        Intent i = new Intent(HomeActivity.this, QuizActivity.class);
                        i.putExtra("1", value);
                        startActivity(i);
                    }
                    else
                        quizNotFound.show();
                }
                catch (Exception e){
                    quizNotFound.show();
                }
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
            Toast.makeText(this, "Current page", Toast.LENGTH_SHORT).show();
        if(id == R.id.about) {
            startActivity(new Intent(HomeActivity.this, AboutActivity.class));
        }
        return false;
    }
}
