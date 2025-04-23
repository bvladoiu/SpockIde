# Webserver Module

## Overview
The Webserver module is a Kotlin JVM component of the SpockIde project that provides server-side functionality using Ktor. It handles HTTP requests, WebSocket connections, and serves dynamic HTML content.

## Features
- Ktor-based web server implementation
- WebSocket support for real-time communication
- HTML content generation with Kotlinx HTML
- CSS styling with Kotlin CSS

## Usage
This module uses a DSL-style programming approach for defining server routes, handling requests, and generating HTML content. Instead of traditional method naming conventions, it employs a more expressive, domain-specific language style.

### Example (conceptual)
```kotlin
// Instead of traditional method calls like createServer() or defineRoute()
// The DSL style allows for more expressive code:

server {
    port(8080)
    
    routing {
        get("/") {
            respondHtml {
                body {
                    h1 { +"Welcome to SpockIde" }
                    p { +"A live, in-browser page editor" }
                }
            }
        }
        
        webSocket("/edit") {
            for (frame in incoming) {
                // Handle WebSocket frames
                when (frame) {
                    is Frame.Text -> {
                        // Process text frame
                    }
                }
            }
        }
    }
}
```

## Dependencies
- Ktor Server Core
- Ktor WebSockets
- Ktor HTML Builder
- Kotlinx HTML
- Kotlin CSS
- Ktor CIO (Coroutine-based I/O)
- Logback Classic (for logging)

## Building and Running
This module can be built and run using Gradle:
```
./gradlew :webserver:build
./gradlew :webserver:run
```

The server will start with the main class `spock.lair.ApplicationKt`.