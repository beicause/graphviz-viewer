package indie.haozi.gvviewer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.getcapacitor.BridgeActivity
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

var intentViewStr: String? = null

class MainActivity : BridgeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        registerPlugin(MPlugin::class.java)
        super.onCreate(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
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
}