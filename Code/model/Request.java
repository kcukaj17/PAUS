package model;

import java.util.Date;

public class Request {
    private int id;
    private int authorId;
    private Date date;
    private String message;
    private boolean done;

    public Request(int id, int authorId, Date date, String message, boolean done) {
        this.id = id;
        this.authorId = authorId;
        this.date = date;
        this.message = message;
        this.done = done;
    }

    public Request(int authorId, Date date, String message, boolean done) {
        this(0, authorId, date, message, done);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
