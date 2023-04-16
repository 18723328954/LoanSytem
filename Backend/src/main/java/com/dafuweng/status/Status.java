package com.dafuweng.status;

public class Status {
    private int resCode;
    private String info;

    public Status(){

    }

    public Status(int resCode, String info) {
        this.resCode = resCode;
        this.info = info;
    }

    // Getters and Setters
    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
