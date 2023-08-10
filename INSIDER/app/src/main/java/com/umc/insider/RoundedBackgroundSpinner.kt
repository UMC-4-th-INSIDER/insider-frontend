package com.umc.insider
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

class RoundedBackgroundSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.spinnerStyle
) : AppCompatSpinner(context, attrs, defStyleAttr) {

    private val path = Path()
    private val rectF = RectF()

    override fun onDraw(canvas: Canvas) {
        val borderWidth = 0f
        val width = this.width
        val height = this.height
        val radius = 30f
        path.reset()
        rectF.set(borderWidth, borderWidth, width - borderWidth, height - borderWidth)
        path.addRoundRect(rectF, radius, radius, Path.Direction.CW)
        canvas.clipPath(path)
        super.onDraw(canvas)
    }
}
