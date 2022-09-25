package com.jakchang.design_system.item

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.jakchang.design_system.utils.extension.getDpToPx
import com.jakchang.design_system.utils.extension.getPxToDp
import com.jakchang.design_system.utils.extension.horizontalFadingEdge
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by leecg@brandi.co.kr on 2022/08/17
 *
 * @param pagerState
 * @param coroutineScope
 * @param tabSize :
 * @param tabRowPadding : 탭 Row 패딩 설정
 * @param tabPadding : 탭들 사이의 패딩 설정
 * @param tabTitleGetter : tabTitle을 가져오는 함수
 * @param tabContent : tabContent를 설정하는 Composable 함수, null일시 default 적용
 * @param tabContentPadding : 탭 내부의 Content에 대한 padding 설정
 * @param tabRowHeight : 탭 Row의 Height을 설정
 * @param tabRowWidth : 탭 Row의 Width을 설정
 * @param fadingEdgeLength : 양옆 fadingEdge 길이 설정
 * @param fadingEdgeColor : 양옆 fadingEdge 색상 설정
 * @param dividerColor : 하단 divider 색상 설정
 * @param dividerHeight : 하단 divider 높이 설정
 * @param tabBackgroundColor : Tab BackgroundColor 설정
 * @param tabContentColor : Tab ContentColor 설정
 * @param indicatorHeight : Tab Indicator 높이 설정
 * @param indicatorColor : Tab Indicator 색상 설정
 * @param indicatorPadding : indicatorPadding 값
 * @param isScrollableTab : 탭의 스크롤 여부 설정
 * @param tabTextColor : selected, unSelected 됐을 때 text 설정 값
 * @param onTabClick : TabClick 이벤트
 *
 */

@Composable
fun SmartTabLayout(
    pagerState: PagerState,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    tabSize: Int,
    tabRowPadding: PaddingValues = PaddingValues(horizontal = 8.dp),
    tabPadding: PaddingValues = PaddingValues(horizontal = 8.dp),
    tabTitleGetter: (Int) -> String,
    tabContent: ((Int) -> (@Composable ColumnScope.() -> Unit))? = null,
    tabContentPadding: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 14.5.dp),
    tabRowHeight: Int = 48,
    tabRowWidth: Int = 0,
    fadingEdgeLength: Int = 28,
    fadingEdgeColor: Color = Color.White,
    dividerColor: Color = Color(0xFFF2F4F7),
    dividerHeight: Int = 1,
    tabBackgroundColor: Color = Color.White,
    tabContentColor: Color = Color.Black,
    indicatorHeight: Int = 2,
    indicatorColor: Color = Color.Black,
    indicatorPadding: PaddingValues = PaddingValues(horizontal = 1.dp),
    isScrollableTab: Boolean = true,
    tabTextColor: TabTextColor = TabTextColor(),
    onTabClick: ((Int) -> Unit)? = null,
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val displayWidth = context.getDpToPx(configuration.screenWidthDp.toFloat())

    val totalTabWidth = remember {
        mutableStateOf(0f)
    }
    val isDisplayOverScroll = remember {
        mutableStateOf(true)
    }
    val isUpdateTabWidth = remember {
        mutableStateOf(false)
    }
    val tabWidthArray = remember {
        Array(tabSize) { 0f }
    }

    if (isScrollableTab) {
        Surface(elevation = 0.dp) {
            ScrollableTabRow(
                modifier = Modifier
                    .let {
                        if (tabRowWidth != 0) it.width(tabRowWidth.dp)
                        else it.fillMaxWidth()
                    }
                    .height(tabRowHeight.dp)
                    .padding(tabRowPadding)
                    .horizontalFadingEdge(
                        pagerState = pagerState,
                        length = fadingEdgeLength.dp,
                        edgeColor = fadingEdgeColor
                    ),
                edgePadding = 0.dp,
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = tabBackgroundColor,
                contentColor = tabContentColor,
                indicator = { _tabPositions ->
                    if (_tabPositions.isNotEmpty()) {
                        TabRowDefaults.Indicator(
                            height = indicatorHeight.dp,
                            modifier = Modifier
                                .pagerTabIndicatorOffset(pagerState, _tabPositions)
                                .padding(indicatorPadding),
                            color = indicatorColor
                        )
                    }
                },
                divider = {}
            ) {
                for (index in 0 until tabSize) {
                    Tab(
                        modifier = Modifier
                            .onGloballyPositioned {
                                val itemPosition = it.boundsInWindow()

                                // 각 탭들의 넓이 합을 구한다
                                totalTabWidth.value += itemPosition.bottomRight.x - itemPosition.bottomLeft.x

                                // 초기화가 안됐을때만 수행, 초기화가 됐을때도 수행하면 리컴퍼지션시 넓이가 계속 넓어진다.
                                if (tabWidthArray[index].toInt() == 0) {
                                    tabWidthArray[index] =
                                        context.getPxToDp((itemPosition.bottomRight.x - itemPosition.bottomLeft.x))
                                }

                                // Tab의 마지막 인덱스를 가리킬때만 실행
                                if (index == tabSize - 1) {
                                    val rowStartPadding: Float =
                                        context.getDpToPx(
                                            tabRowPadding.calculateStartPadding(
                                                LayoutDirection.Ltr
                                            ).value
                                        )
                                    val rowEndPadding: Float =
                                        context.getDpToPx(
                                            tabRowPadding.calculateEndPadding(
                                                LayoutDirection.Ltr
                                            ).value
                                        )
                                    val tabStartPadding: Float =
                                        tabPadding.calculateStartPadding(
                                            LayoutDirection.Ltr
                                        ).value
                                    val tabEndPadding: Float =
                                        tabPadding.calculateEndPadding(
                                            LayoutDirection.Ltr
                                        ).value

                                    val pagerScreenListSize: Float =
                                        tabSize.toFloat()

                                    // rowPadding과 탭 넓이의 합
                                    val totalTabWidthWithOffset: Float =
                                        totalTabWidth.value + rowStartPadding + rowEndPadding

                                    // 화면 - 탭 넓이 의 결과로 남은 여백을 N 등분하여 나타냄
                                    val extraWidthPadding: Float =
                                        (displayWidth - totalTabWidthWithOffset) / pagerScreenListSize

                                    // 탭과 외부 패딩 설정값을 고려했을때 실제 Width
                                    val calculatedTabWidth: Float =
                                        it.boundsInParent().bottomRight.x + ((tabStartPadding + tabEndPadding) * pagerScreenListSize)

                                    var calculatedPadding = PaddingValues()

                                    // 위의 계산된 탭 넓이가 전체 화면보다 작을때
                                    if (calculatedTabWidth < displayWidth) {
                                        val calculatedHorizontalPadding =
                                            context.getPxToDp(extraWidthPadding) / 2
                                        calculatedPadding =
                                            PaddingValues(horizontal = calculatedHorizontalPadding.dp)

                                        isDisplayOverScroll.value = false
                                    } else {
                                        calculatedPadding = tabPadding
                                    }

                                    // 탭 넓이의 업데이트가 한번도 수행되지 않고 Scroll이 화면을 넘어가지 않을때 실행
                                    if (isUpdateTabWidth.value.not() && isDisplayOverScroll.value.not()) {
                                        tabWidthArray.forEachIndexed { _index, _width ->
                                            val startPadding: Float =
                                                calculatedPadding.calculateStartPadding(
                                                    LayoutDirection.Ltr
                                                ).value

                                            val endPadding: Float =
                                                calculatedPadding.calculateEndPadding(
                                                    LayoutDirection.Ltr
                                                ).value

                                            tabWidthArray[_index] =
                                                _width + startPadding + endPadding
                                        }
                                        // 탭 넓이를 업데이트
                                        isUpdateTabWidth.value = true
                                    }
                                }
                            }
                            .let { _modifier ->
                                // TabWidth 업데이트 일때 화면 스크롤이 안넘어가는 경우 계산된 넓이를 반영
                                if (isUpdateTabWidth.value && isDisplayOverScroll.value.not()) {
                                    _modifier.width(tabWidthArray[index].dp)
                                } else {
                                    _modifier
                                }
                            }.let { _modifier ->
                                if (isDisplayOverScroll.value) {
                                    _modifier.padding(tabPadding)
                                } else {
                                    _modifier
                                }
                            },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                                onTabClick?.invoke(index)
                            }
                        },
                        content = tabContent?.invoke(index) ?: {
                            TabText(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(tabContentPadding),
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal,
                                fontColor = if (pagerState.currentPage == index) tabTextColor.selectedColor else tabTextColor.unSelectedColor,
                                text = tabTitleGetter.invoke(index)
                            )
                        }
                    )
                }
            }
        }
    } else {
        Surface(elevation = 0.dp) {
            TabRow(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(tabRowHeight.dp)
                    .padding(tabRowPadding)
                    .horizontalFadingEdge(fadingEdgeLength.dp, fadingEdgeColor),
                selectedTabIndex = pagerState.currentPage,
                backgroundColor = tabBackgroundColor,
                contentColor = tabContentColor,
                indicator = { _tabPositions ->
                    if (_tabPositions.isNotEmpty()) {
                        TabRowDefaults.Indicator(
                            height = indicatorHeight.dp,
                            modifier = Modifier
                                .pagerTabIndicatorOffset(pagerState, _tabPositions)
                                .padding(indicatorPadding),
                            color = indicatorColor
                        )
                    }
                }
            ) {
                for (index in 0 until tabSize) {
                    Tab(
                        modifier = Modifier.padding(tabPadding),
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                                onTabClick?.invoke(index)
                            }
                        },
                        content = tabContent?.invoke(index) ?: {
                            TabText(
                                modifier = Modifier
                                    .padding(tabContentPadding)
                                    .fillMaxHeight()
                                    .wrapContentWidth(),
                                text = tabTitleGetter.invoke(index),
                                fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal,
                                fontColor = if (pagerState.currentPage == index) tabTextColor.selectedColor else tabTextColor.unSelectedColor,
                            )
                        }
                    )
                }
            }
        }
    }

    Divider(
        color = dividerColor,
        modifier = Modifier
            .fillMaxWidth()
            .height(dividerHeight.dp)
    )

}

data class TabTextColor(
    val selectedColor: Color = Color.Black,
    val unSelectedColor: Color = Color.LightGray
)


/*
* Width 계산의 타이밍 이슈때문에 style=  로 넣지않고 개별 property를 직접 넣어줌
* */
@Composable
fun TabText(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight,
    fontColor: Color,
    textAlign: TextAlign = TextAlign.Center,
    text: String,
    verticalArrangement: Arrangement.HorizontalOrVertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            color = fontColor,
            fontWeight = fontWeight,
            textAlign = textAlign
        )
    }
}
