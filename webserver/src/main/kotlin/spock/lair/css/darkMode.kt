package spock.lair.css

import kotlinx.css.*

fun darkModeStyles(): CSSBuilder {
    return CSSBuilder().apply {
        // Basic element styling for dark mode
        rule("body.dark") {
            backgroundColor = Color("var(--color-bg)")
            color = Color("var(--color)")
        }

        rule("body.dark a") {
            color = Color("var(--color-primary-light)")

            hover {
                color = Color.white
            }
        }

        rule("body.dark .button") {
            backgroundColor = Color("var(--color-primary-dark)")
            color = Color.white

            hover {
                backgroundColor = Color("var(--color-primary)")
            }
        }

        rule("body.dark .section") {
            backgroundColor = Color("var(--color-bg-light)")
        }

        rule("body.dark .post-card, body.dark .person-card, body.dark .competence-item") {
            backgroundColor = Color("var(--color-bg-light)")
            put("box-shadow", "var(--shadow-md)")

            hover {
                put("box-shadow", "var(--shadow-lg)")
            }
        }

        rule("body.dark .social-link") {
            backgroundColor = Color("#444444")
            color = Color("var(--color-light)")

            hover {
                backgroundColor = Color("var(--color-primary)")
                color = Color.white
            }
        }

        // Toggle button for dark mode
        rule(".theme-toggle") {
            cursor = Cursor.pointer
            display = Display.inlineBlock
            put("padding", "var(--spacing-xs) var(--spacing-sm)")
            put("border-radius", "var(--border-radius-sm)")
            backgroundColor = Color("var(--color-bg-light)")
            color = Color("var(--color)")
            put("transition", "all var(--transition-fast) ease")

            hover {
                backgroundColor = Color("var(--color-primary-light)")
            }
        }

        rule("body.dark .theme-toggle") {
            backgroundColor = Color("var(--color-bg-light)")
            color = Color("var(--color)")

            hover {
                backgroundColor = Color("var(--color-primary-dark)")
                color = Color.white
            }
        }

        // JavaScript toggle function
        rule("script.theme-toggle-script") {
            put("display", "none")
            put("content", """
                function toggleDarkMode() {
                    document.body.classList.toggle('dark');
                    localStorage.setItem('darkMode', document.body.classList.contains('dark'));
                }

                // Check for saved dark mode preference
                document.addEventListener('DOMContentLoaded', function() {
                    if (localStorage.getItem('darkMode') === 'true') {
                        document.body.classList.add('dark');
                    }

                    // Add event listeners to any theme toggle buttons
                    const toggles = document.querySelectorAll('.theme-toggle');
                    toggles.forEach(toggle => {
                        toggle.addEventListener('click', toggleDarkMode);
                    });
                });
            """
            )
        }
    }
}
