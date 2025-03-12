import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.pahelp.viewModel.ChatViewModel

@Composable
fun SetWallpaper(viewModel: ChatViewModel) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                viewModel.wallpaperUri.value = uri
            }
        }
    )

    Button(onClick = {
        launcher.launch("image/*")
    }) {
        Text("Set Wallpaper")
    }
}