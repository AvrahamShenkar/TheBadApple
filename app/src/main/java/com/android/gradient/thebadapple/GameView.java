package com.android.gradient.thebadapple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;


public class GameView extends View {

    private int lifes;
    private int score;
    private int gaps;
    private int screenWidth;
    private int screenHeight;

    private int wormX;
    private int wormY;
    private int wormSpeed;

    private int seedX;
    private int seedY;
    private int seedSpeed;
    private int seedCount;

    private int poisonX;
    private int poisonY;
    private int poisonSpeed;
    private int poisonCount;

    private Bitmap wormImage;
    private Bitmap seedImage;
    private Bitmap posionImage;
    private Bitmap deadWormImage;
    private Bitmap backgrountImage;
    private Bitmap appleHurtImage;
    private Bitmap badAppleHurtImage;
    private Paint scoreText;

    public GameView(Context context){
        super(context);

        gaps = 10;
        score = 0;
        lifes = 3;
        seedSpeed = 5;
        poisonSpeed = 8;
        seedY = poisonY = 0;
        seedCount = poisonCount;

        wormImage = BitmapFactory.decodeResource(getResources(), R.drawable.worm2);
        seedImage = BitmapFactory.decodeResource(getResources(), R.drawable.seed);
        deadWormImage = BitmapFactory.decodeResource(getResources(), R.drawable.dead_worm);
        posionImage = BitmapFactory.decodeResource(getResources(), R.drawable.posion);
        backgrountImage = BitmapFactory.decodeResource(getResources(), R.drawable.game_bground);
        appleHurtImage = BitmapFactory.decodeResource(getResources(), R.drawable.apple_hurt);

        scoreText = new Paint();
        scoreText.setTextSize(70);
        scoreText.setTypeface(Typeface.DEFAULT_BOLD);
        scoreText.setAntiAlias(true);


    }
    public int getLifes(){ return lifes;}
    public int getScore(){ return score;}

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        boolean isHitPosion = false;

        screenWidth = canvas.getWidth();
        screenHeight = canvas.getHeight();
        wormY = screenHeight - 200;

        int minWormX = gaps;
        int maxWormX = screenWidth - (gaps*2) - wormImage.getWidth();
        wormX += wormSpeed;

        if(wormX < minWormX) {wormX = minWormX;}
        if(wormX > maxWormX) {wormX = maxWormX;}



        if (seedY <= 0 || seedY > (screenHeight+10)){
            //start a new seed
            seedY = gaps;
            seedX = (int) Math.floor(Math.random() * (maxWormX - minWormX) + minWormX);
            seedCount++;
            if(seedCount >= 10) {
                seedCount = 0;
                seedSpeed++;
            }
        }
        else{ seedY += seedSpeed;}
        if(iSHitWorm(seedX, seedY)){
            score += 10;
            seedY = gaps;
        }

        if (poisonY <= 0 || poisonY > (screenHeight+10)){
            //start a new posion
            poisonY = gaps;
            poisonX = (int) Math.floor(Math.random() * (maxWormX - minWormX) + minWormX);
            if(poisonCount >= 10) {
                poisonCount = 0;
                poisonSpeed++;
            }
        }
        else{ poisonY += poisonSpeed;}

        if(iSHitWorm(poisonX, poisonY)){
            isHitPosion = true;
            poisonY = gaps;
            lifes--;
        }

        canvas.drawBitmap(backgrountImage, 0, 0, null);
        canvas.drawBitmap(seedImage, seedX, seedY, null);
        canvas.drawBitmap(posionImage, poisonX, poisonY, null);

        canvas.drawText("Score: " + score, 20, 60 , scoreText);

        int startX = 680;
        for (int i = 0; i < lifes; i++){
            canvas.drawBitmap(appleHurtImage, startX, 10, null);
            startX += 100;
        }

        if(!isHitPosion){
           // canvas.drawBitmap(wormImage, 0, 0, null);
            canvas.drawBitmap(wormImage, wormX, wormY, null);
            canvas.drawBitmap(posionImage, poisonX, poisonY, null);
        }
        else{
            canvas.drawBitmap(deadWormImage, wormX, wormY, null);
            //sleep
            try {
                Thread.sleep(500);
            }
            catch(Exception ex){}
        }

        if(wormSpeed >= 0) {wormSpeed += 2;}
        else {wormSpeed -= 2;}
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int)event.getX();
            if(wormX >= x)
                wormSpeed = -10;
            else
                wormSpeed = +10;
        }
        return true;
    }

    private boolean iSHitWorm(int x, int y){
        if( (wormX < x) && (x < (wormX + wormImage.getWidth())) && (wormY < y ) && (y < (wormY + wormImage.getHeight())))  {
            return true;
        }
        return false;
    }
}












