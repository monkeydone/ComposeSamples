package com.a.compose

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.text.HtmlCompat
import com.a.compose.component.SampleAndroidView

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val container = findViewById<ComposeView>(R.id.compose_view)
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

        container.setContent {
            Column() {
                ShapeDemo()
                PlantDescription(context)
            }
        }
        val container2 = findViewById<ComposeView>(R.id.compose_view2)
        container2.setContent {
            PlantDescriptionV2()
//            AndroidViewBinding(SampleAndroidView::inflate)
//            AndroidViewBinding(MainActivity2::getLayoutInflater)

        }


    }
}


@Composable
private fun PlantDescriptionV2() {
    AndroidView(
        factory = { context ->
            SampleAndroidView(context).apply { }
        },
        update = {
            it.setBackgroundColor(Color.RED)
//            it.setTitle(description)
        }
    )
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
