package com.dynamicdal.dictionary.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dynamicdal.dictionary.R
import java.util.*

@Composable
fun GreetingSection(
    // pick up from datastore / shared prefs
    userName: MutableState<String> = mutableStateOf("Human"),
    isNetworkAvailable: MutableState<Boolean>,
    isDarkTheme: MutableState<Boolean>,
    onToggleTheme: () -> Unit
) {
    val name = userName.value.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = if(isNetworkAvailable.value){ 48.dp } else { 0.dp },
                bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.8f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Greetings, ${name}",
                style = MaterialTheme.typography.h2.copy(color = MaterialTheme.colors.onPrimary),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = "We wish you have a good day!",
                style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onPrimary),
            )
        }
        // This icon can be used to change theme, or to navigate to settings screen, if we make one
        IconButton(
            onClick = { onToggleTheme() }
        ) {
            Icon(
                painter = if(isDarkTheme.value) painterResource(id = R.drawable.ic_dark_theme) else painterResource(id = R.drawable.ic_light_theme),
                contentDescription = "toggle theme",
                modifier = Modifier.size(24.dp),
            )
        }
    }
}