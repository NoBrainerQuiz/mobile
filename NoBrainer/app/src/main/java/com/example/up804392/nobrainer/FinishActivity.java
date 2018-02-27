package com.example.up804392.nobrainer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class FinishActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    /**
     * Creates and sets the layout.
     * activates app draw and initializes toggle.
     * finds the TextView.
     * sets text of current TextView.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        Bundle b = getIntent().getExtras();
        int score = b.getInt("1");
        int size = b.getInt("2");
        final TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText("You scored \n " + score + "/" + size);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navBar);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    /**
     * Controls what actions are taken when items are selected from the app drawer.
     *
     * @param item the item select in the app drawer.
     * @return false as the action is controlled inside the method.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.home)
            startActivity(new Intent(FinishActivity.this, HomeActivity.class));
        if(id == R.id.about) {
            startActivity(new Intent(FinishActivity.this, AboutActivity.class));
        }
        return false;
    }
}
