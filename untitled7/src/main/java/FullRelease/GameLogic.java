package FullRelease;

import javax.swing.*;
import java.util.Random;
import javax.swing.Timer;

public class GameLogic {
    private final GamePanel gamePanel;
    private final Random random = new Random();
    private int playerLives = 3;
    private int enemyLives = 3;
    private boolean gameOver = false;
    private boolean playerStarts = true;
    private int shotCount = 0;
    private int currentRound = 0;
    private boolean secondShot = false;
    private boolean hasMagnifyingGlass = false;
    private boolean hasFreezeAbility = false;
    private boolean[] roundLive;
    private final MagnifyingGlass magnifyingGlass = new MagnifyingGlass();
    private final HealthPotion healthPotion = new HealthPotion();
    private final Freeze freeze = new Freeze();

    public GameLogic(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        initializeRounds();
    }

    private void initializeRounds() {
        roundLive = new boolean[6];
        int liveCount = 0;

        for (int i = 0; i < roundLive.length; i++) {
            roundLive[i] = random.nextBoolean();

            if (roundLive[i]) {
                liveCount++;
            }
        }
        if (liveCount == 0) {
            int randomRound = random.nextInt(roundLive.length);
            roundLive[randomRound] = true;
        }
        magnifyingGlass.setNextRoundLive(roundLive[0]);
    }

    public void startGame() {
        gameOver = false;
        playerLives = 3;
        enemyLives = 3;
        shotCount = 0;
        currentRound = 0;
        playerStarts = true;
        gamePanel.updateTurnLabel("Player");
        applyRoundSettings();
        gamePanel.getShootSign().setVisible(true);
        gamePanel.updateHealthIcons();
        gamePanel.getLampLabel().setVisible(true);
        gamePanel.hideRevolvers();
    }

    public void applyRoundSettings() {
        if (Game.playerWins == 0) {
            gamePanel.getShopButton().setVisible(false);
            gamePanel.getMagnifyingGlassButton().setVisible(false);
            gamePanel.getHealthPotionButton().setVisible(false);
            gamePanel.getFreezeAbilityButton().setVisible(false);
        } else if (Game.playerWins == 1) {
            gamePanel.getShopButton().setVisible(true);
            gamePanel.getMagnifyingGlassButton().setVisible(hasMagnifyingGlass);
            gamePanel.getHealthPotionButton().setVisible(healthPotion.getCount() > 0);
            gamePanel.getFreezeAbilityButton().setVisible(hasFreezeAbility);
        } else if (Game.playerWins == 2) {
            playerLives = 1;
            enemyLives = 1;
            gamePanel.updateHealthIcons();
            gamePanel.getShopButton().setVisible(true);
            gamePanel.getMagnifyingGlassButton().setVisible(hasMagnifyingGlass);
            gamePanel.getHealthPotionButton().setVisible(healthPotion.getCount() > 0);
            gamePanel.getFreezeAbilityButton().setVisible(hasFreezeAbility);
        }
    }

    public void setHasMagnifyingGlass(boolean has) {
        hasMagnifyingGlass = has;
        magnifyingGlass.acquireMagnifyingGlass();
        gamePanel.getMagnifyingGlassButton().setEnabled(has);
        gamePanel.updateItemVisibility();
    }

    public void setHasHealthPotion(boolean has) {
        if (has) {
            healthPotion.addPotion();
        }
        gamePanel.getHealthPotionButton().setEnabled(healthPotion.getCount() > 0);
        gamePanel.updateItemVisibility();
    }

    public void setHasFreezeAbility(boolean has) {
        hasFreezeAbility = has;
        gamePanel.getFreezeAbilityButton().setEnabled(has);
        gamePanel.updateItemVisibility();
    }

    public boolean hasMagnifyingGlass() {
        return hasMagnifyingGlass;
    }

    public int getHealthPotionCount() {
        return healthPotion.getCount();
    }

    public boolean hasFreezeAbility() {
        return hasFreezeAbility;
    }

    public int getPlayerLives() {
        return playerLives;
    }

    public int getEnemyLives() {
        return enemyLives;
    }

    public boolean isGameOver() {
        return !gameOver;
    }

    public boolean isPlayerStarts() {
        return playerStarts;
    }

    public void playRussianRoulette(boolean isPlayer) {
        if (gameOver) {
            return;
        }
        gamePanel.hideRevolvers();
        boolean shotFired = random.nextInt(7 - shotCount) == 0;
        shotCount++;

        boolean currentRoundIsLive = roundLive[currentRound];

        if (shotFired) {
            if (isPlayer) {
                playerLives--;
                gamePanel.updateHealthIcons();
                gamePanel.updateCoinsLabel();
                GamePanel.CoinManager.addCoins(-1);
            } else {
                enemyLives--;
                gamePanel.updateHealthIcons();
                gamePanel.updateCoinsLabel();
                GamePanel.CoinManager.addCoins(2);
            }

            if (isPlayer && playerLives <= 0) {
                gameOver = true;
                gamePanel.updateStatus("Player died", false);
                handleGameOver(true);
            } else if (!isPlayer && enemyLives <= 0) {
                gameOver = true;
                gamePanel.updateStatus("Enemy died", false);
                handleGameOver(false);
            } else {
                gamePanel.hideButtonsWithDelay();
                String whoWasShot = isPlayer ? "Player" : "Enemy";
                String message = whoWasShot + " was shot";
                gamePanel.updateStatus(message, currentRoundIsLive);
                gamePanel.showRevolverWithSparkle(isPlayer, currentRoundIsLive);
                Timer timer = new Timer(3000, e -> {
                    gamePanel.hideRevolvers();
                    gamePanel.hideSparklingEffects();
                    if (isPlayer) {
                        prepareNextTurn(true);
                    } else {
                        if (currentRoundIsLive) {
                            prepareNextTurn(true); 
                        } else {
                            prepareNextTurn(false);
                        }
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
            shotCount = 0;
        } else {
            if (shotCount == 6) {
                playRussianRoulette(isPlayer);
            } else {
                String whoSurvived = isPlayer ? "Player" : "Enemy";
                String message = whoSurvived + " survives.";
                gamePanel.updateStatus(message, true);
                if (isPlayer) {
                    gamePanel.showRevolver2();
                    prepareNextTurn(true);
                } else {
                    gamePanel.showRevolver1();
                    prepareNextTurn(true); 
                }
            }
        }

        advanceRound();
    }


    private void advanceRound() {
        currentRound++;
        if (currentRound >= roundLive.length) {
            currentRound = 0;
        }
        magnifyingGlass.setNextRoundLive(roundLive[currentRound]);
        applyRoundSettings();
    }

    public void enemyTakeTurn() {
        if (gameOver) {
            return;
        }

        boolean decidesToShootPlayer = random.nextInt(2) == 0;

        Timer decisionDelay = new Timer(1000, e -> {
            playRussianRoulette(!decidesToShootPlayer);
            ((Timer) e.getSource()).stop();
        });
        decisionDelay.setRepeats(false);
        decisionDelay.start();
    }

    public void delayEnemyTurn() {
        Timer delayTimer = new Timer(1000, e -> {
            enemyTakeTurn();
            ((Timer) e.getSource()).stop();
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
    }

    public void useFreezeAbility() {
        if (hasFreezeAbility && !freeze.isFrozen() && playerStarts) {
            freeze.activateFreeze();
            gamePanel.updateStatus("Freeze activated!", true);
            gamePanel.getFreezeAbilityButton().setVisible(false);
            hasFreezeAbility = false;
            gamePanel.updateItemVisibility();
            prepareNextTurn(true);
        } else {
            gamePanel.updateStatus("Cannot use Freeze Time!", false);
        }
    }

    public void useMagnifyingGlass() {
        if (hasMagnifyingGlass) {
            String previewMessage = magnifyingGlass.checkRound();
            JOptionPane.showMessageDialog(gamePanel.getFrame(), previewMessage, "Magnifying Glass Result", JOptionPane.INFORMATION_MESSAGE);
            gamePanel.getMagnifyingGlassButton().setVisible(false);
            hasMagnifyingGlass = false;
            gamePanel.updateItemVisibility();
        } else {
            JOptionPane.showMessageDialog(gamePanel.getFrame(), "No magnifying glass available!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void useHealthPotion() {
        if (playerStarts && healthPotion.getCount() > 0 && playerLives < 3) {
            boolean used = healthPotion.usePotion();
            if (used) {
                playerLives++;
                gamePanel.updateStatus("Health restored by 1!", true);
                gamePanel.getHealthPotionButton().setVisible(false);
                gamePanel.updateHealthIcons();
            }
        } else {
            String message = "Full HP" + (healthPotion.getCount() > 0 ? "" : " No health potions available!");
            gamePanel.updateStatus(message, false);
        }
        updateHealthPotionButtonState();
    }

    void updateHealthPotionButtonState() {
        gamePanel.getHealthPotionButton().setEnabled(canUseHealthPotion());
    }

    private boolean canUseHealthPotion() {
        return playerStarts && healthPotion.getCount() > 0 && playerLives < 3;
    }

    void prepareNextTurn(boolean isPlayer) {
        if (secondShot) {
            secondShot = false;
            gamePanel.updateTurnLabel("Player");
        } else if (freeze.isFrozen() && isPlayer) {
            secondShot = true;
            gamePanel.updateTurnLabel("Player");
        } else {
            playerStarts = isPlayer;
            if (isPlayer) {
                gamePanel.updateTurnLabel("Player");
            } else {
                gamePanel.updateTurnLabel("Enemy");
                gamePanel.getPlayerButton().setEnabled(false);
                gamePanel.getEnemyButton().setEnabled(false);
                delayEnemyTurn();
            }
        }
        updateHealthPotionButtonState();
    }

    private void handleGameOver(boolean playerDied) {
        String gameOverMessage;
        if (playerDied) {
            gameOverMessage = "YOU DIED";
        } else {
            gameOverMessage = "YOU WIN!";
            applyRoundSettings();
        }
        gamePanel.updateStatusGameOver(gameOverMessage);
        gamePanel.getPlayerButton().setEnabled(false);
        gamePanel.getEnemyButton().setEnabled(false);

        new Timer(2000, e -> {
            if (!playerDied) {
                if (Game.playerWins < 3) {
                    gamePanel.switchToElevatorPanel();
                } else {
                    gamePanel.switchToMenu();
                }
            } else {
                GamePanel.CoinManager.resetCoins();
                gamePanel.switchToMenu();
            }
            ((Timer) e.getSource()).stop();
        }).start();
    }
}
