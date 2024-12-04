package com.example.sizableboxsample

import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CornerBox(
    modifier: Modifier = Modifier
) {
    var boundaryOffset by remember { mutableStateOf(Offset.Zero) }
    var boundarySize by remember { mutableStateOf(Size.Zero) }
    var holeOffset by remember { mutableStateOf(Offset(200f, 200f)) }
    var holeSize by remember { mutableStateOf(Size(200f, 200f)) }
    val cornerRadius = 30f
    val minSize = cornerRadius * 2

    val animatedHoleOffset by animateOffsetAsState(
        targetValue = holeOffset,
        animationSpec = tween(durationMillis = 50), label = ""
    )
    val animatedHoleSize by animateSizeAsState(
        targetValue = holeSize,
        animationSpec = tween(durationMillis = 50)
    )

    var initialTouchType by remember { mutableStateOf(TouchType.None) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                val size = coordinates.size.toSize()
                boundaryOffset = Offset(0f, 0f)
                boundarySize = size
            }
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { startPoint ->
                        initialTouchType = when {
                            isNearCorner(startPoint, holeOffset, cornerRadius) -> TouchType.TopLeft
                            isNearCorner(startPoint, holeOffset + Offset(holeSize.width, 0f), cornerRadius) -> TouchType.TopRight
                            isNearCorner(startPoint, holeOffset + Offset(0f, holeSize.height), cornerRadius) -> TouchType.BottomLeft
                            isNearCorner(startPoint, holeOffset + Offset(holeSize.width, holeSize.height), cornerRadius) -> TouchType.BottomRight
                            isInsideBox(startPoint, holeOffset, holeSize) -> TouchType.Center
                            else -> TouchType.None
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        when (initialTouchType) {
                            TouchType.TopLeft -> {
                                val newOffset = Offset(
                                    (holeOffset.x + dragAmount.x).coerceAtLeast(boundaryOffset.x),
                                    (holeOffset.y + dragAmount.y).coerceAtLeast(boundaryOffset.y)
                                )
                                val newSize = Size(
                                    (holeSize.width - dragAmount.x).coerceAtLeast(minSize),
                                    (holeSize.height - dragAmount.y).coerceAtLeast(minSize)
                                )
                                if (isValidSize(newOffset, newSize, boundaryOffset, boundarySize, cornerRadius)) {
                                    holeOffset = newOffset
                                    holeSize = newSize
                                }
                            }
                            TouchType.TopRight -> {
                                val newSize = Size(
                                    (holeSize.width + dragAmount.x).coerceAtLeast(minSize),
                                    (holeSize.height - dragAmount.y).coerceAtLeast(minSize)
                                )
                                val newOffset = Offset(
                                    holeOffset.x,
                                    (holeOffset.y + dragAmount.y).coerceAtLeast(boundaryOffset.y)
                                )
                                if (isValidSize(newOffset, newSize, boundaryOffset, boundarySize, cornerRadius)) {
                                    holeOffset = newOffset
                                    holeSize = newSize
                                }
                            }
                            TouchType.BottomLeft -> {
                                val newSize = Size(
                                    (holeSize.width - dragAmount.x).coerceAtLeast(minSize),
                                    (holeSize.height + dragAmount.y).coerceAtLeast(minSize)
                                )
                                val newOffset = Offset(
                                    (holeOffset.x + dragAmount.x).coerceAtLeast(boundaryOffset.x),
                                    holeOffset.y
                                )
                                if (isValidSize(newOffset, newSize, boundaryOffset, boundarySize, cornerRadius)) {
                                    holeOffset = newOffset
                                    holeSize = newSize
                                }
                            }
                            TouchType.BottomRight -> {
                                val newSize = Size(
                                    (holeSize.width + dragAmount.x).coerceAtLeast(minSize),
                                    (holeSize.height + dragAmount.y).coerceAtLeast(minSize)
                                )
                                if (isValidSize(holeOffset, newSize, boundaryOffset, boundarySize, cornerRadius)) {
                                    holeSize = newSize
                                }
                            }
                            TouchType.Center -> {
                                val newOffset = Offset(
                                    (holeOffset.x + dragAmount.x).coerceIn(
                                        boundaryOffset.x,
                                        boundaryOffset.x + boundarySize.width - holeSize.width
                                    ),
                                    (holeOffset.y + dragAmount.y).coerceIn(
                                        boundaryOffset.y,
                                        boundaryOffset.y + boundarySize.height - holeSize.height
                                    )
                                )
                                if (isValidSize(newOffset, holeSize, boundaryOffset, boundarySize, cornerRadius)) {
                                    holeOffset = newOffset
                                }
                            }
                            else -> Unit
                        }
                    }
                )
            }
        ) {
            drawRect(
                color = Color.Black.copy(alpha = 0.2f),
                size = size
            )
            drawRoundRect(
                color = Color.White.copy(alpha = 0.2f),
                topLeft = animatedHoleOffset,
                size = animatedHoleSize,
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
            drawArcCorners(animatedHoleOffset, animatedHoleSize, cornerRadius)
        }
    }
}

private fun DrawScope.drawArcCorners(
    holeOffset: Offset,
    holeSize: Size,
    cornerRadius: Float
) {
    // Draw arcs at each corner
    drawArc(
        color = Color.White,
        startAngle = 180f,
        sweepAngle = 90f,
        useCenter = false,
        topLeft = holeOffset,
        size = Size(cornerRadius * 2, cornerRadius * 2),
        style = Stroke(width = 10f)
    )
    drawArc(
        color = Color.White,
        startAngle = 270f,
        sweepAngle = 90f,
        useCenter = false,
        topLeft = Offset(holeOffset.x + holeSize.width - cornerRadius * 2, holeOffset.y),
        size = Size(cornerRadius * 2, cornerRadius * 2),
        style = Stroke(width = 10f)
    )
    drawArc(
        color = Color.White,
        startAngle = 90f,
        sweepAngle = 90f,
        useCenter = false,
        topLeft = Offset(holeOffset.x, holeOffset.y + holeSize.height - cornerRadius * 2),
        size = Size(cornerRadius * 2, cornerRadius * 2),
        style = Stroke(width = 10f)
    )
    drawArc(
        color = Color.White,
        startAngle = 0f,
        sweepAngle = 90f,
        useCenter = false,
        topLeft = Offset(holeOffset.x + holeSize.width - cornerRadius * 2, holeOffset.y + holeSize.height - cornerRadius * 2),
        size = Size(cornerRadius * 2, cornerRadius * 2),
        style = Stroke(width = 10f)
    )
}

private fun isNearCorner(
    touchPoint: Offset,
    cornerOffset: Offset,
    radius: Float
): Boolean {
    val distance = (touchPoint - cornerOffset).getDistance()
    return distance <= radius * 2
}

private fun isInsideBox(point: Offset, boxOffset: Offset, boxSize: Size): Boolean {
    return point.x in boxOffset.x..(boxOffset.x + boxSize.width) &&
            point.y in boxOffset.y..(boxOffset.y + boxSize.height)
}

private enum class TouchType {
    None, TopLeft, TopRight, BottomLeft, BottomRight, Center
}

private fun isValidSize(
    holeOffset: Offset,
    holeSize: Size,
    boundaryOffset: Offset,
    boundarySize: Size,
    cornerRadius: Float
): Boolean {
    return holeOffset.x >= boundaryOffset.x &&
            holeOffset.y >= boundaryOffset.y &&
            holeOffset.x + holeSize.width <= boundaryOffset.x + boundarySize.width &&
            holeOffset.y + holeSize.height <= boundaryOffset.y + boundarySize.height &&
            holeSize.width > 2 * cornerRadius &&
            holeSize.height > 2 * cornerRadius
}

@Composable
fun animateSizeAsState(
    targetValue: Size,
    animationSpec: androidx.compose.animation.core.AnimationSpec<Float>
): State<Size> {
    val width by androidx.compose.animation.core.animateFloatAsState(
        targetValue = targetValue.width,
        animationSpec = animationSpec, label = ""
    )
    val height by androidx.compose.animation.core.animateFloatAsState(
        targetValue = targetValue.height,
        animationSpec = animationSpec, label = ""
    )
    return remember { derivedStateOf { Size(width, height) } }
}

@Preview(showBackground = true)
@Composable
fun CornerBoxPreview() {
    MaterialTheme {
        CornerBox()
    }
}
