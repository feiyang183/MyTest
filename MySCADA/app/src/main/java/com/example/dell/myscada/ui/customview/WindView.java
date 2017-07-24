package com.example.dell.myscada.ui.customview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

//import android.graphics.Path;
//import android.graphics.PathMeasure;

/**
 * Created by Dell on 2017/4/5.
 */

public class WindView extends View {
    private Paint mPaint;
    private Paint wPaint;
    private Paint uPaint;
    private Paint paint1;
    private Paint paint2;

    private Paint mSymbolPaint;

    private int mViewWidth;
    private int mViewHeight;

    private int mViewCenterX;
    private int mViewCenterY;

    private int mRadius;

    public WindView(Context context){
        this(context,null);
    }

    public WindView(Context context, AttributeSet attrs){
        super(context,attrs);
        initAll();
    }

    public void initAll(){

        initPaint();

        initListener();

        initHandler();

        initAnimator();

        mCurrentState = 0;
        mWorkingAnimator.start();

    }

    private double mCurrentState = 0;

    private ValueAnimator mWorkingAnimator;
    private float mAnimatorValue = 0;

    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;

    private Handler mAnimatorHandler;

    private void initPaint(){
        uPaint = new Paint();
        uPaint.setStyle(Paint.Style.STROKE);
        uPaint.setColor(Color.LTGRAY);
        uPaint.setStrokeWidth(15);
        uPaint.setStrokeCap(Paint.Cap.ROUND);
        uPaint.setAntiAlias(true);
        paint1 = new Paint(uPaint);
        paint1.setColor(Color.BLACK);
        paint1.setStyle(Paint.Style.FILL);
        paint2 = new Paint(paint1);
        paint2.setColor(Color.WHITE);
        Log.e("TAG","初始画笔");
    }

    private void initListener(){
        mUpdateListener = new ValueAnimator.AnimatorUpdateListener(){
            @Override
            public void onAnimationUpdate(ValueAnimator animation){
                mAnimatorValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        };

        mAnimatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorHandler.sendEmptyMessage(0);
//                mWorkingAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };
        Log.e("TAG","初始监听器");
    }

    private void initHandler(){
        Log.e("TAG","初始线程");
        mAnimatorHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                switch ((int)mCurrentState){
                    case 6:
                        mWorkingAnimator.start();
                        break;
                }
            }
        };
    }

    private void initAnimator(){
        mWorkingAnimator = ValueAnimator.ofFloat(0,360.0f).setDuration(2500);

        mWorkingAnimator.addUpdateListener(mUpdateListener);

        mWorkingAnimator.addListener(mAnimatorListener);

        Log.e("TAG","初始动画");
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        drawWind(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
        mViewCenterX = mViewWidth / 2;
        mViewCenterY = mViewHeight / 2;
        mRadius = mViewCenterX <= mViewCenterY ? mViewCenterX : mViewCenterY;
        Log.e("获取半径", "mRadius = " + mRadius + "mViewCenterX = " + mViewCenterX + "mViewCenterY = " + mViewCenterY);
//        mSymbolPaint.setStrokeWidth(mRadius * 2 / 30);
        setMeasuredDimension(mViewWidth,mViewHeight);
    }

    private int measureHeight(int measureSpec){
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 100;
        if(specMode == MeasureSpec.AT_MOST){
            result = specSize;
        }else if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }
        return result;
    }

    private int measureWidth(int measureSpec){
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 100;
        if(specMode == MeasureSpec.AT_MOST){
            result = specSize;
        }else if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w,int h,int oldw,int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
    }

    private void drawWind(Canvas canvas){

        canvas.translate(mViewCenterX, mViewCenterY);
        canvas.scale(0.3f, 0.3f);
        canvas.drawColor(Color.WHITE);
        canvas.save();

        if (mCurrentState == 6) {
            setuPaint(mCurrentState);
            canvas.rotate(mAnimatorValue);
//            RectF rect3 = new RectF(-40,-180,40,40);
            RectF rect3 = new RectF(-mRadius * 3 / 4, -mRadius * 10 / 3, mRadius * 3 / 4, mRadius * 3 / 4);
            for(int i = 0;i < 3;i++){
                canvas.drawOval(rect3,uPaint);
                canvas.rotate(120);
            }
            canvas.restore();
            canvas.drawLine(0,0,-mRadius,mRadius * 5,paint1);
            canvas.drawLine(0,0,mRadius,mRadius * 5,paint1);
            canvas.drawCircle(0,0,mRadius,paint1);
            canvas.scale(0.6f,0.6f);
            canvas.drawCircle(0,0,mRadius,paint2);
        }else {
            setuPaint(mCurrentState);
//            RectF rect1 = new RectF(-40,-180,40,40);
            RectF rect1 = new RectF(-mRadius * 3 / 4, -mRadius * 10 / 3, mRadius * 3 / 4, mRadius * 3 / 4);
            for(int i = 0;i < 3;i++){
                canvas.drawOval(rect1,uPaint);
                canvas.rotate(120);
            }
            canvas.restore();
            canvas.drawLine(0,0,-mRadius,mRadius * 5,paint1);
            canvas.drawLine(0,0,mRadius,mRadius * 5,paint1);
            canvas.drawCircle(0,0,mRadius,paint1);
            canvas.scale(0.6f,0.6f);
            canvas.drawCircle(0,0,mRadius,paint2);
        }
    }

    public void setuPaint(double state) {
        switch ((int) state) {
            case 1:
                uPaint.setColor(Color.parseColor("#FF0000"));
                break;
            case 2:
                uPaint.setColor(Color.parseColor("#87CEEB"));
                break;
            case 3:
                uPaint.setColor(Color.parseColor("#1E90FF"));
                break;
            case 4:
            case 5:
            case 6:
                uPaint.setColor(Color.parseColor("#32CD32"));
                break;
            case 10:
                uPaint.setColor(Color.parseColor("#FFFF00"));
                break;
            case 7:
            case 8:
            case 9:
            default:
                uPaint.setColor(Color.parseColor("#808080"));
                break;
        }
    }

    public void setmCurrentState(double state){
        mCurrentState = state;
    }
}
