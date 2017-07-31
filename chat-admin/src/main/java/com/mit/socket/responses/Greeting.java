package com.mit.socket.responses;

/**
 * Created by Hung Le on 2/28/17.
 */
public class Greeting {
    private String content;

    public Greeting() {
    }

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
