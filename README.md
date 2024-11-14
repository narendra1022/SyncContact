# Contact Sync App

An Android application that syncs contacts from a Firebase Firestore database to the user's phone contacts using Android's `ContactsContract` API. This app allows users to sync contacts with a simple interface.

## Features

- **Sync Contacts**: Fetches contacts from Firestore and syncs them with the device's native contacts list.
- **Real-Time Sync Progress**: Displays a loading indicator while contacts are being synced.
- **Success Feedback**: Displays a success message when contacts are synced successfully.
- **Efficient and Optimized**: Uses modern Android development practices like Jetpack Compose, Kotlin coroutines, and Hilt for dependency injection.

## Tech Stack

- **Kotlin**: The primary language used for development.
- **Jetpack Compose**: A modern UI toolkit for building native UIs on Android.
- **Coroutines**: For asynchronous tasks and handling background operations.
- **Hilt**: A dependency injection library for Android, simplifying the injection of dependencies.
- **Firebase Firestore**: Used for storing and retrieving user contacts.
- **Android ContactsContract**: For syncing contacts with the native contacts provider.

## Architecture

This app follows the **MVVM (Model-View-ViewModel)** architecture pattern for better separation of concerns.

- **Model**: Represents the data layer. In this case, it includes the `Contact` model class and methods for fetching and syncing contacts with the phone's contact list.
- **View**: The UI layer implemented using Jetpack Compose. It displays the data and reacts to changes in the UI state.
- **ViewModel**: The bridge between the view and the model. It contains the logic for handling data, triggering sync, and managing UI states like loading and success.

## Main Screen

- A simple button labeled **"Sync Contacts"**.
- A progress indicator during sync.
- A success message **"Contacts Synced Successfully!"** once syncing is complete.
- A list of synced contacts is displayed below the success message.
