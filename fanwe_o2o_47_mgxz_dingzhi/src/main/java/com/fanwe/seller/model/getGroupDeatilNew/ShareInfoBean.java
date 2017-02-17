package com.fanwe.seller.model.getGroupDeatilNew;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by didik on 2016/10/17.
 */

public class ShareInfoBean implements Parcelable ,Serializable{
    private String clickurl;
    private String summary;
    private String imageurl;
    private String title;

    public String getClickurl() {
        return clickurl;
    }

    public void setClickurl(String clickurl) {
        this.clickurl = clickurl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.clickurl);
        dest.writeString(this.summary);
        dest.writeString(this.imageurl);
        dest.writeString(this.title);
    }

    public ShareInfoBean() {
    }

    protected ShareInfoBean(Parcel in) {
        this.clickurl = in.readString();
        this.summary = in.readString();
        this.imageurl = in.readString();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<ShareInfoBean> CREATOR = new Parcelable
            .Creator<ShareInfoBean>() {
        @Override
        public ShareInfoBean createFromParcel(Parcel source) {
            return new ShareInfoBean(source);
        }

        @Override
        public ShareInfoBean[] newArray(int size) {
            return new ShareInfoBean[size];
        }
    };
}
