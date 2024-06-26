package com.dynamicdal.dictionary.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

// screenshot of the shimmer

@Composable
fun ShimmerCardItem(
    colors: List<Color>,
    cardHeight: Dp,
    cardWidth: Float,
    cardPadding: PaddingValues,
    lineHeight: Dp = cardHeight / 10,
    xShimmer: Float,
    yShimmer: Float,
    padding: Dp,
    gradientWidth: Float,
    lines: Int,
    linePadding: PaddingValues,
    lineWidth: Float,
    shape: Shape,
    lineSpace: Dp,
){

    val brush = Brush.linearGradient(
        colors,
        start = Offset(xShimmer - gradientWidth, yShimmer - gradientWidth),
        end = Offset(xShimmer, yShimmer)
    )

    Column(
        modifier = Modifier.padding(padding)
    ) {
        // card
        Surface(
            shape = shape,
            modifier = Modifier
                .fillMaxWidth(cardWidth)
                .padding(cardPadding)
        ) {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight)
                    .background(brush = brush)
            )
        }
        // lines
        repeat(lines){
            Spacer(modifier = Modifier.padding(top = lineSpace))
            Surface(
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.padding(linePadding)
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth(lineWidth)
                        .height(lineHeight)
                        .background(brush = brush)
                )
            }
        }
    }

}