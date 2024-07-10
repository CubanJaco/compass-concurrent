package cu.jaco.compassconcurrent.feature.main.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cu.jaco.compassconcurrent.ui.theme.CompassConcurrentTheme

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
) {
    CompassConcurrentTheme {
        Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
            Greeting(
                name = "Android",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    CompassConcurrentTheme {
        Greeting("Android")
    }
}