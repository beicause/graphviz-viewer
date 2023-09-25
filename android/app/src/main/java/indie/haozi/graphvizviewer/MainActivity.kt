package indie.haozi.graphvizviewer

import android.os.Bundle
import com.getcapacitor.BridgeActivity

class MainActivity : BridgeActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        registerPlugin(MPlugin::class.java)
        super.onCreate(savedInstanceState)
    }
}