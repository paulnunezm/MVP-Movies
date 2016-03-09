package com.nunez.popularmovies.model.entities;

/**
 * Created by paulnunez on 2/4/16.
 */
public class Review {
    String author;
    String content;
    String url;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
