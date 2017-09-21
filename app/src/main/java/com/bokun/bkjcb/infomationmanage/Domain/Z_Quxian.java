package com.bokun.bkjcb.infomationmanage.Domain;

public class Z_Quxian {
    private int id;

    private String NewName;

    private int sortSeq;

    public Z_Quxian(int id, String newName, int sortSeq) {
        this.id = id;
        this.NewName = newName;
        this.sortSeq = sortSeq;
    }

    public int getId() {
        return id;
    }

    public String getNewName() {
        return NewName;
    }

    public int getSortSeq() {
        return sortSeq;
    }
}
