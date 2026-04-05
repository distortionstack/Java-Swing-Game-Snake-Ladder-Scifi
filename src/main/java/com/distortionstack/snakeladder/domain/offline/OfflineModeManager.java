package com.distortionstack.snakeladder.domain.offline;
import com.distortionstack.snakeladder.include.AssetManager;
import com.distortionstack.snakeladder.ui.DisplayController;


public class OfflineModeManager {
    OfflineGameLogicalManeger gamelogical;
    OfflineComponentManager componentManage;

    OfflineModeManager(AssetManager assetManager,DisplayController displayController) {
        gamelogical = new OfflineGameLogicalManeger();
        componentManage = new OfflineComponentManager(this, assetManager,displayController);
    }

    // getter
    public OfflineComponentManager getComponetManagement() {
        return componentManage;
    }

    public OfflineGameLogicalManeger getGamelogical() {
        return gamelogical;
    }
}
