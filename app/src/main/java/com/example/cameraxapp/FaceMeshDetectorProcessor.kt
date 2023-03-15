package com.example.cameraxapp

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.facemesh.*

class FaceMeshDetectorProcessor(
    context: Context,
    private val viewmodel: CameraXViewModel
) : VisionProcessorBase<List<FaceMesh>>(context) {
    private val detector: FaceMeshDetector
    private var suma: Int
    //private val buton: Button;

    private var sumzOval: Float
    private var pointxOval: Float
    private var pointyOval: Float

    private var yMaxTop: Float
    private var yMaxBottom: Float

    private var cont: Int


    private var one: Boolean



    @FaceMesh.ContourType
    private val DISPLAY_CONTOURS =
        intArrayOf(
            FaceMesh.FACE_OVAL,
            //FaceMesh.LEFT_EYEBROW_TOP,
            //FaceMesh.LEFT_EYEBROW_BOTTOM,
            //FaceMesh.RIGHT_EYEBROW_TOP,
            //FaceMesh.RIGHT_EYEBROW_BOTTOM,
            //FaceMesh.LEFT_EYE,
            //FaceMesh.RIGHT_EYE,
            //FaceMesh.UPPER_LIP_TOP,
            //FaceMesh.UPPER_LIP_BOTTOM,
            //FaceMesh.LOWER_LIP_TOP,
            //FaceMesh.LOWER_LIP_BOTTOM,
            //FaceMesh.NOSE_BRIDGE
        )

    @FaceMesh.ContourType
    private val EYE_CONTOURS =
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

    init {
        //buton = button
        val optionsBuilder = FaceMeshDetectorOptions.Builder()
        if (PreferenceUtils.getFaceMeshUseCase(context) == FaceMeshDetectorOptions.BOUNDING_BOX_ONLY) {
            optionsBuilder.setUseCase(FaceMeshDetectorOptions.BOUNDING_BOX_ONLY)
        }
        detector = FaceMeshDetection.getClient(optionsBuilder.build())

        sumzOval = 0.0f
        pointxOval = 0.0f
        pointyOval = 0.0f
        suma = 1
        one = false
        cont = 0

        yMaxTop = Float.MIN_VALUE
        yMaxBottom = Float.MIN_VALUE

        //viewModel = ViewModelProvider()[CameraXViewModel::class.java]
    }

    override fun stop() {
        super.stop()
        detector.close()
    }

    override fun detectInImage(image: InputImage): Task<List<FaceMesh>> {
        return detector.process(image)
    }

    override fun onSuccess(faces: List<FaceMesh>, graphicOverlay: GraphicOverlay) {
        if (faces.size == 1){
            suma++
            //buton.visibility = View.VISIBLE
            Log.d("CONTADOR2", suma.toString())

            for (face in faces) {
                graphicOverlay.add(FaceMeshGraphic(graphicOverlay, face))
                realFace(face, graphicOverlay)
            }
        }
    }

    private fun getContourPoints(faceMesh: FaceMesh): List<FaceMeshPoint> {
        val contourPoints: MutableList<FaceMeshPoint> = ArrayList()
        for (type in DISPLAY_CONTOURS) {
            contourPoints.addAll(faceMesh.getPoints(type))
        }
        return contourPoints
    }

    private fun getEyePoints(faceMesh: FaceMesh): List<FaceMeshPoint>{
        val eyePoints: MutableList<FaceMeshPoint> = ArrayList()
        for (type in EYE_CONTOURS) {
            eyePoints.addAll(faceMesh.getPoints(type))
        }
        return eyePoints
    }

    private fun realFace(faceMesh: FaceMesh, graphicOverlay: GraphicOverlay){
        sumzOval = 0f
        pointxOval = 0f
        pointyOval = 0f
        one = false
        val points = getContourPoints(faceMesh)
        for (point in points) {
            sumzOval += point.position.z
            if (!one){
                one = true
                //Log.d("x value", point.position.x.toString())
                //Log.d("y value", point.position.y.toString())
                pointxOval = point.position.x
                pointyOval = point.position.y
            }

        }

        if ((sumzOval/points.size) > 58  && (sumzOval/points.size) < 68){
            if (pointxOval > 220 && pointxOval < 240)
                if (pointyOval > 170 && pointyOval < 200){
                    addInstruction(graphicOverlay, "Posicion Correcta")
                    //viewmodel.position2.value = true
                    val pointsE = getEyePoints(faceMesh)
                    for (point in pointsE){
                        if (point.index == 159){
                            yMaxTop = point.position.y
                        }else if (point.index == 145){
                            yMaxBottom = point.position.y
                        }
                    }
                    if (Math.abs(yMaxTop - yMaxBottom) in 0.0..1.0){
                        viewmodel.position2.value = true
                    }

                }
        }else{
            addInstruction(graphicOverlay, "Posicion Incorrecta")
        }

        //Log.d("sum: z value-top", (sumztop/8f).toString())

        //if ((sumztop/8f) > 20 && (sumztop/8f) < 25){
        //    graphicOverlay.add(
        //        InstructionInfoGraphic(
        //            graphicOverlay,
        //            "posicion correcta",
        //            1
        //        )
        //    )
        //    //Log.d("size_success", "true")
        //    //if (anterior != 0.0f)
        //    //    if (anterior > (sumxtop/sumytop)){
        //    //        Log.d("anterior", anterior.toString())
        //    //    }
        //    //anterior = (sumxbottom/sumybottom)
        //}else{
        //    graphicOverlay.add(
        //        InstructionInfoGraphic(
        //            graphicOverlay,
        //            "posicion incorrecta",
        //            1
        //        )
        //    )
        //}

        Log.d("CONTADOR", "AQUI")
    }

    private fun addInstruction(graphicOverlay: GraphicOverlay, msg: String){
        graphicOverlay.add(
            InstructionInfoGraphic(
                graphicOverlay,
                msg,
                1
            )
        )
    }

    override fun onFailure(e: Exception) {
        Log.e(TAG, "Face detection failed $e")
    }

    companion object {
        private const val TAG = "SelfieFaceProcessor"
        var position = false
    }
}