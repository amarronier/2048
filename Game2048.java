package com.javarush.games.game2048;
import com.javarush.engine.cell.*;


public class Game2048 extends Game{

    private static final int SIDE = 4;
    private int[][] gameField;
    private boolean isGameStopped = false;
    private int score;

    private void createGame() {
        gameField = new int[SIDE][SIDE];
        score = 0;
        setScore(score);
        createNewNumber();
        createNewNumber();
    }

    private void drawScene() {
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                setCellColoredNumber(x,y,gameField[y][x]);
            }
        }
    }

    private void createNewNumber() {
        int y_cell, x_cell;
        do {
            y_cell = getRandomNumber(SIDE);
            x_cell = getRandomNumber(SIDE);
        } while (gameField[y_cell][x_cell] != 0);

        if (getRandomNumber(10) < 9) {gameField[y_cell][x_cell] = 2;}
        else gameField[y_cell][x_cell] = 4;

        int max = getMaxTileValue();
        if(max == 2048) win();
    }

    private Color getColorByValue(int value) {
        Color color = Color.BEIGE;
        if (value == 2) color = Color.PINK;
        if (value == 4) color = Color.VIOLET;
        if (value == 8) color = Color.INDIGO;
        if (value == 16) color = Color.BLUE;
        if (value == 32) color = Color.GREEN;
        if (value == 64) color = Color.SALMON;
        if (value == 128) color = Color.ORANGE;
        if (value == 256) color = Color.CORAL;
        if (value == 512) color = Color.ORCHID;
        if (value == 1024) color = Color.MAGENTA;
        if (value == 2048) color = Color.CHOCOLATE;
        return color;
    }

    private void setCellColoredNumber(int x, int y, int value) {
        Color color = getColorByValue(value);
        if (value == 0) setCellValueEx(x, y, color, "");
        else setCellValueEx(x, y, color, String.valueOf(value));
    }

    private boolean compressRow(int[] row){
        boolean isCompress = false;
        for (int i = 1; i < row.length; i++) {
            if (row[i] == 0) continue;
            else if (row[i] != 0 && row[i-1] == 0) {
                row[i-1] = row[i];
                row[i] = 0;
                isCompress = true;
                i = 0;
            }
        }
        return isCompress;
    }

    private boolean mergeRow(int[] row) {
        boolean isMerge = false;

        for (int i = 0; i < row.length - 1; i++) {
            if (row[i] == 0) continue;
            else if (row[i] == row[i+1])  {
                score = score + row[i] + row[i+1];
                setScore(score);
                row[i] = row[i] + row[i+1];
                row[i+1] = 0;
                isMerge = true;
            }
        }
        return isMerge;
    }

    public void onKeyPress(Key key) {

        if (isGameStopped == true) {
            if (key == Key.SPACE) {
                isGameStopped = false;
                createGame();
                drawScene();
                return;
            } else return;
        }

        boolean areMovesExited = canUserMove();
        if (areMovesExited == false) {
            gameOver();
            return;
        }

        if (key == Key.LEFT) {
            moveLeft();
            drawScene();
        }
        else if (key == Key.RIGHT) {
            moveRight();
            drawScene();
        }
        else if (key == Key.UP) {
            moveUp();
            drawScene();
        }
        else if (key == Key.DOWN) {
            moveDown();
            drawScene();
        }
    }

    private void moveLeft() {
        boolean isChanged = false;
        for (int i = 0; i < SIDE; i++) {
            if (compressRow(gameField[i])) {
                isChanged = true;
            }
            if (mergeRow(gameField[i])) {
                isChanged = true;
                compressRow(gameField[i]);
            }
        }

        if (isChanged) createNewNumber();
    }
    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }
    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }
    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void rotateClockwise() {
        int[][] newgameField = new int[SIDE][SIDE];
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                newgameField[x][SIDE - y - 1] = gameField[y][x];
            }
        }

        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                gameField[y][x] = newgameField[y][x];
            }
        }
    }

    private int getMaxTileValue() {
        int MaxTileValue = 0;
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                if (gameField[y][x] > MaxTileValue) MaxTileValue = gameField[y][x];
            }
        }
        return MaxTileValue;
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.BISQUE, "пабиииида!", Color.BLACK, 80 );
    }

    private boolean canUserMove() {
        boolean areMovesExisted = false;
        for (int x = 0; x < SIDE; x++) {
            for (int y = 0; y < SIDE; y++) {
                if(gameField[y][x] == 0) areMovesExisted = true;
                else if ((x > 0 && gameField[y][x] == gameField[y][x-1]) || (y > 0 && gameField[y][x] == gameField[y-1][x])) {
                    areMovesExisted = true;
                }
            }
        }
        return areMovesExisted;
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.BISQUE, "щит хэппенс",Color.BLACK, 80);
    }

    public void initialize() {
        setScreenSize(SIDE,SIDE);
        createGame();
        drawScene();
    }

}