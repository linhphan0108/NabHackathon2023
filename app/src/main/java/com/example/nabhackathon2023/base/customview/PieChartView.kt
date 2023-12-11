package com.example.nabhackathon2023.base.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import com.example.nabhackathon2023.R
import com.example.nabhackathon2023.base.util.dpToPx
import com.example.nabhackathon2023.base.util.shorten
import com.example.nabhackathon2023.base.util.spToPx


private const val PI_IN_DEGREE = 180
class PieChartView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    BaseCustomView(context, attrs, defStyleAttr, defStyleRes) {

    private var paint: Paint
    private var sliceDividerPaint: Paint
    private var captionPaint: Paint
    private var strokeWidthInPercent = 0.15f
    private var strokeWidthInPixel = 0f
    private val strokeColor = Color.RED
    private val center = PointF()
    private val rectF = RectF()
    private var radius = 0f
    private var rotate = -90f
    private var endAngleOfLastSlice = 0f
    private var sweepAngleOfDivider = 0.2f
    private var sweepAngleOfAllDividers = 0f
    private var data: List<Slice>? = null
    private var total = 0f
    private var currency = "$" //todo
    private var totalTextSize = 20.spToPx()
    private var captionTextSize = 10.spToPx()
    private var thresholdToShowCaption = 5.0f//in percent

    init {
        attractAttrs(context, attrs, defStyleAttr);

        paint = Paint().apply {
            isAntiAlias = true
            strokeWidth = 0f
            color = strokeColor
            style = Paint.Style.STROKE
        }

        sliceDividerPaint = Paint().apply {
            isAntiAlias = true
            strokeWidth = 0f
            color = Color.WHITE
            style = Paint.Style.STROKE
        }

        captionPaint = Paint().apply {
            isAntiAlias = true
            strokeWidth = 0f
            color = Color.BLACK
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
        }

        if (isInEditMode){
            setData(Stub.fakeData(resources.getStringArray(R.array.colorList)), 1000.0f)
        }
        setBackgroundColor(Color.CYAN)
    }

    override fun setMinimumDimension() {
        val diameter = 200.dpToPx()//todo
        minimumWidth = diameter.toInt()
        minimumHeight = diameter.toInt()
    }

    override fun hGetMaximumHeight(): Int {
        return 0
    }

    override fun hGetMaximumWidth(): Int {
        return 0
    }

    private fun attractAttrs(context: Context, attrs: AttributeSet?, defStyle: Int) {
        if (attrs == null) {
            return
        }
        val t = context.obtainStyledAttributes(
            attrs,
            R.styleable.PieChartView,
            defStyle,  //if any values are in the theme
            0
        )

        strokeWidthInPercent = t.getFloat(R.styleable.PieChartView_nabStrokeThickness, strokeWidthInPercent)
        sweepAngleOfDivider = t.getFloat(R.styleable.PieChartView_nabDividerThickness, sweepAngleOfDivider)
        totalTextSize = t.getDimension(R.styleable.PieChartView_nabTotalTextSize, totalTextSize)
        captionTextSize = t.getDimension(R.styleable.PieChartView_nabCaptionTextSize, captionTextSize)
        thresholdToShowCaption = t.getFloat(R.styleable.PieChartView_nabThresholdToShowCaption, thresholdToShowCaption)

        //Recycle the typed array
        t.recycle()
    }

    fun setData(data: List<Slice>, total: Float){
        this.data = data
        this.total = total
        if (data.isNotEmpty()){
            calculateDivider()
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (data.isNullOrEmpty()) return

        calculatePieChartBounds()

        drawTotal(canvas)

        for (slice in data!!){
            canvas.save()
            canvas.rotate(rotate, center.x, center.y)
            drawSlice(canvas, slice)
            drawSliceDivider(canvas)
            canvas.restore()
            drawCaption(canvas, slice)
        }
//        drawCaption(canvas, data!![0])

//        animateRotation()
    }

    private fun drawSlice(canvas: Canvas, slice: Slice){
        val colorInt = try {
            Color.parseColor(slice.color)
        }catch (e: IllegalArgumentException){
            Color.GRAY
        }
        paint.color = colorInt
        val sweepAngle = percentToAngle(slice.percent)
        val startAngle = endAngleOfLastSlice + rotateAnimation
        canvas.drawArc(rectF, startAngle, sweepAngle, false, paint)
        endAngleOfLastSlice += sweepAngle
    }

    private fun drawSliceDivider(canvas: Canvas) {
        val sweepAngle = sweepAngleOfDivider
        val startAngle = endAngleOfLastSlice + rotateAnimation
        canvas.drawArc(rectF, startAngle, sweepAngle, false, sliceDividerPaint)
        endAngleOfLastSlice += sweepAngle
    }

    private fun drawTotal(canvas: Canvas){
        val stringTotal = "${total.shorten()}$currency"
        captionPaint.apply {
            textSize = totalTextSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        val xPos = width.toFloat()/2
        val yPos = center.y - (captionPaint.descent() + captionPaint.ascent()) / 2
        canvas.drawText(stringTotal, xPos, yPos, captionPaint)
    }
    private fun drawCaption(canvas: Canvas, slice: Slice){
        if(slice.percent < thresholdToShowCaption) return
        val radiusOfCaption = radius + strokeWidthInPixel + 8.dpToPx() //todo hardcoded
        val sweepAngle = (endAngleOfLastSlice.toDouble() + rotate - percentToAngle(slice.percent)/2)%360
        val xStart = (center.x + (radius + strokeWidthInPixel*0.5) * Math.cos(sweepAngle * Math.PI / PI_IN_DEGREE)).toFloat()
        val yStart = (center.y + (radius + strokeWidthInPixel*0.5) * Math.sin(sweepAngle * Math.PI / PI_IN_DEGREE)).toFloat()
        val xEnd = (center.x + radiusOfCaption * Math.cos(sweepAngle * Math.PI / PI_IN_DEGREE)).toFloat()
        val yEnd = (center.y + radiusOfCaption * Math.sin(sweepAngle * Math.PI / PI_IN_DEGREE)).toFloat()
        captionPaint.apply {
            textSize = captionTextSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            textAlign = if(sweepAngle > -90 && sweepAngle < 90){
                Paint.Align.LEFT
            }else{
                Paint.Align.RIGHT
            }
        }
        canvas.drawLine(xStart, yStart, xEnd, yEnd, captionPaint)
        canvas.drawText(slice.name, xEnd, yEnd, captionPaint)
        canvas.drawCircle(xStart, yStart, 2.dpToPx(), captionPaint)
        Log.i("LINHPHAN", "${slice.name}: $x - $y; angle = $sweepAngle")
    }

    private fun calculatePieChartBounds(){
        val diameter = Math.min(width - paddingStart - paddingEnd, height - paddingTop - paddingBottom)
        var strokeWidth = diameter * 0.5f * strokeWidthInPercent
        if (strokeWidth * 2 > diameter) strokeWidth = diameter * 0.5f
        radius = diameter * 0.5f - strokeWidth * 0.5f
        val ox = width * 0.5f
        val oy = height * 0.5f
        center.set(ox, oy)
        val topPie = ox - radius //include padding
        val leftPie = oy - radius //include padding
        val rightPie = ox + radius //include padding
        val bottomPie = oy + radius //include padding
        paint.strokeWidth = strokeWidth
        sliceDividerPaint.strokeWidth = strokeWidth
        strokeWidthInPixel = strokeWidth

        rectF.set(leftPie, topPie, rightPie, bottomPie)
    }

    private fun calculateDivider(){
        if (data.isNullOrEmpty()) return
        val numberOfDivider = data!!.size - 1
        sweepAngleOfAllDividers = numberOfDivider * sweepAngleOfDivider
    }

    private fun percentToAngle(percent: Float): Float{
        val remainingAngle = 360 - sweepAngleOfAllDividers
        return (percent * remainingAngle)/ 100
    }

    private var count = 0
    private var rotateAnimation = 0f
    private fun animateRotation(){
        count++
        rotateAnimation = if (count < 20) 18f * count else 0f
        if (count <= 20) {
            postDelayed({
                invalidate()
            }, 16)
        }
    }
}