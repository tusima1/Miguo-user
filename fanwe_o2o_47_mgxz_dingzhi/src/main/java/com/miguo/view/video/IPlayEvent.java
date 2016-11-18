package com.miguo.view.video;


/**
 * Created by Administrator on 2016/11/17.
 */

public interface IPlayEvent {
    int START = 1;
    int PAUSE = 2;
    int FINISH = 3;
    int PLAYING = 4;

    void onAvChange(int status, Object object);
}
