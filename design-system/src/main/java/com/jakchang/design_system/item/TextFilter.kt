package com.jakchang.design_system.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList

/**
 * Created by leecg@brandi.co.kr on 2022/07/05
 *
 * @param modifier : TextFilter Row 에 대한 modifier
 * @param textFilterList : TextFilter에 들어가는 Data List
 * @param filterTextGetter : TextFilter별 이름을 설정하는 함수
 * @param onTextFilterClick : TextFilter Click했을 때 이벤트 처리
 * @param textStyle : TextFilter의 textStyle
 * @param selectedFilterData : 현재 선택된 TextFilter
 * @param leftContent : TextFilter 왼쪽에 붙는 Composable 컨텐츠
 */

@Composable
fun <T> TextFilter(
    modifier: Modifier? = null,
    filterBoxModifier: Modifier? = null,
    filterTextModifier: Modifier? = null,
    textFilterList: ImmutableList<T>,
    filterTextGetter: (Int, T) -> String,
    onTextFilterClick: (T) -> Unit,
    textStyle: TextStyle = LocalTextStyle.current,
    selectedFilterData: T,
    borderWidth: Int = 1,
    textFilterColor: TextFilterColor = TextFilterColor(),
    textFilterBorder: TextFilterBorder = TextFilterBorder()
) {
    val filterItemSize = textFilterList.size

    Row(
        modifier = modifier ?: Modifier,
        horizontalArrangement = Arrangement.End,
    ) {
        textFilterList.forEachIndexed { _index, _filterData ->
            val backgroundColor =
                if (_filterData == selectedFilterData) textFilterColor.selectedBackgroundColor else textFilterColor.unSelectedBackgroundColor
            val textColor =
                if (_filterData == selectedFilterData) textFilterColor.selectedTextColor else textFilterColor.unSelectedTextColor
            val borderColor =
                if (_filterData == selectedFilterData) textFilterColor.selectedBorderColor else textFilterColor.unSelectedBorderColor

            val borderShape = when (_index) {
                0 -> textFilterBorder.firstItemBorder
                filterItemSize - 1 -> textFilterBorder.lastItemBorder
                else -> textFilterBorder.remainItemBorder
            }

            Box(
                modifier = filterBoxModifier ?: Modifier
                    .border(width = borderWidth.dp, color = borderColor, shape = borderShape)
                    .background(color = backgroundColor, shape = borderShape)
                    .wrapContentHeight()
                    .clickable {
                        onTextFilterClick.invoke(_filterData)
                    }) {

                Text(
                    modifier = filterTextModifier ?: Modifier.padding(
                        vertical = 6.dp,
                        horizontal = 8.dp
                    ),
                    text = filterTextGetter.invoke(_index, _filterData),
                    style = textStyle.copy(
                        color = textColor
                    )
                )
            }
        }
    }
}

data class TextFilterColor(
    val selectedBackgroundColor: Color = Color(0xff5F6773),
    val unSelectedBackgroundColor: Color = Color.White,
    val selectedTextColor: Color = Color.White,
    val unSelectedTextColor: Color = Color(0xFF808893),
    val selectedBorderColor: Color = Color(0xFF5F6773),
    val unSelectedBorderColor: Color = Color(0xFFEDEEF3),
)

data class TextFilterBorder(
    val firstItemBorder: Shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp),
    val lastItemBorder: Shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp),
    val remainItemBorder: Shape = RectangleShape
)