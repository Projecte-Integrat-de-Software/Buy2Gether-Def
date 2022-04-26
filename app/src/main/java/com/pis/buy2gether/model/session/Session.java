package com.pis.buy2gether.model.session;

public enum Session {
    INSTANCE;
    private String sessionId = "";

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

}


