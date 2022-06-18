package com.saharia.notesell.model;

public class Qmodel {
    private String QpostID;
    private String QpostImage;
    private String QpostedBy;
    private String QpostDescription;

    private long postedAt;

    private int postLike;

    private int CommentCount;

    public int getPostLike() {
        return postLike;
    }

    public void setPostLike(int postLike) {
        this.postLike = postLike;
    }

    public Qmodel() {
    }

    public Qmodel(String qpostID, String qpostImage, String qpostedBy, String qpostDescription, long postedAt) {
        QpostID = qpostID;
        QpostImage = qpostImage;
        QpostedBy = qpostedBy;
        QpostDescription = qpostDescription;
        this.postedAt = postedAt;
    }

    public String getQpostID() {
        return QpostID;
    }

    public void setQpostID(String qpostID) {
        QpostID = qpostID;
    }

    public String getQpostImage() {
        return QpostImage;
    }

    public void setQpostImage(String qpostImage) {
        QpostImage = qpostImage;
    }

    public String getQpostedBy() {
        return QpostedBy;
    }

    public void setQpostedBy(String qpostedBy) {
        QpostedBy = qpostedBy;
    }

    public String getQpostDescription() {
        return QpostDescription;
    }

    public void setQpostDescription(String qpostDescription) {
        QpostDescription = qpostDescription;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }


    public int getCommentCount() {
        return CommentCount;
    }

    public void setCommentCount(int commentCount) {
        CommentCount = commentCount;
    }
}
