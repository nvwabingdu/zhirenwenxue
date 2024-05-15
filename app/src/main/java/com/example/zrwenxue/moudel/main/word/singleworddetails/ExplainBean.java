package com.example.zrwenxue.moudel.main.word.singleworddetails;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2021-12-30
 * Time: 15:00
 */
public class ExplainBean {
    String speech;
    String explain;


    public ExplainBean(String speech, String explain) {
        this.speech = speech;
        this.explain = explain;
    }

    public String getSpeech() {
        return speech;
    }

    public void setSpeech(String speech) {
        this.speech = speech;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    @Override
    public String toString() {
        return "ExplainBean{" +
                "speech='" + speech + '\'' +
                ", explain='" + explain + '\'' +
                '}';
    }
}
