package com.jakchang.design_system.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay

/**
 * 해당 슬라이더는 Infinite Scroll 입니다.
 *
 * @param bannerType 배너타입
 * @param pagerState PagerState
 * @param banners 배너 데이터 리스트
 * @param delayMillis autoScroll delay Milliseconds
 * @param bannerClickListener 배너 클릭 리스너
 * @param goToBannerEvent GoToBannerEvent
 * @param bannerHeight 배너 높이 설정
 *
 * */
@Composable
fun <T> BannerSlider(
    modifier: Modifier? = null,
    banners: ImmutableList<T>,
    pagerState: PagerState = rememberPagerState(getInitialPager(banners.size)),
    onPagerStateChanged: ((Int, T) -> Unit)? = null,
    delayMillis: Long = 2000,
    bannerClickListener: ((Int, T) -> Unit)? = null,
    bannerImageGetter: (T) -> String,
    bannerHeight: Int = 0,
    isIndicatorVisible: Boolean = true,
    bannerIndicator: (@Composable (T) -> Unit)? = null,
    scrollEnabled: Boolean = true
) {
    val isScrollEnabled = banners.size > 1

    val pagerCurrentPage = remember {
        derivedStateOf { pagerState.currentPage }
    }

    if (isScrollEnabled) {
        LaunchedEffect(true) {
            while (scrollEnabled) {
                delay(delayMillis)
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }

        LaunchedEffect(pagerCurrentPage.value) {
            val currentPageIndex = (pagerState.currentPage % banners.size)
            onPagerStateChanged?.invoke(currentPageIndex, banners[currentPageIndex])
        }
    }

    Box(
        modifier = modifier ?: Modifier
            .fillMaxWidth()
            .height(bannerHeight.dp)
    ) {
        HorizontalPager(
            count = if (isScrollEnabled) Integer.MAX_VALUE else 1,
            state = pagerState,
            userScrollEnabled = isScrollEnabled
        ) { _currentIndex ->
            val currentPageIndex = _currentIndex % banners.size

            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        bannerClickListener?.invoke(currentPageIndex, banners[currentPageIndex])
                    },
                model = bannerImageGetter.invoke(banners[currentPageIndex]),
                contentScale = ContentScale.Crop,
                contentDescription = ""
            )
        }

        if (isIndicatorVisible) {
            val currentPagerIndex = (pagerState.currentPage % banners.size)
            val currentPageValue = currentPagerIndex + 1

            bannerIndicator?.let {
                bannerIndicator(banners[currentPagerIndex])
            } ?: also {
                DefaultBannerIndicator(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0x66000000))
                        .padding(top = 6.dp, bottom = 6.dp, start = 14.dp, end = 14.dp),
                    textStyle = TextStyle(
                        color = Color.White
                    ),
                    currentPageValue = currentPageValue,
                    totalCount = banners.size
                )
            }
        }
    }
}

private fun getInitialPager(bannerListSize: Int): Int {
    val middleIndex = (Integer.MAX_VALUE / 2)
    val rest = middleIndex % bannerListSize
    return (middleIndex - rest)
}

@Composable
fun DefaultBannerIndicator(
    modifier: Modifier,
    textStyle: TextStyle,
    currentPageValue: Int,
    totalCount: Int
) {
    Text(text = "$currentPageValue / $totalCount", modifier = modifier, style = textStyle)
}