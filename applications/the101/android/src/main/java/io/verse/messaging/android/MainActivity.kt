package io.verse.messaging.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.tagd.android.app.AppCompatActivity
import io.verse.messaging.android.pull.PullMessageRequest
import io.verse.messaging.android.pull.defaultPullService
import io.verse.messaging.core.inapp.InAppMessage
import io.verse.messaging.core.inapp.InAppMessageWidget
import io.verse.messaging.core.inapp.defaultInAppMessageService

class MainActivity : AppCompatActivity() {

    override fun onCreateView(savedInstanceState: Bundle?) {
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Button(onClick = {
                            sendInAppMessage(HelloWorldMessage("Hello World!"))
                        }) {
                            GreetingView("greet")
                        }
                    }
                }
            }
        }
        fetchPullMessages()
    }

    private fun fetchPullMessages() {
        defaultPullService()
            ?.fetch(
                request = PullMessageRequest("/pull-messaging"),
                success = {
                    println("Response: $it")
                },
                failure = {
                    it.printStackTrace()
                }
            )
    }

    private fun <W : InAppMessageWidget> sendInAppMessage(message: InAppMessage<W>) {
        defaultInAppMessageService()?.publish(message)
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
