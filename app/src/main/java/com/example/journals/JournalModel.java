package com.example.journals;

import com.google.firebase.Timestamp;

public class JournalModel {
    private String title;
    private String content;
    private String post;
    private Timestamp time;

    public JournalModel() {
    }

    public JournalModel(String title, String content, String post, Timestamp time) {
        this.title = title;
        this.content = content;
        this.post = post;
        this.time = time;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
