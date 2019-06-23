package com.example.ex4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.MotionEventCompat;

import static java.lang.Math.pow;

/**
 * Joystick class
 */
public class Joystick extends View {
    private float x,y, bRad, hRad, centerX, centerY;
    Canvas c;
    public boolean isPlayerMoving, start;
    private String aileronCmd= "set controls/flight/aileron ";
    private String elavatorCmd = "set controls/flight/elevator ";
    public Joystick(Context context) {
        super(context);
        bRad = 400;
        start = false;
    }

    /**
     * in_circle function - draw the circle
     * @param x x val
     * @param y y val
     * @param radius
     * @return bollean val
     */
    private boolean in_circle(float x, float y, float radius){
        if(pow((x - centerX),2)+ pow((y - centerY),2) < pow(radius,2)){
            return true;
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
    @SuppressLint("NewApi")
    @Override
    /**
     * onDraw function- draw the joystick on the canvas
     */
    protected void onDraw(Canvas canvas) {
        c = canvas;
        if(!isPlayerMoving) {
            this.x = this.getWidth() / 2;
            this.y = this.getHeight() / 2;
            bRad  = Math.min(getWidth(), getHeight()) / 2;
            hRad = Math.min(getWidth(), getHeight()) / 12;
            this.centerX = this.x;
            this.centerY = this.y;
        }
        @SuppressLint("DrawAllocation") Paint paint = new Paint();
        canvas.drawRGB(171, 199, 243);

        paint.setColor(Color.rgb(217,221,218));
        canvas.drawCircle(centerX,centerY,bRad, paint);
        paint.setColor(Color.rgb(97,61,85));
        canvas.drawCircle(x,y,hRad, paint);

    }

    /**
     * onTouchEvent function- uses when user touch on screen and set the values and the
     * location of the joystick
     * @param event MotionEvent type event
     * @return boolean val
     */
    public boolean onTouchEvent(MotionEvent event){

        int action= MotionEventCompat.getActionMasked(event);
        switch(action){
            case MotionEvent.ACTION_DOWN:{
                int pointerIndex=MotionEventCompat.getActionIndex(event);
                float x=event.getX();
                float y=event.getY();
                if((x<this.x+bRad && x>this.x-bRad)&&(y<this.y+bRad && y>this.y-bRad)){
                    isPlayerMoving=true;
                }
            }
            case MotionEvent.ACTION_MOVE:{
                if(!isPlayerMoving) {
                    return true;
                }
                else {
                    float x = event.getX();
                    float y = event.getY();
                    if(in_circle(x,y,bRad-hRad)) {
                        this.x = event.getX();
                        this.y = event.getY();
                        float aileronValue = (x - centerX) / bRad;
                        float elevatorValue = (y - centerY) / bRad;
                        TcpClient.getInstance().Send(aileronCmd + aileronValue + "\r\n");
                        TcpClient.getInstance().Send(elavatorCmd+ elevatorValue + "\r\n");
                    }
                    else{
                        //calc the distance
                        float distance = (float)Math.sqrt((Math.pow(x-centerX,2)) +
                                Math.pow(y-centerY,2));
                        float proportion = (bRad - hRad)/ distance;
                        float normalizedX = centerX + (x - centerX) * proportion;
                        float  normalizedY  = centerY + (y - centerY) * proportion;
                        float aileronValue = (normalizedX - centerX) / bRad;
                        float elevatorValue = (normalizedY - centerY) / bRad;
                        this.x = normalizedX;
                        this.y = normalizedY;
                        TcpClient.getInstance().Send(aileronCmd + aileronValue + "\r\n");
                        TcpClient.getInstance().Send(elavatorCmd + elevatorValue + "\r\n");

                    }
                    System.out.println("x: "+this.x+", y: "+this.y+"...Moving");
                    invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_UP :
            case MotionEvent.ACTION_CANCEL:{
                isPlayerMoving = false;
                TcpClient.getInstance().Send(elavatorCmd + 0 + "\r\n");
                TcpClient.getInstance().Send(aileronCmd + 0 + "\r\n");
                invalidate();
                break;
            }

        }
        return true;
    }
}
