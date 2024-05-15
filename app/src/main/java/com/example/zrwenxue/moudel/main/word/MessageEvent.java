package com.example.zrwenxue.moudel.main.word;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-04-07
 * Time: 20:51
 */
public class MessageEvent {
    private String message;
    public MessageEvent(String message){
        this.message=message;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
