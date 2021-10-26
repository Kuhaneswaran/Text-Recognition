package com.kuhan.textrecognition.utils

import android.graphics.Point
import android.graphics.Rect
import com.google.gson.GsonBuilder
import com.kuhan.textrecognition.App
import java.io.File
import com.google.mlkit.vision.text.Text as MLKitText

class MLKitUtils {

    fun getTextBlock(text: MLKitText): Text {
        val textBlocks = text.textBlocks.map { block ->

            val lines = block.lines.map { line ->

                val elements = line.elements.map { element ->
                    Element(element.text,
                        element.boundingBox,
                        element.cornerPoints,
                        element.recognizedLanguage)
                }

                Line(line.text,
                    line.boundingBox,
                    line.cornerPoints,
                    line.recognizedLanguage,
                    elements)
            }

            TextBlock(block.text,
                block.boundingBox,
                block.cornerPoints,
                block.recognizedLanguage,
                lines)
        }

        return Text(textBlocks, text.text)
    }

    fun getTextFile(text: MLKitText): File? {
        return try {
            val parsedText = MLKitUtils().getTextBlock(text)
            val file = File.createTempFile("MLKitTextOutput", ".json", App.context.cacheDir)
            val json = GsonBuilder().setPrettyPrinting().create().toJson(parsedText)
            file.writeText(json)
            file
        } catch (ex: Throwable) {
            null
        }
    }

    data class Text(
        var textBlocks: List<TextBlock>,
        val text: String,
    )

    // for reference
    open class TextBase(
        val string: String?,
        val boundingBox: Rect?,
        val cornerPoints: Array<Point?>?,
        val recognizedLanguage: String?,
    )

    class Element(
        val string: String,
        val boundingBox: Rect?,
        val cornerPoints: Array<Point?>?,
        val recognizedLanguage: String?,
    )

    class Line(
        val string: String,
        val boundingBox: Rect?,
        val cornerPoints: Array<Point?>?,
        val recognizedLanguage: String?,
        val elements: List<Element>,
    )

    class TextBlock(
        val string: String,
        val boundingBox: Rect?,
        val cornerPoints: Array<Point?>?,
        val recognizedLanguage: String?,
        val lines: List<Line>,
    )
}