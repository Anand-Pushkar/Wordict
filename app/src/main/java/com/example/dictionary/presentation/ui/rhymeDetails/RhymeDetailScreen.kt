package com.example.dictionary.presentation.ui.rhymeDetails

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.example.dictionary.R
import com.example.dictionary.domain.model.rhyme.Rhyme
import com.example.dictionary.domain.model.rhyme.Rhymes
import com.example.dictionary.presentation.components.LoadingListShimmer
import com.example.dictionary.presentation.components.NothingHere
import com.example.dictionary.presentation.components.SearchAppBar
import com.example.dictionary.presentation.navigation.Screen
import com.example.dictionary.presentation.theme.YellowTheme
import com.example.dictionary.presentation.theme.immersive_sys_ui
import com.example.dictionary.util.RHYME
import com.example.dictionary.util.TAG
import java.util.*


@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalStdlibApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun RhymeDetailScreen(
    isDark: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    viewModel: RhymeDetailViewModel,
    onNavigateToSearchScreen: (String) -> Unit,
    onNavigationToDefinitionDetailScreen: (String) -> Unit,
    query: String
) {

    if (query.isEmpty()) {
        Log.d(TAG, "RhymeDetailScreen: query is empty")
        // show invalid search or something like that
    } else {
        // fire a one-off event to get the rhymes from api
        val onLoad = viewModel.onLoad.value
        if (!onLoad) {
            viewModel.onLoad.value = true
            viewModel.onTriggerEvent(RhymeDetailScreenEvent.GetRhymesEvent(query))
        }

        val rhymes = viewModel.rhymes.value
        val loading = viewModel.loading.value
        val scaffoldState = rememberScaffoldState()
        val dialogQueue = viewModel.dialogQueue


        YellowTheme(
            darkTheme = isDark,
            isNetworkAvailable = isNetworkAvailable,
            scaffoldState = scaffoldState,
            dialogQueue = dialogQueue.queue.value,
            displayProgressBar = loading,
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                scaffoldState = scaffoldState,
                backgroundColor = MaterialTheme.colors.primary,
                snackbarHost = {
                    scaffoldState.snackbarHostState
                },
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    BgCard(
                        isDark = isDark,
                        loading = loading,
                        onLoad = onLoad,
                        onNavigateToSearchScreen = onNavigateToSearchScreen,
                        rhymes = rhymes,
                        addToFavorites = {
                            viewModel.onTriggerEvent(RhymeDetailScreenEvent.AddToFavoritesEvent(scaffoldState))
                        },
                        removeFromFavorites = {
                            viewModel.onTriggerEvent(RhymeDetailScreenEvent.RemoveFromFavoritesEvent(scaffoldState ))
                        }
                    )
                    MainCard(
                        loading = loading,
                        onLoad = onLoad,
                        rhymes = rhymes,
                        onNavigationToDefinitionDetailScreen = onNavigationToDefinitionDetailScreen
                    )
                }
            }
        }
    }
}

@ExperimentalStdlibApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun BgCard(
    isDark: MutableState<Boolean>,
    loading: Boolean,
    onLoad: Boolean,
    onNavigateToSearchScreen: (String) -> Unit,
    rhymes: Rhymes?,
    addToFavorites: () -> Unit,
    removeFromFavorites: () -> Unit
) {

    Surface(
        color = if(isDark.value){ immersive_sys_ui } else { MaterialTheme.colors.surface },
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp)
        ) {
            SearchAppBar(
                onNavigateToSearchScreen = onNavigateToSearchScreen,
                route = Screen.SEARCH_SCREEN_ROUTE.withArgs(RHYME)
            )

            if (loading && rhymes == null) {
                LoadingListShimmer(
                    cardHeight = 35.dp,
                    cardWidth = 0.5f,
                    lines = 0,
                    cardPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 12.dp),
                )
            } else if (!loading && rhymes == null && onLoad) {
                // invalid search
                Text(
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 20.dp),
                    text = "Invalid Search!",
                    style = MaterialTheme.typography.h1.copy(color = MaterialTheme.colors.onPrimary),
                )
            } else rhymes?.let { rhymes ->

                if (!rhymes.rhymeList.isNullOrEmpty()) {

                    Text(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 20.dp),
                        text = rhymes.mainWord.replaceFirstChar {
                            if (it.isLowerCase())
                                it.titlecase(Locale.getDefault())
                            else
                                it.toString()
                        },
                        style = MaterialTheme.typography.h1.copy(color = MaterialTheme.colors.onPrimary),
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 24.dp)
                    ) {
                        Text(
                            text = "",
                            style = MaterialTheme.typography.h5.copy(color = MaterialTheme.colors.onPrimary)
                        )

                        val resource: Painter = if (rhymes.isFavorite) {
                            painterResource(id = R.drawable.ic_star_red)
                        } else {
                            painterResource(
                                id = if (isDark.value) {
                                    R.drawable.ic_star_white_border
                                } else {
                                    R.drawable.ic_star_black_border
                                }
                            )
                        }
                        Image(
                            modifier = Modifier
                                .width(32.dp)
                                .height(32.dp)
                                .clickable(
                                    onClick = {
                                        if(!rhymes.isFavorite){
                                            addToFavorites()
                                        }else{
                                            removeFromFavorites()
                                        }
                                    },
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ),
                            painter = resource,
                            contentDescription = "Favorite"
                        )
                    }
                } else {
                    // invalid search
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 20.dp),
                        text = "Invalid Search!",
                        style = MaterialTheme.typography.h1.copy(color = MaterialTheme.colors.onPrimary),
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalStdlibApi
@Composable
fun MainCard(
    loading: Boolean,
    onLoad: Boolean,
    rhymes: Rhymes?,
    onNavigationToDefinitionDetailScreen: (String) -> Unit,
) {

    Log.d(TAG, "MainCard: ${rhymes?.syllableInfo}")
    Surface(
        color = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 260.dp),
        shape = RoundedCornerShape(40.dp)
            .copy(bottomStart = ZeroCornerSize, bottomEnd = ZeroCornerSize),
        elevation = 16.dp,
    ) {

        if (loading && rhymes == null) {
            LoadingListShimmer(
                cardHeight = 30.dp,
                cardWidth = 0.6f,
                lineHeight = 24.dp,
                lines = 3,
                repetition = 3,
                linePadding = PaddingValues(start = 32.dp, end = 8.dp)
            )
        } else if (!loading && rhymes == null && onLoad) {
            NothingHere()
        } else rhymes?.let { rhymes ->

            if (!rhymes.rhymesMap.isNullOrEmpty()) {

                rhymes.rhymesMap?.let { map ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 48.dp, top = 8.dp)
                    ) {
                        map.forEach { (numSyllable, rhymes) ->
                            item {
                                Section(
                                    type = "$numSyllable syllable",
                                    rhymes = rhymes,
                                    onNavigationToDefinitionDetailScreen = onNavigationToDefinitionDetailScreen
                                )
                            }
                        }
                    }
                }
            } else {
                NothingHere()
            }
        }
    }
}

@Composable
fun Section(
    type: String,
    rhymes: List<Rhyme>,
    onNavigationToDefinitionDetailScreen: (String) -> Unit,
) {
    Log.d(TAG, "Section: list size = ${rhymes.size}")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 8.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = type,
            style = MaterialTheme.typography.h2.copy(
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colors.onPrimary
            )
        )

        var index = 0
        rhymes.forEach { rhyme ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 24.dp, top = 6.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(end = 12.dp),
                        text = "${index + 1}",
                        style = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onPrimary)
                    )
                    Text(
                        text = "${rhyme.word}.",
                        style = MaterialTheme.typography.h3.copy(color = MaterialTheme.colors.onPrimary)
                    )
                }
                IconButton(
                    onClick = {
                        val route = Screen.DEFINITION_DETAIL_ROUTE.withArgs(rhyme.word)
                        onNavigationToDefinitionDetailScreen(route)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = "Clear Search Icon",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
            index += 1
        }
    }
}