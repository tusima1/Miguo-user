package com.fanwe.seller.model;

/**
 * Created by whiskeyfei on 15-7-24.
 */
public class TypeModel {
    private int imageId;
    private String content;
    private  boolean ifSelected=false;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public boolean isIfSelected() {
        return ifSelected;
    }

    public void setIfSelected(boolean ifSelected) {
        this.ifSelected = ifSelected;
    }

    @Override
    public String toString() {
        return "ImageModel{" +
                "imageId=" + imageId +
                ", content='" + content + '\'' +
                '}';
    }
}
