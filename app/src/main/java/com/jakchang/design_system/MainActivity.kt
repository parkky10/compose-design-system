package com.jakchang.design_system

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.jakchang.design_system.item.SmartTabLayout
import com.jakchang.design_system.ui.theme.JakchangcomposedesignsystemTheme
import com.jakchang.design_system.utils.reflection.hackMinTabWidth
import com.jakchang.design_system.utils.theme.ClearRippleTheme

class MainActivity : ComponentActivity() {
    val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hackMinTabWidth()
        setContent {
            JakchangcomposedesignsystemTheme {
                CompositionLocalProvider(
                    LocalRippleTheme provides ClearRippleTheme
                ) {
                    val pagerState: PagerState = rememberPagerState()

                    Column() {
                        SmartTabLayout(
                            pagerState = pagerState,
                            tabSize = 10,
                            tabTitleGetter = { _index ->
                                val sb = StringBuilder()
                                for(i in 0 until _index+1){
                                    sb.append(i)
                                }
                                "${sb.toString()}"
                            }
                        )

                        HorizontalPager(count = 10, state = pagerState) {
                            Text(modifier = Modifier.fillMaxSize(),text = "11111")
                        }
                    }
                }
            }
        }
    }
}