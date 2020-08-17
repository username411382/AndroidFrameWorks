package com.ruihe.demo.common.chat.mode;

/**
 * 描述：
 * Created by ruihe on 2016/7/1.
 */
public class Emoji {
    private int code;
    private int res;

    public Emoji(int code, int res) {
        this.code = code;
        this.res = res;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getRes() {
        return this.res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
