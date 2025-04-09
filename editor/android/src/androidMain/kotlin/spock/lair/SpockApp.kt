package spock.lair

import android.app.Application
import android.content.Context

class SpockApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: SpockApp
        val context: Context
            get() = instance.applicationContext
    }
}