# RoomShare - Android Frontend

This is the Kotlin-based Jetpack Compose frontend for the RoomShare application, featuring an **AI Hotel Search Assistant** that suggests rooms and global hotels with detailed star ratings and navigation.

## 📱 Prerequisites

*   **Android Studio** (Recommend Iguana or Ladybug)
*   **Android SDK 34**
*   **A Physical Android Device** or **Emulator** (Running API 26+)

## 🚀 Setup Instructions

Follow these steps exactly to run the app on your phone or emulator:

### 1. Open the project
In Android Studio, select **Open** and choose the `frontend` directory:
```bash
cd frontend
```

### 2. Configure the Backend URL
If you want the app to connect correctly to your backend, you **MUST** update the `RetrofitClient` with your PC's IP address:
1. Open `RetrofitClient.kt`
2. Change the `BASE_URL` to: `http://<your-pc-ip>:8000/api/`
3. To find your IP on Windows, open Command Prompt and type `ipconfig`. Look for "IPv4 Address" under your main WiFi/Ethernet connection.

### 3. Gradle Sync
Click on the **"Sync Project with Gradle Files"** button (small elephant icon) in Android Studio to download all libraries.

### 4. Run the App
Connect your Android phone or start an emulator and click the **Run** (green triangle) button.

## ✨ AI Search Assistant Features
The app features a chat-based "AI Assistant" accessible through the navigation bottom bar.

*   **Smart Parsing**: Enter queries like *"Find me a hotel in Chennai under 7000"*.
*   **Hybrid Cards**: Results show both local rental rooms (Blue badge) and global hotels (Green badge).
*   **Detailed Views**: Cards now show:
    *   **Stars**: Gold star icons for rated hotels.
    *   **Phone/Website**: Quick chips to contact the property.
    *   **Distance**: Km from city center.
*   **Navigate**: A dedicated "Navigate" button that launches Google Maps directly.

## 📡 Key Services
*   `ApiService.kt`: Defines the Retrofit interface.
*   `RetrofitClient.kt`: Configures the network client.
*   `Data.kt`: Holds results and response data models for AI Search.

## ⚠️ Troubleshooting
*   **Unresolved reference: Uri/Intent**: Ensure imports for `android.net.Uri` and `android.content.Intent` are in `AIAssistantScreen.kt`.
*   **Network Failure**: 
    1.  Check if your phone can reach `http://<your-pc-ip>:8000/api/listed-room/` in its browser.
    2.  Check if Windows Firewall is blocking Python.
    3.  Ensure your app has `<uses-permission android:name="android.permission.INTERNET" />` in `AndroidManifest.xml`.
*   **Gradle Build Failure**: Ensure you are using Java 17+ for your Gradle settings in Android Studio.
