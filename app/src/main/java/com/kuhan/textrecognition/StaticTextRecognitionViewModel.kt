package com.kuhan.textrecognition

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuhan.textrecognition.utils.getCameraIntent

class StaticTextRecognitionViewModel : ViewModel() {

    val imageUri = MutableLiveData<Uri?>().apply { value = null }
    private var cameraUri: Uri? = null

    fun updateUri(uri: Uri? = cameraUri) {
        imageUri.value = uri
    }

    fun prepareCameraIntent(context: Activity): Intent? {
        val pair = getCameraIntent(context)
        cameraUri = pair?.second
        return pair?.first
    }
}