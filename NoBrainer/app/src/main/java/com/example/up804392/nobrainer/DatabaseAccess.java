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
     * Private constructor to avoid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
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

    public Cursor getAnswers(int id){
        String query = "select AnswerText from Answer where QuestID =" + id;
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }

    public Cursor checkAnswer(int questID, String answerText){
        String query = "select AnswerCorrect from Answer where QuestID = " + questID +" and AnswerText =" + "'" + answerText + "'";
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }

    public Cursor correctAnswer(int questID){
        String query = "select AnswerText, AnswerCorrect from Answer where QuestID = " + questID;
        Cursor cursor = database.rawQuery(query, null);
        return cursor;
    }
}
