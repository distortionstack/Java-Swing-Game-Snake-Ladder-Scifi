package com.distortionstack.snakeladder.domain;

import java.io.Serializable;

import com.distortionstack.snakeladder.include.config.GameLogical;

public class GameStatus implements Serializable {
    private static final long SerialVersionUID = 10l;
    private int index = GameLogical.START_INDEX; // เริ่มที่ index 0 (ตำแหน่งเริ่มต้น)
    private boolean isWinner;


    void setIndex(int index) {
        if (index < GameLogical.START_INDEX) index = GameLogical.START_INDEX; // ถ้าต่ำกว่า 0 ให้ค้างไว้ที่ 0
        if (index > GameLogical.END_INDEX) {
            index = GameLogical.END_INDEX; // ถ้าเกิน 100 ให้ค้างไว้ที่ 100
            isWinner = true; // ประกาศผู้ชนะเมื่อถึง 100
        }
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }

    public boolean isWinner() {
        return isWinner;
    }    
}