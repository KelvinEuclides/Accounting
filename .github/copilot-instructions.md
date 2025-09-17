# Accounting Android App Development Instructions

Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.

## Working Effectively

### Prerequisites and Setup
- JDK 17 or higher (verified working with OpenJDK 17.0.16)
- Android SDK API 34 minimum, API 35 target
- Gradle 8.9 (automatically downloaded via wrapper)
- Network connectivity required for initial build (downloads dependencies)

### Bootstrap and Build the Repository
1. **Make scripts executable (if needed):**
   ```bash
   chmod +x scripts/version.sh
   chmod +x scripts/setup-versioning.sh
   chmod +x gradlew
   ```

2. **Initial build - NEVER CANCEL:**
   ```bash
   ./gradlew build --no-daemon
   ```
   - **CRITICAL**: First build takes 15-20 minutes due to dependency downloads. NEVER CANCEL.
   - Set timeout to 30+ minutes for first build
   - **NETWORK DEPENDENCY**: Requires access to dl.google.com and other Maven repositories
   - If build fails with "Could not GET" errors, network access to Android/Google Maven repositories is blocked
   - Subsequent builds are much faster (2-5 minutes) once dependencies are cached

3. **Debug build (faster option):**
   ```bash
   ./gradlew assembleDebug
   ```
   - Takes 5-10 minutes after initial setup. NEVER CANCEL.
   - Set timeout to 15+ minutes
   - **May fail with network connectivity issues** if dependencies not cached

### Testing - NEVER CANCEL ANY TEST COMMANDS
- **Unit tests:**
  ```bash
  ./gradlew test
  ```
  - Takes 3-5 minutes. NEVER CANCEL. Set timeout to 10+ minutes.
  - **Network required** for first run to download test dependencies

- **Android instrumented tests:**
  ```bash
  ./gradlew connectedAndroidTest
  ```
  - Takes 10-15 minutes. NEVER CANCEL. Set timeout to 20+ minutes.
  - Requires connected Android device or emulator
  - **Network required** for first run

- **Lint check:**
  ```bash
  ./gradlew lintDebug
  ```
  - Takes 2-3 minutes. Set timeout to 5+ minutes.
  - **Network required** for first run to download lint dependencies

- **Test with coverage:**
  ```bash
  ./gradlew testDebugUnitTestCoverage
  ```
  - Takes 5-8 minutes. Set timeout to 15+ minutes.
  - **Network required** for first run

### Running the Application
- **Install debug APK to connected device/emulator:**
  ```bash
  ./gradlew installDebug
  ```

- **You cannot run the Android app in this environment** - it requires an Android device or emulator with UI interaction capabilities.

## Version Management

### Using the Version Script
- **Show current version:**
  ```bash
  ./scripts/version.sh show
  ```

- **Increment version:**
  ```bash
  ./scripts/version.sh patch --commit    # Bug fixes
  ./scripts/version.sh minor --commit    # New features
  ./scripts/version.sh major --commit    # Breaking changes
  ```

- **Version format:** Major.Minor.Patch (Build) - currently 1.2.2 (6)
- Version information stored in `version.properties`
- Used by Gradle build system to set versionCode and versionName

## Validation Requirements

### Always run these validation steps before committing:
1. **Build validation:**
   ```bash
   ./gradlew assembleDebug
   ```

2. **Lint check:**
   ```bash
   ./gradlew lintDebug
   ```

3. **Unit tests:**
   ```bash
   ./gradlew test
   ```

### Manual Validation Scenarios
Since the Android app cannot be run in this environment, focus on:
1. **Build success** - verify APK is generated in `app/build/outputs/apk/debug/`
2. **No lint errors** - check lint reports in `app/build/reports/lint-results-debug.html`
3. **Test passage** - verify all unit tests pass
4. **Code compilation** - ensure Kotlin code compiles without errors

## Project Structure and Navigation

### Key Directories
- **Main source:** `app/src/main/java/com/anje/kelvin/aconting/`
- **Unit tests:** `app/src/test/java/` (currently empty - needs tests)
- **UI tests:** `app/src/androidTest/java/` (has LoginScreenTest.kt)
- **Resources:** `app/src/main/res/`
- **Scripts:** `scripts/` (version.sh, setup-versioning.sh)

### Architecture Overview
- **Pattern:** MVVM with Repository pattern
- **UI:** Jetpack Compose with Material Design 3
- **DI:** Hilt (Dagger) 
- **Database:** Room + Realm (mixed setup - prefer Room for new code)
- **Navigation:** Jetpack Navigation Compose
- **Language:** 100% Kotlin
- **Security:** Encrypted SharedPreferences for user data

### Detailed Package Structure
- **data/database/** - Room database setup with entities, DAOs, converters
- **data/repository/** - Repository pattern implementations
- **data/preferences/** - SharedPreferences and user settings
- **data/entity/** - Legacy data entities (mixed with Room entities)
- **domain/model/** - Domain models and business logic entities
- **presentation/screen/** - All Compose UI screens
- **presentation/viewmodel/** - MVVM ViewModels for screens
- **presentation/navigation/** - Navigation logic and setup
- **ui/theme/** - Material 3 theming (Color.kt, Theme.kt, Type.kt)
- **ui/localization/** - Portuguese string resources and localization
- **di/** - Hilt dependency injection modules
- **util/** - Utility classes and helper functions

### Key Files and Their Purpose
- **ComposeMainActivity.kt** - Main entry point with theme and navigation setup
- **AccountingApplication.kt** - Application class with Hilt setup
- **AccountingNavigation.kt** - App navigation between login/register/main screens
- **MainScreen.kt** - Main dashboard with bottom navigation
- **LoginScreen.kt** - User authentication screen  
- **version.properties** - Version configuration for automated versioning
- **build.gradle** (app level) - Main build configuration with dependencies
- **AndroidManifest.xml** - App labeled as "Pakitini" with ComposeMainActivity as launcher

### Application Information
- **App Name:** Pakitini (Portuguese accounting app)
- **Package:** com.anje.kelvin.aconting
- **Current Version:** 1.2.2 (Build 6)
- **Min SDK:** 24, Target SDK: 35
- **Language Support:** Portuguese localization in ui/localization/

### Screen Structure
The app has these main screens:
- **LoginScreen** - Authentication entry point
- **MainScreen** - Dashboard with bottom navigation to:
  - HomeScreen
  - SalesScreen  
  - ExpenseScreen
  - InventoryScreen
  - ReportsScreen
  - DepositScreen
  - TransactionScreen

### Common Development Tasks

#### Adding New Features
1. Always build first: `./gradlew assembleDebug`
2. Follow MVVM pattern: ViewModel -> Repository -> Data layer
3. Use Hilt for dependency injection
4. Create Compose UI with Material 3 components
5. Add navigation routes in AccountingNavigation.kt or MainScreen.kt
6. Write unit tests (currently missing - add to `app/src/test/`)

#### Modifying Existing Features
1. Check related ViewModels in `presentation/viewmodel/`
2. Update Repository classes in `data/repository/`
3. Modify UI in `presentation/screen/`
4. Test changes with `./gradlew test && ./gradlew assembleDebug`

#### Database Changes
- Room entities in `domain/model/`
- DAOs in `data/dao/`
- Database class typically in `data/database/`
- Consider migration scripts for schema changes

### CI/CD Integration

#### GitHub Actions Workflows
- **build-and-test.yml** - Runs on PRs, performs lint, test, and build
- **version-and-release.yml** - Automated versioning on main branch pushes
- **manual-release.yml** - Manual release creation

#### Before Committing
Always run the full validation suite:
```bash
./gradlew lintDebug && ./gradlew test && ./gradlew assembleDebug
```

## Common Issues and Solutions

### Build Issues
- **Network connectivity errors:** 
  - First build requires internet access to dl.google.com, mavenCentral(), and other repositories
  - Error "Could not GET" indicates blocked network access to required Maven repositories
  - All Gradle commands will fail until network access is restored
  - Dependencies must be downloaded before any build/test commands work
- **Out of memory:** Increase heap size in gradle.properties (already configured with 2GB)
- **KAPT issues:** Build cache disabled to avoid annotation processing problems

### Development Issues
- **Missing tests:** Unit test directory is empty - create tests for new features
- **Mixed database setup:** Both Room and Realm configured - prefer Room for new code
- **Legacy support:** Some older Android support libraries present during migration

### File Locations for Quick Reference
- **Gradle config:** `build.gradle`, `app/build.gradle`, `gradle.properties`
- **Version info:** `version.properties`
- **Main activity:** `app/src/main/java/com/anje/kelvin/aconting/ComposeMainActivity.kt`
- **Navigation:** `app/src/main/java/com/anje/kelvin/aconting/presentation/navigation/AccountingNavigation.kt`
- **Themes:** `app/src/main/java/com/anje/kelvin/aconting/ui/theme/`
- **Strings/Localization:** `app/src/main/java/com/anje/kelvin/aconting/ui/localization/`

## Common Command Outputs
Reference these instead of running commands to save time.

### Repository Root Directory (ls -la)
```
README.md
PROJECT_MANAGEMENT.md  
PROJECT_ROADMAP.md
app/
build.gradle
gradle/
gradle.properties
gradlew*
gradlew.bat*
scripts/
settings.gradle
version.properties
.github/
.idea/
```

### App Source Structure (ls -la app/src/)
```
androidTest/  - Android instrumented tests
main/         - Main application source
test/         - Unit tests (currently empty)
```

### Version Information (./scripts/version.sh show)
```
Current Version Information:
  Version: 1.2.2
  Version Code: 12206
  Components: Major=1, Minor=2, Patch=2, Build=6
```

### Key Configuration Files Content
**version.properties:**
```
VERSION_MAJOR=1
VERSION_MINOR=2  
VERSION_PATCH=2
VERSION_BUILD=6
```

**Root build.gradle dependencies:**
- Android Gradle Plugin 8.7.2
- Kotlin 1.9.25
- Hilt 2.48
- Realm 10.15.1

## Critical Reminders
- **NEVER CANCEL builds or tests** - Android builds can take 15+ minutes initially
- **Always set appropriate timeouts** - 30+ minutes for builds, 20+ minutes for tests
- **Network dependency** - Initial build requires internet connectivity
- **Validation is mandatory** - Always run lint, test, and build before commits
- **This is a Compose app** - Use modern Jetpack Compose patterns, not legacy Views