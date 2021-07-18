package com.example.dictionary.presentation.ui.onboarding

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.example.dictionary.R
import com.example.dictionary.presentation.navigation.Screen
import com.example.dictionary.presentation.theme.DictionaryTheme
import com.example.dictionary.presentation.theme.PinkTheme
import com.example.dictionary.util.TAG
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun Onboarding(
    darkTheme: Boolean,
    onboardingComplete: () -> Unit,
) {
    Log.d(TAG, "Onboarding: ##!!")
    PinkTheme {
        Scaffold(
            topBar = { AppBar() },
            backgroundColor = MaterialTheme.colors.primary,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        onboardingComplete()
                    },
                    modifier = Modifier
                        .navigationBarsPadding()
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Explore,
                        contentDescription = stringResource(R.string.label_continue)
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(innerPadding)
            ) {
                Text(
                    text = stringResource(R.string.label_enter_name),
                    style = MaterialTheme.typography.h1,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 32.dp
                    )
                )

                Spacer(Modifier.height(56.dp)) // center grid accounting for FAB
            }
        }
    }
}

@Composable
private fun AppBar() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier.padding(16.dp)
        )
    }
}


@Preview(name = "Onboarding")
@Composable
private fun OnboardingPreview() {
    Onboarding(darkTheme = false, onboardingComplete = { },
        //onNavigateToHomeScreen = {}
    )
}

