# NY Times App

A modern Android application that displays articles from The New York Times API, built with Clean Architecture principles and modern Android development practices.

## Features

- View most popular articles from NY Times
- Search articles by keywords
- Filter articles by section and media presence
- Sort articles by views, date, and title
- View detailed article information
- Responsive and modern UI with animations
- Offline support with caching
- Error handling and user-friendly messages

## Architecture

The project follows Clean Architecture principles and is organized into the following modules:

### Core Module

Contains base classes and utilities used across the application:

- Base classes for Repository, UseCase, and ViewModel
- Network handling with OkHttp and Retrofit
- Error handling and mapping
- Resource wrapper for handling data states
- Dependency injection setup

### Features Module

Contains feature-specific implementations:

- Articles feature with list and detail views
- Domain layer with entities and use cases
- Data layer with repositories and data sources
- Presentation layer with ViewModels and UI components

## Technical Stack

- **Language**: Kotlin
- **Architecture**: Clean Architecture with MVVM
- **Dependency Injection**: Hilt
- **Networking**: Retrofit2 + OkHttp3
- **Asynchronous**: Kotlin Coroutines + Flow
- **Image Loading**: Coil
- **UI**: Material Design Components
- **Testing**: JUnit, Mockito, Espresso

## Project Structure

```
NYTimesApp/
├── core/                      # Core module
│   ├── base/                 # Base classes
│   ├── di/                   # Dependency injection
│   ├── error/               # Error handling
│   ├── network/             # Network related code
│   └── utils/               # Utility functions
│
├── features/                 # Features module
│   └── articles/            # Articles feature
│       ├── data/           # Data layer
│       ├── domain/         # Domain layer
│       └── presentation/   # Presentation layer
│
└── app/                     # Application module
```

## Setup

1. Clone the repository:

```bash
git clone https://github.com/yourusername/NYTimesApp.git
```

2. Open the project in Android Studio

3. Add your NY Times API key:

   - Create a file named `local.properties` in the root directory
   - Add your API key: `NYTIMES_API_KEY=your_api_key_here`

4. Build and run the project

## API Key

To use this app, you need to obtain an API key from The New York Times:

1. Visit [NY Times Developer Portal](https://developer.nytimes.com/)
2. Create an account and register your application
3. Get your API key
4. Add it to the `local.properties` file

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- The New York Times for providing the API
- All contributors who have helped shape this project
