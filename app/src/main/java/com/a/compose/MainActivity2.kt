package com.a.compose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val container = findViewById<ComposeView>(R.id.compose_view)
        container.setContent {
            Column() {
                ShapeDemo()
                val context = """
                    private fun PlantDescription(description: String) {
                        // Remembers the HTML formatted description. Re-executes on a new description
                        val htmlDescription = remember(description) {
                            HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
                        }

                        // Displays the TextView on the screen and updates with the HTML description when inflated
                        // Updates to htmlDescription will make AndroidView recompose and update the text
                        AndroidView(
                            factory = { context ->
                                TextView(context).apply {
                                    movementMethod = LinkMovementMethod.getInstance()
                                }
                            },
                            update = {
                                it.text = htmlDescription
                            }
                        )
                    }
                """.trimIndent()

                PlantDescription(context)

            }
        }
    }
}


@Composable
private fun PlantDescription(description: String) {
    // Remembers the HTML formatted description. Re-executes on a new description
    val htmlDescription = remember(description) {
        HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    // Displays the TextView on the screen and updates with the HTML description when inflated
    // Updates to htmlDescription will make AndroidView recompose and update the text
    AndroidView(
        factory = { context ->
            TextView(context).apply {
                movementMethod = LinkMovementMethod.getInstance()
            }
        },
        update = {
            it.text = htmlDescription
        }
    )
}
