package com.pratik.codechatandroid;

/**
 * Created by abc on 12/18/2017.
 */

public class InstantMessage {
    private String author;
    private String message;

    public InstantMessage(String author, String message) {
        this.author = author;
        this.message = message;
    }

    //optional
    public InstantMessage(){

    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }
}
