# Browser Module

## Overview
The Browser module is a Kotlin Multiplatform component of the SpockIde project that provides browser automation and interaction capabilities. It targets both JVM and JavaScript platforms, enabling seamless integration between server-side browser control and client-side browser functionality.

## Features
- Kotlin Multiplatform setup with JVM and JS targets
- Browser automation using Microsoft Playwright
- Ktor-based client-server communication
- WebSocket support for real-time interaction
- Native hooks for system-level integration

## Usage
This module uses a DSL-style programming approach for browser automation and interaction. Instead of traditional method naming conventions, it employs a more expressive, domain-specific language style.

### Example (conceptual)
```kotlin
// Instead of traditional method calls like createBrowser() or navigateTo()
// The DSL style allows for more expressive code:

browser {
    page("https://example.com") {
        // Wait for page to load
        waitForSelector("#main-content")
        
        // Interact with elements
        element("#search-input") {
            type("SpockIde")
            press(Key.ENTER)
        }
        
        // Capture screenshots
        screenshot("search-results.png")
        
        // Execute JavaScript
        evaluate("""
            document.querySelector('.results').style.backgroundColor = 'yellow';
        """)
        
        // Listen for events
        onDialog {
            accept()
        }
    }
}
```

## JVM and JS Integration
The module provides seamless integration between JVM and JS components:

- **JVM Side**: Controls browser instances, manages automation, and handles server-side logic
- **JS Side**: Interacts with the browser DOM, handles events, and communicates with the JVM side via WebSockets

## Dependencies
### Common
- Kotlinx Coroutines Core

### JVM
- Microsoft Playwright for browser automation
- Ktor Server and Client components
- Native Hooks for system integration
- Logback for logging

### JS
- Ktor Client for JS
- Ktor WebSockets
- Kotlin Wrappers for browser

## Building and Running
This module can be built and run using Gradle:
```
./gradlew :browser:build
./gradlew :browser:jvmRun
```

The JVM application will start with the main class `spock.lair.JvmMainKt`.