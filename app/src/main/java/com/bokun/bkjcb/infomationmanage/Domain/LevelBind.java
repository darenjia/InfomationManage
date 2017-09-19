package com.bokun.bkjcb.infomationmanage.Domain;

public class LevelBind {
    private int id;

    private int gLevelid;

    private int qLevelid;

    public LevelBind(int id, int gLevelid, int qLevelid) {
        this.id = id;
        this.gLevelid = gLevelid;
        this.qLevelid = qLevelid;
    }

    public int getId() {
        return id;
    }

    public int getGLevelid() {
        return gLevelid;
    }

    public int getQLevelid() {
        return qLevelid;
    }
}
