package com.example.dictionary.presentation.navigation

sealed class Screen(
    val route: String
) {
    object ONBOARDING_ROUTE: Screen("onboarding")
    object HOME_ROUTE: Screen("home")
    object DEFINITION_DETAIL_ROUTE: Screen("definitionDetail")
    object RHYME_DETAIL_ROUTE: Screen("rhymeDetail")
    object SEARCH_SCREEN_ROUTE: Screen("searchScreen")

    fun withArgs(vararg args: String): String{
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
