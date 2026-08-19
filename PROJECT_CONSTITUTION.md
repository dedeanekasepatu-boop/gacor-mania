# GACOR DRIVER AI — PROJECT CONSTITUTION

**Version:** 1.0 (Foundation Phase)  
**Governing Roles:**  
- **Lead Architect:** ChatGPT  
- **Coding Executor:** Gemini  
- **Source of Truth:** GitHub Repository (`dedeanekasepatu-boop/grab-mod`)

---

## I. FUNDAMENTAL DIRECTIVES & PROHIBITIONS

1. **Strict Scope Discipline:**
   - Do NOT implement unauthorized features beyond explicit Lead Architect instructions.
   - Do NOT advance to future roadmap milestones (V1.1 to V2.0) without explicit instruction.
   - Do NOT alter project architecture or tech stack without approval.

2. **Legal & Security Guardrails:**
   - **NO Illegal Third-Party Integration:** No unauthorized APIs or unofficial scraping against Grab, Gojek/GoCar, or inDrive.
   - **NO Code Injection:** No code injection, hooking, or runtime memory manipulation of third-party apps.
   - **NO GPS Spoofing:** Strictly forbid fake mock locations, location faking, or spoofing.
   - **NO Order Manipulation:** No bypassing, dispatch tampering, or automated order snatching scripts.
   - **NO Credential Storage:** Never request, harvest, or persist driver platform passwords, OTPs, or session tokens.
   - **Least Privilege:** Request only the minimum Android permissions strictly required for legitimate diagnostic features.

3. **Data Integrity & Honesty:**
   - **NO Fake Real-Time Data:** Never display simulated fake orders, fake surges, or fabricated heatmaps as genuine data.
   - **Display Transparency:** If genuine device telemetry is unavailable or waiting for permissions, explicitly display `"--"`.
   - **NO False Claims:** Never advertise "GPS Boosters" or "Signal Amplifiers" that violate physical radio/satellite limitations. Only monitor, diagnose, and optimize legitimate OS resource usage.

4. **Sanitized Logging Policy:**
   - Use internal logging tagged strictly with `GACOR_DRIVER`.
   - Ensure all output strings are scrubbed of any potential sensitive identifiers or tokens.

---

## II. ARCHITECTURAL PILLARS

1. **Language & UI:** Kotlin + Jetpack Compose (Material 3).
2. **Architecture Pattern:** Clean Architecture + MVVM.
3. **Local Database:** Room Database with Kotlin Symbol Processing (KSP).
4. **Concurrency:** Kotlin Coroutines & Flow.
5. **Location & Network Services:** Official Android Location APIs + Android ConnectivityManager.

---

## III. DEFINITION OF DONE FOR FOUNDATION (V1.0)

- [x] Project configuration & unique Application ID initialized.
- [x] Clean Architecture structure organized (`core`, `data`, `domain`, `presentation`).
- [x] Room Database entities, DAOs, and abstract DB instance operational.
- [x] Android Location Services abstraction (`LocationProvider` & `DefaultLocationProvider`) operational.
- [x] Android Network Diagnostics abstraction (`NetworkMonitor` & `ConnectivityManagerNetworkMonitor`) operational.
- [x] Runtime Permission handling for Fine & Coarse Location implemented with non-coercive UI.
- [x] Material 3 Dashboard screen matching all required fields (GPS, Network, Driver Mode, Demand Intelligence, App Status).
- [x] Safe internal logger (`GacorLogger` with tag `GACOR_DRIVER`) in place.
- [x] `README.md` and `PROJECT_CONSTITUTION.md` established.
- [x] Successful compilation and build verification.

---

*This Constitution serves as the binding reference for all engineering contributions to Gacor Driver AI.*
