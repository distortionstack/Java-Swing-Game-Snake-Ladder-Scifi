package com.distortionstack.snakeladder.domain;

import java.io.Serializable;

import com.distortionstack.snakeladder.include.config.GameLogical;

public class GameStatus implements Serializable {
    private static final long serialVersionUID = 10L;
    private int index = GameLogical.START_INDEX; 
    private boolean isWinner;


    void setIndex(int index) {
        if (index < GameLogical.START_INDEX) index = GameLogical.START_INDEX;
        if (index > GameLogical.END_INDEX) {
            index = GameLogical.END_INDEX; 
            isWinner = true; 
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
