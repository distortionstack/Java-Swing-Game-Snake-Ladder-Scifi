package com.distortionstack.snakeladder.domain.offline;

import javax.swing.JButton;

import com.distortionstack.snakeladder.include.AssetManager;
import com.distortionstack.snakeladder.ui.DisplayController;
import com.distortionstack.snakeladder.ui.offline.OfflineGame;
import com.distortionstack.snakeladder.ui.offline.OfflineLobby;

public class OfflineComponentManager {
    OfflineModeManager offline;
    OfflineLobby offlineLobby;
    OfflineGame offlineGame;
    DisplayController displayController;

    OfflineComponentManager(OfflineModeManager offline, AssetManager assetManager,DisplayController displayController) {
        this.offline = offline;

        offlineLobby = new OfflineLobby(assetManager,displayController);
        LobbyListenerSetUp();

        offlineGame = new OfflineGame(assetManager, offline.getGamelogical(),displayController);
        GameListenerSetUp();

        this.displayController = displayController;
    }

    public void GameListenerSetUp() {
        offlineGame.addDiceButtonListener(e -> {
            offline.getGamelogical().diceRoll();
            offline.getGamelogical().updateIndex();

            System.out.println(
                    "Target Index is: " + offline.getGamelogical().getCurrentPlayerIndex()
                );

            new OfflineAnimationThread(
                    offline.getGamelogical().getCurrentPlayer(),
                    offlineGame,
                    offline.getGamelogical());
        });
    }

    public void LobbyListenerSetUp() {
        for (JButton button : offlineLobby.getSelectSkinButton()) {
            button.addActionListener(e -> {
                button.setEnabled(false);
                offline.getGamelogical().addPlayer(button);
                System.out.println(offline.getGamelogical().getPlayerList().getLast().getSkincode());
            });
        }
        offlineLobby.getStartButton().addActionListener(e -> {
            displayController.startOfflineGame();
        });
    }

    public OfflineGame getOfflineGame() {
        return offlineGame;
    }

    public OfflineLobby getOfflineLobby() {
        return offlineLobby;
    }

}
