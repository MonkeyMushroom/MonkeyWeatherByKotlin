package com.monkey.monkeyweather.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.util.DensityUtil

/**
 * 空气质量指数
 * 0-50 优
 * 51-100 良
 * 101-150 轻度污染
 * 151-200 中度污染
 * 201-300 重度污染
 * 300- 严重污染
 */
class AirQualityView constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private var floorPaint: Paint? = null
    private var mRect: RectF

    init {
        floorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        floorPaint!!.style = Paint.Style.STROKE
        floorPaint!!.strokeWidth = resources.getDimensionPixelSize(R.dimen.bold_line_height).toFloat()
        floorPaint!!.color = resources.getColor(R.color.light_text_color)

        mRect = RectF()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec))
    }

    private fun measure(measureSpec: Int): Int {
        var result: Int
        val mode = View.MeasureSpec.getMode(measureSpec)
        val size = View.MeasureSpec.getSize(measureSpec)
        if (mode == View.MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = DensityUtil.dp2px(context, 150f)
            if (mode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, size)
            }
        }
        return result
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRect.set(paddingLeft.toFloat(), paddingTop.toFloat(),
                w - paddingRight.toFloat(), h - paddingBottom.toFloat())
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawArc(mRect, -225f, 270f, false, floorPaint)
    }
}