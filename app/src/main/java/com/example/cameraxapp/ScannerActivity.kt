package com.example.cameraxapp

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.websitebeaver.documentscanner.DocumentScanner

class ScannerActivity : AppCompatActivity() {

    private lateinit var croppedImageView: ImageView

    private val documentScanner = DocumentScanner(
        this,
        { croppedImageResults ->
            // display the first cropped image
            croppedImageView.setImageBitmap(
                BitmapFactory.decodeFile(croppedImageResults.first())
            )
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
        setContentView(R.layout.activity_scanner)

        // cropped image
        croppedImageView = findViewById(R.id.cropped_image_view)

        // start document scan
        documentScanner.startScan()
    }
}