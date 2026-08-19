# GACOR DRIVER AI

> **Driver Cockpit & Intelligence Foundation Assistant for Online Ride-Hailing Drivers**

---

## 1. Project Overview

**Gacor Driver AI** is a professional, driver-focused Android application designed to assist online transportation drivers (GrabCar, GoCar, inDrive). The platform provides real-time situational intelligence regarding GPS health, network connection diagnostics, and localized driver session management—strictly operating through legal, official Android operating system APIs without spoofing, hacking, or third-party injection.

---

## 2. Status

* **Current Stage:** `V1.0 FOUNDATION`
* **Status:** Foundation Complete & Operational.
* **Scope Notice:** Advanced prediction engines, AI modules, and third-party platform integrations are strictly scheduled for future versions as outlined in the Roadmap.

---

## 3. Technology Stack

* **Platform:** Android (Min SDK 24 / Target SDK 36)
* **Language:** Kotlin (100%)
* **UI Framework:** Jetpack Compose (Material Design 3, Dark-mode optimized)
* **Architecture:** Clean Architecture + MVVM (Model-View-ViewModel)
* **Dependency Injection:** Container-based Constructor Injection
* **Local Persistence:** Room Database (KSP-compiled)
* **Reactive Concurrency:** Kotlin Coroutines & Flow
* **Location Services:** Android Location API & Google Play Services FusedLocationProviderClient
* **Networking & Diagnostics:** Android ConnectivityManager NetworkCallback & Socket Latency Diagnostic

---

## 4. Architecture Structure

The project follows strict Clean Architecture separation of concerns:

```
app/src/main/java/com/example/
├── GacorApplication.kt             # Application entry point & AppContainer singleton
├── MainActivity.kt                 # Single activity hosting Jetpack Compose & Navigation
│
├── core/                           # System abstractions & utilities
│   ├── common/                     # AppContainer, Constants, Resource/Result sealed classes
│   ├── location/                   # LocationProvider abstraction & DefaultLocationProvider
│   ├── network/                    # NetworkMonitor abstraction & ConnectivityManagerNetworkMonitor
│   ├── permissions/                # Permission status state holders & rationale
│   └── util/                       # GacorLogger (tag: GACOR_DRIVER, sanitized) & Formatters
│
├── data/                           # Data layer implementations
│   ├── local/                      # Room AppDatabase, DAOs (LocationDao, NetworkDao, DriverSessionDao)
│   │   ├── dao/
│   │   └── entity/                 # Room entities for location, network, and sessions
│   └── repository/                 # Repository implementations (Location, Network, DriverSession)
│
├── domain/                         # Business rules & pure domain models
│   ├── model/                      # LocationData, NetworkStatus, DriverPlatform, DriverSession
│   ├── repository/                 # Domain repository interfaces
│   └── usecase/                    # Pure use cases (Location, Network, Latency, DriverSession)
│
└── presentation/                   # UI Presentation Layer
    ├── components/                 # Reusable driver-friendly Material 3 components
    ├── dashboard/                  # DashboardScreen, DashboardViewModel, DashboardUiState
    ├── navigation/                 # Screen definitions & NavGraph
    └── theme/                      # High-contrast driver cockpit palette (Color, Theme, Type)
```

---

## 5. Key Capabilities (V1.0 Foundation)

1. **GPS Status Monitor:**
   - Real-time provider status tracking (`Waiting for permission`, `Active (Online)`, `GPS Disabled`, etc.)
   - High-precision telemetry (Accuracy in meters, Latitude, Longitude, Speed in km/h)
   - Signal quality categorization (Excellent, Good, Fair, Poor) based on accuracy boundaries.

2. **Network Quality Monitor:**
   - Active network transport detection (Wi-Fi, Cellular / Mobile Data, Ethernet, VPN)
   - Connection state and metered/unmetered detection
   - Cellular/Wi-Fi signal strength reporting (dBm)
   - Diagnostic network ping latency measurement (in ms) with on-demand refresh.

3. **Driver Mode Selector:**
   - Select active platform between **GrabCar**, **GoCar**, and **inDrive**.
   - Persists driver choice reactively into the Room database.

4. **Transparent Data Integrity:**
   - Strictly NO fake data, simulated orders, or synthetic APIs.
   - If telemetry is unavailable or waiting for permissions, `--` is explicitly rendered.

---

## 6. Constraints & Compliance

- **No Third-Party Injection:** Does not decompile, inject, or tamper with Grab, Gojek, or inDrive applications.
- **No GPS Spoofing:** Does not simulate, mock, or fake location data.
- **No Order Manipulation:** Does not interact with or tamper with dispatch queues.
- **No Credential Harvesting:** Never asks for or stores driver platform passwords, OTPs, or API tokens.
- **Least Privilege:** Strictly requests fine and coarse location permissions with transparent rationales.

---

## 7. Development Roadmap

* **V1.0 FOUNDATION** (Completed) - Architecture, GPS/Network abstraction, Room DB, Dashboard UI, Permissions.
* **V1.1 GPS INTELLIGENCE** - Advanced satellite telemetry analysis, dead reckoning, and signal loss detection.
* **V1.2 NETWORK INTELLIGENCE** - Cellular band analysis, carrier switching diagnostics, jitter metrics.
* **V1.3 DRIVER SESSION** - Shift duration, active mileage tracking, break reminders.
* **V1.4 DEMAND DATA** - Public municipal and legal transit demand aggregator.
* **V1.5 HOTSPOT MAP** - Real-time verified demand heatmap visualizer.
* **V1.6 DEMAND PREDICTION** - Historical demand regression modeling.
* **V1.7 AI DRIVER ASSISTANT** - Conversational driver copilot.
* **V2.0 MULTI-PLATFORM INTELLIGENCE** - Integrated multi-platform aggregator intelligence.

---

## 8. How to Run

1. Open project in Android Studio or AI Studio.
2. Ensure Android SDK 36 is configured.
3. Build and run the app on an Android device or emulator (API 24+).
4. Grant location permissions when prompted to initiate real-time GPS & network diagnostics.
