package com.example.matchmate.card

import CardStackController
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch

fun Modifier.draggableCard(controller: CardStackController): Modifier = pointerInput(Unit) {
    detectDragGestures(
        onDrag = { change, dragAmount ->
            controller.scope.launch {
                controller.offsetX.snapTo(controller.offsetX.value + dragAmount.x)
                controller.offsetY.snapTo(controller.offsetY.value + dragAmount.y)
                controller.rotation.snapTo((controller.offsetX.value / 60).coerceIn(-40f, 40f))
            }
            change.consume()
        },
        onDragEnd = {
            controller.scope.launch {
                when {
                    controller.offsetX.value > 300f -> controller.swipeRight()
                    controller.offsetX.value < -300f -> controller.swipeLeft()
                    else -> controller.reset()
                }
            }
        }
    )
}