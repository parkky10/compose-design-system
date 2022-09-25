package com.jakchang.design_system.utils.extension

import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.PagerState

// 출처 : https://gist.github.com/dovahkiin98/85acb72ab0c4ddfc6b53413c955bcd10

fun Modifier.horizontalFadingEdge(
    length: Dp,
    edgeColor: Color? = null,
) = composed(
    debugInspectorInfo {
        name = "length"
        value = length
    }
) {
    val color = edgeColor ?: MaterialTheme.colors.surface

    drawWithContent {
        val lengthValue = length.toPx()

        drawContent()

        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    color,
                    Color.Transparent,
                ),
                startX = 0f,
                endX = lengthValue,
            ),
            size = Size(
                lengthValue,
                this.size.height,
            ),
        )

        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    color,
                ),
                startX = size.width - lengthValue,
                endX = size.width,
            ),
            topLeft = Offset(x = size.width - lengthValue, y = 0f),
        )
    }
}

fun Modifier.horizontalFadingEdge(
    pagerState: PagerState,
    length: Dp,
    offset: Dp = 0.dp,
    edgeColor: Color? = null
) = composed(

    debugInspectorInfo {
        name = "length"
        value = length
    }

) {
    val color = edgeColor ?: MaterialTheme.colors.surface
    val isFirstItemFullyVisible = remember {
        derivedStateOf {
            pagerState.currentPage == 0 && pagerState.currentPageOffset.toInt() == 0
        }
    }

    val isLastItemFullyVisible = remember {
        derivedStateOf {
            println("TAG : ${pagerState.currentPageOffset}")
            pagerState.currentPageOffset.toInt() == 0
        }
    }

    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    var isScrolledByUser by remember { mutableStateOf(false) }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.isScrollInProgress }.collect { isScrolling ->
            isScrolledByUser = isScrolling && isDragged
        }
    }
//    val isLastItemFullyVisible = remember {
//        derivedStateOf {
//            pagerState.layoutInfo
//                .visibleItemsInfo
//                .any { it.key == lastIndexKey }.let { _isLastIndexVisible ->
//                    if (_isLastIndexVisible) {
//                        val layoutInfo = pagerState.layoutInfo
//                        val lastItemInfo = layoutInfo.visibleItemsInfo.lastOrNull() ?: return@let false
//
//                        return@let lastItemInfo.size + lastItemInfo.offset == layoutInfo.viewportEndOffset
//                    } else {
//                        return@let false
//                    }
//                }
//        }
//    }

    val calculatedLength =
        if (isFirstItemFullyVisible.value || isLastItemFullyVisible.value) offset else length

    drawWithContent {
        val lengthValue = calculatedLength.toPx()

        drawContent()

        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    color,
                    Color.Transparent,
                ),
                startX = 0f,
                endX = lengthValue,
            ),
            size = Size(
                lengthValue,
                this.size.height,
            ),
        )

        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    color,
                ),
                startX = size.width - lengthValue,
                endX = size.width,
            ),
            topLeft = Offset(x = size.width - lengthValue, y = 0f),
        )
    }
}

fun Modifier.horizontalFadingEdge(
    listState: LazyListState,
    length: Dp,
    offset: Dp = 0.dp,
    edgeColor: Color? = null,
    lastIndexKey: Any
) = composed(

    debugInspectorInfo {
        name = "length"
        value = length
    }

) {
    val color = edgeColor ?: MaterialTheme.colors.surface
    val isFirstItemFullyVisible = remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
        }
    }

    val isLastItemFullyVisible = remember {
        derivedStateOf {
            listState.layoutInfo
                .visibleItemsInfo
                .any { it.key == lastIndexKey }.let { _isLastIndexVisible ->
                    if (_isLastIndexVisible) {
                        val layoutInfo = listState.layoutInfo
                        val lastItemInfo =
                            layoutInfo.visibleItemsInfo.lastOrNull() ?: return@let false

                        return@let lastItemInfo.size + lastItemInfo.offset == layoutInfo.viewportEndOffset
                    } else {
                        return@let false
                    }
                }
        }
    }

    val calculatedLength =
        if (isFirstItemFullyVisible.value || isLastItemFullyVisible.value) offset else length

    drawWithContent {
        val lengthValue = calculatedLength.toPx()

        drawContent()

        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    color,
                    Color.Transparent,
                ),
                startX = 0f,
                endX = lengthValue,
            ),
            size = Size(
                lengthValue,
                this.size.height,
            ),
        )

        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    color,
                ),
                startX = size.width - lengthValue,
                endX = size.width,
            ),
            topLeft = Offset(x = size.width - lengthValue, y = 0f),
        )
    }
}