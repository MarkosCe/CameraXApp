package com.example.cameraxapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.camera.core.CameraSelector;

import com.google.common.base.Preconditions;
import com.google.mlkit.vision.facemesh.FaceMeshDetectorOptions;

public class PreferenceUtils {

    public static boolean isCameraLiveViewportEnabled(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        //String prefKey = context.getString(R.string.pref_key_camera_live_viewport);
        return sharedPreferences.getBoolean("cameraLiveViewport", false);
    }

    public static boolean shouldHideDetectionInfo(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean("shouldHideInfo", false);
    }

    public static int getFaceMeshUseCase(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        //String prefKey = context.getString(R.string.pref_key_face_mesh_use_case);
        return Integer.parseInt(
                sharedPreferences.getString("faceMeshUseCase", String.valueOf(FaceMeshDetectorOptions.FACE_MESH)));
    }

    @Nullable
    public static android.util.Size getCameraXTargetResolution(Context context, int lensfacing) {
        Preconditions.checkArgument(
                lensfacing == CameraSelector.LENS_FACING_BACK
                        || lensfacing == CameraSelector.LENS_FACING_FRONT);
        String prefKey =
                lensfacing == CameraSelector.LENS_FACING_BACK
                        ? context.getString(R.string.pref_key_camerax_rear_camera_target_resolution)
                        : context.getString(R.string.pref_key_camerax_front_camera_target_resolution);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return android.util.Size.parseSize(sharedPreferences.getString(prefKey, null));
        } catch (Exception e) {
            return null;
        }
    }
}
