package spock.lair.html

import kotlinx.css.footer
import kotlinx.html.*
import spock.lair.PageObject
import spock.lair.components.themeSwitch


fun FlowContent.scaffold(page: PageObject, block: FlowContent.() -> Unit) {
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
          block.invoke(this)
        }
        footer
}
