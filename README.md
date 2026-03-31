# 🏠 RoomShare - AI-Powered Co-living & Rental Platform

RoomShare is a cutting-edge, multi-platform ecosystem designed to redefine modern co-living and rental discovery. By leveraging AI-driven behavioral matching and global property analysis, RoomShare provides a seamless experience for finding roommates and managing rental agreements.

## 🚀 Repository Overview

This repository is organized into three main components:

1.  **`/backend`**: The brain of the project. A high-performance Django REST Framework API that handles neural matching, AI property searches, and secure user management.
2.  **`/web`**: A premium React dashboard for property owners and users to manage their listings and explore the RoomShare ecosystem via a sleek browser interface.
3.  **`/frontend/app`**: The flagship Android application built with Jetpack Compose, delivering a high-fidelity, interactive mobile experience.

---

## 🛠️ Global Prerequisites

To run the entire ecosystem locally, you will need:
-   **Python 3.10+** (for the Backend)
-   **Node.js 18+** (for the Web Dashboard)
-   **Java 17 & Android Studio** (for the Mobile App)
-   **MySQL or MariaDB** (for the database)

---

## 📦 Component Setup & Deployment

### 1. Backend (Django REST Framework)
The backend acts as the central hub for AI processing and data storage.

*   **Local Setup**:
    1.  `cd backend`
    2.  `python -m venv venv` and activate it.
    3.  `pip install -r requirements.txt`
    4.  Create a `.env` file (refer to `.env.example`).
    5.  `python manage.py migrate`
    6.  `python manage.py runserver`
*   **Deployment**: 
    -   Recommended Platform: **DigitalOcean App Platform** or **Heroku**.
    -   Database: Use **PlanetScale** or a managed MySQL/RDS instance.
    -   Storage: Configure **AWS S3** for media (profile pictures, room photos).

### 2. Web Dashboard (React + Vite)
Built with Tailwind CSS and modern UI components.

*   **Local Setup**:
    1.  `cd web`
    2.  `npm install`
    3.  `npm run dev`
*   **Deployment**: 
    -   Recommended Platform: **Vercel** or **Netlify**.
    -   Connect your repository and point the "Root Directory" to `web/`.
    -   Set build command to `npm run build` and output directory to `dist/`.

### 3. Android Application (Kotlin)
The mobile app provides the most interactive experience with AI-Chatbot and Real-time matching.

*   **Local Setup**:
    1.  Open the `frontend/app` folder in Android Studio.
    2.  Wait for Gradle to sync.
    3.  In `RetrofitClient.kt`, update `BASE_URL` to your laptop's local IP (if testing locally) or your deployed backend URL.
    4.  Build and run on a physical device or emulator.
*   **Deployment**: 
    -   Generate a Signed Bundle (`Build > Generate Signed Bundle/APK`).
    -   Upload the `.aab` to **Google Play Console**.

---

## 🌟 Key Features
-   📡 **AI Location Agent**: Conversational search for rooms and hotels worldwide using OpenStreetMap.
-   🧠 **Neural Matching**: Compatibility scoring based on behavioral 200+ behavioral signals.
-   💳 **Razorpay Integration**: Simulation of secure booking payments and subscription management.
-   💬 **Real-time Messaging**: Direct communication between potential roommates.
-   🔐 **Zero-Trust Security**: Biometric verification and identity linkage for all users.

---

The RoomShare AI Agent and Chatbot are powered by Large Language Models (LLMs). The system is configured to use **Mistral AI** via its official SDK for high-performance reasoning and natural language processing.

### 🤖 Mistral AI Integration
The backend uses Mistral AI to handle intent classification, search parameter extraction, and conversational responses.

1.  **API Key**: Ensure you have a valid Mistral API key from [console.mistral.ai](https://console.mistral.ai/).
2.  **Environment Configuration**: Add your key to the `backend/.env` file:
    ```env
    MISTRAL_API_KEY=your_mistral_api_key_here
    ```
3.  **SDK**: The system uses the `mistralai` Python package.
4.  **Automatic Fallback**: If the API key is missing or the service is unavailable, the system provides helpful template-based responses to ensure a smooth user experience.

---
*RoomShare - Engineering the Future of Residential Living.*
