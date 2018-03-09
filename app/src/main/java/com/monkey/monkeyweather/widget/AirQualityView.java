package com.monkey.monkeyweather.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.monkey.monkeyweather.R;
import com.monkey.monkeyweather.util.DensityUtil;

/**
 * 空气质量指数
 * 0-50 优
 * 51-100 良
 * 101-150 轻度污染
 * 151-200 中度污染
 * 201-300 重度污染
 * 300-500 严重污染
 */
public class AirQualityView extends View {

    private Paint mBottomPaint;
    private Paint mTopPaint;
    private Paint mTextPaint;
    private RectF mArcRectF;
    private Rect mTextRect;
    private int mStrokeWidth;
    private int mAirQuality;

    public AirQualityView(Context context) {
        this(context, null);
    }

    public AirQualityView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AirQualityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBottomPaint.setStyle(Paint.Style.STROKE);
        mStrokeWidth = getResources().getDimensionPixelSize(R.dimen.bold_line_height);
        mBottomPaint.setStrokeWidth(mStrokeWidth);
        mBottomPaint.setColor(getResources().getColor(R.color.base_gray));

        mTopPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTopPaint.setStyle(Paint.Style.STROKE);
        mTopPaint.setStrokeWidth(mStrokeWidth);
        mTopPaint.setTextSize(DensityUtil.Companion.sp2px(getContext(), 30));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.too_small_text_size));
        mTextPaint.setColor(getResources().getColor(R.color.light_text_color));

        mArcRectF = new RectF();
        mTextRect = new Rect();
    }

    public void setAirQuality(int airQuality) {
        ValueAnimator numberAnim = ValueAnimator.ofInt(0, airQuality);
        numberAnim.setDuration(airQuality * 2000 / 500);
        numberAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAirQuality = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        numberAnim.start();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ValueAnimator colorAnim = ValueAnimator.ofArgb(getResources().getColor
                    (R.color.air_quality_good), getResources().getColor(R.color.air_quality_serious));
            colorAnim.setDuration(airQuality * 2000 / 500);
            colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int color = (int) animation.getAnimatedValue();
                    mTopPaint.setColor(color);
                }
            });
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    private int measure(int measureSpec) {
        int result;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = DensityUtil.Companion.dp2px(getContext(), 100);
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mArcRectF.set(mStrokeWidth / 2, mStrokeWidth / 2, w - mStrokeWidth / 2, h - mStrokeWidth / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(mArcRectF, -225, 270, false, mBottomPaint);
        canvas.drawArc(mArcRectF, -225, mAirQuality * 270 / 500, false, mTopPaint);

        String qualityStr = String.valueOf(mAirQuality);
        float qualityStrWidth = mTopPaint.measureText(qualityStr);
        canvas.drawText(qualityStr, 0, qualityStr.length(),
                (getWidth() - qualityStrWidth) / 2, getHeight() / 2, mTopPaint);

        mTextPaint.setTextSize(getResources().getDimension(R.dimen.too_small_text_size));
        String nameStr = getContext().getString(R.string.air_quality_index);
        mTextPaint.getTextBounds(nameStr, 0, nameStr.length(), mTextRect);
        canvas.drawText(nameStr, 0, nameStr.length(), (getWidth() - mTextRect.width()) / 2,
                getHeight() / 2 + mTextRect.height() + mStrokeWidth, mTextPaint);
    }
}
