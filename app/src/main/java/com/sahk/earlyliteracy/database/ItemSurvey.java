package com.sahk.earlyliteracy.database;

/**
 * Created by security on 29/11/2016.
 */

public class ItemSurvey {

    public static String TYPE_RADIO = "radio";
    public static String TYPE_CHECKBOX = "checkbox";
    public static String TYPE_TEXT = "text";

    private int questionNo;
    private String question;
    private String type;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String answer;

    public int getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(int questionNo) {
        this.questionNo = questionNo;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getAnswer() {
        if(answer==null){
            answer = "";
        }
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
