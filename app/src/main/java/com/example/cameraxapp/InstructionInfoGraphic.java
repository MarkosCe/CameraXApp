package com.example.cameraxapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import androidx.annotation.Nullable;

public class InstructionInfoGraphic extends GraphicOverlay.Graphic {

    private static final int TEXT_COLOR = Color.WHITE;
    private static final float TEXT_SIZE = 60.0f;

    private final Paint textPaint;
    private final GraphicOverlay overlay;
    private final String info;
    //private final long frameLatency;
    //private final long detectorLatency;

    // Only valid when a stream of input images is being processed. Null for single image mode.
    @Nullable
    private final Integer framesPerSecond;
    private boolean showLatencyInfo = true;

    public InstructionInfoGraphic(
            GraphicOverlay overlay,
            String info,
            @Nullable Integer framesPerSecond) {
        super(overlay);
        this.overlay = overlay;
        this.info = info;
        this.framesPerSecond = framesPerSecond;
        textPaint = new Paint();
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setShadowLayer(5.0f, 0f, 0f, Color.BLACK);
        postInvalidate();
    }

    /**
     * Creates an {@link InferenceInfoGraphic} to only display image size.
     */
    public InstructionInfoGraphic(GraphicOverlay overlay) {
        this(overlay,"nada", null);
        showLatencyInfo = false;
    }

    @Override
    public synchronized void draw(Canvas canvas) {
        float x = TEXT_SIZE * 0.5f;
        float y = TEXT_SIZE * 1.5f;

        canvas.drawText(
                "InputImage size: " + overlay.getImageHeight() + "x",
                x,
                y,
                textPaint);

        if (!showLatencyInfo) {
            return;
        }
        // Draw FPS (if valid) and inference latency
        if (info != null) {
            canvas.drawText(
                    ":" + info,
                    x,
                    y + TEXT_SIZE,
                    textPaint);
        } else {
            canvas.drawText("nada", x, y + TEXT_SIZE, textPaint);
        }
        canvas.drawText(
                "nada", x, y + TEXT_SIZE * 2, textPaint);
    }
}
