package com.pamn.ggmatch.app.architecture.view.profiles

data class ProfileTextVariables(
    val title: String = "Edit profile",
    val backText: String = "Back",

    val selectGalleryPictureText: String = "Choose from gallery",
    val takePictureText: String = "Take a picture",
    val imageHint: String = "Upload pending (Storage not connected)",

    val usernameLabel: String = "Username",

    val riotTitle: String = "Verify Riot Account",
    val riotGameNameLabel: String = "Game name",
    val riotTagLabel: String = "Tag",
    val riotVerifyButtonText: String = "Verify profile",
    val riotVerifyingText: String = "Verifying...",

    val favoriteRoleTitle: String = "Favorite Role",
    val favoriteRoleHint: String = "Max 2 roles",
    val scheduleTitle: String = "Select schedule",
    val languageTitle: String = "Select language",
    val playstyleTitle: String = "Select playstyle",

    val saveButtonText: String = "Save changes",
    val savingText: String = "Saving...",

    val notAuthenticatedText: String = "You are not authenticated.",
    val invalidUsernameText: String = "Invalid username (3..20, letters/digits/_).",
    val invalidPreferencesText: String = "Invalid preferences.",
    val loadErrorText: String = "Couldn't load the profile.",
    val saveErrorText: String = "Couldn't save the profile.",
    val riotSavedText: String = "Riot verified and saved.",
    val profileSavedText: String = "Profile saved.",
)
