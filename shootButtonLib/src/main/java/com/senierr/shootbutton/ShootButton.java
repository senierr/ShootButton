package com.senierr.shootbutton;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * 自定义带倒计时拍摄按钮
 *
 * @author zhouchunjie
 * @date 2017/5/15
 */

public class ShootButton extends View {

    public static final int MODE_CENTER_CIRCLE= 0;
    public static final int MODE_CENTER_RECT = 1;

    private static final int WIDTH_DEFAULT = 100;   // 默认宽
    private static final int HEIGHT_DEFAULT = 100;   // 默认高

    private Context mContext;
    private Paint mPaint;               // 画笔
    private ColorMatrixColorFilter colorFilter; // 按下颜色矩阵
    private RectF mCircleOval;          // 圆环区域
    private RectF mCenterOval;          // 中心区域
    private float circleMinRadius;      // 圆环内边半径

    private float mCircleWidth;         // 圆环宽度
    private int mCircleColor;           // 圆环颜色
    private int mProgressBgColor;       // 进度条背景颜色
    private int mProgressColor;         // 进度条颜色
    private int mCenterColor;           // 中心图标颜色
    private float mCenterPadding;       // 中心图标与圆环间距
    private float mCenterRectRadius;    // 中心圆角矩形的圆角半径

    private int mCenterMode;            // 中心显示模式

    private int mMaxProgress;           // 进度最大值
    private int mProgress;              // 进度

    private boolean isPressed = false;

    public ShootButton(Context context) {
        this(context, null);
    }

    public ShootButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShootButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //获取自定义属性和默认值
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.ShootButton);
        mCircleWidth = mTypedArray.getDimension(R.styleable.ShootButton_circleWidth, 10);
        mCircleColor = mTypedArray.getColor(R.styleable.ShootButton_circleColor, Color.WHITE);
        mProgressBgColor = mTypedArray.getColor(R.styleable.ShootButton_progressBgColor, Color.GRAY);
        mProgressColor = mTypedArray.getColor(R.styleable.ShootButton_progressColor, Color.RED);
        mCenterColor = mTypedArray.getColor(R.styleable.ShootButton_centerColor, Color.RED);
        mCenterPadding = mTypedArray.getDimension(R.styleable.ShootButton_centerPadding, 10);
        mCenterRectRadius = mTypedArray.getDimension(R.styleable.ShootButton_centerRectRadius, 10);
        mMaxProgress = mTypedArray.getInt(R.styleable.ShootButton_maxProgress, 100);
        mProgress = mTypedArray.getInt(R.styleable.ShootButton_progress, 100);
        mCenterMode = mTypedArray.getInt(R.styleable.ShootButton_centerMode, MODE_CENTER_CIRCLE);
        mTypedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        colorFilter = new ColorMatrixColorFilter(new float[]{
                0.6F, 0, 0, 0, 0,
                0, 0.6F, 0, 0, 0,
                0, 0, 0.6F, 0, 0,
                0, 0, 0, 1, 0
        });
        mCircleOval = new RectF();
        mCenterOval = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        if (widthMode == MeasureSpec.EXACTLY) {     // match/具体数值
            width = widthSize;
        } else {    // wrap/un
            int widthDest = WIDTH_DEFAULT + getPaddingLeft() + getPaddingRight();
            width = Math.min(widthDest, widthSize);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            int heightDest = HEIGHT_DEFAULT + getPaddingTop() + getPaddingBottom();
            height = Math.min(heightDest, heightSize);
        }

        // 确定圆环画布区域
        int minLength = Math.min(width, height);
        mCircleOval.set(width / 2 - minLength / 2 + mCircleWidth / 2 + getPaddingLeft(),
                height / 2 - minLength / 2 + mCircleWidth / 2 + getPaddingTop(),
                width / 2 + minLength / 2 - mCircleWidth / 2 - getPaddingRight(),
                height / 2 + minLength / 2 - mCircleWidth / 2 - getPaddingBottom());
        // 圆环内边半径
        circleMinRadius = (mCircleOval.right - mCircleOval.left - mCircleWidth) / 2;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isPressed) {
            mPaint.setColorFilter(colorFilter);
        } else {
            mPaint.setColorFilter(null);
        }

        // 是否显示进度
        if (mProgress > 0) {
            // 进度
            mPaint.setColor(mProgressColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mCircleWidth);
            canvas.drawArc(mCircleOval, 0, 360, false, mPaint);

            // 进度背景
            mPaint.setColor(mProgressBgColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mCircleWidth);
            float angle = (int) (360 * (double) mProgress / mMaxProgress);
            canvas.drawArc(mCircleOval, -90, 360 - angle, false, mPaint);
        } else {
            // 圆环
            mPaint.setColor(mCircleColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mCircleWidth);
            canvas.drawArc(mCircleOval, 0, 360, false, mPaint);
        }

        // 中心图标
        if (mCenterMode == MODE_CENTER_CIRCLE) {
            // 中心图标-圆
            mPaint.setColor(mCenterColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2,
                    circleMinRadius - mCenterPadding, mPaint);
        } else {
            // 中心图标-方块
            mPaint.setColor(mCenterColor);
            mPaint.setStyle(Paint.Style.FILL);
            int width = (int) Math.sqrt(Math.pow(circleMinRadius - mCenterPadding, 2) / 2);
            mCenterOval.set(getWidth() / 2 - width,
                    getHeight() / 2 - width,
                    getWidth() / 2 + width,
                    getHeight() / 2 + width);
            canvas.drawRoundRect(mCenterOval, mCenterRectRadius, mCenterRectRadius, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPressed = true;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                isPressed = false;
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    public float getCircleWidth() {
        return mCircleWidth;
    }

    public void setCircleWidth(float circleWidth) {
        this.mCircleWidth = circleWidth;
        invalidate();
    }

    public int getCircleColor() {
        return mCircleColor;
    }

    public void setCircleColor(int circleColor) {
        this.mCircleColor = circleColor;
        invalidate();
    }

    public void setCircleColorRes(int circleColorRes) {
        this.mCircleColor = ContextCompat.getColor(mContext, circleColorRes);
        invalidate();
    }

    public int getProgressBgColor() {
        return mProgressBgColor;
    }

    public void setProgressBgColor(int progressBgColor) {
        this.mProgressBgColor = progressBgColor;
        invalidate();
    }

    public void setProgressBgColorRes(int progressBgColorRes) {
        this.mProgressBgColor = ContextCompat.getColor(mContext, progressBgColorRes);
        invalidate();
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressColor(int progressColor) {
        this.mProgressColor = progressColor;
        invalidate();
    }

    public void setProgressColorRes(int progressColorRes) {
        this.mProgressColor = ContextCompat.getColor(mContext, progressColorRes);
        invalidate();
    }

    public int getCenterColor() {
        return mCenterColor;
    }

    public void setCenterColor(int centerColor) {
        this.mCenterColor = centerColor;
        invalidate();
    }

    public void setCenterColorRes(int centerColorRes) {
        this.mCenterColor = ContextCompat.getColor(mContext, centerColorRes);
        invalidate();
    }

    public float getCenterPadding() {
        return mCenterPadding;
    }

    public void setCenterPadding(float centerPadding) {
        this.mCenterPadding = centerPadding;
        invalidate();
    }

    public float getCenterRectRadius() {
        return mCenterRectRadius;
    }

    public void setCenterRectRadius(float centerRectRadius) {
        this.mCenterRectRadius = centerRectRadius;
        invalidate();
    }

    public int getCenterMode() {
        return mCenterMode;
    }

    public void setCenterMode(int centerMode) {
        this.mCenterMode = centerMode;
        invalidate();
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
        invalidate();
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();
    }
}
