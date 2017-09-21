package com.bokun.bkjcb.infomationmanage.Domain;

public class LevelBind {
    private int id;

    private int G_LevelID;

    private int Q_LevelID;

    public LevelBind(int id, int gLevelid, int qLevelid) {
        this.id = id;
        this.G_LevelID = gLevelid;
        this.Q_LevelID = qLevelid;
    }

    public int getId() {
        return id;
    }

    public int getGLevelid() {
        return G_LevelID;
    }

    public int getQLevelid() {
        return Q_LevelID;
    }
}
