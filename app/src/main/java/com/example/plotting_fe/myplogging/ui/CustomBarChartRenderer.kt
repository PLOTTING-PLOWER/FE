package com.example.plotting_fe.myplogging.ui

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.renderer.BarChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class RoundedBarChartRenderer(
    chart: BarDataProvider,
    animator: ChartAnimator,
    viewPortHandler: ViewPortHandler
) : BarChartRenderer(chart, animator, viewPortHandler) {

    private val mBarShadowRectBuffer = RectF()
    private val path = Path()

    private val renderPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    override fun drawDataSet(c: Canvas, dataSet: IBarDataSet, index: Int) {
        val barWidth = mChart.barData.barWidth
        val cornerRadius = 50f  // 모서리 둥글기 정도
        val barSpacing = barWidth * 1f  // 막대 사이의 여백 (바 너비의 20%)

        val buffer = mBarBuffers[index]
        buffer.feed(dataSet)

        val trans = mChart.getTransformer(dataSet.axisDependency)
        trans.pointValuesToPixel(buffer.buffer)

        for (j in 0 until buffer.size() step 4) {
            if (!mViewPortHandler.isInBoundsLeft(buffer.buffer[j + 2])) continue
            if (!mViewPortHandler.isInBoundsRight(buffer.buffer[j])) break

            // 여백을 적용한 막대 위치 계산
            val left = buffer.buffer[j] + barSpacing/2
            val top = buffer.buffer[j + 1]
            val right = buffer.buffer[j + 2] - barSpacing/2
            val bottom = buffer.buffer[j + 3]

            if (right - left <= 0) continue

            mBarShadowRectBuffer.set(left, top, right, bottom)

            path.reset()
            // 모든 모서리를 둥글게 처리
            path.moveTo(left + cornerRadius, bottom)
            // 왼쪽 하단 모서리
            path.quadTo(left, bottom, left, bottom - cornerRadius)
            // 왼쪽 선
            path.lineTo(left, top + cornerRadius)
            // 왼쪽 상단 모서리
            path.quadTo(left, top, left + cornerRadius, top)
            // 상단 선
            path.lineTo(right - cornerRadius, top)
            // 오른쪽 상단 모서리
            path.quadTo(right, top, right, top + cornerRadius)
            // 오른쪽 선
            path.lineTo(right, bottom - cornerRadius)
            // 오른쪽 하단 모서리
            path.quadTo(right, bottom, right - cornerRadius, bottom)
            // 하단 선
            path.lineTo(left + cornerRadius, bottom)
            path.close()

            if (dataSet.color != 0) {
                renderPaint.color = dataSet.getColor(j / 4)
            }

            c.drawPath(path, renderPaint)
        }
    }
}