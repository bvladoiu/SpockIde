package spock.lair.html

import kotlinx.css.footer
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.header
import kotlinx.html.li
import kotlinx.html.main
import kotlinx.html.nav
import kotlinx.html.section
import kotlinx.html.ul


fun HTML.scaffold() {
    head {

    }
    body {
        header {
            logoLink()
            nav {
                ul {
                    li { "Home" }
                    li { "Press" }
                    li { "Dev-Blog" }
                    li { "Company" }
                }
            }
            themeSwitch()
            langSelect()
            cta()
        }
        main {
            section("hero") { }
            section("competences") { }
            section("latest") { }
        }
        footer

    }

}