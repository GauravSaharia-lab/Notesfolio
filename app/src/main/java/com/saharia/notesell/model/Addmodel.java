package com.saharia.notesell.model;

public class Addmodel

{
    private String postID;
    private String postImage;
    private String postedBy;
    private String postDescription;
    private String postCategory;
    private String postPrice;
    private long postedAt;
    private String postTitile;
    private String pdf;
    private long viewsCount;
    boolean favourite;

    public Addmodel() {
    }

    public Addmodel(String postID, String postImage, String postedBy, String postDescription, String postCategory, String postPrice, long postedAt, String postTitile, String pdf, long viewsCount, boolean favourite) {
        this.postID = postID;
        this.postImage = postImage;
        this.postedBy = postedBy;
        this.postDescription = postDescription;
        this.postCategory = postCategory;
        this.postPrice = postPrice;
        this.postedAt = postedAt;
        this.postTitile = postTitile;
        this.pdf = pdf;
        this.viewsCount = viewsCount;
        this.favourite = favourite;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public String getPostPrice() {
        return postPrice;
    }

    public void setPostPrice(String postPrice) {
        this.postPrice = postPrice;
    }

    public long getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(long postedAt) {
        this.postedAt = postedAt;
    }

    public String getPostTitile() {
        return postTitile;
    }

    public void setPostTitile(String postTitile) {
        this.postTitile = postTitile;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public long getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
