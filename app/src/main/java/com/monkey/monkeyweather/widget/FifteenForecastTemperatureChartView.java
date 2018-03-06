package com.monkey.monkeyweather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.monkey.monkeyweather.R;
import com.monkey.monkeyweather.util.DensityUtil;

/**
 * 15天趋势预报温度折线图
 */
public class FifteenForecastTemperatureChartView extends View {

    private int mFifteenMinTemp;
    private int mFifteenMaxTemp;
    private int mFifteenTempRange;
    private Integer mYesterdayMaxTemp;
    private Integer mYesterdayMinTemp;
    private Integer mTodayMaxTemp;
    private Integer mTodayMinTemp;
    private Integer mTomorrowMaxTemp;
    private Integer mTomorrowMinTemp;

    private Paint mTempPointPaint;
    private Paint mTempRingPaint;
    private Paint mTempTextPaint;
    private RectF mTempRingRectF;
    private float mTempPointMargin;
    private float mTempPointRadius1 = 12;
    private float mTempPointRadius2 = 8;

    public FifteenForecastTemperatureChartView(Context context) {
        this(context, null);
    }

    public FifteenForecastTemperatureChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FifteenForecastTemperatureChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTempPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTempPointPaint.setStyle(Paint.Style.FILL);
        mTempPointPaint.setColor(getResources().getColor(R.color.base_white));
        mTempRingPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTempRingPaint.setStyle(Paint.Style.STROKE);
        mTempRingPaint.setColor(getResources().getColor(R.color.light_text_color));
        mTempRingPaint.setStrokeWidth(getResources().getDimensionPixelSize(R.dimen.base_line_height));
        mTempTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTempTextPaint.setStyle(Paint.Style.STROKE);
        mTempTextPaint.setColor(getResources().getColor(R.color.base_text_color));
        mTempTextPaint.setTextSize(getResources().getDimension(R.dimen.base_text_size));

        mTempRingRectF = new RectF();
    }

    /**
     * 设置未来15天内的最低温度和最高温度范围，用于确定今天温度所在的位置，不能超出这个范围
     *
     * @param minTemp 最低温度
     * @param maxTemp 最高温度
     */
    public void setFifteenTempRange(int minTemp, int maxTemp) {
        mFifteenMinTemp = minTemp;
        mFifteenMaxTemp = maxTemp;
        mFifteenTempRange = maxTemp - minTemp;
    }

    /**
     * @param yesterdayMaxTemp 昨日最高温度
     * @param yesterdayMinTemp 昨日最低温度
     * @param todayMaxTemp     今日最高温度
     * @param todayMinTemp     今日最低温度
     * @param tomorrowMaxTemp  明日最高温度
     * @param tomorrowMinTemp  明日最低温度
     */
    public void setCurrentTemp(Integer yesterdayMaxTemp, Integer yesterdayMinTemp, Integer todayMaxTemp
            , Integer todayMinTemp, Integer tomorrowMaxTemp, Integer tomorrowMinTemp) {
        mYesterdayMaxTemp = yesterdayMaxTemp;
        mYesterdayMinTemp = yesterdayMinTemp;
        mTodayMaxTemp = todayMaxTemp;
        mTodayMinTemp = todayMinTemp;
        mTomorrowMaxTemp = tomorrowMaxTemp;
        mTomorrowMinTemp = tomorrowMinTemp;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int result;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = DensityUtil.Companion.dp2px(getContext(), 72);
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int result;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = DensityUtil.Companion.dp2px(getContext(), 180);
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size);
            }
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTempPointMargin = getResources().getDimensionPixelSize(R.dimen.base_margin);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawConnectLine(canvas);
        drawTempPoint(canvas);
        drawTempText(canvas);
    }

    private void drawTempPoint(Canvas canvas) {
        if (mTodayMaxTemp != null) {//今日最高温度点，白圆上盖灰环
            canvas.drawCircle(getWidth() / 2, getTempY(mTodayMaxTemp), mTempPointRadius1, mTempPointPaint);
            mTempRingRectF.set(getWidth() / 2 - mTempPointRadius2, getTempY(mTodayMaxTemp) - mTempPointRadius2, getWidth() / 2 + mTempPointRadius2, getTempY(mTodayMaxTemp) + mTempPointRadius2);
            canvas.drawOval(mTempRingRectF, mTempRingPaint);
        }
        if (mTodayMinTemp != null) {//今日最低温度点
            canvas.drawCircle(getWidth() / 2, getTempY(mTodayMinTemp), mTempPointRadius1, mTempPointPaint);
            mTempRingRectF.set(getWidth() / 2 - mTempPointRadius2, getTempY(mTodayMinTemp) - mTempPointRadius2, getWidth() / 2 + mTempPointRadius2, getTempY(mTodayMinTemp) + mTempPointRadius2);
            canvas.drawOval(mTempRingRectF, mTempRingPaint);
        }
    }

    private void drawConnectLine(Canvas canvas) {
        if (mYesterdayMaxTemp != null) {
            canvas.drawLine(getWidth() / 2, getTempY(mTodayMaxTemp), 0,
                    (getTempY(mTodayMaxTemp) + getTempY(mYesterdayMaxTemp)) / 2, mTempRingPaint);
        }
        if (mYesterdayMinTemp != null) {
            canvas.drawLine(getWidth() / 2, getTempY(mTodayMinTemp), 0,
                    (getTempY(mTodayMinTemp) + getTempY(mYesterdayMinTemp)) / 2, mTempRingPaint);
        }
        if (mTomorrowMaxTemp != null) {
            canvas.drawLine(getWidth() / 2, getTempY(mTodayMaxTemp), getWidth(),
                    (getTempY(mTodayMaxTemp) + getTempY(mTomorrowMaxTemp)) / 2, mTempRingPaint);
        }
        if (mTomorrowMinTemp != null) {
            canvas.drawLine(getWidth() / 2, getTempY(mTodayMinTemp), getWidth(),
                    (getTempY(mTodayMinTemp) + getTempY(mTomorrowMinTemp)) / 2, mTempRingPaint);
        }
    }

    /**
     * 根据温度算出该温度点所在y轴坐标
     *
     * @param temp 温度
     * @return y
     */
    private float getTempY(int temp) {
        return getHeight() - getPaddingBottom() - mTempPointMargin - ((getHeight() - getPaddingTop()
                - getPaddingBottom() - 2 * mTempPointMargin) * (temp - mFifteenMinTemp) / mFifteenTempRange);
    }

    private void drawTempText(Canvas canvas) {
        String maxTempStr = mTodayMaxTemp + "℃";
        float maxTempTextWidth = mTempTextPaint.measureText(maxTempStr);
        canvas.drawText(maxTempStr, 0, maxTempStr.length(),
                getWidth() / 2 - maxTempTextWidth / 2, getTempY(mTodayMaxTemp) -
                        getResources().getDimensionPixelSize(R.dimen.small_margin), mTempTextPaint);

        String minTempStr = mTodayMinTemp + "℃";
        float minTempTextWidth = mTempTextPaint.measureText(minTempStr);
        canvas.drawText(minTempStr, 0, minTempStr.length(),
                getWidth() / 2 - minTempTextWidth / 2, getTempY(mTodayMinTemp) +
                        getResources().getDimensionPixelSize(R.dimen.base_margin), mTempTextPaint);
    }
}
