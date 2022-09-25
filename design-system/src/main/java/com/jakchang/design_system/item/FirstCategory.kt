//package com.jakchang.design_system.item
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.onSizeChanged
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import com.jakchang.design_system.util.extension.getDpToPx
//import com.jakchang.design_system.util.extension.getPxToDp
//import com.jakchang.design_system.util.extension.horizontalFadingEdge
//import kotlinx.collections.immutable.ImmutableList
//import kotlinx.coroutines.launch
//import kr.co.brandi.brandi_app.app.util.DisplayUtils
//import kr.co.brandi.design_system.compose.color.AppColors
//import kr.co.brandi.design_system.compose.typo.bdsTypography
//import kr.co.brandi.design_system.compose.widget.BdsText
//import kr.co.core_engine.core.extension.horizontalFadingEdge
//import kr.co.core_engine.core.extension.onClick
//
///**
// * Created by leecg@brandi.co.kr on 2022/08/22
// * @param modifier
// * @param firstCategoryList : 카테고리 데이터 클래스 리스트
// * @param onClickListener : 카테고리 클릭시 처리 이벤트
// * @param getSelectedState : selected된 상태여부를 판단하는 함수, 데이터클래스 selected설정 로직이 달라 따로 받는다.
// * @param categoryNameList : 카테고리 이름을 보여주기 위한 리스트, 데이터클래스 마다 category 이름 설정 로직이 달라 따로 받는다.
// * @param isUseAllText : 카테고리 0번째 인덱스가 전체인지 아닌지 판단하는 플래그
// * @param centerToScrollTime : centerScroll 시간을 key값으로 하여 가운데 이동 Effect 호출, 외부에서 State로 넘겨서 시간을 업데이트하면 가운데 이동
// *
// */
//
//@Composable
//fun <T> FirstCategory(
//    modifier: Modifier? = null,
//    firstCategoryList: ImmutableList<T>,
//    onClickListener: (T) -> Unit,
//    getSelectedState: (T) -> Boolean,
//    screenWidth: Int,
//    centerToScrollTime: Long
//) {
//    val listState = rememberLazyListState()
//    val coroutineScope = rememberCoroutineScope()
//    var prevCenterToScrollTime by rememberSaveable { mutableStateOf(0L) }
//    val itemSideMargin = 32f
//    var selectedIndex = 0
//    val itemWidthArray = Array<Int>(firstCategoryList.size) { 0 }
//    val context = LocalContext.current
//
//    LazyRow(
//        modifier = modifier ?: Modifier
//            .background(color = Color.White)
//            .fillMaxWidth()
//            .wrapContentHeight()
//            .padding(horizontal = 16.dp)
//            .horizontalFadingEdge(
//                listState = listState,
//                length = 28.dp,
//                edgeColor = Color.White,
//                lastIndexKey = "${firstCategoryList.size - 1}"
//            ),
//        verticalAlignment = Alignment.CenterVertically,
//        state = listState,
//    ) {
//
//        firstCategoryList.forEachIndexed { _index, _firstCategory ->
//            val isSelected = getSelectedState.invoke(_firstCategory)
//            if (isSelected) {
//                selectedIndex = _index
//            }
//
//            item("$_index") {
//                FirstCategoryItem(
//                    modifier = Modifier.onSizeChanged { _categoryItemSize ->
//                        itemWidthArray[_index] = _categoryItemSize.width
//                    },
//                    text = mutableCategoryNameList[_index],
//                    isSelect = isSelected,
//                    onClick = {
//                        onClickListener.invoke(_firstCategory)
//                        selectedIndex = _index
//                    },
//                    isLastIndex = (_index == firstCategoryList.size - 1)
//                )
//            }
//        }
//
//    }
//
//    if (prevCenterToScrollTime != centerToScrollTime) {
//        LaunchedEffect(centerToScrollTime) {
//            prevCenterToScrollTime = centerToScrollTime
//
//            coroutineScope.launch {
//                val scrollOffset =
//                    (screenWidth - itemWidthArray[selectedIndex].toFloat() - context.getDpToPx(
//                        itemSideMargin
//                    )) / 2
//
//                listState.animateScrollToItem(
//                    scrollOffset = - context.getPxToDp(scrollOffset),
//                    index = selectedIndex
//                )
//            }
//        }
//    }
//}
//
///**
// * 1차 카테고리 아이템
// */
//@Composable
//private fun FirstCategoryItem(
//    modifier: Modifier,
//    text: String,
//    isSelect: Boolean,
//    onClick: () -> Unit,
//    isLastIndex: Boolean
//) {
//    Row(
//        modifier = modifier
//            .padding(start = 0.dp, top = 8.dp, end = if (isLastIndex) 0.dp else 8.dp, bottom = 8.dp)
//            .height(32.dp)
//            .border(
//                width = 1.dp,
//                color = if (isSelect) AppColors.Primary100 else AppColors.Gray20,
//                shape = RoundedCornerShape(9999.dp)
//            )
//            .background(
//                color = if (isSelect) AppColors.Primary100 else AppColors.Gray10,
//                shape = RoundedCornerShape(9999.dp)
//            )
//            .clickable {
//                onClick.invoke()
//            },
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Text(
//            text = text,
//            style = bdsTypography.subtitle1.copy(
//                fontWeight = if (isSelect) FontWeight.Bold else FontWeight.Normal,
//                color = if (isSelect) AppColors.White else AppColors.Gray60,
//            ),
//            textAlign = TextAlign.Center,
//            modifier = Modifier
//                .wrapContentHeight()
//                .padding(start = 12.dp, end = 12.dp, top = 1.dp, bottom = 0.dp)
//        )
//    }
//}