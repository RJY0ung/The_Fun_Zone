package com.example.thefunzone;

import android.provider.BaseColumns;

//container for different constants. can change column name here
public final class QuizContract {

    private QuizContract(){
    }

    public static class QuestionsTable implements BaseColumns {

        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_QUESTION = "questions";
        public static final String COLUMN_OPTION1 = "option1";
        public static final String COLUMN_OPTION2 = "option2";
        public static final String COLUMN_OPTION3 = "option3";
        public static final String COLUMN_OPTION4 = "option4";
        public static final String COLUMN_ANSWER_NUM= "answer_num";
        public static final String COLUMN_DIFFICULTY = "difficulty";
    }
}
