package com.fanwe.base;

/**
 * Created by Administrator on 2016/7/27.
 */
import java.util.List;
public class Result <T>{

    private List<T> body ;


    private String title;


    public void setBody(List<T> body){

        this.body = body;

    }

    public List<T> getBody(){

        return this.body;

    }

    public void setTitle(String title){

        this.title = title;

    }

    public String getTitle(){

        return this.title;

    }

}