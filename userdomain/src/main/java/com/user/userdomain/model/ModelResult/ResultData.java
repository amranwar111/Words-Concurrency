
package com.user.userdomain.model.ModelResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("news_date")
    @Expose
    private Object newsDate;
    @SerializedName("news_time")
    @Expose
    private Object newsTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Object getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(Object newsDate) {
        this.newsDate = newsDate;
    }

    public Object getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(Object newsTime) {
        this.newsTime = newsTime;
    }

}
