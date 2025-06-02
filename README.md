NY Times App
A modern Android application that displays articles from The New York Times API, built with Clean Architecture principles and modern Android development practices.

📱 Screenshots
<div align="center"> <table> <tr> <td align="center"> <img src="https://drive.google.com/uc?export=view&id=1mYkG8L6QBUyu2cM9EYdUS-5mewHJcVnS" alt="Articles List" width="300"/> <br><b>Articles List</b> </td> <td align="center"> <img src="https://drive.google.com/uc?export=view&id=1XPFhExcV28EGnB-jwkFdW4lV0e7CU2LA" alt="Article Details" width="300"/> <br><b>Article Details</b> </td> </tr> </table> </div> <!-- Alternative: If Google Drive doesn't work, use this format: | Articles List | Article Details | |:---:|:---:| | ![Articles List](screenshots/articles_listd in the `screenshots/` folder of this repository. -->
✨ Features
📰 Article Browsing: View most popular articles from NY Times

🔍 Search Functionality: Search articles by keywords

🏷️ Advanced Filtering: Filter articles by section and media presence

📊 Smart Sorting: Sort articles by views, date, and title

📖 Detailed View: View comprehensive article information

🎨 Modern UI: Responsive design with smooth animations

⚠️ Error Handling: User-friendly error messages and states

🏗️ Architecture
The project follows Clean Architecture principles and is organized into modular components:

Core Module
Contains shared infrastructure and utilities:

Base Classes: Repository, UseCase, and ViewModel foundations

Network Layer: OkHttp and Retrofit configuration

Error Management: Centralized error handling and mapping

Resource Wrapper: State management for data operations

Dependency Injection: Hilt setup and modules

Features Module
Feature-specific implementations following Clean Architecture:

Domain Layer: Business logic, entities, and use cases

Data Layer: Repositories, data sources, and API models

Presentation Layer: ViewModels, UI components, and screens

🛠️ Technical Stack
Category	Technology
Language	Kotlin
Architecture	Clean Architecture + MVI
Dependency Injection	Hilt
Networking	Retrofit2 + OkHttp3
Asynchronous	Kotlin Coroutines + Flow
Image Loading	Coil
UI Framework	Material Design Components
Testing	JUnit, Mockito, Espresso
📁 Project Structure
text
NYTimesApp/
├── 📦 core/                    # Core infrastructure module
│   ├── 🏗️ base/               # Base classes and interfaces
│   ├── 💉 di/                 # Dependency injection modules
│   ├── ⚠️ error/              # Error handling utilities
│   ├── 🌐 network/            # Network configuration
│   └── 🔧 utils/              # Common utility functions
│
├── 🎯 features/               # Feature modules
│   └── 📰 articles/           # Articles feature
│       ├── 💾 data/           # Data layer implementation
│       ├── 🧠 domain/         # Business logic layer
│       └── 🎨 presentation/   # UI layer
│
└── 📱 app/                    # Application module
    ├── 🚀 MainActivity
    └── 📋 Application class
🚀 Getting Started
Prerequisites
Android Studio Arctic Fox or later

JDK 11 or higher

Android SDK 21+

Installation
Clone the repository:

bash
git clone https://github.com/AliAhmedEissa/Boubyan-Task.git
cd Boubyan-Task
Open in Android Studio:

Launch Android Studio

Select "Open an existing project"

Navigate to the cloned directory

Configure API Key:

Create local.properties in the root directory

Add your NY Times API key:

text
NYTIMES_API_KEY=your_api_key_here
Build and Run:

Sync project with Gradle files

Run the application on device/emulator

🔑 API Key Setup
To use this application, you need a NY Times API key:

Register: Visit NY Times Developer Portal

Create Account: Sign up and verify your email

Register App: Create a new application

Get API Key: Copy your assigned API key

Configure: Add to local.properties file

API Endpoint Used:

text
https://api.nytimes.com/svc/mostpopular/v2/viewed/{period}.json?api-key={your-key}
🧪 Testing
The project includes comprehensive testing:

Unit Tests
bash
./gradlew test
Instrumentation Tests
bash
./gradlew connectedAndroidTest
Test Coverage
Domain layer: Use cases and business logic

Data layer: Repository implementations

Presentation layer: ViewModel behavior

🤝 Contributing
We welcome contributions! Please follow these steps:

Fork the repository

Create your feature branch:

bash
git checkout -b feature/amazing-feature
Commit your changes:

bash
git commit -m 'Add some amazing feature'
Push to the branch:

bash
git push origin feature/amazing-feature
Open a Pull Request

Contribution Guidelines
Follow existing code style and architecture

Add tests for new features

Update documentation as needed

Ensure all tests pass before submitting

📄 License
This project is licensed under the MIT License - see the LICENSE file for details.

🙏 Acknowledgments
The New York Times for providing the comprehensive API

Android Community for excellent development resources

Contributors who have helped improve this project

<div align="center"> <b>Built with ❤️ using modern Android development practices</b> </div>
