package com.kuhan.textrecognition

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.kuhan.textrecognition.databinding.FragmentStaticTextRecognitionBinding

class StaticTextRecognitionFragment : Fragment() {

    private val activityForResult = ActivityResultContracts.StartActivityForResult()
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>

    private lateinit var binding: FragmentStaticTextRecognitionBinding
    private val viewModel by viewModels<StaticTextRecognitionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cameraLauncher = registerForActivityResult(activityForResult) {
            if (it.resultCode == Activity.RESULT_OK) viewModel.updateUri()
        }

        galleryLauncher = registerForActivityResult(activityForResult) {
            viewModel.updateUri(it.data?.data ?: return@registerForActivityResult)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {

        binding = FragmentStaticTextRecognitionBinding.inflate(inflater, container, false)

        binding.btnTakePhoto.setOnClickListener {
            cameraLauncher.launch(viewModel.prepareCameraIntent(requireActivity()))
        }

        binding.btnSelectGallery.setOnClickListener {
            galleryLauncher.launch(prepareGalleryIntent())
        }

        viewModel.imageUri.observe(viewLifecycleOwner) {
            binding.boOverlay.clear()
            binding.ivPreview.setImageURI(it)
            processImage(it)
        }

        return binding.root
    }

    private fun processImage(uri: Uri?) {
        val image = uri ?: return showToast("Invalid URI")
        val inputImage = InputImage.fromFilePath(requireActivity(), image)
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS).process(inputImage)
            .addOnSuccessListener {
                binding.boOverlay.add(it, inputImage.width, inputImage.height)
            }
            .addOnFailureListener { showToast(it.localizedMessage) }
    }
}
