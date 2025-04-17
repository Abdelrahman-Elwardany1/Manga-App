# Manga Reader App 📖

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.4-brightgreen)](https://developer.android.com/jetpack/compose)
[![Clean Architecture](https://img.shields.io/badge/Clean%20Architecture-✓-success)](https://developer.android.com/topic/architecture)

An Android app for discovering and reading manga, powered by the **MangaDex API**. Built with modern Android practices, including Jetpack Compose, Clean Architecture, and real-time synchronization.

<div style="display: flex; justify-content: space-between;">
  <img src="app/src/main/res/pic1.png" alt="Home screen" width="30%">
  <img src="app/src/main/res/pic2.png" alt="City screen" width="30%">
  <img src="app/src/main/res/pic3.png" alt="Place screen" width="30%">
  <img src="app/src/main/res/pic4.png" alt="Place screen" width="30%">
</div>

---

## 📱 Features  
### Core Functionality  
- **Explore Manga**: Browse popular, latest, and trending manga with cover art, descriptions, and chapters.  
- **Search & Filter**: Search manga by title or filter by genre/category.  
- **Reading Experience**: Read chapters seamlessly with paginated navigation.  
- **User Authentication**: Secure sign-up/login using **Firebase Authentication**.  

### Personalized Experience  
- **Reading History**: Track progress with timestamps (last read date/time) using **Room Database**.  
- **User Profile**: Manage your name, profile picture, and sync data via **Firebase Firestore**.  

---

## 🛠️ Technologies  
- **Android**: Kotlin, Coroutines, ViewModel
- **UI**: Jetpack Compose, Coil (image loading)
- **Networking**: Retrofit (MangaDex API integration)
- **Authentication**: Firebase Authentication
- **Database**:
  - **Cloud**: Firebase Firestore (user profiles)
  - **Local**: Room (offline reading history)
- **Architecture**: Clean Architecture (feature-based modules), MVVM

---

## 🏗️ Architecture  
The app follows **feature-based Clean Architecture** with **MVVM** to ensure separation of concerns, testability, and scalability:

---

## Getting Started  
### Clone the repository:
   ```bash  
   git clone https://github.com/Abdelrahman-Elwardany1/Manga-App.git
