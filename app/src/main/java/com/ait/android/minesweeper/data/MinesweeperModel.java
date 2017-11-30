package com.ait.android.minesweeper.data;

import java.util.Random;

/**
 * Created by ellamary on 9/21/17.
 */

public class MinesweeperModel {

    private static MinesweeperModel minesweeperModel = null;

    private MinesweeperModel() {

    }

    public static MinesweeperModel getInstance() {
        if(minesweeperModel == null) {
            minesweeperModel = new MinesweeperModel();
        }

        return minesweeperModel;
    }

    public static final int boardSize = 10;
    private static final int numMines = 15;
    private Random rnd = new Random();
    private boolean flagMode;
    private boolean gameOver;

    private Field[][] minefield = new Field[boardSize][boardSize];


    public void placeMines() {

        gameOver = false;

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                minefield[i][j] = new Field(i, j);
            }
        }

        for (int i = 0; i < numMines; i++) {
            int xCoord = rnd.nextInt(boardSize);
            int yCoord = rnd.nextInt(boardSize);

            while(minefield[xCoord][yCoord].isMine()) {
                xCoord = rnd.nextInt(boardSize);
                yCoord = rnd.nextInt(boardSize);
            }
            minefield[xCoord][yCoord].setMine(true);
        }
    }

    public void setFlagMode(boolean f) {
        flagMode = f;
    }

    public boolean getFlagMode() {
        return flagMode;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isGameWon() {
        boolean won = true;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if(minefield[i][j].isMine() && !minefield[i][j].isFlag()) {
                    won = false;
                }
            }
        }
        return won;
    }

    public Field getFieldContent(int x, int y) {
        return minefield[x][y];
    }

    public int getSurrounding(int x, int y) {
        int count = 0;

        if(minefield[x][y].isMine()) {
            return -1;
        }
        else {
            if (x >= 1 && y >= 1 && minefield[x - 1][y - 1].isMine()) {
                count++;
            }
            if (y >= 1 && minefield[x][y - 1].isMine()) {
                count++;
            }
            if (x < boardSize - 1 && y >= 1 && minefield[x + 1][y - 1].isMine()) {
                count++;
            }
            if (x >= 1 && minefield[x - 1][y].isMine()) {
                count++;
            }
            if (x < boardSize - 1 && minefield[x + 1][y].isMine()) {
                count++;
            }
            if (x >= 1 && y < boardSize - 1 && minefield[x - 1][y + 1].isMine()) {
                count++;
            }
            if (y < boardSize - 1 && minefield[x][y + 1].isMine()) {
                count++;
            }
            if (x < boardSize - 1 && y < boardSize - 1 && minefield[x + 1][y + 1].isMine()) {
                count++;
            }
            return count;
        }
    }

    public void reveal(int x, int y) {
        if(x >= 0 && y >= 0 && x < boardSize && y < boardSize && !minefield[x][y].isClicked()) {
            if(minefield[x][y].isMine()) {
                gameOver = true;
            } else if(getSurrounding(x,y) != 0) {
                minefield[x][y].setClicked(true);
                return;
            } else {
                minefield[x][y].setClicked(true);
                reveal(x+1, y);
                reveal(x-1, y);
                reveal(x, y+1);
                reveal(x, y-1);
                reveal(x-1, y-1);
                reveal(x-1, y+1);
                reveal(x+1, y-1);
                reveal(x+1, y+1);

            }
        }
    }

    public void resetGame() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                minefield[i][j].reset();
            }
        }
        placeMines();
    }


}
