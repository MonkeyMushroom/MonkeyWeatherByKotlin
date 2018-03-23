package com.monkey.monkeyweather.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.util.ColorArgbEvaluator
import com.monkey.monkeyweather.util.DensityUtil

/**
 * 空气质量指数
 * 0-50 优 #71B81F
 * 51-100 良 #DBB300
 * 101-150 轻度污染 #DC7F13
 * 151-200 中度污染 #E3612A
 * 201-300 重度污染 #8E6EDC
 * 300-400 严重污染 #3a2470
 */
class AirQualityView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val mBottomRingPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mTopRingPaint: Paint
    private val mNamePaint: Paint
    private val mNumberPaint: Paint
    private val mArcRectF: RectF
    private val mTextRect: Rect
    private val mStrokeWidth: Int
    private var mAirQuality: Int = 0

    init {
        mBottomRingPaint.style = Paint.Style.STROKE
        mStrokeWidth = resources.getDimensionPixelSize(R.dimen.bold_line_height)
        mBottomRingPaint.strokeWidth = mStrokeWidth.toFloat()
        mBottomRingPaint.color = resources.getColor(R.color.base_gray)

        mTopRingPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTopRingPaint.style = Paint.Style.STROKE
        mTopRingPaint.strokeWidth = mStrokeWidth.toFloat()

        mNamePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mNamePaint.style = Paint.Style.FILL
        mNamePaint.textSize = resources.getDimensionPixelSize(R.dimen.too_small_text_size).toFloat()
        mNamePaint.color = resources.getColor(R.color.light_text_color)

        mNumberPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mNumberPaint.style = Paint.Style.FILL
        mNumberPaint.textSize = DensityUtil.sp2px(context, 26f).toFloat()
        mNumberPaint.color = resources.getColor(R.color.air_quality_good)

        mArcRectF = RectF()
        mTextRect = Rect()
    }

    fun setAirQuality(airQuality: Int) {
        //指数递增动画
        val numberAnim = ValueAnimator.ofInt(0, airQuality)
        numberAnim.duration = (airQuality * 10).toLong()
        numberAnim.addUpdateListener { animation ->
            mAirQuality = animation.animatedValue as Int
            invalidate()
        }
        numberAnim.start()
        //颜色渐变动画
        val colorAnim: ValueAnimator
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            colorAnim = ValueAnimator.ofArgb(*getAirQualityAnimColorValues(airQuality))
        } else {
            colorAnim = ValueAnimator()
            colorAnim.setIntValues(*getAirQualityAnimColorValues(airQuality))
            colorAnim.setEvaluator(ColorArgbEvaluator())
        }
        colorAnim.duration = (airQuality * 10).toLong()
        colorAnim.addUpdateListener { animation ->
            //先转16进制，再parse
            val color = Color.parseColor("#" + Integer.toHexString(animation.animatedValue as Int))
            mTopRingPaint.color = color
            mNumberPaint.color = color
        }
        colorAnim.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec))
    }

    private fun measure(measureSpec: Int): Int {
        var result: Int
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        if (mode == MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = DensityUtil.dp2px(context, 100f)
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size)
            }
        }
        return result
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mArcRectF.set((mStrokeWidth / 2).toFloat(), (mStrokeWidth / 2).toFloat(),
                (w - mStrokeWidth / 2).toFloat(), (h - mStrokeWidth / 2).toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawArc(mArcRectF, -225f, 270f, false, mBottomRingPaint)
        canvas.drawArc(mArcRectF, -225f, (mAirQuality * 270 / 400).toFloat(), false, mTopRingPaint)

        if (mAirQuality != 0) {
            val qualityStr = mAirQuality.toString()
            val qualityStrWidth = mNumberPaint.measureText(qualityStr)
            canvas.drawText(qualityStr, 0, qualityStr.length,
                    (width - qualityStrWidth) / 2, (height / 2).toFloat(), mNumberPaint)
        }

        val nameStr = context.getString(R.string.air_quality_index)
        mNamePaint.getTextBounds(nameStr, 0, nameStr.length, mTextRect)
        canvas.drawText(nameStr, 0, nameStr.length, ((width - mTextRect.width()) / 2).toFloat(),
                (height / 2 + mTextRect.height() + resources.getDimensionPixelSize(R.dimen.too_small_margin)).toFloat(), mNamePaint)
    }

    companion object {
        //污染级别颜色，初始值浅灰色
        private val mColorArr = intArrayOf(0x00efefef, 0x0071B81F, 0x00DBB300
                , 0x00DC7F13, 0x00E3612A, 0x008E6EDC, 0x003a2470)

        /**
         * 根据空气质量返回数组，该数组表示从优、良、轻度污染等的色值渐变
         */
        fun getAirQualityAnimColorValues(airQuality: Int): IntArray {
            var colorArr = IntArray(0)
            when {
                airQuality <= 50 -> {//优
                    colorArr = IntArray(2)
                    colorArr[0] = mColorArr[0]
                    colorArr[1] = mColorArr[1]
                }
                airQuality in 51..100 -> {//良
                    colorArr = IntArray(3)
                    colorArr[0] = mColorArr[0]
                    colorArr[1] = mColorArr[1]
                    colorArr[2] = mColorArr[2]
                }
                airQuality in 101..150 -> {//轻度污染
                    colorArr = IntArray(4)
                    colorArr[0] = mColorArr[0]
                    colorArr[1] = mColorArr[1]
                    colorArr[2] = mColorArr[2]
                    colorArr[3] = mColorArr[3]
                }
                airQuality in 151..200 -> {//中度污染
                    colorArr = IntArray(5)
                    colorArr[0] = mColorArr[0]
                    colorArr[1] = mColorArr[1]
                    colorArr[2] = mColorArr[2]
                    colorArr[3] = mColorArr[3]
                    colorArr[4] = mColorArr[4]
                }
                airQuality in 201..300 -> {//重度污染
                    colorArr = IntArray(6)
                    colorArr[0] = mColorArr[0]
                    colorArr[1] = mColorArr[1]
                    colorArr[2] = mColorArr[2]
                    colorArr[3] = mColorArr[3]
                    colorArr[4] = mColorArr[4]
                    colorArr[5] = mColorArr[5]
                }
                airQuality > 300 -> {//严重污染
                    colorArr = IntArray(7)
                    colorArr[0] = mColorArr[0]
                    colorArr[1] = mColorArr[1]
                    colorArr[2] = mColorArr[2]
                    colorArr[3] = mColorArr[3]
                    colorArr[4] = mColorArr[4]
                    colorArr[5] = mColorArr[5]
                    colorArr[5] = mColorArr[6]
                }
            }
            return colorArr
        }

        /**
         * 根据空气质量返回颜色资源
         */
        fun getAirQualityColorResource(context: Context, airQuality: Int): Int {
            val resources = context.resources
            var color = resources.getColor(R.color.air_quality_good)
            when {
                airQuality <= 50 -> //优
                    color = resources.getColor(R.color.air_quality_good)
                airQuality in 51..100 -> //良
                    color = resources.getColor(R.color.air_quality_fine)
                airQuality in 101..150 -> //轻度污染
                    color = resources.getColor(R.color.air_quality_mild)
                airQuality in 151..200 -> //中度污染
                    color = resources.getColor(R.color.air_quality_medium)
                airQuality in 201..300 -> //重度污染
                    color = resources.getColor(R.color.air_quality_bad)
                airQuality > 300 -> //严重污染
                    color = resources.getColor(R.color.air_quality_serious)
            }
            return color
        }

        /**
         * 根据空气质量返回级别和建议
         */
        fun getAirQualityText(airQuality: Int): Array<String?> {
            val arr = arrayOfNulls<String>(2)
            var text = ""
            var suggest = ""
            when {
                airQuality <= 50 -> { //优
                    text = "优"
                    suggest = "空气很好，快呼吸新鲜空气，拥抱大自然吧"
                }
                airQuality in 51..100 -> { //良
                    text = "良"
                    suggest = "空气质量可以接受，可能对少数异常敏感的人群健康有较弱影响"
                }
                airQuality in 101..150 -> { //轻度污染
                    text = "轻度污染"
                    suggest = "空气较差，请尽量减少外出，关闭门窗"
                }
                airQuality in 151..200 -> { //中度污染
                    text = "中度污染"
                    suggest = "空气很差，请减少外出，关闭门窗"
                }
                airQuality in 201..300 -> {//重度污染
                    text = "重度污染"
                    suggest = "空气很差，请减少外出，关闭门窗"
                }
                airQuality > 300 -> { //严重污染
                    text = "严重污染"
                    suggest = "空气很差，请减少外出，关闭门窗"
                }
            }
            arr[0] = text
            arr[1] = suggest
            return arr
        }
    }
}