package com.monkey.monkeyweather.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.monkey.monkeyweather.R;
import com.monkey.monkeyweather.util.ColorArgbEvaluator;
import com.monkey.monkeyweather.util.DensityUtil;

/**
 * 空气质量指数
 * 0-50 优 #71B81F
 * 51-100 良 #DBB300
 * 101-150 轻度污染 #DC7F13
 * 151-200 中度污染 #E3612A
 * 201-300 重度污染 #8E6EDC
 * 300-400 严重污染 #3a2470
 */
public class AirQualityView extends View {
    //污染级别颜色
    private static int[] mColorArr = {0x0071B81F, 0x00DBB300, 0x00DC7F13, 0x00E3612A, 0x008E6EDC, 0x003a2470};

    private Paint mBottomRingPaint;
    private Paint mTopRingPaint;
    private Paint mNamePaint;
    private Paint mNumberPaint;
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
        mBottomRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBottomRingPaint.setStyle(Paint.Style.STROKE);
        mStrokeWidth = getResources().getDimensionPixelSize(R.dimen.bold_line_height);
        mBottomRingPaint.setStrokeWidth(mStrokeWidth);
        mBottomRingPaint.setColor(getResources().getColor(R.color.base_gray));

        mTopRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTopRingPaint.setStyle(Paint.Style.STROKE);
        mTopRingPaint.setStrokeWidth(mStrokeWidth);

        mNamePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNamePaint.setStyle(Paint.Style.FILL);
        mNamePaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.too_small_text_size));
        mNamePaint.setColor(getResources().getColor(R.color.light_text_color));

        mNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mNumberPaint.setStyle(Paint.Style.FILL);
        mNumberPaint.setTextSize(DensityUtil.Companion.sp2px(context, 26));

        mArcRectF = new RectF();
        mTextRect = new Rect();
    }

    public void setAirQuality(int airQuality) {
        //指数递增动画
        ValueAnimator numberAnim = ValueAnimator.ofInt(0, airQuality);
        numberAnim.setDuration(airQuality * 2000 / 400);
        numberAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAirQuality = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        numberAnim.start();
        //颜色渐变动画
        ValueAnimator colorAnim;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            colorAnim = ValueAnimator.ofArgb(getAirQualityColorArr(airQuality));
        } else {
            colorAnim = new ValueAnimator();
            colorAnim.setIntValues(getAirQualityColorArr(airQuality));
            colorAnim.setEvaluator(new ColorArgbEvaluator());
        }
        colorAnim.setDuration(airQuality * 2000 / 400);
        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //先转16进制，再parse
                int color = Color.parseColor("#" + Integer.toHexString((int) animation.getAnimatedValue()));
                mTopRingPaint.setColor(color);
                mNumberPaint.setColor(color);
            }
        });
        colorAnim.start();
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
        canvas.drawArc(mArcRectF, -225, 270, false, mBottomRingPaint);
        canvas.drawArc(mArcRectF, -225, mAirQuality * 270 / 400, false, mTopRingPaint);

        String qualityStr = String.valueOf(mAirQuality);
        float qualityStrWidth = mNumberPaint.measureText(qualityStr);
        canvas.drawText(qualityStr, 0, qualityStr.length(),
                (getWidth() - qualityStrWidth) / 2, getHeight() / 2, mNumberPaint);

        String nameStr = getContext().getString(R.string.air_quality_index);
        mNamePaint.getTextBounds(nameStr, 0, nameStr.length(), mTextRect);
        canvas.drawText(nameStr, 0, nameStr.length(), (getWidth() - mTextRect.width()) / 2,
                getHeight() / 2 + mTextRect.height() + getResources().getDimensionPixelSize
                        (R.dimen.too_small_margin), mNamePaint);
    }

    /**
     * 根据空气质量返回数组，该数组表示从优、良、轻度污染等的色值渐变
     */
    public static int[] getAirQualityColorArr(int airQuality) {
        int[] colorArr = new int[0];
        if (airQuality <= 50) {//优
            colorArr = new int[1];
            colorArr[0] = mColorArr[0];
        } else if (airQuality > 50 && airQuality <= 100) {//良
            colorArr = new int[2];
            colorArr[0] = mColorArr[0];
            colorArr[1] = mColorArr[1];
        } else if (airQuality > 100 && airQuality <= 150) {//轻度污染
            colorArr = new int[3];
            colorArr[0] = mColorArr[0];
            colorArr[1] = mColorArr[1];
            colorArr[2] = mColorArr[2];
        } else if (airQuality > 150 && airQuality <= 200) {//中度污染
            colorArr = new int[4];
            colorArr[0] = mColorArr[0];
            colorArr[1] = mColorArr[1];
            colorArr[2] = mColorArr[2];
            colorArr[3] = mColorArr[3];
        } else if (airQuality > 200 && airQuality <= 300) {//重度污染
            colorArr = new int[5];
            colorArr[0] = mColorArr[0];
            colorArr[1] = mColorArr[1];
            colorArr[2] = mColorArr[2];
            colorArr[3] = mColorArr[3];
            colorArr[4] = mColorArr[4];
        } else if (airQuality > 300) {//严重污染
            colorArr = new int[5];
            colorArr[0] = mColorArr[0];
            colorArr[1] = mColorArr[1];
            colorArr[2] = mColorArr[2];
            colorArr[3] = mColorArr[3];
            colorArr[4] = mColorArr[4];
        }
        return colorArr;
    }

    /**
     * 根据空气质量返回颜色资源
     */
    public static int getAirQualityColorResource(Context context, int airQuality) {
        Resources resources = context.getResources();
        int color = resources.getColor(R.color.air_quality_good);
        if (airQuality <= 50) {//优
            color = resources.getColor(R.color.air_quality_good);
        } else if (airQuality > 50 && airQuality <= 100) {//良
            color = resources.getColor(R.color.air_quality_fine);
        } else if (airQuality > 100 && airQuality <= 150) {//轻度污染
            color = resources.getColor(R.color.air_quality_mild);
        } else if (airQuality > 150 && airQuality <= 200) {//中度污染
            color = resources.getColor(R.color.air_quality_medium);
        } else if (airQuality > 200 && airQuality <= 300) {//重度污染
            color = resources.getColor(R.color.air_quality_bad);
        } else if (airQuality > 300) {//严重污染
            color = resources.getColor(R.color.air_quality_serious);
        }
        return color;
    }

    /**
     * 根据空气质量返回级别和建议
     */
    public static String[] getAirQualityText(int airQuality) {
        String[] arr = new String[2];
        String text = "";
        String suggest = "";
        if (airQuality <= 50) {//优
            text = "优";
        } else if (airQuality > 50 && airQuality <= 100) {//良
            text = "良";
        } else if (airQuality > 100 && airQuality <= 150) {//轻度污染
            text = "轻度污染";
        } else if (airQuality > 150 && airQuality <= 200) {//中度污染
            text = "中度污染";
        } else if (airQuality > 200 && airQuality <= 300) {//重度污染
            text = "重度污染";
            suggest = "空气很差，请减少外出，关闭门窗";
        } else if (airQuality > 300) {//严重污染
            text = "严重污染";
        }
        arr[0] = text;
        arr[1] = suggest;
        return arr;
    }
}
