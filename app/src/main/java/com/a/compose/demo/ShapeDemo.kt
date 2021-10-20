package com.a.compose

import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.a.compose.component.SampleList
import kotlinx.coroutines.CoroutineStart


@Preview
@Composable
fun ShapeDemo() {
    SampleList(title = "ShapeDemo") {
        LazyColumn {
            item{
                Divider(Modifier.height(1.dp))
                CanvasDemoV1()
                Divider(Modifier.height(1.dp))
                ArcTextExample()
            }
        }
    }

}

@Composable
fun CanvasDemoV1() {
    Canvas(modifier = Modifier.size(100.dp,200.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        drawLine(
            start = Offset(x = canvasWidth, y = 0f),
            end = Offset(x = 0f, y = canvasHeight),
            color = Color.Blue,
            strokeWidth = 5F
        )
        drawCircle(
            color = Color.Blue,
            center = Offset(x = canvasWidth / 2, y = canvasHeight / 2),
            radius = size.minDimension / 4
        )

        val canvasQuadrantSize = size / 2F
        drawRect(
            color = Color.Green,
            size = canvasQuadrantSize
        )

        inset(50F, 120F) {
            drawRect(
                color = Color.Green,
                size = canvasQuadrantSize
            )
        }

        val canvasSize = size
        drawRect(
            color = Color.Gray,
            topLeft = Offset(x = canvasWidth / 3F, y = canvasHeight / 3F),
            size = canvasSize / 3F
        )

        rotate(degrees = 45F) {
            drawRect(
                color = Color.Gray,
                topLeft = Offset(x = canvasWidth / 3F, y = canvasHeight / 3F),
                size = canvasSize / 3F
            )
        }

        withTransform({
            translate(left = canvasWidth / 5F)
            rotate(degrees = 45F)
        }) {
            drawRect(
                color = Color.Gray,
                topLeft = Offset(x = canvasWidth / 3F, y = canvasHeight / 3F),
                size = canvasSize / 3F
            )
        }

    }
}


@Composable
fun ArcTextExample() {
    val paint = Paint().asFrameworkPaint()
    Canvas(modifier = Modifier.size(100.dp)) {
        paint.apply {
            isAntiAlias = true
            textSize = 24f
        }

        drawIntoCanvas {
            val path = Path()
            path.addArc(RectF(0f, 100f, 200f, 300f), 270f, 180f)
            it.nativeCanvas.drawTextOnPath("Hello World Example", path, 0f, 0f, paint)
        }
    }
}

