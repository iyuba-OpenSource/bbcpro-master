package com.ai.bbcpro.ui.event;

public class RefreshListEvent {

    String message;

    public RefreshListEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
