package spock.lair.tools.shortcuts

import com.github.kwhat.jnativehook.GlobalScreen
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener
import java.util.logging.LogManager

object Shortcuts : NativeKeyListener {

    private val actions = mutableMapOf<Int, () -> Unit>()

    init {
        LogManager.getLogManager().reset()
        GlobalScreen.registerNativeHook()
        GlobalScreen.addNativeKeyListener(this)
    }

    fun on(keyCode: Int, action: () -> Unit) {
        actions[keyCode] = action
    }

    override fun nativeKeyPressed(e: NativeKeyEvent) {
        if (e.modifiers == 3) {
            when (e.keyCode) {
                NativeKeyEvent.VC_D -> {
                    println("d key pressed!")
                    actions[e.keyCode]?.invoke()
                }
            }
        }
    }
}