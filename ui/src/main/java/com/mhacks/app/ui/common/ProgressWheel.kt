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
    private var defTextSize = 24f
    private var layoutHeight = 0
    private var layoutWidth = 0
    private var marginBtwTexts = 20f

    //Colors (with defaults)
    private var progressColor = Color.GREEN
    private var rimColor = -0x11111112
    private var countTextColor = Color.BLACK
    private var defTextColor = Color.BLACK

    //Padding (with defaults)
    private var paddingTop = 5f
    private var paddingBottom = 5f
    private var paddingLeft = 5f
    private var paddingRight = 5f

    //Rectangles
    private var rimBounds = RectF()
    private var progressBounds = RectF()

    //Paints
//    private val circlePaint = Paint()
    private val barPaint = Paint()
    private var countTextPaint = TextPaint()
    private val defTextPaint = TextPaint()

    private var centerText: String? = ""
    private var defText: String? = null

    private var countTextWidth: Float = 0f
    private var countTextHeight: Float = 0f
    private var defTextHeight: Float = 0f
    private var defTextWidth: Float = 0f

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
        barPaint.color = progressColor
        barPaint.isAntiAlias = true
        barPaint.style = Paint.Style.STROKE
        barPaint.strokeWidth = barWidth
        barPaint.strokeCap = Paint.Cap.ROUND
        
//        circlePaint.color = rimColor
//        circlePaint.isAntiAlias = true
//        circlePaint.style = Paint.Style.STROKE
//        circlePaint.strokeWidth = barWidth

        countTextPaint.color = countTextColor
        countTextPaint.flags = Paint.ANTI_ALIAS_FLAG
        defTextPaint.color = defTextColor
        defTextPaint.flags = Paint.ANTI_ALIAS_FLAG
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

        rimBounds = RectF(
                paddingLeft + barWidth,
                paddingTop + barWidth,
                width.toFloat() - paddingRight - barWidth,
                height.toFloat() - paddingBottom - barWidth)

        progressBounds = RectF(
                paddingLeft + barWidth,
                paddingTop + barWidth,
                width.toFloat() - paddingRight - barWidth,
                height.toFloat() - paddingBottom - barWidth)

        // Count number text
        countTextPaint.textSize = countTextSize
        val fontMetrics = countTextPaint.fontMetrics
        countTextHeight = fontMetrics.bottom
        countTextWidth = countTextPaint.measureText(if (centerText == null || centerText!!.isEmpty()) " " else centerText)

        // Definition text
        if (defText != null){
            defTextPaint.textSize = defTextSize
            val fontDefMetrics = defTextPaint.fontMetrics
            defTextHeight = fontDefMetrics.bottom
            defTextWidth = defTextPaint.measureText(if (defText!!.isEmpty()) " " else defText)
        }
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        // Load attributes
        val a = context.obtainStyledAttributes(
                attrs, R.styleable.ProgressWheel, defStyle, 0)

        if (a.hasValue(R.styleable.ProgressWheel_countText))
            centerText = a.getString(R.styleable.ProgressWheel_countText)
        if (a.hasValue(R.styleable.ProgressWheel_definitionText))
            defText = a.getString(R.styleable.ProgressWheel_definitionText)

        barWidth = a.getDimension(R.styleable.ProgressWheel_barWidth, barWidth)
        progressColor = a.getColor(R.styleable.ProgressWheel_progressColor, progressColor)
        rimColor = a.getColor(R.styleable.ProgressWheel_rimColor, rimColor)
        countTextColor = a.getColor(R.styleable.ProgressWheel_countTextColor, countTextColor)
        defTextColor = a.getColor(R.styleable.ProgressWheel_defTextColor, defTextColor)
        countTextSize = a.getDimension(R.styleable.ProgressWheel_countTextSize, countTextSize)
        defTextSize = a.getDimension(R.styleable.ProgressWheel_defTextSize, defTextSize)
        percentage = a.getInt(R.styleable.ProgressWheel_percentage, percentage)
        marginBtwTexts = a.getDimension(R.styleable.ProgressWheel_marginBtwText, marginBtwTexts)

        a.recycle()

        // Set up a default TextPaint object
        countTextPaint = TextPaint()
        countTextPaint.flags = Paint.ANTI_ALIAS_FLAG
        countTextPaint.textAlign = Paint.Align.LEFT

        // Update TextPaint and text measurements from attributes
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        canvas.drawArc(rimBounds, 0f, 360f, false, circlePaint)
        canvas.drawArc(progressBounds, -90f, percentage.toFloat(), false, barPaint)

        if (centerText != null){
            val horizontalCountTextOffset = countTextPaint.measureText(centerText) / 2
            canvas.drawText(centerText!!,
                    this.width / 2 - horizontalCountTextOffset,
                    (this.height / 2).toFloat() + if (defText == null) countTextSize /2 else 0f,
                    countTextPaint
            )
        }


        if (defText != null){
            val horizontalDefTextOffset = defTextPaint.measureText(defText) / 2
            canvas.drawText(defText!!,
                    this.width / 2 - horizontalDefTextOffset,
                    (this.height / 2).toFloat() + countTextHeight + marginBtwTexts,
                    defTextPaint
            )
        }

    }

    fun setCenterText(countText: String) {
        centerText = countText
        invalidate()
    }

    fun setDefText(defText: String) {
        this.defText = defText
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