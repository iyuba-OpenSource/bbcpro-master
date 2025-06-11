package com.ai.bbcpro.event;

public class LogoutEvent {
    public int getUid() {
        return uid;
    }

    int uid ;

    public LogoutEvent(int uid) {
        this.uid = uid;
    }
}
