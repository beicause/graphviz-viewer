package indie.haozi.gvviewer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.getcapacitor.BridgeActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

var intentViewStr: String? = null
private const val AD_UNIT_ID = "ca-app-pub-9433844779206817/3683529521"

class MainActivity : BridgeActivity(), OnUserEarnedRewardListener {
    private final var TAG = "MainActivity"
    private var rewardedInterstitialAd:RewardedInterstitialAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        registerPlugin(MPlugin::class.java)
        super.onCreate(savedInstanceState)
    }

    private fun loadAd() {
        RewardedInterstitialAd.load(this, AD_UNIT_ID,
                    AdRequest.Builder().build(), object : RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedInterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    rewardedInterstitialAd = ad
                    rewardedInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                        override fun onAdClicked() {
                            // Called when a click is recorded for an ad.
                            Log.d(TAG, "Ad was clicked.")
                        }

                        override fun onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            // Set the ad reference to null so you don't show the ad a second time.
                            Log.d(TAG, "Ad dismissed fullscreen content.")
                            rewardedInterstitialAd = null
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            // Called when ad fails to show.
                            Log.e(TAG, "Ad failed to show fullscreen content.")
                            rewardedInterstitialAd = null
                        }

                        override fun onAdImpression() {
                            // Called when an impression is recorded for an ad.
                            Log.d(TAG, "Ad recorded an impression.")
                        }

                        override fun onAdShowedFullScreenContent() {
                            // Called when ad is shown.
                            Log.d(TAG, "Ad showed fullscreen content.")
                        }
                    }

                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.toString())
                    rewardedInterstitialAd = null
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
                intentViewStr = str
                Log.d("Receive intent view", str)
            }
        }
    }

    override fun onUserEarnedReward(p0: RewardItem) {
        Log.d(TAG, "User earned reward.")
        TODO("Not yet implemented")
    }
}