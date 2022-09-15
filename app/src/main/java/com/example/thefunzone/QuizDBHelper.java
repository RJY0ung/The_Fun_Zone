package com.example.thefunzone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.thefunzone.QuizContract.*;

import java.util.ArrayList;

public class QuizDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Quiz.db";

    //always increment this by 1 if need to update database schema
    private static final int DATABASE_VERSION = 1;

    private  SQLiteDatabase db;

    public QuizDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION4 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NUM + " INTEGER, " +
                QuestionsTable.COLUMN_DIFFICULTY + " TEXT" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();

    }

    //if need to update table, this function would delete the old version and create the new version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);

    }

    private void fillQuestionsTable(){
        Questions q1 = new Questions("What is 2 + 1?", "1", "2", "3", "4", 3, Questions.DIFFICULTY_EASY);
        addQuestion(q1);

        Questions q2 = new Questions("What is 2 + 4?", "4", "6", "9", "1", 2, Questions.DIFFICULTY_EASY);
        addQuestion(q2);

        Questions q3 = new Questions("What is 5 + 3?", "8", "11", "3", "5", 1, Questions.DIFFICULTY_EASY);
        addQuestion(q3);

        Questions q4 = new Questions("What is 10 + 5?", "12", "15", "16", "11", 2, Questions.DIFFICULTY_EASY);
        addQuestion(q4);

        Questions q5 = new Questions("Whats is 12 + 8?", "18", "9", "15", "20", 4, Questions.DIFFICULTY_EASY);
        addQuestion(q5);

        Questions q6 = new Questions("What is 10 - 5?", "6", "7", "4", "5", 4, Questions.DIFFICULTY_MEDIUM);
        addQuestion(q6);

        Questions q7 = new Questions("What is 12 - 4?", "10", "8", "6", "4", 2, Questions.DIFFICULTY_MEDIUM);
        addQuestion(q7);

        Questions q8 = new Questions("What is 10 - 7?", "5", "7", "2", "3", 4, Questions.DIFFICULTY_MEDIUM);
        addQuestion(q8);

        Questions q9 = new Questions("What is 19 - 8?", "11", "17", "12", "10", 1, Questions.DIFFICULTY_MEDIUM);
        addQuestion(q9);

        Questions q10 = new Questions("What is 23 - 15?", "13", "11", "8", "6", 3, Questions.DIFFICULTY_MEDIUM);
        addQuestion(q10);

        Questions q11 = new Questions("What is 2 x 5?", "10", "12", "14", "16", 1, Questions.DIFFICULTY_HARD);
        addQuestion(q11);

        Questions q12 = new Questions("What is 6 / 3?", "4", "3", "2", "1", 3, Questions.DIFFICULTY_HARD);
        addQuestion(q12);

        Questions q13 = new Questions("What is 15 / 3?", "2", "5", "7", "8", 2, Questions.DIFFICULTY_HARD);
        addQuestion(q13);

        Questions q14 = new Questions("What is 4 x 7?", "7", "14", "21", "28", 4, Questions.DIFFICULTY_HARD);
        addQuestion(q14);

        Questions q15 = new Questions("What is 27 / 9?", "1", "2", "3", "4", 3, Questions.DIFFICULTY_HARD);
        addQuestion(q15);
    }

    private void addQuestion(Questions questions) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, questions.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, questions.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, questions.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, questions.getOption3());
        cv.put(QuestionsTable.COLUMN_OPTION4, questions.getOption4());
        cv.put(QuestionsTable.COLUMN_ANSWER_NUM, questions.getAnswerNum());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY, questions.getDifficulty());

        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    public ArrayList<Questions> getAllQuestions(){
        ArrayList<Questions> questionsList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if(c.moveToFirst()){
            do{
                Questions questions = new Questions();
                questions.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                questions.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                questions.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                questions.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                questions.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                questions.setAnswerNum(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NUM)));
                questions.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                questionsList.add(questions);
            }while(c.moveToNext());
        }

        c.close();
        return questionsList;
    }

    public ArrayList<Questions> getQuestions(String difficulty){
        ArrayList<Questions> questionsList = new ArrayList<>();
        db = getReadableDatabase();

        String[] selectionArgs = new String[]{difficulty};
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME +
                " WHERE " + QuestionsTable.COLUMN_DIFFICULTY + " = ?", selectionArgs);

        if(c.moveToFirst()){
            do{
                Questions questions = new Questions();
                questions.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                questions.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                questions.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                questions.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                questions.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                questions.setAnswerNum(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NUM)));
                questions.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                questionsList.add(questions);
            }while(c.moveToNext());
        }

        c.close();
        return questionsList;
    }

}
