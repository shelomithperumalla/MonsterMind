# MonsterMind 🧠⚡

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?style=flat-square&logo=kotlin)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-SDK%2026%2B-3DDC84?style=flat-square&logo=android)](https://developer.android.com/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-1F6FEB?style=flat-square&logo=androidstudio)](https://developer.android.com/jetpack/compose)
[![Hilt DI](https://img.shields.io/badge/Hilt-2.51.1-FF6D00?style=flat-square&logo=googledagger)](https://dagger.dev/hilt/)
[![ML Kit](https://img.shields.io/badge/ML%20Kit-OCR-4285F4?style=flat-square&logo=tensorflow)](https://developers.google.com/ml-kit)
[![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)](LICENSE)

**MonsterMind** is an intelligent, privacy-first Android study assistant that transforms handwritten notes and textbook scans into structured knowledge. Using on-device machine learning, MonsterMind extracts text, automatically categorizes content, generates interactive flashcards, and creates visual knowledge graphs—all without sending data to the cloud.

## 🎯 The Problem

Students and lifelong learners accumulate handwritten notes, photograph textbook pages, and capture reference materials, but struggle to organize, retrieve, and synthesize this information effectively. Existing solutions either require constant cloud synchronization (risking privacy), demand manual categorization, or depend on internet connectivity for core functionality. MonsterMind solves this by delivering:

- **Offline-First Architecture**: Complete functionality without internet or cloud dependencies
- **Privacy by Default**: All data remains on your device; zero telemetry or tracking
- **Intelligent Extraction**: Advanced on-device OCR transforms images into structured, searchable text
- **Smart Organization**: Automatic categorization and tagging based on content analysis
- **Interactive Learning**: Auto-generated flashcards and quizzes accelerate retention
- **Zero Friction**: Minimal setup, no authentication, no subscriptions

## ✨ Core Features

### 🖼️ On-Device OCR Engine
Leverages Google ML Kit Text Recognition (v16.0.0) to extract text from images in real-time with high accuracy. Processes handwritten notes, printed pages, and mixed-content documents entirely on your device—no cloud calls, no latency, no privacy concerns.

### ⚙️ Smart Image Processing & Compression
Integrated image optimization pipeline via Coil (v2.6.0) automatically resizes, compresses, and formats captured images. Reduces storage overhead while maintaining OCR quality. Supports JPEG (85% quality), WebP, and PNG formats with intelligent format selection.

### 🗺️ Dynamic Knowledge Graph
Automatically builds visual topic maps from extracted content. Identifies relationships between concepts, hierarchical structures, and cross-cutting themes. Enables intuitive navigation through your learning material and reveals gaps in understanding at a glance.

### 📇 Automated Flashcard & Quiz Generation
Intelligently parses extracted text to generate flashcards and quiz questions. Supports multiple question types: multiple choice, fill-in-the-blank, true/false, and open-ended. Built-in spaced repetition scheduling optimizes retention using proven cognitive science principles.

### 💾 Local-First Database Architecture
Built on Room Database (v2.6.1) with SQLite. Persists all memories, metadata, and user preferences locally. Supports full-text search, advanced filtering, and complex queries. Zero cloud sync complexity; data ownership remains entirely with the user.

### 🔍 Advanced Search & Recall
Natural language search engine with relevance scoring. Finds memories by keyword, category, date range, or content similarity. Contextual suggestions and related-memory recommendations powered by local analytics.

### 🎨 Material Design 3 Interface
Modern, accessible UI built with Jetpack Compose. Responsive layouts adapt seamlessly to phones, tablets, and foldables. Dark mode support for comfortable studying in any lighting condition.

## 📋 Tech Stack

### Core Framework
| Component | Library | Version | Purpose |
|-----------|---------|---------|---------|
| **Language** | Kotlin | 2.0 | Type-safe, expressive code |
| **UI Framework** | Jetpack Compose | Latest | Modern declarative UI |
| **Design System** | Material Design 3 | Latest | Consistent, accessible design |
| **Min/Target SDK** | Android | 26 / 35 | API 26 (Android 8.0) and above |

### Architecture & Dependency Injection
| Component | Library | Version | Purpose |
|-----------|---------|---------|---------|
| **DI Framework** | Dagger Hilt | 2.51.1 | Compile-time safe dependency injection |
| **Hilt Navigation** | hilt-navigation-compose | 1.2.0 | Scoped ViewModels and navigation |
| **Architecture Pattern** | MVVM + Clean Architecture | — | Separation of concerns, testability |

### Database & Persistence
| Component | Library | Version | Purpose |
|-----------|---------|---------|---------|
| **ORM Framework** | Room Database | 2.6.1 | Type-safe database abstraction |
| **Database Engine** | SQLite | Bundled | Local relational persistence |
| **Serialization** | Kotlinx Serialization | Latest | JSON/Protocol Buffer support |

### Machine Learning & Image Processing
| Component | Library | Version | Purpose |
|-----------|---------|---------|---------|
| **OCR Engine** | ML Kit Text Recognition | 16.0.0 | On-device handwriting & print recognition |
| **Image Loading** | Coil Compose | 2.6.0 | Efficient image loading and caching |
| **Image Optimization** | Coil Transform | 2.6.0 | Automatic resizing and compression |

### Asynchronous Processing & Reactive Programming
| Component | Library | Version | Purpose |
|-----------|---------|---------|---------|
| **Concurrency** | Kotlin Coroutines | Latest | Structured async/await patterns |
| **Reactive Streams** | Kotlin Flow | Latest | Cold, composable data streams |
| **State Management** | StateFlow | Latest | Hot observable state container |

### Testing & Quality Assurance
| Component | Library | Version | Purpose |
|-----------|---------|---------|---------|
| **Unit Testing** | JUnit 4 | 4.13.2 | Core test framework |
| **Mocking** | Mockk | Latest | Kotlin-first mocking library |
| **Instrumentation** | Espresso | Latest | Android UI testing framework |
| **Compose Testing** | Compose UI Test | Latest | Composable testing utilities |

### Permissions & Runtime Behavior
| Component | Library | Version | Purpose |
|-----------|---------|---------|---------|
| **Permissions** | accompanist-permissions | 0.35.0-alpha | Declarative permission handling |

## 📁 Project Architecture

```
MonsterMind/
│
├── app/
│   ├── build.gradle.kts              # App-level configuration
│   ├── proguard-rules.pro             # ProGuard/R8 configuration
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/monstermind/
│       │   │   │
│       │   │   ├── data/              # Data layer (Room DB, entities, DAOs)
│       │   │   │   ├── dao/
│       │   │   │   │   ├── MemoryDao.kt
│       │   │   │   │   └── TagDao.kt
│       │   │   │   ├── database/
│       │   │   │   │   ├── MemoryDatabase.kt
│       │   │   │   │   ├── Converters.kt
│       │   │   │   │   └── DatabaseMigrations.kt
│       │   │   │   └── entity/
│       │   │   │       ├── Memory.kt
│       │   │   │       ├── Tag.kt
│       │   │   │       ├── MemoryTag.kt
│       │   │   │       ├── Flashcard.kt
│       │   │   │       └── QuizQuestion.kt
│       │   │   │
│       │   │   ├── di/                # Dependency injection modules
│       │   │   │   ├── DatabaseModule.kt
│       │   │   │   ├── RepositoryModule.kt
│       │   │   │   ├── MLModule.kt
│       │   │   │   └── CoroutineModule.kt
│       │   │   │
│       │   │   ├── domain/            # Business logic and use cases
│       │   │   │   ├── model/
│       │   │   │   │   ├── MemoryModel.kt
│       │   │   │   │   ├── FlashcardModel.kt
│       │   │   │   │   ├── KnowledgeGraphNode.kt
│       │   │   │   │   └── SearchResult.kt
│       │   │   │   ├── repository/
│       │   │   │   │   ├── MemoryRepository.kt
│       │   │   │   │   ├── SearchRepository.kt
│       │   │   │   │   └── AnalyticsRepository.kt
│       │   │   │   └── usecase/
│       │   │   │       ├── ExtractTextFromImageUseCase.kt
│       │   │   │       ├── SaveMemoryUseCase.kt
│       │   │   │       ├── SearchMemoriesUseCase.kt
│       │   │   │       ├── GenerateFlashcardsUseCase.kt
│       │   │   │       ├── GenerateQuizUseCase.kt
│       │   │   │       └── BuildKnowledgeGraphUseCase.kt
│       │   │   │
│       │   │   ├── ui/                # Presentation layer (Compose)
│       │   │   │   ├── screen/
│       │   │   │   │   ├── HomeScreen.kt
│       │   │   │   │   ├── CaptureScreen.kt
│       │   │   │   │   ├── DetailScreen.kt
│       │   │   │   │   ├── SearchScreen.kt
│       │   │   │   │   ├── FlashcardScreen.kt
│       │   │   │   │   ├── QuizScreen.kt
│       │   │   │   │   ├── KnowledgeGraphScreen.kt
│       │   │   │   │   └── SettingsScreen.kt
│       │   │   │   ├── viewmodel/
│       │   │   │   │   ├── HomeViewModel.kt
│       │   │   │   │   ├── CaptureViewModel.kt
│       │   │   │   │   ├── DetailViewModel.kt
│       │   │   │   │   ├── SearchViewModel.kt
│       │   │   │   │   ├── FlashcardViewModel.kt
│       │   │   │   │   ├── QuizViewModel.kt
│       │   │   │   │   ├── KnowledgeGraphViewModel.kt
│       │   │   │   │   └── SettingsViewModel.kt
│       │   │   │   ├── component/
│       │   │   │   │   ├── MemoryCard.kt
│       │   │   │   │   ├── SearchBar.kt
│       │   │   │   │   ├── FlashcardView.kt
│       │   │   │   │   ├── QuizCard.kt
│       │   │   │   │   ├── GraphNode.kt
│       │   │   │   │   ├── LoadingIndicator.kt
│       │   │   │   │   ├── ErrorDialog.kt
│       │   │   │   │   └── BottomNavigation.kt
│       │   │   │   ├── theme/
│       │   │   │   │   ├── Color.kt
│       │   │   │   │   ├── Type.kt
│       │   │   │   │   ├── Shape.kt
│       │   │   │   │   └── Theme.kt
│       │   │   │   └── navigation/
│       │   │   │       ├── NavGraph.kt
│       │   │   │       └── NavigationDestination.kt
│       │   │   │
│       │   │   ├── ml/                # Machine learning integration
│       │   │   │   ├── ocr/
│       │   │   │   │   ├── TextRecognitionService.kt
│       │   │   │   │   ├── OCRResult.kt
│       │   │   │   │   └── OCRProcessor.kt
│       │   │   │   ├── classification/
│       │   │   │   │   ├── ContentClassifier.kt
│       │   │   │   │   ├── CategoryKeywords.kt
│       │   │   │   │   └── ClassificationResult.kt
│       │   │   │   └── nlp/
│       │   │   │       ├── TextAnalyzer.kt
│       │   │   │       ├── KeywordExtractor.kt
│       │   │   │       └── SummaryGenerator.kt
│       │   │   │
│       │   │   ├── util/              # Utilities and helpers
│       │   │   │   ├── Constants.kt
│       │   │   │   ├── ImageUtils.kt
│       │   │   │   ├── FileUtils.kt
│       │   │   │   ├── DateFormatter.kt
│       │   │   │   ├── Logger.kt
│       │   │   │   └── Extensions.kt
│       │   │   │
│       │   │   └── MainActivity.kt    # Application entry point
│       │   │
│       │   └── res/
│       │       ├── drawable/         # Vector drawables and images
│       │       ├── values/           # Colors, strings, dimensions
│       │       └── values-*/         # Localization resources
│       │
│       ├── test/                     # Unit tests
│       │   └── java/com/monstermind/
│       │       ├── data/
│       │       ├── domain/
│       │       ├── ml/
│       │       └── ui/
│       │
│       └── androidTest/              # Instrumentation tests
│           └── java/com/monstermind/
│               ├── ui/
│               └── integration/
│
├── build.gradle.kts                 # Root-level configuration
├── settings.gradle.kts              # Build configuration
├── gradle.properties                # Gradle properties
├── gradlew                          # Gradle wrapper script
├── .gitignore                       # Git ignore patterns
├── LICENSE                          # MIT License
├── README.md                        # This file
└── CONTRIBUTING.md                 # Contribution guidelines

```

## 🚀 Getting Started

### Prerequisites

- **Android Studio**: Ladybug (2024.2.1) or newer
- **JDK**: 17 or higher (OpenJDK or Oracle JDK)
- **Android SDK**: API 35 (Android 15) installed
- **Gradle**: 8.2.0+ (managed by Gradle wrapper)
- **Git**: Version control for cloning the repository

### Installation

#### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/MonsterMind.git
cd MonsterMind
```

#### 2. Open in Android Studio

```bash
# Option 1: Open Android Studio and select "Open" → Navigate to MonsterMind folder
# Option 2: Use command line to open directly
open -a "Android Studio" .  # macOS
explorer .                   # Windows (then open in Android Studio)
```

#### 3. Sync Gradle

Android Studio will automatically detect `build.gradle.kts` and prompt you to sync. Allow the sync to complete—Gradle will download all dependencies.

```bash
# Or sync manually from command line
./gradlew build
```

#### 4. Configure SDK Paths (if needed)

If Android Studio cannot locate the Android SDK, configure the path:

1. **Android Studio** → **Preferences/Settings** → **Appearance & Behavior** → **System Settings** → **Android SDK**
2. Set **SDK Location** to your Android SDK installation (e.g., `~/Library/Android/sdk` on macOS, `C:\Users\YourName\AppData\Local\Android\sdk` on Windows)
3. Ensure **API 35** is checked in the SDK list
4. Click **Apply** → **OK**

#### 5. Build the Debug APK

```bash
./gradlew assembleDebug
```

Output: `app/build/outputs/apk/debug/app-debug.apk`

#### 6. Install on Device or Emulator

```bash
# Ensure an Android device or emulator is connected
adb devices

# Install debug APK
./gradlew installDebug

# Launch the app
adb shell am start -n com.monstermind/.MainActivity
```

### Running Tests

#### Unit Tests

```bash
./gradlew test
```

#### Instrumentation Tests (on device/emulator)

```bash
./gradlew connectedAndroidTest
```

#### Lint & Code Quality Checks

```bash
./gradlew lint
./gradlew detekt  # Optional: if Detekt plugin is enabled
```

## 🔐 Permissions & Privacy

### Required Runtime Permissions

MonsterMind requests the following permissions explicitly at runtime:

| Permission | Purpose | Requested At |
|-----------|---------|--------------|
| `READ_MEDIA_IMAGES` | Access photo library to import note images | First capture screen |
| `CAMERA` | Capture images of notes using device camera | Capture screen |
| `ACCESS_MEDIA_LOCATION` | Read location metadata from images (optional) | Settings screen |

### Privacy & Data Handling

- **Zero Cloud Sync**: All data remains on your device permanently
- **No Telemetry**: MonsterMind does not collect usage analytics or crash reports
- **No Authentication**: No accounts, logins, or user tracking
- **No Third-Party SDKs**: No ad networks, analytics libraries, or remote APIs (except Google ML Kit, which operates offline)
- **Local Storage Only**: All memories, flashcards, and preferences stored in encrypted SQLite database
- **User Control**: Export, delete, or wipe all data at any time via Settings

### Manifest Declaration

```xml
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.INTERNET" />
<!-- INTERNET is declared for Google Play Services only; no data is sent -->
```

## 📊 Architecture Deep Dive

### Clean Architecture Principles

MonsterMind follows Clean Architecture with strict separation of concerns:

```
┌─────────────────────────────────────┐
│         Presentation Layer          │
│  (UI, ViewModels, Composables)      │
└────────────────┬────────────────────┘
                 │
┌────────────────▼────────────────────┐
│          Domain Layer               │
│  (Business Logic, Use Cases)        │
└────────────────┬────────────────────┘
                 │
┌────────────────▼────────────────────┐
│          Data Layer                 │
│  (Repositories, Room DB, Entities)  │
└─────────────────────────────────────┘
```

### MVVM with Unidirectional Data Flow (UDF)

Each feature follows this pattern:

```
User Action (Tap, Input)
          │
          ▼
    ViewModel Event
          │
          ▼
    Business Logic (UseCase)
          │
          ▼
    Repository Query
          │
          ▼
    Database / ML Service
          │
          ▼
    State Update (StateFlow)
          │
          ▼
    UI Recomposition (Compose)
          │
          ▼
    Updated UI Render
```

### Dependency Injection with Hilt

Hilt provides compile-time safe, scoped dependency injection:

- **@Singleton**: App-wide singletons (Database, Repositories)
- **@ActivityScoped**: ViewModel instances tied to Activity lifecycle
- **@Qualifiers**: Distinguish multiple implementations of same interface

Example:

```kotlin
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUseCase: SearchMemoriesUseCase,
    private val analyticsRepository: AnalyticsRepository
) : ViewModel() {
    // ViewModel implementation
}
```

### Room Database Architecture

Type-safe database abstraction with compile-time verification:

- **Entities**: `Memory`, `Tag`, `Flashcard`, `QuizQuestion`
- **DAOs**: Type-safe query definitions
- **TypeConverters**: Custom type serialization (e.g., `List<String>` → JSON)
- **Migration**: Versioned schema updates without data loss

## 🛣️ Roadmap & Future Scope

### Phase 1: Core Stability (Current)
- ✅ On-device OCR integration
- ✅ Image capture and processing
- ✅ Basic memory storage and search
- ✅ Flashcard generation
- ✅ Quiz mode
- ✅ Knowledge graph visualization

### Phase 2: Enhanced Learning (Planned)
- 📋 **Spaced Repetition Engine**: Algorithm-driven review scheduling
- 📋 **Progress Analytics**: Visual performance tracking and learning insights
- 📋 **Collaborative Tagging**: User-defined tags and custom categories
- 📋 **Study Sessions**: Scheduled, focused study blocks with timer

### Phase 3: Advanced Features (Proposed)
- 📋 **Fine-Tuned Offline LLM**: Smaller language model for better summarization and question generation
- 📋 **PDF & Markdown Export**: Convert memories and flashcards to standard formats
- 📋 **Multi-Device Sync (Encrypted)**: Optional encrypted sync via end-to-end encryption
- 📋 **Custom Note Hierarchy**: Nested folders and collections for organization
- 📋 **Audio Transcription**: Convert voice recordings to searchable text
- 📋 **Integration with Study Platforms**: Export to Anki, Quizlet, Notion

### Phase 4: Mobile Excellence (Future)
- 📋 **Tablet Optimization**: Native support for larger screens and multi-pane layouts
- 📋 **Handwriting Recognition Improvements**: Enhanced support for multiple languages and cursive
- 📋 **Wear OS Integration**: Quick capture and review on smartwatches
- 📋 **Widget Support**: Home screen widgets for quick access and review

## 🤝 Contributing

We welcome contributions from the community! MonsterMind is built by and for learners who value privacy, offline-first tools, and intelligent study assistance.

### Code of Conduct

- Be respectful and constructive in all interactions
- Provide context and reasoning in discussions
- Help others learn and grow as developers
- Report issues responsibly and privately

### How to Contribute

#### 1. Fork the Repository

```bash
# Visit https://github.com/yourusername/MonsterMind
# Click "Fork" in the top-right corner
```

#### 2. Clone Your Fork

```bash
git clone https://github.com/yourusername/MonsterMind.git
cd MonsterMind
git remote add upstream https://github.com/originalauthor/MonsterMind.git
```

#### 3. Create a Feature Branch

```bash
git checkout -b feature/amazing-feature
```

Use descriptive branch names:
- `feature/flashcard-scheduling`
- `bugfix/ocr-text-alignment`
- `chore/upgrade-kotlin-version`

#### 4. Commit Your Changes

Write clear, concise commit messages:

```bash
git commit -m "Add spaced repetition algorithm for flashcards

- Implement SM-2 spacing algorithm
- Add review scheduling persistence
- Optimize recall timing for long-term retention
- Add unit tests for scheduler
"
```

#### 5. Push to Your Fork

```bash
git push origin feature/amazing-feature
```

#### 6. Open a Pull Request

- Navigate to the original repository
- Click "Compare & Pull Request"
- Fill in the PR template with:
  - **Description**: What does this PR do?
  - **Testing**: How did you test this change?
  - **Screenshots**: If UI changes, include before/after screenshots
  - **Checklist**: Confirm tests pass, code is formatted, no breaking changes

### Pull Request Guidelines

- **One feature per PR**: Keep changes focused and reviewable
- **Test coverage**: Add tests for new features; maintain >80% coverage
- **Code style**: Follow Kotlin conventions; run `./gradlew detekt` locally
- **Documentation**: Update README or docs if behavior changes
- **No breaking changes**: Maintain backward compatibility for public APIs

### Reporting Issues

Found a bug? Open an issue with:

1. **Title**: Clear, specific description
2. **Environment**: Android version, device model, MonsterMind version
3. **Steps to Reproduce**: Exact steps to trigger the issue
4. **Expected vs. Actual**: What should happen vs. what actually happens
5. **Logs**: Logcat output if applicable
6. **Screenshots**: Visual evidence if applicable

### Development Setup

```bash
# Install pre-commit hooks (optional but recommended)
./scripts/install-hooks.sh

# Format code before committing
./gradlew ktlintFormat

# Run all checks
./gradlew build test lint
```

## 📄 License

MonsterMind is distributed under the **MIT License**. See `LICENSE` file for complete details.

### MIT License Summary

- ✅ **Use**: You can use, modify, and distribute MonsterMind for any purpose
- ✅ **Private**: No requirements to share modifications (though we'd appreciate it!)
- ✅ **Commercial**: You can use MonsterMind in commercial projects
- ❌ **Warranty**: No warranty; use at your own risk
- ✅ **Attribution**: Include license and copyright notice in your distributions

### Full License

```
MIT License

Copyright (c) 2024 MonsterMind Contributors

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 🔗 Resources & Documentation

### Official Android Resources
- [Android Developer Documentation](https://developer.android.com/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [ML Kit Text Recognition](https://developers.google.com/ml-kit/vision/text-recognition)

### Kotlin & Coroutines
- [Kotlin Language Documentation](https://kotlinlang.org/docs/)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-overview.html)
- [Flow Documentation](https://kotlinlang.org/docs/flow.html)

### Architecture & Design Patterns
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [MVVM Pattern](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel)
- [Unidirectional Data Flow](https://redux.js.org/understanding/thinking-in-redux)

### Dependency Injection
- [Dagger & Hilt Documentation](https://dagger.dev/)
- [Hilt for Android](https://dagger.dev/hilt/)

### Testing
- [JUnit Documentation](https://junit.org/junit4/)
- [Mockk for Kotlin](https://mockk.io/)
- [Espresso Testing](https://developer.android.com/training/testing/espresso)

## 💬 Community & Support

### Questions & Discussions
- **GitHub Discussions**: Open a discussion for questions, ideas, and feature requests
- **Issues**: Report bugs and feature requests via GitHub Issues
- **Slack/Discord**: Join our community channels (links in GitHub repository description)

### Feedback & Suggestions
We value your feedback! Share your ideas for improving MonsterMind:

1. Open a GitHub Discussion
2. Describe your use case
3. Suggest improvements or new features
4. Vote on existing suggestions

### Getting Help

**Having trouble?**

1. Check this README and contributing guidelines
2. Search existing GitHub Issues
3. Check discussions for similar questions
4. Open a new issue with detailed information

## 🎓 Acknowledgments

MonsterMind is built with gratitude to:

- The Kotlin and Android developer communities
- Google's ML Kit team for powerful on-device ML tools
- The Jetpack Compose team for modern, declarative UI tooling
- Contributors and users who make MonsterMind better

---

## 📈 Project Status

**Current Version**: 1.0.0-beta  
**Last Updated**: 2024  
**Maintenance Status**: Active Development  
**Issue Response Time**: 1-2 weeks  

### Known Limitations

- Handwriting recognition works best for clear, legible writing
- OCR accuracy varies with image quality; camera images > photographs
- Large datasets (1000+ memories) may require device with ≥4GB RAM
- Knowledge graph visualization optimized for up to 500 nodes

### Browser Support (for exported content)

- Chrome/Chromium 90+
- Firefox 88+
- Safari 14+
- Edge 90+

---

**Built with ❤️ for learners who value privacy and offline-first technology.**

**Start learning smarter today.** 🚀

---

*MonsterMind — Your phone remembers. You don't have to.*
