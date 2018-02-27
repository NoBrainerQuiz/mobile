package com.example.up804392.nobrainer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by james on 19/02/2018.
 */

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a single instance of DatabaseAccess.
     *
     * @param context
     * @return the instance of DatabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public SQLiteDatabase getDatabase(){
        return database;
    }

    /**
     *
     * Get question from the database using a question id.
     *
     * @param id the id of the question.
     *
     * @return the question String.
     */
    public String getQuestion(int id){
        String query3 = "select Quest from Question where QuestID =" + id;
        Cursor cursor = database.rawQuery(query3, null);
        if(cursor != null) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex("Quest"));
        }
        else
            return "";
    }

    /**
     *
     * Get all the answers related to the question searched.
     *
     * @param id the of the current question
     * @return the cursor of th query with all the answers.
     */
    public Cursor getAnswers(int id){
        String query = "select AnswerText from Answer where QuestID =" + id;
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }

    /**
     *
     *Run query to get check the correct answer.
     *
     * @param questID the current question id.
     * @param answerText the current answer text as String.
     * @return the cursor with the query results.
     */
    public Cursor checkAnswer(int questID, String answerText){
        String query = "select AnswerCorrect from Answer where QuestID = " + questID +" and AnswerText =" + "'" + answerText + "'";
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }

    /**
     *
     *Run query to get all answers to check if user got answer correct.
     *
     * @param questID the current question id.
     * @return the cursor with the query results.
     */
    public Cursor correctAnswer(int questID){
        String query = "select AnswerText, AnswerCorrect from Answer where QuestID = " + questID;
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }
}
