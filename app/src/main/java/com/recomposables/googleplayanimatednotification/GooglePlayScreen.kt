package com.recomposables.googleplayanimatednotification

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.twotone.Mic
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

enum class State {
    UNDEFINED, GPLAYLOGO, SEARCHTEXT, NOTIFICATION, NOTIFICATIONTEXT
}

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun GooglePlayScreen() {

    var state by rememberSaveable { mutableStateOf(State.GPLAYLOGO) }
    var animateBell by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(800)
        state = State.UNDEFINED
        delay(800)
        state = State.SEARCHTEXT
        delay(2000)
        state = State.NOTIFICATION
        delay(1000)
        state = State.NOTIFICATIONTEXT
        animateBell = true
        delay(2000)
        state = State.NOTIFICATION
        delay(500)
        state = State.SEARCHTEXT
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Surface(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(16.dp), shape = RoundedCornerShape(24.dp),
                color = Color(0xFFECEEFF)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(imageVector = Icons.Outlined.Search, contentDescription = null)

                    GPlayLogo(state = state)

                    SearchText(state = state)

                    ProfileImage(state = state, animateBell = animateBell)

                }
            }
        },
        content = { _ ->

        }
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun GPlayLogo(state: State) {
    AnimatedVisibility(
        visible = state == State.GPLAYLOGO,
        enter = fadeIn(),
        exit = fadeOut(animationSpec = tween(durationMillis = 800))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            AnimatedVisibility(
                visible = state == State.GPLAYLOGO,
                exit = scaleOut(animationSpec = tween(650))
            ) {
                Image(
                    modifier = Modifier.size(28.dp),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "Google Play",
                fontWeight = FontWeight.Medium,
                fontSize = 17.sp,
                color = Color.DarkGray
            )
        }
    }
}
@Composable
fun SearchText(state: State) {
    AnimatedVisibility(
        modifier = Modifier.padding(end = 4.dp),
        visible = state == State.SEARCHTEXT,
        enter = fadeIn(animationSpec = tween(800)),
        exit = fadeOut()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Search for apps & ...",
                fontSize = 17.sp,
                letterSpacing = 1.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(imageVector = Icons.TwoTone.Mic, contentDescription = null)
        }
    }
}

@Composable
fun ProfileImage(state: State, animateBell: Boolean) {

    val density = LocalDensity.current

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bell))


    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        isPlaying = animateBell,
    )


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .wrapContentSize()
            .defaultMinSize(minHeight = 36.dp, minWidth = 36.dp)
            .padding(end = 8.dp)
    ) {
        AnimatedVisibility(
            visible = state == State.NOTIFICATION || state == State.NOTIFICATIONTEXT,
            enter = fadeIn(
                animationSpec = tween(500)
            ) + slideInHorizontally(
                initialOffsetX = {
                    with(density) {
                        this.density.dp.roundToPx()
                    }
                },
                animationSpec = tween(
                    durationMillis = 5000,
                    easing = LinearEasing
                ),
            ),
            exit = fadeOut(
            ) + slideOutHorizontally(
                targetOffsetX = {
                    with(density) {
                        this.density.dp.roundToPx()
                    }
                },
            ),
        ) {
            Surface(color = Color(0xFF008000), contentColor = Color.White, shape = CircleShape) {
                Row {
                    LottieAnimation(
                        composition = composition,
                        progress = progress,
                    )
                    AnimatedVisibility(
                        visible = state == State.NOTIFICATIONTEXT,
                        enter = fadeIn(
                        ) + slideInHorizontally(
                            initialOffsetX = {
                                with(density) {
                                    this.density.dp.roundToPx()
                                }
                            },
                        ),
                        exit = fadeOut() + slideOutHorizontally(
                            targetOffsetX = {
                                with(density) {
                                    this.density.dp.roundToPx()
                                }
                            },
                        )
                    ) {
                        Text(text = "100", fontSize = 16.sp, modifier = Modifier.padding(end = 4.dp))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
        Surface(shape = CircleShape) {
            Image(
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(36.dp),
                painter = painterResource(id = R.drawable.user_image),
                contentDescription = null
            )
        }
    }
}