package dvpn.libtailscale.example

import android.app.Activity
import android.content.Intent
import android.net.VpnService
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import app.dvpn.libtailscale.Tailscale
import app.dvpn.libtailscale.service.TailscaleVPNService
import dvpn.libtailscale.example.service.DVPNService
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val tailscale = koinInject<Tailscale>()
    val scope = rememberCoroutineScope()
    var isVpnActive by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    val vpnPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // VPN permission granted, start the service
            scope.launch {
                runCatching {
                    tailscale.login("your-auth-key")
                    tailscale.useExitNode("your-exit-node-id")
                    val intent = Intent(context, DVPNService::class.java).apply {
                        action = TailscaleVPNService.ActionStartVPN
                    }
                    context.startService(intent)
                }
            }
        } else {
            // Permission denied, reset the switch
            isVpnActive = false
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .background(Color.White)
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Enable VPN")
            Switch(
                checked = isVpnActive,
                onCheckedChange = { checked ->
                    isVpnActive = checked
                    if (checked) {
                        // Check if VPN permission is needed
                        val vpnIntent = VpnService.prepare(context)
                        if (vpnIntent != null) {
                            // Need to request VPN permission
                            vpnPermissionLauncher.launch(vpnIntent)
                        } else {
                            // Already have permission, start the service
                            scope.launch {
                                runCatching {
                                    tailscale.login("your-auth-key")
                                    tailscale.useExitNode("your-exit-node-id")
                                    val intent = Intent(context, DVPNService::class.java).apply {
                                        action = TailscaleVPNService.ActionStartVPN
                                    }
                                    context.startService(intent)
                                }
                            }
                        }
                    } else {
                        val intent = Intent(context, DVPNService::class.java).apply {
                            action = TailscaleVPNService.ActionStopVPN
                        }
                        context.stopService(intent)
                    }
                }
            )
        }
    }
}
