package com.distortionstack.snakeladder.domain;

/**
 * Immutable snapshot of what happened in one turn.
 * ฝั่ง Logic สร้างแล้วส่งให้ฝั่ง UI — UI ไม่ต้องไป query state ใน Logic เอง
 */
public class TurnResult {
    private final int diceValue;   // ค่าลูกเต๋าที่ทอยได้
    private final int startIndex;  // ตำแหน่งก่อนเดิน
    private final int walkEndIndex;// ตำแหน่งหลังเดิน (ก่อน warp)
    private final int finalIndex;  // ตำแหน่งสุดท้าย (หลัง warp ถ้ามี)
    private final boolean isWarp;  // โดนงูหรือบันไดไหม
    private final boolean isWin;   // ชนะเลยไหม

    public TurnResult(int diceValue, int startIndex, int walkEndIndex,
                      int finalIndex, boolean isWarp, boolean isWin) {
        this.diceValue    = diceValue;
        this.startIndex   = startIndex;
        this.walkEndIndex = walkEndIndex;
        this.finalIndex   = finalIndex;
        this.isWarp       = isWarp;
        this.isWin        = isWin;
    }

    public int  getDiceValue()    { return diceValue;    }
    public int  getStartIndex()   { return startIndex;   }
    public int  getWalkEndIndex() { return walkEndIndex; }
    public int  getFinalIndex()   { return finalIndex;   }
    public boolean isWarp()       { return isWarp;       }
    public boolean isWin()        { return isWin;        }
}