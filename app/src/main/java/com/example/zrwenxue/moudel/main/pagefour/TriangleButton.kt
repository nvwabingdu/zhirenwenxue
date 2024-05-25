package com.example.zrwenxue.moudel.main.pagefour

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.example.newzr.R

class TriangleButton(context: Context, attrs: AttributeSet?) : View(context, attrs){

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var color: Int = 0
    private var isDownTriangle: Boolean = false

    init {
        // 从 XML 属性中获取按钮的颜色和方向
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TriangleButton,
            0, 0
        ).apply {
            try {
                color = getColor(R.styleable.TriangleButton_buttonColor, 0)
                isDownTriangle = getBoolean(R.styleable.TriangleButton_isDownTriangle, false)
            } finally {
                recycle()
            }
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()

        paint.color = color
        val path = Path()
        if (isDownTriangle) {
            path.moveTo(0f, 0f)
            path.lineTo(width, 0f)
            path.lineTo(width / 2, height)
        } else {
            path.moveTo(width / 2, 0f)
            path.lineTo(0f, height)
            path.lineTo(width, height)
        }
        path.close()
        canvas.drawPath(path, paint)
    }
}