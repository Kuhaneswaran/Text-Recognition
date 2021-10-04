package com.kuhan.textrecognition

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun AppCompatActivity.openFragment(fragment: Fragment) = with(this) {
    this.supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, fragment)
        .addToBackStack(null)
        .commit()
}

fun Fragment.showToast(message: String?, length: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(context, message ?: "Error", length).show()

fun Activity.checkPermissions(permissions: Array<String>): Boolean = permissions.all {
    ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
}

fun Fragment.checkPermission(permission: String): Boolean =
    activity?.checkPermissions(arrayOf(permission)) ?: false

fun prepareGalleryIntent(): Intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }

fun getCameraIntent(context: Activity): Pair<Intent, Uri>? {

    // Create camera intent
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    // Ensure that there's a camera activity to handle the intent
    context.packageManager.let { intent.resolveActivity(it) ?: return null }

    // Create the File where the photo should go
    val file: File = createImageFile(context) ?: return null
    val authority = "${BuildConfig.APPLICATION_ID}.fileprovider"
    val photoURI: Uri = FileProvider.getUriForFile(context, authority, file)
    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

    return Pair(intent, photoURI)
}

fun createImageFile(context: Activity): File? {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val storageDir = ContextCompat.getExternalFilesDirs(context, Environment.DIRECTORY_PICTURES)
    return try {
        val dir = if (storageDir.isNotEmpty()) storageDir[0] else null
        File.createTempFile("JPEG_${timeStamp}_", ".jpg", dir)
    } catch (ex: Throwable) {
        null
    }
}