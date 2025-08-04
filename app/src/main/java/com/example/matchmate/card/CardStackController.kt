import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope

class CardStackController(
    val scope: CoroutineScope,
    private val screenWidth: Float,
    private val animationSpec: AnimationSpec<Float> = tween(durationMillis = 300, easing = FastOutSlowInEasing)
) {
    val offsetX = Animatable(0f)
    val offsetY = Animatable(0f)
    val rotation = Animatable(0f)

    var onSwipedLeft: () -> Unit = {}
    var onSwipedRight: () -> Unit = {}

    suspend fun swipeLeft() {
        offsetX.animateTo(-screenWidth, animationSpec)
        onSwipedLeft()
        reset()
    }

    suspend fun swipeRight() {
        offsetX.animateTo(screenWidth, animationSpec)
        onSwipedRight()
        reset()
    }

    suspend fun reset() {
        offsetX.snapTo(0f)
        offsetY.snapTo(0f)
        rotation.snapTo(0f)
    }
}

@Composable
fun rememberCardStackController(): CardStackController {
    val scope = rememberCoroutineScope()
    val screenWidth = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    return remember { CardStackController(scope, screenWidth) }
}