package com.example.nabhackathon2023.base.customview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
import android.view.View

private const val TAG = "BaseCustomView"

abstract class BaseCustomView @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    View(context, attrs, defStyleAttr, defStyleRes) {

    init {
        setMinimumDimension()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        logSpec(MeasureSpec.getMode(widthMeasureSpec))
        Log.e(TAG, "size:" + MeasureSpec.getSize(widthMeasureSpec))
        Log.e(TAG, "********************")
        setMeasuredDimension(
            getImprovedDefaultWidth(widthMeasureSpec),
            getImprovedDefaultHeight(heightMeasureSpec)
        )
        //        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    private fun getImprovedDefaultHeight(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        when (specMode) {
            MeasureSpec.UNSPECIFIED -> return hGetMaximumHeight()
            MeasureSpec.AT_MOST -> return hGetMinimumHeight()
            MeasureSpec.EXACTLY -> return specSize
        }
        //you shouldn't come here
        Log.e(TAG, "unknown specmode")
        return specSize
    }

    private fun getImprovedDefaultWidth(measureSpec: Int): Int {
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        when (specMode) {
            MeasureSpec.UNSPECIFIED -> return hGetMaximumWidth()
            MeasureSpec.AT_MOST -> return hGetMinimumWidth()
            MeasureSpec.EXACTLY -> return specSize
        }
        //you shouldn't come here
        Log.e(TAG, "unknown specmode")
        return specSize
    }

    //Override these methods to provide a maximum size
    //"h" stands for hook pattern
    protected abstract fun hGetMaximumHeight(): Int
    protected abstract fun hGetMaximumWidth(): Int
    protected abstract fun setMinimumDimension()

    // For minimum height use the View's methods
    // "h" stands for hook pattern
    private fun hGetMinimumHeight(): Int {
        return this.suggestedMinimumHeight
    }

    private fun hGetMinimumWidth(): Int {
        return this.suggestedMinimumWidth
    }

    private fun logSpec(specMode: Int) {
        /**
         * Documentation says that this mode is passed in when the layout wants to
         * know what the true size is. True size could be as big as it could be; layout will likely then scroll it.
         * With that thought, we have returned the maximum size for our view.
         */
        if (specMode == MeasureSpec.UNSPECIFIED) {
            Log.e(TAG, "mode: unspecified")
            return
        }
        /**
         * wrap_content:  The size that gets passed could be much larger, taking up the rest of the space. So it might
         * say, “I have 411 pixels. Tell me your size that doesn’t exceed 411 pixels.” The question then to the
         * programmer is: What should I return?
         */
        if (specMode == MeasureSpec.AT_MOST) {
            Log.e(TAG, "mode: at most")
            return
        }
        /**
         * match_parent: the size will be equal parent's size
         * exact pixels: specified size which is set
         */
        if (specMode == MeasureSpec.EXACTLY) {
            Log.e(TAG, "mode: exact")
            return
        }
    }
}