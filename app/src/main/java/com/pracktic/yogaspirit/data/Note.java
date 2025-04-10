package com.pracktic.yogaspirit.data;

import com.pracktic.yogaspirit.utils.KeyGen;

import java.io.Serializable;

public class Note implements Serializable {
    private String title, content, id;


    public Note(){

    }

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
        id = KeyGen.generateKey();
    }

    public String getId() {
        return id;
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
}
