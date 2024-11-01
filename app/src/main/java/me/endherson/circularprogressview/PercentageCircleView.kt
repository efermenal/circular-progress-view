package me.endherson.circularprogressview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class PercentageCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context,attrs, defStyleAttr) {

    private var animatedPercentage = 0
    var initialPercentage: Int = 0
        set(value) {
            field = value
            if (animatedPercentage == value) return
            animatedPercentage = value
            invalidate()
        }

    private val backgroundPlaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = LIGHT_GRAY_COLOR
    }

    private val borderPlaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = BORDER_WIDTH
        color = BLUE_COLOR
    }

    private val textPlaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = TEXT_SIZE
        color = BLACK_COLOR
        textAlign = Paint.Align.CENTER
    }

    fun startAnimation(to: Int) {
        startAnimation(0, to)
    }

    fun startAnimation(from: Int, to: Int) {
        ensureBounds(from, to)
        startAnimationInternal(from, to)
    }

    private fun ensureBounds(from: Int, to: Int) {
        require(from >= 0) {
            "From value cannot be less than 0: $from"
        }
        require(to <= 100) {
            "to value cannot be more than 100: $to"
        }
    }

    private fun startAnimationInternal(from: Int, to: Int) {
        val animator = ValueAnimator.ofInt(from, to).apply {
            duration = ANIMATION_DURATION
            addUpdateListener { animation ->
                animatedPercentage = animation.animatedValue as Int
                invalidate() // force to redraw with new percentage
            }
        }
        animator.start()
    }

    fun resumeAnimation(to: Int) {
        ensureBounds(animatedPercentage, to)
        startAnimationInternal(animatedPercentage, to)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val currentWidth = width
        val currentHeight = height
        val radius = currentWidth.coerceAtMost(currentHeight) / 2
        val xCoordinateForCenter = currentWidth / 2f
        val yCoordinateForCenter = currentHeight / 2f

        // draw the circle
        canvas.drawCircle(xCoordinateForCenter, yCoordinateForCenter, radius - BORDER_WIDTH, backgroundPlaint)

        // calculate angle and draw arc
        val sweepAngle = (360 * animatedPercentage) / 100f
        canvas.drawArc(BORDER_WIDTH, BORDER_WIDTH, currentWidth - BORDER_WIDTH, currentHeight - BORDER_WIDTH, TOP_ANGLE, sweepAngle, false, borderPlaint)

        // determine the text to be shown amd draw it
        val textToShow = if(animatedPercentage != COMPLETED) "$animatedPercentage%" else "Done!"
        canvas.drawText(textToShow, xCoordinateForCenter, yCoordinateForCenter + textPlaint.textSize / 4, textPlaint)
    }

    companion object {
        private const val LIGHT_GRAY_COLOR =  0xFFE0E0E0.toInt()
        private const val BLUE_COLOR =  0xFF03A9F4.toInt()
        private const val BLACK_COLOR =  0xFF000000.toInt()
        private const val TEXT_SIZE = 100f
        private const val BORDER_WIDTH = 10f
        private const val TOP_ANGLE = -90f
        private const val COMPLETED = 100
        private const val ANIMATION_DURATION = 2000L
    }
}
