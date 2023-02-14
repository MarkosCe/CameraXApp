package com.example.cameraxapp

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cameraxapp.databinding.ActivityImageBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException


class ImageActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityImageBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val image = intent.getStringExtra("uri")
        val uri = Uri.parse(image)

        val imageInput: InputImage = InputImage.fromFilePath(applicationContext, uri)

        viewBinding.imageView.setImageURI(uri)

        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        val result = recognizer.process(imageInput)
            .addOnSuccessListener { visionText ->
                processTextRecognitionResult(visionText);
            }
            .addOnFailureListener{ e ->
                e.printStackTrace();
            }
    }

    private fun processTextRecognitionResult(texts: Text) {
        val blocks: List<Text.TextBlock> = texts.textBlocks
        if (blocks.isEmpty()) {
            Toast.makeText(applicationContext, "No text found", Toast.LENGTH_SHORT).show();
            return
        }
        val texto = ""
        //mGraphicOverlay.clear()
        for (i in blocks.indices) {
            if (blocks[i].text.contains("NOMBRE")) {
                Toast.makeText(this, blocks[i].text, Toast.LENGTH_SHORT).show()
                viewBinding.textView.text = blocks[i].text
            }
            val lines: List<Text.Line> = blocks[i].lines
            //Toast.makeText(this, blocks.get(i).getText(), Toast.LENGTH_SHORT).show();
            for (j in lines.indices) {
                val elements: List<Text.Element> = lines[j].elements
                for (k in elements.indices) {
                    Log.d("ELEMENT", "Word: ${elements[k]}")
                    /*val textGraphic: GraphicOverlay.Graphic = TextGraphic(
                        mGraphicOverlay,
                        elements[k]
                    )
                    mGraphicOverlay.add(textGraphic)*/
                }
            }
        }
        //Toast.makeText(this, texto, Toast.LENGTH_LONG).show();
    }
}