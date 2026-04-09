package com.distortionstack.snakeladder.domain;

import java.io.Serializable;

class LobbyStatus implements Serializable {
    private static final long SerialVersionUID = 10l;
    boolean isReady;

    public void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    public boolean isReady() {
        return isReady;
    }
}