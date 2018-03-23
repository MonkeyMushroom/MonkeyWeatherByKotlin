package com.monkey.monkeyweather.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.monkey.monkeyweather.R
import com.monkey.monkeyweather.util.DensityUtil
import com.monkey.monkeyweather.util.ScreenUtil
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日出日落view
 */
class SunriseSunsetView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private var mDashedPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mGradientPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mArcPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mTextPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mCirclePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mSunPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mSunSweepAngle = 0f
    private var mLightGradient: LinearGradient? = null
    private var mDarkGradient: LinearGradient? = null
    private var mSunRadius = 0f
    private var mPadding = 0f
    private var mBaseMargin = 0f
    private var mArcRectF: RectF

    private var mStartAngle = -135f
    private var mArcSweepAngle = 90f

    private val mDarkColor = Color.parseColor("#FE8000")
    private val mLightColor = Color.parseColor("#FFD9B3")
    private val mSlightColor = Color.parseColor("#FFEAD6")
    private val mSunColor = Color.parseColor("#FFD400")

    init {
        mDashedPaint.style = Paint.Style.STROKE
        mDashedPaint.strokeWidth = resources.getDimensionPixelSize(R.dimen.bold_line_height).toFloat()
        mDashedPaint.pathEffect = DashPathEffect(floatArrayOf(15f, 15f), 0f)
        mDashedPaint.color = mDarkColor

        mGradientPaint.style = Paint.Style.FILL

        mArcPaint.style = Paint.Style.STROKE
        mArcPaint.strokeWidth = resources.getDimensionPixelSize(R.dimen.bold_line_height).toFloat()
        mArcPaint.color = mDarkColor

        mTextPaint.style = Paint.Style.FILL
        mTextPaint.textSize = resources.getDimension(R.dimen.small_text_size)
        mTextPaint.color = mDarkColor

        mCirclePaint.style = Paint.Style.FILL
        mCirclePaint.color = mDarkColor

        mSunPaint.style = Paint.Style.FILL

        mArcRectF = RectF()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //如果圆弧要刚好包裹在view里面的话，宽和高是要满足这个关系的
        val width = measureWidth(widthMeasureSpec)
        val height = (2 - Math.sqrt(2.0)) / (2 * Math.sqrt(2.0)) * width
        setMeasuredDimension(width, height.toInt())
    }

    private fun measureWidth(measureSpec: Int): Int {
        var result: Int
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        if (mode == MeasureSpec.EXACTLY) {
            result = size
        } else {
            result = ScreenUtil.getScreenWidth(context)
            if (mode == MeasureSpec.AT_MOST) {
                result = Math.min(result, size)
            }
        }
        return result
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
//        mPadding = resources.getDimensionPixelSize(R.dimen.base_margin).toFloat()
        mBaseMargin = resources.getDimensionPixelSize(R.dimen.base_margin).toFloat()
        mSunRadius = DensityUtil.dp2px(context, 10f).toFloat()
        mLightGradient = LinearGradient(w / 2.toFloat(), 0f, w / 2.toFloat(), h.toFloat()
                , mSlightColor, resources.getColor(R.color.base_white), Shader.TileMode.CLAMP)
        mDarkGradient = LinearGradient(w / 2.toFloat(), 0f, w / 2.toFloat(), h.toFloat()
                , mLightColor, resources.getColor(R.color.base_white), Shader.TileMode.CLAMP)
        mArcRectF.set(-h.toFloat(), 0f, w + h.toFloat(), w + 2 * h.toFloat())
    }

    private val mFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    private var mStartTimeStr: String = ""
    private var mEndTimeStr: String = ""

    private var mTimePercent: Float = 0f
    private var mSunriseStr = ""
    private var mSunsetStr = ""

    /**
     * 设置日出日落时间
     */
    fun setSunriseSunsetTime(startTime: String, endTime: String) {
        mStartTimeStr = startTime
        mEndTimeStr = endTime
        mSunriseStr = "日出${startTime.substring(startTime.indexOf(" "))}"
        mSunsetStr = "日落${endTime.substring(startTime.indexOf(" "))}"
        val startMilliseconds = mFormat.parse(startTime).time
        val endMilliseconds = mFormat.parse(endTime).time
        val curMilliseconds = System.currentTimeMillis()
        mTimePercent = when {
            curMilliseconds < startMilliseconds -> 0f
            curMilliseconds > endMilliseconds -> 1f
            else -> (curMilliseconds - startMilliseconds) / (endMilliseconds - startMilliseconds).toFloat()
        }
    }

    private var isAnimed: Boolean = false//是否已经做过动画了

    fun startAnim() {
        val rect = Rect()
        val visible = getGlobalVisibleRect(rect)//是否是显示状态（包括部分显示）
        if (!visible || mTimePercent == 0f) {
            return
        }
        if (!isAnimed) {
            val anim = ValueAnimator.ofFloat(0f, mTimePercent)
            anim.duration = (2000 * mTimePercent).toLong()
            anim.addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                mSunSweepAngle = value * mArcSweepAngle
                invalidate()
            }
            anim.start()
            isAnimed = true
        } else {
            mSunSweepAngle = mTimePercent * mArcSweepAngle
        }
    }

    override fun onDraw(canvas: Canvas) {
        //如果用padding留白的话，对于坐标计算很麻烦，偷懒用缩放留白
        val ratio = 1 - resources.getDimensionPixelSize(R.dimen.base_margin) / height.toFloat()
        canvas.scale(ratio, ratio, width.toFloat() / 2, height.toFloat() / 2)

        canvas.drawArc(mArcRectF, mStartAngle, mArcSweepAngle, false, mDashedPaint)//虚线弧
        mGradientPaint.shader = mLightGradient
        canvas.drawArc(mArcRectF, mStartAngle, mArcSweepAngle, false, mGradientPaint)//底部浅色扇形
        canvas.drawArc(mArcRectF, mStartAngle, mSunSweepAngle, false, mArcPaint)//实线弧
        mGradientPaint.shader = mDarkGradient
        canvas.drawArc(mArcRectF, mStartAngle, mSunSweepAngle, true, mGradientPaint)//深色阴影扇形
        canvas.drawCircle(0f, height.toFloat(), DensityUtil.dp2px(context, 2.5f).toFloat(), mCirclePaint)//左定点
        canvas.drawCircle(width.toFloat(), height.toFloat(), DensityUtil.dp2px(context, 2.5f).toFloat(), mCirclePaint)//右定点

        //日出时间日落时间文本
        val textWidth = mTextPaint.measureText(mSunriseStr)
        canvas.drawText(mSunriseStr, 0, mSunriseStr.length, mBaseMargin, height.toFloat(), mTextPaint)
        canvas.drawText(mSunsetStr, 0, mSunsetStr.length, width - textWidth - mBaseMargin, height.toFloat(), mTextPaint)

        //太阳随着角度变化移动
        canvas.save()
        canvas.rotate(mSunSweepAngle, width.toFloat() / 2, width / 2 + height.toFloat())
        mSunPaint.color = Color.WHITE
        canvas.drawCircle(0f, height.toFloat(), mSunRadius + 4, mSunPaint)
        mSunPaint.color = mSunColor
        canvas.drawCircle(0f, height.toFloat(), mSunRadius, mSunPaint)
        canvas.restore()

    }
}