
# Live In-Browser Page Editor Implementation Plan

Based on my analysis of your project structure, I can help you implement the live in-browser page editor using Ktor and Kotlin/JS as described in your plan. Here's how we'll approach this:

## Current Project Status

Your project already has the basic structure in place:
- Ktor server configured with HTML DSL support
- Kotlin/JS module for client-side logic
- Build system set up to compile JS and serve it from the static directory
- Basic routing and templating modules

## Implementation Steps

### Phase 1: Setup & Basic Rendering

#### 1. Define Server-Side Components (HTML DSL)
Add these functions to `Templating.kt`:

```kotlin
// In Templating.kt
fun FlowContent.heroSection(id: String, title: String) {
    div {
        attributes["data-component"] = "heroSection"
        attributes["data-id"] = id
        h1 {
            attributes["data-editable"] = "true"
            +title
        }
    }
}

fun FlowContent.contentSection(id: String, title: String, initialContent: String) {
    div {
        attributes["data-component"] = "contentSection"
        attributes["data-id"] = id
        h2 {
            attributes["data-editable"] = "true"
            +title
        }
        p {
            attributes["data-editable"] = "true"
            +initialContent
        }
    }
}
```

#### 2. Render Initial Page Structure
Modify `Routing.kt` to use these components:

```kotlin
// In Routing.kt
get("/home") {
    call.respondHtml {
        head {
            title { +"Page Editor" }
            link(rel = "stylesheet", href = "/static/styles.css", type = "text/css")
        }
        body {
            div {
                id = "page-content"
                heroSection("hero-1", "Welcome to the Page Editor")
                contentSection("section-1", "First Section", "This is the content of the first section.")
                contentSection("section-2", "Second Section", "This is the content of the second section.")
            }
            script(src = "/static/web.js") {
                attributes["defer"] = "true"
            }
        }
    }
}
```

#### 3. Create Base CSS
Create a file `static/styles.css`:

```css
:root {
    --theme-background: #ffffff;
    --theme-text: #333333;
}

[data-theme="dark"] {
    --theme-background: #333333;
    --theme-text: #ffffff;
}

body {
    font-family: Arial, sans-serif;
    background-color: var(--theme-background);
    color: var(--theme-text);
    margin: 0;
    padding: 20px;
    transition: all 0.3s ease;
}

#page-content {
    max-width: 800px;
    margin: 0 auto;
}

[data-component="heroSection"] {
    padding: 40px 20px;
    background-color: #f5f5f5;
    margin-bottom: 20px;
    border-radius: 8px;
}

[data-component="contentSection"] {
    padding: 20px;
    background-color: #ffffff;
    margin-bottom: 20px;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
}

[data-editable="true"]:hover {
    outline: 2px dashed #007bff;
    cursor: pointer;
}

.is-editing [data-editable="true"] {
    outline: 2px solid #007bff;
}

.add-button, .remove-button {
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 50%;
    width: 30px;
    height: 30px;
    font-size: 18px;
    cursor: pointer;
    margin: 5px;
    display: none;
}

.is-editing .add-button, .is-editing .remove-button {
    display: inline-block;
}
```

#### 4. Basic Client-Side Setup
Update `JsMain.kt`:

```kotlin
// In JsMain.kt
package spock.lair.ktor

import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.*

fun main() {
    window.onload = {
        setupEditor()
    }
}

fun setupEditor() {
    val pageContent = document.getElementById("page-content")
    
    // Set up keyboard shortcuts
    document.addEventListener("keydown", { event ->
        val e = event as KeyboardEvent
        when (e.key) {
            "F3" -> { enableEditMode(); e.preventDefault() }
            "F4" -> { disableEditMode(); e.preventDefault() }
            "F2" -> { toggleTheme(); e.preventDefault() }
            "F5" -> { savePage(); e.preventDefault() }
            "F8" -> { loadPage(); e.preventDefault() }
            "F9" -> { deployPage(); e.preventDefault() }
        }
    })
    
    console.log("Editor initialized")
}
```

### Phase 2: Core Editing Functionality

#### 5. Edit Mode (F3/F4)
Add to `JsMain.kt`:

```kotlin
fun enableEditMode() {
    val pageContent = document.getElementById("page-content") ?: return
    pageContent.classList.add("is-editing")
    
    // Make editable elements actually editable
    pageContent.querySelectorAll("[data-editable='true']").forEach { element ->
        element.setAttribute("contenteditable", "true")
    }
    
    // Add component controls
    addComponentControls()
    
    console.log("Edit mode enabled")
}

fun disableEditMode() {
    val pageContent = document.getElementById("page-content") ?: return
    pageContent.classList.remove("is-editing")
    
    // Make elements non-editable
    pageContent.querySelectorAll("[data-editable='true']").forEach { element ->
        element.removeAttribute("contenteditable")
    }
    
    // Remove all control buttons
    document.querySelectorAll(".add-button, .remove-button").forEach { it.remove() }
    
    console.log("Edit mode disabled")
}

fun addComponentControls() {
    val pageContent = document.getElementById("page-content") ?: return
    
    // Add remove buttons to components
    pageContent.querySelectorAll("[data-component]").forEach { component ->
        val removeBtn = document.createElement("button") as HTMLButtonElement
        removeBtn.className = "remove-button"
        removeBtn.textContent = "-"
        removeBtn.onclick = { component.remove() }
        component.appendChild(removeBtn)
    }
    
    // Add "add" buttons between components
    val components = pageContent.querySelectorAll("[data-component]")
    components.forEach { component ->
        val addBtn = document.createElement("button") as HTMLButtonElement
        addBtn.className = "add-button"
        addBtn.textContent = "+"
        addBtn.onclick = { 
            addComponent("contentSection", component)
        }
        component.parentNode?.insertBefore(addBtn, component.nextSibling)
    }
}
```

#### 6. Add/Remove Elements
Add to `JsMain.kt`:

```kotlin
fun addComponent(type: String, targetElement: Element) {
    val newId = "component-${Date.now()}"
    val newElement = document.createElement("div")
    
    newElement.setAttribute("data-component", type)
    newElement.setAttribute("data-id", newId)
    
    when (type) {
        "heroSection" -> {
            newElement.innerHTML = """
                <h1 data-editable="true">New Hero Title</h1>
            """.trimIndent()
        }
        "contentSection" -> {
            newElement.innerHTML = """
                <h2 data-editable="true">New Section</h2>
                <p data-editable="true">New content goes here.</p>
            """.trimIndent()
        }
    }
    
    // Add remove button
    val removeBtn = document.createElement("button") as HTMLButtonElement
    removeBtn.className = "remove-button"
    removeBtn.textContent = "-"
    removeBtn.onclick = { newElement.remove() }
    newElement.appendChild(removeBtn)
    
    // Insert after target
    targetElement.parentNode?.insertBefore(newElement, targetElement.nextSibling)
    
    // Add an "add" button after the new component
    val addBtn = document.createElement("button") as HTMLButtonElement
    addBtn.className = "add-button"
    addBtn.textContent = "+"
    addBtn.onclick = { 
        addComponent("contentSection", newElement)
    }
    newElement.parentNode?.insertBefore(addBtn, newElement.nextSibling)
    
    // Make the new elements editable
    newElement.querySelectorAll("[data-editable='true']").forEach { element ->
        element.setAttribute("contenteditable", "true")
    }
}
```

#### 7. Theme Toggling
Add to `JsMain.kt`:

```kotlin
fun toggleTheme() {
    val body = document.body
    if (body?.getAttribute("data-theme") == "dark") {
        body.removeAttribute("data-theme")
    } else {
        body?.setAttribute("data-theme", "dark")
    }
    console.log("Theme toggled")
}
```

### Phase 3: Persistence & Deployment

#### 8. Quick Save (F5)
Add to `JsMain.kt`:

```kotlin
fun savePage() {
    val pageContent = document.getElementById("page-content") ?: return
    val content = pageContent.innerHTML
    
    // Remove editing artifacts before saving
    val tempDiv = document.createElement("div")
    tempDiv.innerHTML = content
    tempDiv.querySelectorAll(".add-button, .remove-button").forEach { it.remove() }
    
    val cleanContent = tempDiv.innerHTML
    
    fetch("/api/save/home", {
        method = "POST",
        body = cleanContent
    }).then { response ->
        if (response.ok) {
            console.log("Page saved successfully")
        } else {
            console.error("Failed to save page")
        }
    }
}
```

Add to `Routing.kt`:

```kotlin
post("/api/save/{pageName}") {
    val pageName = call.parameters["pageName"] ?: return@post call.respondText("Missing page name", status = HttpStatusCode.BadRequest)
    val content = call.receiveText()
    
    val file = File("static/$pageName.html")
    file.writeText(content)
    
    call.respondText("Page saved successfully", status = HttpStatusCode.OK)
}
```

#### 9. Quick Load (F8)
Add to `JsMain.kt`:

```kotlin
fun loadPage() {
    fetch("/api/load/home").then { response ->
        if (response.ok) {
            response.text().then { content ->
                val pageContent = document.getElementById("page-content")
                pageContent?.innerHTML = content
                
                // Rebind controls if in edit mode
                if (pageContent?.classList?.contains("is-editing") == true) {
                    rebindEditingControls()
                }
                
                console.log("Page loaded successfully")
            }
        } else {
            console.error("Failed to load page")
        }
    }
}

fun rebindEditingControls() {
    // Re-add component controls if we're in edit mode
    addComponentControls()
    
    // Make editable elements actually editable again
    val pageContent = document.getElementById("page-content")
    pageContent?.querySelectorAll("[data-editable='true']")?.forEach { element ->
        element.setAttribute("contenteditable", "true")
    }
}
```

Add to `Routing.kt`:

```kotlin
get("/api/load/{pageName}") {
    val pageName = call.parameters["pageName"] ?: return@get call.respondText("Missing page name", status = HttpStatusCode.BadRequest)
    
    val file = File("static/$pageName.html")
    if (!file.exists()) {
        call.respondText("Page not found", status = HttpStatusCode.NotFound)
        return@get
    }
    
    val content = file.readText()
    call.respondText(content, ContentType.Text.Html)
}
```

#### 10. Deploy Trigger (F9)
Add to `JsMain.kt`:

```kotlin
fun deployPage() {
    fetch("/api/deploy/home", {
        method = "POST"
    }).then { response ->
        if (response.ok) {
            console.log("Deployment triggered successfully")
        } else {
            console.error("Failed to trigger deployment")
        }
    }
}
```

Add to `Routing.kt`:

```kotlin
post("/api/deploy/{pageName}") {
    val pageName = call.parameters["pageName"] ?: return@post call.respondText("Missing page name", status = HttpStatusCode.BadRequest)
    
    // For now, just log the deployment trigger
    application.log.info("Deployment triggered for $pageName")
    
    // Read the content from the saved file
    val file = File("static/$pageName.html")
    if (!file.exists()) {
        call.respondText("Page not found", status = HttpStatusCode.NotFound)
        return@post
    }
    
    // In a real implementation, you would process the HTML and deploy it
    // For now, we'll just mark it as deployable
    val deployableFile = File("static/$pageName.deployable.html")
    deployableFile.writeText(file.readText())
    
    call.respondText("Deployment triggered successfully", status = HttpStatusCode.OK)
}
```

## Running the Application

To run the application:

1. Execute the Gradle task `copyJsStaticForDev` to build the JS bundle and copy it to the static directory
2. Execute the Gradle task `runWebserverDev` to start the Ktor server
3. Navigate to `http://localhost:8080/home` in your browser

## Keyboard Shortcuts

- F2: Toggle theme (light/dark)
- F3: Enable edit mode
- F4: Disable edit mode
- F5: Save page
- F8: Load page
- F9: Deploy page

This implementation follows your plan and provides a simple, direct editing experience with server-side rendering for the initial state and client-side editing using Kotlin/JS.