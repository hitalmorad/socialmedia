# 🚀 Loopsy – Modern Social Media Platform  
🔗 [In Development](https://github.com/Shamik200/loopsy)  

[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2025.08.00-%230075FF.svg)](https://developer.android.com/jetpack/compose)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Android](https://img.shields.io/badge/Platform-Android-brightgreen)](https://android.com)
[![API](https://img.shields.io/badge/API-28%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=28)
[![Firebase](https://img.shields.io/badge/Firebase-Backend-FFA000.svg)](https://firebase.google.com/)
[![Cloudinary](https://img.shields.io/badge/Cloudinary-Media_Storage-3448C5.svg)](https://cloudinary.com/)
[![Hilt](https://img.shields.io/badge/Hilt-Dependency_Injection-2196F3.svg)](https://dagger.dev/hilt/)
[![CameraX](https://img.shields.io/badge/CameraX-Camera_API-4CAF50.svg)](https://developer.android.com/camerax)
<a href="https://github.com/Shamik200"><img alt="License" src="https://img.shields.io/static/v1?label=GitHub&message=Shamik200&color=C51162"/></a>

---

## 💡 About  
**Loopsy** is a cutting-edge social media platform built with modern Android development practices. It combines the best of social networking with innovative UI/UX design, featuring **liquid animations**, **real-time interactions**, and **AI-powered content discovery**. Built entirely with Jetpack Compose and MVVM architecture for scalable, maintainable code.

---

## 🧠 Core Features  
- 📱 **Modern UI/UX** with liquid bottom navigation and smooth animations  
- 🔥 **Multi-media Content** - Posts, Stories, Reels with advanced camera integration  
- 👥 **Social Networking** - Follow, like, comment, share with real-time updates  
- 🎯 **Smart Discovery** - AI-powered content recommendation engine  
- 💬 **Real-time Messaging** - Instant chat with multimedia support  
- 📸 **Advanced Camera** - CameraX integration with filters and effects  
- ☁️ **Cloud Storage** - Cloudinary for optimized media delivery  
- 🔐 **Secure Authentication** - Firebase Auth with Google Sign-In  
- 🎨 **Material 3 Design** - Beautiful, accessible, and responsive UI  

---

## ⚙️ Tech Stack  

| Layer           | Technology                                                           |
|-----------------|----------------------------------------------------------------------|
| **Frontend**    | Jetpack Compose, Material 3, Navigation Compose                     |
| **Architecture**| MVVM, Hilt Dependency Injection, Repository Pattern                 |
| **Backend**     | Firebase (Auth, Firestore, Storage, Analytics, Messaging)           |
| **Media**       | Cloudinary, CameraX, ExoPlayer, Coil Image Loading                  |
| **Animation**   | Compose Animations, Lottie, Custom Liquid Navigation                |
| **Testing**     | JUnit, Espresso, Compose Testing                                    |

---

## 🏗️ Architecture  

**Loopsy** follows **MVVM (Model-View-ViewModel)** architecture with **Repository Pattern** for clean separation of concerns:

| Component       | Responsibility                                                       |
|-----------------|----------------------------------------------------------------------|
| **UI Layer**    | Jetpack Compose screens and components                              |
| **ViewModel**   | Business logic, state management, UI events                         |
| **Repository**  | Data abstraction layer, API calls, caching                          |
| **Data Layer**  | Firebase integration, local storage, network operations             |
| **DI Layer**    | Hilt modules for dependency injection                                |

---

## 📱 App Flow Structure  

### **Authentication Flow**
- **Splash Screen** → **Welcome** → **Login/SignUp** → **Onboarding** → **Home**

### **Main Navigation**
- **🏠 Home** - Social feed with posts, stories, and reels
- **🔍 Discover** - Explore trending content and users  
- **➕ Create** - Multi-option content creation modal
  - 📝 Create Post
  - 📖 Create Story  
  - 🎬 Create Reel
  - ⚙️ More Options
- **💬 Messages** - Real-time chat and conversations
- **👤 Profile** - User profile, settings, and activity

---

## 🎨 UI Components & Features  

### **🌊 Liquid Bottom Navigation**
- Custom animated navigation with floating pill indicator
- Diamond-shaped center create button with gradient
- Smooth transitions and micro-interactions

### **📸 Advanced Camera Integration**
- CameraX implementation for high-quality media capture
- Real-time filters and effects
- Video recording with audio support
- Gallery integration for media selection

### **🔥 Content Creation**
- **Posts**: Multi-image/video support with captions and hashtags
- **Stories**: Ephemeral content with creative tools
- **Reels**: Short-form video content with editing capabilities

---

## 📊 Data Models  

### **👤 User Model**
```kotlin
data class User(
    val id: String,
    val name: String,
    val username: String,
    val email: String,
    val profileImageUrl: String,
    val bio: String,
    val interests: List<String>,
    val followers: List<String>,
    val following: List<String>,
    val isVerified: Boolean,
    val friendshipScore: Int,
    val streakCount: Int
)
```

### **📝 Post Model**
```kotlin
data class Post(
    val id: String,
    val userId: String,
    val content: String,
    val mediaUrls: List<String>,
    val mediaType: MediaType,
    val likes: List<String>,
    val comments: List<Comment>,
    val hashtags: List<String>,
    val location: String
)
```

---

## 🛠 Quick Setup  

### **Prerequisites**
- Android Studio Ladybug | 2024.2.1+
- JDK 8+
- Android SDK 28+
- Firebase Project Setup

### **Installation**
```bash
# Clone the repository
git clone https://github.com/Shamik200/loopsy.git
cd loopsy/socialmedia

# Open in Android Studio
# Sync project with Gradle files
# Add your Firebase configuration files
# Run the app on device/emulator
```

### **Firebase Configuration**
1. Create a Firebase project
2. Add Android app with package name: `com.loop.socialmedia`
3. Download `google-services.json` and place in `app/` directory
4. Enable Authentication, Firestore, Storage, and Analytics

### **Cloudinary Setup**
1. Create Cloudinary account
2. Add your cloud configuration in the app
3. Configure upload presets for media handling

---

## 📂 Project Structure

```
socialmedia/
 ┣ app/src/main/java/com/loop/socialmedia/
 ┃ ┣ data/
 ┃ ┃ ┣ model/           # Data classes (User, Post, Story, Message)
 ┃ ┃ ┗ repository/      # Repository implementations
 ┃ ┣ di/                # Hilt dependency injection modules
 ┃ ┣ ui/
 ┃ ┃ ┣ components/      # Reusable UI components
 ┃ ┃ ┣ navigation/      # Navigation setup and routing
 ┃ ┃ ┣ screens/         # Main app screens
 ┃ ┃ ┣ st/             # Auth and onboarding screens
 ┃ ┃ ┗ theme/          # Material 3 theming
 ┃ ┣ MainActivity.kt    # Main activity with Compose setup
 ┃ ┗ LoopApplication.kt # Application class with Hilt
 ┣ build.gradle.kts     # App-level dependencies
 ┗ google-services.json # Firebase configuration
```

---

## 🚀 Current Development Status  

### **✅ Completed Features**
- ✅ Project architecture and dependency injection setup
- ✅ Firebase integration (Auth, Firestore, Storage)
- ✅ Cloudinary media upload configuration
- ✅ Authentication flow (Login, SignUp, Onboarding)
- ✅ Navigation system with liquid bottom navigation
- ✅ Data models and repository pattern
- ✅ UI components and theming system
- ✅ CameraX integration for media capture

### **🚧 In Development**
- 🚧 Social feed implementation
- 🚧 Real-time messaging system
- 🚧 Content creation workflows
- 🚧 User profile management
- 🚧 Discovery and search functionality

### **📋 Upcoming Features**
- 📋 AI-powered content recommendations
- 📋 Advanced video editing tools
- 📋 Push notifications
- 📋 Analytics and insights
- 📋 Dark mode support
- 📋 Accessibility improvements

---

## 🧪 Testing  

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest

# Run UI tests
./gradlew connectedDebugAndroidTest
```

---

## 📱 Screenshots  

*Coming Soon - Screenshots will be added as features are completed*

---

## 🤝 Contributing  

This project is currently in active development. Contributions, issues, and feature requests are welcome!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License  

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🧠 Author

Made with ❤️ by [Shamik Munjani](https://github.com/Shamik200)  
📫 [LinkedIn](https://www.linkedin.com/in/shamik-munjani) · 📧 shamikmunjani@gmail.com  
🔗 Project: [https://github.com/Shamik200/loopsy](https://github.com/Shamik200/loopsy)

---

<div align="center">

### 🌟 Star this repo if you find it helpful!

**Loopsy** - *Connecting people through innovative social experiences*

</div>
