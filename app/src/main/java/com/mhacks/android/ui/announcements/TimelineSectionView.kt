package com.mhacks.android.ui.announcements

import android.content.Context
import android.graphics.Canvas
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

/**
 * Created by jeffreychang on 8/31/17.
 */

public class TimelineSectionView : View {

    constructor(context: Context): super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            :super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    public fun init(context: Context, attrs: AttributeSet?) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        initDrawable()
    }

    private fun initDrawable() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

}