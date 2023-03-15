package com.example.cameraxapp

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import com.google.mlkit.vision.common.PointF3D
import com.google.mlkit.vision.facemesh.FaceMesh
import com.google.mlkit.vision.facemesh.FaceMeshDetectorOptions
import com.google.mlkit.vision.facemesh.FaceMeshPoint

class FaceMeshGraphic(overlay: GraphicOverlay, private val faceMesh: FaceMesh) :
    GraphicOverlay.Graphic(overlay) {

    private val positionPaint: Paint
    private val boxPaint: Paint
    private val useCase: Int
    private var one: Boolean
    private var flag: Boolean
    private var zMin: Float
    private var zMax: Float

    private var cont: Int
    private var sumxtop: Float
    private var sumytop: Float
    private var sumxbottom: Float
    private var sumybottom: Float

    private val overlayGraphic: GraphicOverlay

    @FaceMesh.ContourType
    private val DISPLAY_CONTOURS =
        intArrayOf(
            //FaceMesh.FACE_OVAL,
            //FaceMesh.LEFT_EYEBROW_TOP,
            //FaceMesh.LEFT_EYEBROW_BOTTOM,
            //FaceMesh.RIGHT_EYEBROW_TOP,
            //FaceMesh.RIGHT_EYEBROW_BOTTOM,
            FaceMesh.LEFT_EYE,
            FaceMesh.RIGHT_EYE,
            //FaceMesh.UPPER_LIP_TOP,
            //FaceMesh.UPPER_LIP_BOTTOM,
            //FaceMesh.LOWER_LIP_TOP,
            //FaceMesh.LOWER_LIP_BOTTOM,
            //FaceMesh.NOSE_BRIDGE
        )

    /** Draws the face annotations for position on the supplied canvas. */
    override fun draw(canvas: Canvas) {

        // Draws the bounding box.
        val rect = RectF(faceMesh.boundingBox)

        //Log.d("box height",faceMesh.boundingBox.height().toString() )
        //Log.d("box width",faceMesh.boundingBox.width().toString() )

        // If the image is flipped, the left will be translated to right, and the right to left.
        val x0 = translateX(rect.left)
        val x1 = translateX(rect.right)
        rect.left = Math.min(x0, x1)
        rect.right = Math.max(x0, x1)
        rect.top = translateY(rect.top)
        rect.bottom = translateY(rect.bottom)
        canvas.drawRect(rect, boxPaint)

        // Draw face mesh
        /*val points =
            if (useCase == USE_CASE_CONTOUR_ONLY) getContourPoints(faceMesh) else faceMesh.allPoints
        val triangles = faceMesh.allTriangles

        zMin = Float.MAX_VALUE
        zMax = Float.MIN_VALUE
        for (point in points) {
            zMin = Math.min(zMin, point.position.z)
            zMax = Math.max(zMax, point.position.z)

            //Log.d("z value", point.position.z.toString())
            //sumxtop += point.position.z
            //if (!one){
            //    one = true
            //    //Log.d("x value", point.position.x.toString())
            //    //Log.d("y value", point.position.y.toString())
            //    sumxbottom = point.position.x
            //    sumybottom = point.position.y
            //}
            //if (cont < 8){
            //    sumxtop += point.position.x
            //    sumytop += point.position.y
            //    Log.d("z value", point.position.z.toString())
            //    cont++
            //}else{
            //    sumxbottom += point.position.x
            //    sumybottom += point.position.y
            //}
        }

        //Log.d("sum z value", (sumxtop/points.size).toString())
        //if ((sumxtop/points.size) > 58  && (sumxtop/points.size) < 68){
        //    if (sumxbottom > 220 && sumxbottom < 240)
        //        if (sumybottom > 170 && sumybottom < 200){
        //            //Log.d("position", "Correcto")
        //            //addInfo("Posicion correcta")
        //        }
        //}else{
        //    //addInfo("Posicion incorrecta")
        //    //Log.d("sum", (sumxtop/points.size).toString())
        //    //Log.d("position", "Incorrecto")
        //}

       //Log.d("suma top", (sumxtop/sumytop).toString())
       //Log.d("suma bottom", (sumxbottom/sumybottom).toString())
       //if ((sumxbottom/sumybottom) > 0.6 && (sumxbottom/sumybottom) < 0.7){
       //    Log.d("size_success", "true")
       //}
       //Log.d("CONTADOR", "AQUI")
        // Draw face mesh points
        for (point in points) {
            if(point.index == 145 || point.index == 159 || point.index == 386 || point.index == 374) {
               one = true
                //updatePaintColorByZValue(
                //    positionPaint,
                //    canvas,
                //    /* visualizeZ= */true,
                //    /* rescaleZForVisualization= */true,
                //    point.position.z,
                //    zMin,
                //    zMax)
                canvas.drawCircle(
                    translateX(point.position.x),
                    translateY(point.position.y),
                    FACE_POSITION_RADIUS,
                    positionPaint
                )
                cont++
                Log.d("value", point.index.toString())

                //Log.d("position x", point.position.x.toString())
            }
        }

        if (useCase == FaceMeshDetectorOptions.FACE_MESH) {
            // Draw face mesh triangles
            for (triangle in triangles) {
                val point1 = triangle.allPoints[0].position
                val point2 = triangle.allPoints[1].position
                val point3 = triangle.allPoints[2].position
                drawLine(canvas, point1, point2)
                drawLine(canvas, point1, point3)
                drawLine(canvas, point2, point3)
            }
        }*/
    }

    private fun addInfo(string: String){
        overlayGraphic.add(
                   InstructionInfoGraphic(
                        overlayGraphic,
                        string,
                        1
                    )
                )
    }

    private fun drawLine(canvas: Canvas, point1: PointF3D, point2: PointF3D) {
        updatePaintColorByZValue(
            positionPaint,
            canvas,
            /* visualizeZ= */true,
            /* rescaleZForVisualization= */true,
            (point1.z + point2.z) / 2,
            zMin,
            zMax)
        canvas.drawLine(
            translateX(point1.x),
            translateY(point1.y),
            translateX(point2.x),
            translateY(point2.y),
            positionPaint
        )
    }

    private fun getContourPoints(faceMesh: FaceMesh): List<FaceMeshPoint> {
        val contourPoints: MutableList<FaceMeshPoint> = ArrayList()
        for (type in DISPLAY_CONTOURS) {
            contourPoints.addAll(faceMesh.getPoints(type))
        }
        return contourPoints
    }

    companion object {
        private const val USE_CASE_CONTOUR_ONLY = 999
        private const val FACE_POSITION_RADIUS = 8.0f
        private const val BOX_STROKE_WIDTH = 5.0f
    }

    init {
        val selectedColor = Color.WHITE
        positionPaint = Paint()
        positionPaint.color = selectedColor

        boxPaint = Paint()
        boxPaint.color = selectedColor
        boxPaint.style = Paint.Style.STROKE
        boxPaint.strokeWidth = BOX_STROKE_WIDTH

        //useCase = PreferenceUtils.getFaceMeshUseCase(applicationContext)
        sumxtop = 0.0f
        sumytop = 0.0f
        sumxbottom = 0.0f
        sumybottom = 0.0f
        cont = 0
        one = false
        flag = false
        useCase = 999;
        zMin = java.lang.Float.MAX_VALUE
        zMax = java.lang.Float.MIN_VALUE

        overlayGraphic = overlay
    }
}
