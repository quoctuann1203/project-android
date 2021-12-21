package com.example.learnenglish2;

import java.io.Serializable;
import java.util.List;

public class CorrectWord implements Serializable {
    int level;
    List<String> question;

    public CorrectWord(int level, List<String> question) {
        this.level = level;
        this.question = question;
    }

    public int getLevel() {
        return level;
    }

    public List<String> getQuestion() {
        return question;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setQuestion(List<String> question) {
        this.question = question;
    }

}
