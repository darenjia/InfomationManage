package com.bokun.bkjcb.infomationmanage.Domain;

public class Z_Quxian {
    private int id;

    private String newName;

    private int sortSeq;

    public Z_Quxian(int id, String newName, int sortSeq) {
        this.id = id;
        this.newName = newName;
        this.sortSeq = sortSeq;
    }

    public int getId() {
        return id;
    }

    public String getNewName() {
        return newName;
    }

    public int getSortSeq() {
        return sortSeq;
    }
}
