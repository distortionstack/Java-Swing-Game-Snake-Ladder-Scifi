package com.distortionstack.snakeladder.domain;

import java.io.Serializable;

public class GameStatus implements Serializable {
    private static final long SerialVersionUID = 10l;
    int index;
    int visibleIndex;
    boolean isWinner;


    public void setIndex(int index) {
        this.index = index;
    }

    public void setVisibleIndex(int visibleIndex) {
        this.visibleIndex = visibleIndex;
    }

    public int getIndex() {
        return index;
    }

    public int getVisibleIndex() {
        return visibleIndex;
    }
}