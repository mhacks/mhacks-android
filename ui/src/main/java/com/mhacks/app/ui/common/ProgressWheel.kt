package com.mhacks.app.ui.common

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import org.mhacks.mhacksui.R

class ProgressWheel : View {

    //Sizes (with defaults)
    private var barWidth = 24f
    private var countTextSize = 48f
    private var mDefTextSize = 24f
    private var layoutHeight = 0
    private var layoutWidth = 0
    private var marginBtwTexts = 20f

    //Colors (with defaults)
    private var mProgressColor = Color.GREEN
    private var mRimColor = -0x11111112
    private var mCountTextColor = Color.BLACK
    private var mDefTextColor = Color.BLACK

    //Padding (with defaults)
    private var paddingTop = 5f
    private var paddingBottom = 5f
    private var paddingLeft = 5f
    private var paddingRight = 5f

    //Rectangles
    private var mRimBounds = RectF()
    private var mProgressBounds = RectF()

    //Paints
    private val mCirclePaint = Paint()
    private val mBarPaint = Paint()
    private var mCountTextPaint = TextPaint()
    private val mDefTextPaint = TextPaint()


    private var centerText: String? = ""
    private var mDefText: String? = null


    private var mCountTextWidth: Float = 0f
    private var mCountTextHeight: Float = 0f
    private var mDefTextHeight: Float = 0f
    private var mDefTextWidth: Float = 0f

    // Set percentage
    private var percentage = 0

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        layoutWidth = w
        layoutHeight = h
        setupBounds()
        setupPaints()

        invalidate()
    }


    private fun setupPaints() {
        mBarPaint.color = mProgressColor
        mBarPaint.isAntiAlias = true
        mBarPaint.style = Paint.Style.STROKE
        mBarPaint.strokeWidth = barWidth
        mBarPaint.strokeCap = Paint.Cap.ROUND


        mCirclePaint.color = mRimColor
        mCirclePaint.isAntiAlias = true
        mCirclePaint.style = Paint.Style.STROKE
        mCirclePaint.strokeWidth = barWidth

        mCountTextPaint.color = mCountTextColor
        mCountTextPaint.flags = Paint.ANTI_ALIAS_FLAG
        mDefTextPaint.color = mDefTextColor
        mDefTextPaint.flags = Paint.ANTI_ALIAS_FLAG
    }

    private fun setupBounds() {
        val minValue = Math.min(layoutWidth, layoutHeight)

        // Calc the Offset if needed
        val xOffset = layoutWidth - minValue
        val yOffset = layoutHeight - minValue

        // Offset
        paddingTop = this.getPaddingTop().toFloat() + yOffset / 2
        paddingBottom = this.getPaddingBottom().toFloat() + yOffset / 2
        paddingLeft = this.getPaddingLeft().toFloat() + xOffset / 2
        paddingRight = this.getPaddingRight().toFloat() + xOffset / 2

        val width = width
        val height = height

        mRimBounds = RectF(
                paddingLeft + barWidth,
                paddingTop + barWidth,
                width.toFloat() - paddingRight - barWidth,
                height.toFloat() - paddingBottom - barWidth)

        mProgressBounds = RectF(
                paddingLeft + barWidth,
                paddingTop + barWidth,
                width.toFloat() - paddingRight - barWidth,
                height.toFloat() - paddingBottom - barWidth)

        // Count number text
        mCountTextPaint.textSize = countTextSize
        val fontMetrics = mCountTextPaint.fontMetrics
        mCountTextHeight = fontMetrics.bottom
        mCountTextWidth = mCountTextPaint.measureText(if (centerText == null || centerText!!.isEmpty()) " " else centerText)

        // Definition text
        if (mDefText != null){
            mDefTextPaint.textSize = mDefTextSize
            val fontDefMetrics = mDefTextPaint.fontMetrics
            mDefTextHeight = fontDefMetrics.bottom
            mDefTextWidth = mDefTextPaint.measureText(if (mDefText!!.isEmpty()) " " else mDefText)
        }
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.ProgressWheel, defStyle, 0)

        if (a.hasValue(R.styleable.ProgressWheel_countText))
            centerText = a.getString(R.styleable.ProgressWheel_countText)
        if (a.hasValue(R.styleable.ProgressWheel_definitionText))
            mDefText = a.getString(R.styleable.ProgressWheel_definitionText)

        barWidth = a.getDimension(R.styleable.ProgressWheel_barWidth, barWidth)
        mProgressColor = a.getColor(R.styleable.ProgressWheel_progressColor, mProgressColor)
        mRimColor = a.getColor(R.styleable.ProgressWheel_rimColor, mRimColor)
        mCountTextColor = a.getColor(R.styleable.ProgressWheel_countTextColor, mCountTextColor)
        mDefTextColor = a.getColor(R.styleable.ProgressWheel_defTextColor, mDefTextColor)
        countTextSize = a.getDimension(R.styleable.ProgressWheel_countTextSize, countTextSize)
        mDefTextSize = a.getDimension(R.styleable.ProgressWheel_defTextSize, mDefTextSize)
        percentage = a.getInt(R.styleable.ProgressWheel_percentage, percentage)
        marginBtwTexts = a.getDimension(R.styleable.ProgressWheel_marginBtwText, marginBtwTexts)

        a.recycle()

        // Set up a default TextPaint object
        mCountTextPaint = TextPaint()
        mCountTextPaint.flags = Paint.ANTI_ALIAS_FLAG
        mCountTextPaint.textAlign = Paint.Align.LEFT

        // Update TextPaint and text measurements from attributes
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawArc(mRimBounds, 0f, 360f, false, mCirclePaint)
        canvas.drawArc(mProgressBounds, -90f, percentage.toFloat(), false, mBarPaint)

        if (centerText != null){
            val horizontalCountTextOffset = mCountTextPaint.measureText(centerText) / 2
            canvas.drawText(centerText!!,
                    this.width / 2 - horizontalCountTextOffset,
                    (this.height / 2).toFloat() + if (mDefText == null) countTextSize/2 else 0f,
                    mCountTextPaint
            )
        }


        if (mDefText != null){
            val horizontalDefTextOffset = mDefTextPaint.measureText(mDefText) / 2
            canvas.drawText(mDefText!!,
                    this.width / 2 - horizontalDefTextOffset,
                    (this.height / 2).toFloat() + mCountTextHeight + marginBtwTexts,
                    mDefTextPaint
            )
        }

    }

    fun setCenterText(countText: String) {
        centerText = countText
        invalidate()
    }

    fun setDefText(defText: String) {
        mDefText = defText
        invalidate()
    }

    fun setPercentage(per: Int) {
        startAnimation(per)
    }

    private fun startAnimation(per: Int) {
        val diff = (((per * .01) * 360)  - percentage).toInt()
        val valueAnimator = ValueAnimator
                .ofInt(percentage, percentage + diff)
                .setDuration(1500)
        valueAnimator.interpolator = FastOutSlowInInterpolator()
        valueAnimator.addUpdateListener { animation ->
            percentage = animation.animatedValue as Int
            invalidate()
        }
        valueAnimator.start()
    }
}