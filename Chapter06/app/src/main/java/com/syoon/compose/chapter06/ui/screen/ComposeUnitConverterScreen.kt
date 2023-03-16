package com.syoon.compose.chapter06.ui.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.syoon.compose.chapter06.R

// sead class로 정의
sealed class ComposeUnitConverterScreen(
    val route: String, // 화면을 식별하는 고유값
    @StringRes val label: Int,
    @DrawableRes val icon: Int
) {
    companion object {
        val screens = listOf(
            Temperature,
            Distances
        )

        // 두 개의 화면으로 이루어짐
        const val route_temperature = "temperature"
        const val route_distances = "distances"
    }

    private object Temperature : ComposeUnitConverterScreen(
        route_temperature,
        R.string.temperature,
        R.drawable.baseline_thermostat_24
    )

    private object Distances : ComposeUnitConverterScreen(
        route_distances,
        R.string.distances,
        R.drawable.baseline_square_foot_24
    )
}