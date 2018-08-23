package com.badapple.shenkar.thebadapple;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

import com.android.gradient.thebadapple.R;


public class GameView extends View {

    private boolean isStart;
    private boolean isHitPosion;

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

    private Bitmap[] wormImage;
    private Bitmap seedImage;
    private Bitmap posionImage;
    private Bitmap deadWormImage;
    private Bitmap appleHurtImage;
    private Bitmap leftSkin;
    private Bitmap rightSkin;
    private Paint scoreText;

    private SoundPlayer soundPlayer;

    public GameView(Context context){
        super(context);
        setBackgroundResource(R.drawable.game_bground);

        isStart = true;
        gaps = 10;
        score = 0;
        lifes = 3;
        seedSpeed = 5;
        poisonSpeed = 8;
        seedY = poisonY = 0;
        seedCount = poisonCount;

        wormImage = new Bitmap[2];
        wormImage[0] = BitmapFactory.decodeResource(getResources(), R.drawable.worm1);
        wormImage[1] = BitmapFactory.decodeResource(getResources(), R.drawable.worm2);
        seedImage = BitmapFactory.decodeResource(getResources(), R.drawable.seed);
        deadWormImage = BitmapFactory.decodeResource(getResources(), R.drawable.dead_worm);
        posionImage = BitmapFactory.decodeResource(getResources(), R.drawable.posion);
        appleHurtImage = BitmapFactory.decodeResource(getResources(), R.drawable.apple_hurt);
        leftSkin = BitmapFactory.decodeResource(getResources(), R.drawable.left);
        rightSkin = BitmapFactory.decodeResource(getResources(), R.drawable.right);
        scoreText = new Paint();
        scoreText.setTextSize(70);
        scoreText.setTypeface(Typeface.DEFAULT_BOLD);
        scoreText.setAntiAlias(true);
        isHitPosion = false;

        soundPlayer = new SoundPlayer(this.getContext());

    }
    public int getLifes(){ return lifes;}
    public int getScore(){ return score;}

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);


        if (isHitPosion) {
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {}
        }

        isHitPosion = false;

        screenWidth = canvas.getWidth();
        screenHeight = canvas.getHeight();
        wormY = screenHeight - 200;

        int minWormX = gaps;
        int maxWormX = screenWidth - (gaps*2) - wormImage[0].getWidth();
        if(isStart) {
            wormX = screenWidth/2;
        }
        else {
            wormX += wormSpeed;
        }



        if(wormX < minWormX) {wormX = minWormX;}
        if(wormX > maxWormX) {wormX = maxWormX;}

        Bitmap leftBm = Bitmap.createScaledBitmap(leftSkin, (int)(screenWidth*0.15), screenHeight, false);
        Bitmap rightBm = Bitmap.createScaledBitmap(rightSkin, (int)(screenWidth*0.15), screenHeight, false);
        canvas.drawBitmap(leftBm, 0, 0, null);
        canvas.drawBitmap(rightBm, screenWidth - rightBm.getWidth(), 0, null);
        if(isHitSkins(leftBm, rightBm, screenWidth))
        {
            isHitPosion = true;
            lifes--;
            isStart = true;
        }

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
            soundPlayer.PlayHit();
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
            soundPlayer.PlaySpray();
            isStart = true;
        }

       // canvas.drawB(backgrountImage, 0, 0, null);
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
            if(wormSpeed < 0){ canvas.drawBitmap(wormImage[0], wormX, wormY, null);}
            else { canvas.drawBitmap(wormImage[1], wormX, wormY, null);}
            canvas.drawBitmap(posionImage, poisonX, poisonY, null);
        }
        else{
            canvas.drawBitmap(deadWormImage, wormX, (int)(wormY*0.99), null);
            //sleep

        }

        if(!isStart){
        if(wormSpeed >= 0) {wormSpeed += 1;}
        else {wormSpeed -= 1;}}
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int)event.getX();
            if(wormX >= x)
                wormSpeed = -10;
            else
                wormSpeed = +10;

            isStart = false;
        }
        return true;
    }

    private boolean isHitSkins(Bitmap left, Bitmap right, int canvasWidth){

        int leftMin = (int)(left.getWidth()*0.6);
        int rightMax = (int)((canvasWidth - right.getWidth())*1.1);

        if(wormX < leftMin) {return true;}
        if((wormX + wormImage[0].getWidth()) > rightMax ) {return true;}

        return false;
    }

    private boolean iSHitWorm(int x, int y) {
        if ((wormX < x) && (x < (wormX + wormImage[0].getWidth())) && (wormY < y) && (y < (wormY + wormImage[0].getHeight()))) {
            return true;
        }
        return false;
    }

}












