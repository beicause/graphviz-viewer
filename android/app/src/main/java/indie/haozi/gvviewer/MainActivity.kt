package indie.haozi.gvviewer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.getcapacitor.BridgeActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import java.io.BufferedReader
import java.io.InputStreamReader

var mIntentViewStr: String? = null
var mInterstitialAd: InterstitialAd? = null
private const val TAG = "MainActivity"

class MainActivity : BridgeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        registerPlugin(MPlugin::class.java)
        super.onCreate(savedInstanceState)
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-9433844779206817/2773798188", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError.toString())
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        receiveIntent(intent)
    }

    private fun receiveIntent(intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_VIEW -> {
                val str = contentResolver.openInputStream(intent.data!!)!!.let {
                    val reader = BufferedReader(InputStreamReader(it))
                    val s = reader.readLines().joinToString("\n")
                    reader.close()
                    it.close()
                    s
                }
                mIntentViewStr = str
                Log.d("Receive intent view", str)
            }
        }
    }
}