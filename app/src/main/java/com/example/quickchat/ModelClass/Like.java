package com.example.quickchat.ModelClass;

public class Like {
    String senderID;
    int likeCount;

    public Like() {
    }

    public Like(String senderID) {
        this.senderID = senderID;

    }

    public Like(int likeCount) {
        this.likeCount = likeCount;
    }


    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }
}
