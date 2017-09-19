package com.bokun.bkjcb.infomationmanage.Domain;

public class VersionInfo {
    private String dataV;

    private String programV;

    public VersionInfo(String dataV, String programV) {
        this.dataV = dataV;
        this.programV = programV;
    }

    public VersionInfo() {

    }

    public void setDataV(String dataV) {
        this.dataV = dataV;
    }

    public void setProgramV(String programV) {
        this.programV = programV;
    }

    public String getDataV() {
        return dataV;
    }

    public String getProgramV() {
        return programV;
    }
}
