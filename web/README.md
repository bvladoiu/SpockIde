# Web Module

## Overview
The Web module is a Kotlin Multiplatform component of the SpockIde project that provides web-related functionality. It targets both JVM and JavaScript platforms, with a focus on browser interactions.

## Features
- Kotlin Multiplatform setup with JVM and JS targets
- Browser-focused JavaScript implementation
- Integration with Kotlin browser wrappers

## Usage
This module uses a DSL-style programming approach for creating web components. Instead of traditional method naming conventions, it employs a more expressive, domain-specific language style.

### Example (conceptual)
```kotlin
// Instead of traditional method calls like buildPage() or createElement()
// The DSL style allows for more expressive code:

page {
    header {
        title("My Web Page")
    }
    
    section("main-content") {
        paragraph {
            text("This is a sample paragraph")
        }
        
        button("Click me") {
            onClick {
                // Handle click event
            }
        }
    }
    
    footer {
        copyright("© 2023")
    }
}
```

## Dependencies
- Kotlin Multiplatform
- Kotlin Wrappers for browser JS

## Building
This module can be built using Gradle:
```
./gradlew :web:build
```