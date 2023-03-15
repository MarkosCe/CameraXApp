package com.example.cameraxapp

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.cameraxapp.databinding.ActivityMainBinding
import com.example.cameraxapp.databinding.ActivityScannerBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.websitebeaver.documentscanner.DocumentScanner

class ScannerActivity : AppCompatActivity() {

    private lateinit var croppedImageView: ImageView

    private lateinit var viewBinding: ActivityScannerBinding

    private val documentScanner = DocumentScanner(
        this,
        { croppedImageResults ->
            // display the first cropped image
            viewBinding.croppedImageView.setImageURI(Uri.parse(croppedImageResults.first().toString()))
            val imageInput: InputImage = InputImage.fromFilePath(applicationContext, Uri.parse(croppedImageResults.first().toString()))

            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            val result = recognizer.process(imageInput)
                .addOnSuccessListener { visionText ->
                    processTextRecognitionResult(visionText);
                }
                .addOnFailureListener{ e ->
                    e.printStackTrace();
                }
        },
        {
            // an error happened
                errorMessage -> Log.v("documentscannerlogs", errorMessage)
        },
        {
            // user canceled document scan
            Log.v("documentscannerlogs", "User canceled document scan")
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityScannerBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // start document scan
        documentScanner.startScan()
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
            Log.d("BLOCK", "bl: ${blocks[i].text}")
            if (blocks[i].text.contains("NOMBRE")) {
                //Log.d("BLOCK", "bl: ${blocks[i].text}")
                viewBinding.textViewName.text = blocks[i].text
            }
            if (blocks[i].text.contains("DOMICILI0") || blocks[i].text.contains("DOMICILIO")){
                //Log.d("BLOCK", "bl: ${blocks[i].text}")
                viewBinding.textViewDom.text = blocks[i].text
            }
            val lines: List<Text.Line> = blocks[i].lines
            for (j in lines.indices) {
                val elements: List<Text.Element> = lines[j].elements
                for (k in elements.indices) {
                    Log.d("ELEMENT", "Word: ${elements[k].text}")
                    /*val textGraphic: GraphicOverlay.Graphic = TextGraphic(
                        mGraphicOverlay,
                        elements[k]
                    )
                    mGraphicOverlay.add(textGraphic)*/
                }
            }
        }
    }
}