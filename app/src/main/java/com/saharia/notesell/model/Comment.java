package com.saharia.notesell.model;

public class Comment {
    private String CommentBody;

    private long commentTime;
    private String commentedBY;

    public Comment() {
    }


    public String getCommentBody() {
        return CommentBody;
    }

    public void setCommentBody(String commentBody) {
        CommentBody = commentBody;
    }

    public long getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(long commentTime) {
        this.commentTime = commentTime;
    }

    public String getCommentedBY() {
        return commentedBY;
    }

    public void setCommentedBY(String commentedBY) {
        this.commentedBY = commentedBY;
    }
}
