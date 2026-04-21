package com.distortionstack.snakeladder.domain;

import java.io.Serializable;

class LobbyStatus implements Serializable {
    private static final long serialVersionUID = 10L;
    boolean isReady;

    void setReady(boolean isReady) {
        this.isReady = isReady;
    }

    public boolean isReady() {
        return isReady;
    }
}
