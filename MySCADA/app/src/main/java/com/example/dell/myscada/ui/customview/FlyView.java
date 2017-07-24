package com.example.dell.myscada.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Dell on 2017/4/10.
 */

public class FlyView extends View {
    private Paint mCirclePaint;
    private Paint mSymbolPaint;
    private Paint paintDegree;
    private Path mPath;

    private float mDegree = 0.0f;

    private PointF mPoint0;
    private PointF mPoint1;
    private PointF mPoint2;
    private PointF mPoint3;

    private int mViewWidth;
    private int mViewHeight;

    private int mViewCenterX;
    private int mViewCenterY;

    private int mRadius;

    private String[] mDirection = {" 0º"," 90º","180º","270º"};

    public FlyView(Context context){
        super(context);
        init();
    }

    public FlyView(Context context, AttributeSet attrs){
        super(context, attrs);
        init();

    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.drawCircle(mViewCenterX,mViewCenterY,mRadius,mCirclePaint);
        canvas.save();
        for(int i = 0;i < 12 ;i++){
            if(i == 0 || i == 3 || i == 6 || i == 9){
                paintDegree.setStrokeWidth(mRadius * 3 / 100);
                canvas.drawLine(mViewCenterX,0,mViewCenterX,mRadius * 3 / 30,paintDegree);
                paintDegree.setTextSize(mRadius * 5 / 30);
                paintDegree.setStrokeWidth(mRadius * 2 / 100);
                canvas.drawText(mDirection[i/3],mViewCenterX - mRadius * 2 / 13,mRadius * 8 / 30,paintDegree);
            }else {
                paintDegree.setStrokeWidth(mRadius * 1 / 60);
                paintDegree.setTextSize(30);
                canvas.drawLine(mViewCenterX,0,mViewCenterX,mRadius * 2 / 30,paintDegree);
            }
            canvas.rotate(30,mViewCenterX,mViewCenterY);
        }

        initPath();
        canvas.restore();
        canvas.rotate(mDegree,mViewCenterX,mViewCenterY);
        canvas.drawPath(mPath,mSymbolPaint);

    }

    private void init(){
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setColor(Color.parseColor("#0097DF"));

        paintDegree = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintDegree.setColor(Color.BLACK);

        mSymbolPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSymbolPaint.setColor(getResources().getColor(android.R.color.white));
        mSymbolPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mSymbolPaint.setStrokeCap(Paint.Cap.ROUND);
        mSymbolPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    private void initPath(){
        mPath = new Path();
        mPath.moveTo(mPoint0.x,mPoint0.y);
        mPath.lineTo(mPoint1.x,mPoint1.y);
        mPath.lineTo(mPoint2.x,mPoint2.y);
        mPath.lineTo(mPoint3.x,mPoint3.y);
        mPath.lineTo(mPoint0.x,mPoint0.y);
        mPath.addCircle(mViewCenterX,mViewCenterY,mRadius / 6, Path.Direction.CW);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
        mViewCenterX = mViewWidth / 2;
        mViewCenterY = mViewHeight / 2;
        mRadius = mViewCenterX <= mViewCenterY ? mViewCenterX : mViewCenterY;
        mSymbolPaint.setStrokeWidth(mRadius * 2 / 30);
        setMeasuredDimension(mViewWidth,mViewHeight);
    }

    private int measureHeight(int measureSpec){
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 200;
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
        int result = 500;
        if(specMode == MeasureSpec.AT_MOST){
            result = specSize;
        }else if(specMode == MeasureSpec.EXACTLY){
            result = specSize;
        }
        return result;
    }

    public void updateDirection(float degree){
        mDegree = degree;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w,int h,int oldw,int oldh){
        if(mViewWidth != 0){
            mPoint0 = new PointF(mViewCenterX ,mViewCenterY / 3);
            mPoint1 = new PointF(mViewCenterX + mRadius / 8,mViewCenterY );
            mPoint2 = new PointF(mViewCenterX ,mViewCenterY / 3 * 5);
            mPoint3 = new PointF(mViewCenterX - mRadius / 8,mViewCenterY );
        }
        super.onSizeChanged(w,h,oldw,oldh);
    }
}
