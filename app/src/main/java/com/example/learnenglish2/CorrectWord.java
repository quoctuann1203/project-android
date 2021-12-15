package com.example.learnenglish2;

public class CorrectWord {
    int level;
    String[] question;

    public CorrectWord(int level, String[] question) {
        this.level = level;
        this.question = question;
    }

    public int getLevel() {
        return level;
    }

    public String[] getQuestion() {
        return question;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setQuestion(String[] question) {
        this.question = question;
    }

}
