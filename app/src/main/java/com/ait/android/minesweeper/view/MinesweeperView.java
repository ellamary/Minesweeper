package com.ait.android.minesweeper.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.ait.android.minesweeper.R;
import com.ait.android.minesweeper.data.Field;
import com.ait.android.minesweeper.data.MinesweeperModel;

/**
 * Created by ellamary on 9/21/17.
 */

public class MinesweeperView extends View {

    public static final String GAME_OVER = "GAME OVER";
    Paint paintBg, paintLine, paintText1, paintText2, paintText3, paintText4, paintZero;
    int boardSize = MinesweeperModel.getInstance().boardSize;
    Bitmap bitmapMine, bitmapFlag;

    public MinesweeperView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paintBg = new Paint();
        paintBg.setColor(Color.GRAY);
        paintBg.setStyle(Paint.Style.FILL);

        paintZero = new Paint();
        paintZero.setColor(Color.rgb(178, 178, 178));
        paintZero.setStyle(Paint.Style.FILL);

        paintLine = new Paint();
        paintLine.setColor(Color.BLACK);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(3);

        paintText1 = new Paint();
        paintText1.setColor(Color.rgb(30, 61, 188));

        paintText2 = new Paint();
        paintText2.setColor(Color.rgb(46, 89, 43));

        paintText3 = new Paint();
        paintText3.setColor(Color.rgb(188, 30, 22));

        paintText4 = new Paint();
        paintText4.setColor(Color.rgb(55, 34, 124));

        bitmapMine = BitmapFactory.decodeResource(getResources(), R.drawable.mine);
        bitmapFlag = BitmapFactory.decodeResource(getResources(), R.drawable.flag);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBg);

        drawClicked(canvas);

        drawBoard(canvas);
    }

    private void drawClicked(Canvas canvas) {

        bitmapFlag = Bitmap.createScaledBitmap(bitmapFlag, getWidth()/boardSize, getHeight()/boardSize, false);
        bitmapMine = Bitmap.createScaledBitmap(bitmapMine, getWidth()/boardSize, getHeight()/boardSize, false);


        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {

                Field f = MinesweeperModel.getInstance().getFieldContent(i,j);
                int centerX = i*getWidth() / boardSize + getWidth() / (boardSize * 2) + (int)((paintText1.descent() + paintText1.ascent()) / 2);
                int centerY = j*getHeight() / boardSize + getHeight() / (boardSize * 2) - (int)((paintText1.descent() + paintText1.ascent()) / 2);

                int x = i*getWidth() / boardSize;
                int y = j*getHeight() / boardSize;



                paintText1.setTextSize((int) (getWidth()/(boardSize*1.5)));
                paintText2.setTextSize((int) (getWidth()/(boardSize*1.5)));
                paintText3.setTextSize((int) (getWidth()/(boardSize*1.5)));
                paintText4.setTextSize((int) (getWidth()/(boardSize*1.5)));

                if(f.isClicked()) {
                    if(f.isFlag()) {
                        canvas.drawBitmap(bitmapFlag, x, y, null);
                    }
                    else if(f.isMine()) {
                        canvas.drawBitmap(bitmapMine, x, y, null);
                    }
                    else {
                        int surr = MinesweeperModel.getInstance().getSurrounding(i, j);
                        canvas.drawRect(x, y, x+getWidth()/boardSize, y+getHeight()/boardSize, paintZero);
                        if(surr == 0) {
                        } else if(surr == 1) {
                            canvas.drawText("" + surr, centerX +10 , centerY+10, paintText1);
                        } else if(surr == 2) {
                            canvas.drawText("" + surr, centerX +10 , centerY+10, paintText2);
                        } else if(surr == 3) {
                            canvas.drawText("" + surr, centerX +10 , centerY+10, paintText3);
                        } else {
                            canvas.drawText("" + surr, centerX +10 , centerY+10, paintText4);
                        }
                    }
                }
            }
        }
    }

    private void drawBoard(Canvas canvas) {
        for(int i = 1; i < boardSize; i++) {
            canvas.drawLine((i*getWidth())/boardSize, 0, (i*getWidth())/boardSize, getHeight(), paintLine);
            canvas.drawLine(0, (i*getHeight())/boardSize, getWidth(), (i*getHeight())/boardSize, paintLine);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int tX = ((int) event.getX()) / (getWidth() / boardSize);
            int tY = ((int) event.getY()) / (getHeight() / boardSize);

            Field currentField = MinesweeperModel.getInstance().getFieldContent(tX, tY);

            if(!MinesweeperModel.getInstance().isGameOver() && !MinesweeperModel.getInstance().isGameWon()) {
                if (MinesweeperModel.getInstance().getFlagMode()) {
                    if (!currentField.isMine()) {
                        gameOver();
                    }
                    currentField.setFlag(true);
                    currentField.setClicked(true);
                } else {
                    MinesweeperModel.getInstance().reveal(tX, tY);
                    if (currentField.isMine() && !currentField.isFlag()) {
                        gameOver();
                    }
                }
            }

            if(MinesweeperModel.getInstance().isGameWon()) {
                gameWon();
            }
            invalidate();
        }

        return super.onTouchEvent(event);
    }

    private void gameOver() {
        Snackbar.make(findViewById(R.id.minesweeperView), GAME_OVER, Snackbar.LENGTH_LONG).show();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (MinesweeperModel.getInstance().getFieldContent(i, j).isMine()) {
                    MinesweeperModel.getInstance().getFieldContent(i, j).setClicked(true);
                }
            }
        }
        MinesweeperModel.getInstance().setGameOver(true);
    }

    private void gameWon() {
        Snackbar.make(findViewById(R.id.minesweeperView), R.string.game_won, Snackbar.LENGTH_LONG).show();
    }

    public void newGame() {
        MinesweeperModel.getInstance().resetGame();
        invalidate();
    }


}
