package com.example.navigation.navigation

import kotlinx.serialization.Serializable

interface AppRoute {

    @Serializable
    data object Home : AppRoute

    @Serializable
    object AddFlashCard : AppRoute

    @Serializable
    object FlashCardRevision : AppRoute
}
