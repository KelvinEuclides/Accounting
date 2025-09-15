# Accounting App

A modern Android accounting application built with Jetpack Compose, Room database, and automated CI/CD versioning.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [Build Instructions](#build-instructions)
- [Version Management](#version-management)
- [Testing](#testing)
- [CI/CD Pipeline](#cicd-pipeline)
- [Contributing](#contributing)
- [License](#license)

## Overview

This accounting application helps small businesses and individuals manage their finances with features including:

- Sales tracking and inventory management
- Expense monitoring and categorization
- Deposit recording
- Financial reporting and analytics
- User authentication and data security

The app is built using modern Android development practices with Kotlin, Jetpack Compose, and follows MVVM architecture with Repository pattern.

## Features

### Core Functionality
- **Sales Management**: Record sales transactions with itemized details
- **Inventory Control**: Track product stock levels and pricing
- **Expense Tracking**: Categorize and monitor business expenses
- **Deposit Recording**: Log income and deposit transactions
- **Financial Reports**: Generate comprehensive financial analytics
- **User Authentication**: Secure login with encrypted user data

### Technical Features
- **Modern UI**: Built with Jetpack Compose and Material Design 3
- **Portuguese Localization**: Full Portuguese language support
- **Automated Versioning**: CI/CD pipeline with semantic versioning
- **Type Safety**: 100% Kotlin with null safety
- **Dependency Injection**: Hilt for clean architecture

## Architecture

The application follows Clean Architecture principles with MVVM pattern:

```
app/
├── data/
│   ├── repository/         # Repository implementations
│   └── preferences/        # SharedPreferences wrapper
├── domain/
│   └── model/              # Domain models
├── presentation/
│   ├── screen/             # Compose UI screens
│   ├── viewmodel/          # ViewModels
│   └── navigation/         # Navigation graph
├── di/                     # Hilt dependency injection
└── ui/
    ├── theme/              # Material Design theme
    └── localization/       # String resources
```

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17 or higher
- Android SDK API 34
- Kotlin 1.9.25 or later

### Installation

1. Clone the repository:
```bash
git clone https://github.com/KelvinEuclides/Accounting.git
cd Accounting
```

2. Open the project in Android Studio

3. Sync the project with Gradle files

4. Run the app on an emulator or physical device

## Development Setup

### Local Development

1. **Configure Python Environment** (for scripts):
```bash
# Make scripts executable
chmod +x scripts/version.sh
chmod +x scripts/setup-versioning.sh
```

2. **Install Dependencies**:
```bash
# Build the project
./gradlew build

# Install debug version
./gradlew installDebug
```

3. **Setup Git Hooks** (optional):
```bash
./scripts/setup-versioning.sh
```

### Environment Configuration

The app uses `version.properties` for dynamic versioning:

```properties
VERSION_MAJOR=1
VERSION_MINOR=1
VERSION_PATCH=1
VERSION_BUILD=3
```

## Build Instructions

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

### Run Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest

# Lint check
./gradlew lintDebug
```

### Build with Custom Version
```bash
# Using version management script
./scripts/version.sh patch    # Increment patch version
./scripts/version.sh minor    # Increment minor version
./scripts/version.sh major    # Increment major version

# Build with updated version
./gradlew assembleDebug
```

## Version Management

The project uses automated semantic versioning with the following conventions:

### Commit Message Format
- `feat:` or `feature:` - Minor version bump
- `fix:` or `patch:` - Patch version bump  
- `BREAKING CHANGE:` or `major:` - Major version bump

### Manual Version Management
```bash
# Show current version
./scripts/version.sh show

# Increment versions
./scripts/version.sh patch
./scripts/version.sh minor  
./scripts/version.sh major

# With automatic commit and tag
./scripts/version.sh patch --commit --tag
```

### Version Structure
- **Major**: Breaking changes (1.x.x)
- **Minor**: New features (x.1.x)
- **Patch**: Bug fixes (x.x.1)
- **Build**: CI incremented (version code)

## Testing

### Running Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest

# Test with coverage
./gradlew testDebugUnitTestCoverage
```

### Test Structure
- **Unit Tests**: `app/src/test/`
- **Integration Tests**: `app/src/androidTest/`
- **UI Tests**: Compose UI testing with `app/src/androidTest/`

## CI/CD Pipeline

The project includes automated GitHub Actions workflows:

### Automated Versioning (`version-and-release.yml`)
- Triggers on push to `main`/`master`
- Analyzes commit messages for version bumps
- Builds APK files
- Creates Git tags and GitHub releases
- Updates `version.properties`

### Build and Test (`build-and-test.yml`)
- Runs on pull requests
- Executes lint checks and tests
- Uploads build artifacts

### Manual Release (`manual-release.yml`)
- Manually triggered workflow
- Allows custom version specification
- Creates releases with custom notes

### Workflow Permissions
Required GitHub repository settings:
```yaml
permissions:
  contents: write
  pull-requests: write
  actions: write
```

## Contributing

### Development Workflow

1. **Create Feature Branch**:
```bash
git checkout -b feature/your-feature-name
```

2. **Make Changes**:
- Follow Kotlin coding standards
- Write unit tests for new features
- Update documentation as needed

3. **Commit with Semantic Messages**:
```bash
git commit -m "feat: add new expense categorization feature"
git commit -m "fix: resolve database migration issue"
git commit -m "BREAKING CHANGE: update user authentication system"
```

4. **Push and Create Pull Request**:
```bash
git push origin feature/your-feature-name
```

5. **Automated Versioning**:
- Merge to main triggers automatic version bump
- GitHub release created with APK files
- Version updated for next development cycle

### Code Style Guidelines

- **Language**: Kotlin preferred, minimize Java usage
- **Architecture**: Follow MVVM with Repository pattern
- **UI**: Use Jetpack Compose for all new screens
- **Dependency Injection**: Use Hilt annotations
- **Testing**: Write unit tests for business logic

### Git Conventions

- Use semantic commit messages
- Keep commits atomic and focused
- Include tests with feature commits
- Update documentation for public APIs

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For questions or issues:
1. Check existing GitHub issues
2. Create a new issue with detailed description
3. Include logs and reproduction steps
4. Specify Android version and device information

## Changelog

See [Releases](https://github.com/KelvinEuclides/Accounting/releases) for detailed changelog and version history.