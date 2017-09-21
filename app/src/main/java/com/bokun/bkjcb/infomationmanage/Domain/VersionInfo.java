package com.bokun.bkjcb.infomationmanage.Domain;

public class VersionInfo {
    private String data_V;

    private String program_V;

    public VersionInfo(String dataV, String programV) {
        this.data_V = dataV;
        this.program_V = programV;
    }

    public VersionInfo() {

    }

    public void setDataV(String dataV) {
        this.data_V = dataV;
    }

    public void setProgramV(String programV) {
        this.program_V = programV;
    }

    public String getDataV() {
        return data_V;
    }

    public String getProgramV() {
        return program_V;
    }
}
