package com.bokun.bkjcb.infomationmanage.Http;

/**
 * Created by DengShuai on 2017/7/19.
 */

public class DefaultEvent {
    public static final int GET_DATA_SUCCESS = 0;
    public static final int GET_DATA_NULL = 1;
    public static final int SOFT_NEED_UPDATE = 2;
    public static final int DATA_NEED_UPDATE = 3;
    public static final int SUCCESS = 4;
    public static final int FAILED = 5;
    private int state_code;
    private int type;

    public DefaultEvent(int state_code, int type) {
        this.state_code = state_code;
        this.type = type;
    }

    public DefaultEvent(int state_code) {
        this.state_code = state_code;
    }

    public void setState_code(int state_code) {
        this.state_code = state_code;
    }

    public int getState_code() {
        return state_code;
    }

    public int getType() {
        return type;
    }
}
