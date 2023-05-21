package com.example.stdmanager.models;

import java.io.Serializable;

public class ScoreInfo implements Serializable {
    private int studentID, subjectID;
    private double score;
    private String studentFullName, subjectName;

    public ScoreInfo(int studentID, int subjectID, double score, String studentFullName, String subjectName) {
        this.studentID = studentID;
        this.subjectID = subjectID;
        this.score = score;
        this.studentFullName = studentFullName;
        this.subjectName = subjectName;
    }

    public int getStudentID() {
        return studentID;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getStudentFullName() {
        return studentFullName;
    }

    public String getSubjectName() {
        return subjectName;
    }
}
