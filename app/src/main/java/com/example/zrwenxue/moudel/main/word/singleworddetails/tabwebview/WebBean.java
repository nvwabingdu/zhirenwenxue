package com.example.zrwenxue.moudel.main.word.singleworddetails.tabwebview;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-01-01
 * Time: 11:47
 */
public class WebBean {
    int rawID;
    String name;
    String findWord;
    String datahtml;


    public WebBean(int rawID, String name, String findWord, String datahtml) {
        this.rawID = rawID;
        this.name = name;
        this.findWord = findWord;
        this.datahtml = datahtml;
    }

    public int getRawID() {
        return rawID;
    }

    public void setRawID(int rawID) {
        this.rawID = rawID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFindWord() {
        return findWord;
    }

    public void setFindWord(String findWord) {
        this.findWord = findWord;
    }

    public String getDatahtml() {
        return datahtml;
    }

    public void setDatahtml(String datahtml) {
        this.datahtml = datahtml;
    }
}
