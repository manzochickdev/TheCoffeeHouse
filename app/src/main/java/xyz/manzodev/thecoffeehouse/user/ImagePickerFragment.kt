package xyz.manzodev.thecoffeehouse.user


import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.FragmentImagePickerBinding
import xyz.manzodev.thecoffeehouse.utils.PermissionBuilder
import xyz.manzodev.thecoffeehouse.home.NewsDetailActivity
import android.Manifest.permission
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import androidx.core.app.ActivityCompat.startActivityForResult
import android.provider.MediaStore
import android.content.Intent
import android.graphics.Bitmap
import android.R.attr.data
import androidx.core.app.NotificationCompat.getExtras
import java.io.IOException


class ImagePickerFragment : BottomSheetDialogFragment() {

    lateinit var permissionBuilder: PermissionBuilder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var fragmentImagePickerBinding = DataBindingUtil.inflate<FragmentImagePickerBinding>(inflater,R.layout.fragment_image_picker, container, false)
        fragmentImagePickerBinding.btnCamera.setOnClickListener {


            permissionBuilder = PermissionBuilder()
            permissionBuilder.withFragment(this)
                .withPermission(Manifest.permission.CAMERA)
                .withRationale("Need Camera", "Need Camera")
                .withSuccessListener {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, 10000)
                }
                .build()
        }
        fragmentImagePickerBinding.btnStorage.setOnClickListener {
            permissionBuilder = PermissionBuilder()
            permissionBuilder.withFragment(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withRationale("Need storage", "Need storage")
                .withSuccessListener {
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_PICK
                    startActivityForResult(Intent.createChooser(intent, "Select"), 10001)
                }.build()
        }
        fragmentImagePickerBinding.btnCancel.setOnClickListener { dismiss() }
        return fragmentImagePickerBinding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionBuilder.onRequestResult(requestCode,grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            when (requestCode) {
                10000 -> try {
                    val extras = data.extras
                    val bitmap = extras.get("data") as Bitmap
                    (activity!! as UserDetailActivity).updateField("image_profile",bitmap)
                } catch (e: Exception) {
                } finally {
                    dismiss()
                }
                10001 -> {
                    val uri = data.data
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, uri)
                        (activity!! as UserDetailActivity).updateField("image_profile",bitmap)
                    } catch (e: IOException) {
                    } finally {
                        dismiss()
                    }
                }
            }
        }
    }
}
