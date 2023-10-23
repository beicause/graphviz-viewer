package indie.haozi.gvviewer

import android.content.res.Configuration
import android.os.Build
import android.util.Log
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback

private const val TAG = "MPlugin"

@CapacitorPlugin(name = "M")
class MPlugin : Plugin() {
    init {
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                this@MPlugin.notifyListeners("onAdClicked", JSObject())
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.")
            }

            override fun onAdDismissedFullScreenContent() {
                this@MPlugin.notifyListeners("onAdClicked", JSObject())
                // Called when ad is dismissed.
                Log.d(TAG, "Ad dismissed fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                this@MPlugin.notifyListeners("onAdClicked", JSObject())
                // Called when ad fails to show.
                Log.e(TAG, "Ad failed to show fullscreen content.")
                mInterstitialAd = null
            }

            override fun onAdImpression() {
                this@MPlugin.notifyListeners("onAdClicked", JSObject())
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "Ad recorded an impression.")
            }

            override fun onAdShowedFullScreenContent() {
                this@MPlugin.notifyListeners("onAdClicked", JSObject())
                // Called when ad is shown.
                Log.d(TAG, "Ad showed fullscreen content.")
            }
        }
    }

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
            put("data", mIntentViewStr)
        }
        call.resolve(json)
        mIntentViewStr = null
    }

    @PluginMethod
    fun showInterstitialAd(call: PluginCall) {
        Log.d(TAG, "mInterstitialAd show ad")
        mInterstitialAd?.let {
            activity.runOnUiThread { mInterstitialAd?.show(activity) }
        }
        call.resolve()
    }
}