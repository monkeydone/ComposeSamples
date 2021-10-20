package com.a.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.a.compose.component.SampleItem
import com.a.compose.component.SampleList

@Composable
fun BaselineShiftSample() {
    Text(
        fontSize = 20.sp,
        text = buildAnnotatedString {
            append(text = "Hello")
            withStyle(SpanStyle(baselineShift = BaselineShift.Superscript, fontSize = 16.sp)) {
                append("superscript")
                withStyle(SpanStyle(baselineShift = BaselineShift.Subscript)) {
                    append("subscript")
                }
            }
        }
    )
}

@Composable
fun BaselineShiftAnnotatedStringSample() {
    val annotatedString = buildAnnotatedString {
        append("Text ")
        withStyle(SpanStyle(baselineShift = BaselineShift.Superscript)) {
            append("Demo")
        }
    }
    Text(text = annotatedString)
}


@Composable
fun AnnotatedStringConstructorSample() {
    val s = AnnotatedString(
        text = "Hello World",
        // make "Hello" italic.
        spanStyles = listOf(
            AnnotatedString.Range(SpanStyle(fontStyle = FontStyle.Italic), 0, 5)
        ),
        // create two paragraphs with different alignment and indent settings.
        paragraphStyles = listOf(
            AnnotatedString.Range(ParagraphStyle(textAlign = TextAlign.Center), 0, 6),
            AnnotatedString.Range(ParagraphStyle(textIndent = TextIndent(5.sp)), 6, 11)
        )
    )

    Text(text = s)

}

@Composable
fun AnnotatedStringBuilderSample() {
    val s = with(AnnotatedString.Builder("Hello")) {
        // push green text style so that any appended text will be green
        pushStyle(SpanStyle(color = Color.Green))
        // append new text, this text will be rendered as green
        append(" World")
        // pop the green style
        pop()
        // append a string without style
        append("!")
        // then style the last added word as red, exclamation mark will be red
        addStyle(SpanStyle(color = Color.Red), "Hello World".length, this.length)

        toAnnotatedString()
    }
    Text(text = s)

}

@Composable
fun AnnotatedStringBuilderPushSample() {
    val s = with(AnnotatedString.Builder()) {
        // push green text color so that any appended text will be rendered green
        pushStyle(SpanStyle(color = Color.Green))
        // append string, this text will be rendered green
        append("Hello")
        // pop the green text style
        pop()
        // append new string, this string will be default color
        append(" World")

        toAnnotatedString()
    }
    Text(text = s)

}

@Composable
fun AnnotatedStringBuilderPushParagraphStyleSample() {
    val s = with(AnnotatedString.Builder()) {
        // push a ParagraphStyle to be applied to any appended text after this point.
        pushStyle(ParagraphStyle(lineHeight = 18.sp))
        // append a paragraph which will have lineHeight 18.sp
        append("Paragraph One\n")
        // pop the ParagraphStyle
        pop()
        // append new paragraph, this paragraph will not have the line height defined.
        append("Paragraph Two\n")

        toAnnotatedString()
    }
    Text(text = s)
}

@Composable
fun AnnotatedStringBuilderPushStringAnnotationSample() {
    val s = with(AnnotatedString.Builder()) {
        // push a string annotation to be applied to any appended text after this point.
        pushStringAnnotation("ParagrapLabel", "paragraph1")
        // append a paragraph, the annotation "paragraph1" is attached
        append("Paragraph One\n")
        // pop the annotation
        pop()
        // append new paragraph
        append("Paragraph Two\n")

        toAnnotatedString()
    }
    Text(text = s)
}

@Composable
fun AnnotatedStringBuilderWithStyleSample() {
    val s = with(AnnotatedString.Builder()) {
        withStyle(SpanStyle(color = Color.Green)) {
            // green text style will be applied to all text in this block
            append("Hello")
        }
        toAnnotatedString()
    }
    Text(text = s)

}

@Composable
fun AnnotatedStringBuilderLambdaSample() {
    // create an AnnotatedString using the lambda builder
    val s = buildAnnotatedString {
        // append "Hello" with red text color
        withStyle(SpanStyle(color = Color.Red)) {
            append("Hello")
        }
        append(" ")
        // append "Hello" with blue text color
        withStyle(SpanStyle(color = Color.Blue)) {
            append("World!")
        }
        toAnnotatedString()
    }
    Text(text = s)

}

@Composable
fun AnnotatedStringAddStringAnnotationSample() {
    val s = with(AnnotatedString.Builder()) {
        append("link: Jetpack Compose")
        // attach a string annotation that stores a URL to the text "Jetpack Compose".
        addStringAnnotation(
            tag = "URL",
            annotation = "https://developer.android.com/jetpack/compose",
            start = 6,
            end = 21
        )
        toAnnotatedString()

    }
    Text(text = s.toString())

}

@Preview
@Composable
fun TextDemo() {
    SampleList(title = "Text Demo") {
        LazyColumn {
            item {

                SampleItem("AnnotatedStringConstructorSample") {
                    AnnotatedStringConstructorSample()
                }
                SampleItem("AnnotatedStringBuilderSample") {
                    AnnotatedStringBuilderSample()
                }
                Divider(Modifier.height(2.dp))
                AnnotatedStringBuilderPushSample()
                Divider(Modifier.height(2.dp))
                AnnotatedStringBuilderPushParagraphStyleSample()
                Divider(Modifier.height(2.dp))
                AnnotatedStringBuilderPushStringAnnotationSample()
                Divider(Modifier.height(2.dp))
                AnnotatedStringBuilderWithStyleSample()
                Divider(Modifier.height(2.dp))
                AnnotatedStringBuilderLambdaSample()
                Divider(Modifier.height(2.dp))
                AnnotatedStringAddStringAnnotationSample()
                Divider(Modifier.height(2.dp))
                BaselineShiftSample()
                Divider(Modifier.height(2.dp))
                BaselineShiftAnnotatedStringSample()
                Divider(Modifier.height(2.dp))
                FontFamilySansSerifSample()
                Divider(Modifier.height(2.dp))
                FontFamilySerifSample()
                Divider(Modifier.height(2.dp))
//            FontFamilySynthesisSample()

            }
            item {

                Divider(Modifier.height(2.dp))
                TextStyleSample()
                Divider(Modifier.height(2.dp))
                TextOverflowClipSample()
                Divider(Modifier.height(2.dp))
                TextOverflowEllipsisSample()
                Divider(Modifier.height(2.dp))
                TextOverflowVisibleFixedSizeSample()
                Divider(Modifier.height(2.dp))
                TextOverflowVisibleMinHeightSample()
                Divider(Modifier.height(2.dp))
                TextDecorationLineThroughSample()
                Divider(Modifier.height(2.dp))
                TextDecorationUnderlineSample()
                Divider(Modifier.height(2.dp))
                TextDecorationCombinedSample()
                Divider(Modifier.height(2.dp))
                SpanStyleSample()
                Divider(Modifier.height(2.dp))
                ParagraphStyleSample()
                Divider(Modifier.height(2.dp))
                ParagraphStyleAnnotatedStringsSample()
                Divider(Modifier.height(2.dp))
            }
        }
    }

}
@Composable
fun FontFamilySansSerifSample() {
    Text(
        text = "Demo Text sans-serif",
        fontFamily = FontFamily.SansSerif
    )
}

@Composable
fun FontFamilySerifSample() {
    Text(
        text = "Demo Text serif",
        fontFamily = FontFamily.Serif
    )
}

@Composable
fun FontFamilyMonospaceSample() {
    Text(
        text = "Demo Text monospace",
        fontFamily = FontFamily.Monospace
    )
}

@Composable
fun FontFamilyCursiveSample() {
    Text(
        text = "Demo Text cursive",
        fontFamily = FontFamily.Cursive
    )
}

//@Composable
//fun CustomFontFamilySample() {
//    val fontFamily = FontFamily(
//        Font(
//            resId = R.font.my_font_400_regular,
//            weight = FontWeight.W400,
//            style = FontStyle.Normal
//        ),
//        Font(
//            resId = R.font.my_font_400_italic,
//            weight = FontWeight.W400,
//            style = FontStyle.Italic
//        )
//    )
//    Text(text = "Demo Text", fontFamily = fontFamily)
//}

//@Composable
//fun FontFamilySynthesisSample() {
//    // The font family contains a single font, with normal weight
//    val fontFamily = FontFamily(
//        Font(resId = R.font.my_font_400_regular, weight = FontWeight.Normal)
//    )
//    // Configuring the Text composable to be bold
//    // Using FontSynthesis.Weight to have the system render the font bold my making the glyphs
//    // thicker
//    Text(
//        text = "Demo Text",
//        style = TextStyle(
//            fontFamily = fontFamily,
//            fontWeight = FontWeight.Bold,
//            fontSynthesis = FontSynthesis.Weight
//        )
//    )
//}


@Composable
fun ParagraphStyleSample() {
    val textStyle = TextStyle(
        textAlign = TextAlign.Justify,
        lineHeight = 20.sp,
        textIndent = TextIndent(firstLine = 14.sp, restLine = 3.sp)
    )
    Text(
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                "incididunt ut labore et dolore magna aliqua.\nUt enim ad minim veniam, quis " +
                "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
        style = textStyle
    )
}

@Composable
fun ParagraphStyleAnnotatedStringsSample() {
    val text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
            "incididunt ut labore et dolore magna aliqua.\nUt enim ad minim veniam, quis " +
            "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."

    val paragraphStyle1 = ParagraphStyle(
        textIndent = TextIndent(firstLine = 14.sp)
    )
    val paragraphStyle2 = ParagraphStyle(
        lineHeight = 30.sp
    )

    Text(
        text = buildAnnotatedString {
            append(text)
            addStyle(paragraphStyle1, 0, text.indexOf('\n') + 1)
            addStyle(paragraphStyle2, text.indexOf('\n') + 1, text.length)
        }
    )
}


@Composable
fun SpanStyleSample() {
    Text(
        fontSize = 16.sp,
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Red)) {
                append("Hello")
            }
            withStyle(SpanStyle(color = Color.Blue)) {
                append(" World")
            }
        }
    )
}

@Composable
fun TextDecorationLineThroughSample() {
    Text(
        text = "Demo Text",
        textDecoration = TextDecoration.LineThrough
    )
}

@Composable
fun TextDecorationUnderlineSample() {
    Text(
        text = "Demo Text",
        textDecoration = TextDecoration.Underline
    )
}

@Composable
fun TextDecorationCombinedSample() {
    Text(
        text = "Demo Text",
        textDecoration = TextDecoration.Underline + TextDecoration.LineThrough
    )
}

@Composable
fun TextStyleSample() {
    Text(
        text = "Demo Text".repeat(2),
        style = TextStyle(
            color = Color.Red,
            fontSize = 16.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.W800,
            fontStyle = FontStyle.Italic,
            letterSpacing = 0.5.em,
            background = Color.LightGray,
            textDecoration = TextDecoration.Underline
        )
    )
}

@Composable
fun TextOverflowClipSample() {
    Text(
        text = "Hello ".repeat(2),
        modifier = Modifier
            .size(100.dp, 70.dp)
            .background(Color.Cyan),
        fontSize = 35.sp,
        overflow = TextOverflow.Clip
    )
}

@Composable
fun TextOverflowEllipsisSample() {
    Text(
        text = "Hello ".repeat(2),
        modifier = Modifier
            .width(100.dp)
            .background(Color.Cyan),
        fontSize = 35.sp,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1
    )
}

@Composable
fun TextOverflowVisibleFixedSizeSample() {
    val background = remember { mutableStateOf(Color.Cyan) }
    Box(modifier = Modifier.size(100.dp, 250.dp)) {
        Text(
            text = "Hello World".repeat(4),
            modifier = Modifier
                .size(100.dp, 150.dp)
                .background(background.value)
                .clickable {
                    background.value = if (background.value == Color.Cyan) {
                        Color.Gray
                    } else {
                        Color.Cyan
                    }
                },
            fontSize = 35.sp,
            overflow = TextOverflow.Visible
        )
    }
}

@Composable
fun TextOverflowVisibleMinHeightSample() {
    val background = remember { mutableStateOf(Color.Cyan) }
    val count = remember { mutableStateOf(1) }
    Box(modifier = Modifier.size(100.dp, 100.dp)) {
        Text(
            text = "Hello".repeat(count.value),
            modifier = Modifier
                .width(100.dp)
                .heightIn(min = 70.dp)
                .background(background.value)
                .clickable {
                    background.value =
                        if (background.value == Color.Cyan) Color.Gray else Color.Cyan
                    count.value = if (count.value == 1) 2 else 1
                },
            fontSize = 35.sp,
            overflow = TextOverflow.Visible
        )
    }
}