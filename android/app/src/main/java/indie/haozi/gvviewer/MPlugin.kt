package indie.haozi.gvviewer

import android.content.res.Configuration
import android.os.Build
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin

@CapacitorPlugin(name = "M")
class MPlugin : Plugin() {
    @PluginMethod
    fun getSystemTheme(call: PluginCall) {
        val json = JSObject().apply {
            val isDark =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) context.resources.configuration.isNightModeActive
                else ((context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES)
            put("isDark", isDark)
        }
        call.resolve(json)
    }

    @PluginMethod
    fun getIntentViewText(call: PluginCall) {
        val json = JSObject().apply {
            put("data", intentViewStr)
        }
        call.resolve(json)
        intentViewStr = null
    }
}