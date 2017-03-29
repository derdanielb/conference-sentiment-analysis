package net.csa.tweet.collector.model;

public class Tweet {

    private String message;

    public Tweet(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

}