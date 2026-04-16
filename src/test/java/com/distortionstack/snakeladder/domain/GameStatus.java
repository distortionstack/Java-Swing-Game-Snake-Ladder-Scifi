package com.distortionstack.snakeladder.domain;

import java.io.Serializable;

public class GameStatus implements Serializable {
    private static final long SerialVersionUID = 10l;
    private int index;
    private int visibleIndex;
    private boolean isWinner;


    public void setIndex(int index) {
        if (index < 0) index = 0;
        if (index > 100) {
            index = 100; // ถ้าเกิน 100 ให้ค้างไว้ที่ 100
            isWinner = true; // ประกาศผู้ชนะเมื่อถึง 100
        }
        this.index = index;
    }

    public void setVisibleIndex(int visibleIndex) {
        if (visibleIndex < 0 || visibleIndex > 100) {
            throw new IllegalArgumentException("VisibleIndex must be 0-100");
        }
        this.visibleIndex = visibleIndex;
    }

    public int getIndex() {
        return index;
    }

    public int getVisibleIndex() {
        return visibleIndex;
    }

    public boolean isWinner() {
        return isWinner;
    }    
}