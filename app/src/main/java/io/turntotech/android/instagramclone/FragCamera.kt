package io.turntotech.android.instagramclone

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment


class FragCamera : Fragment() {

    lateinit var imgLaunchCamera: ImageView
    lateinit var imgOpenGallery: ImageView
    private val CAMERA_REQUEST_CODE = 0
    private val REQUEST_GALLERY_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_camera, container, false)

        imgLaunchCamera = view.findViewById(R.id.imgLaunchCamera)
        imgOpenGallery = view.findViewById(R.id.imgOpenGallery)

        imgLaunchCamera.setOnClickListener {
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(activity!!.packageManager) != null) {
                startActivityForResult(callCameraIntent, CAMERA_REQUEST_CODE)
            }
        }

        imgOpenGallery.setOnClickListener {
            val openGalleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            openGalleryIntent.type = "image/*"
            if (openGalleryIntent.resolveActivity(activity!!.packageManager) != null) {
                startActivityForResult(openGalleryIntent, REQUEST_GALLERY_CODE)
            }
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==0)return

        when (requestCode) {
            REQUEST_GALLERY_CODE-> {

                val selectedPhotoUri: Uri? = data?.data

                val bitmap = when {

                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                        val source = ImageDecoder.createSource(
                            activity!!.contentResolver,
                            selectedPhotoUri!!
                        )
                        ImageDecoder.decodeBitmap(source)
                    }
                    else -> {

                        MediaStore.Images.Media.getBitmap(
                            activity!!.contentResolver,
                            selectedPhotoUri
                        )
                    }
                }

                val width = 300
                val ratio:Float = bitmap.width.toFloat() / bitmap.height.toFloat()
                val height:Int = Math.round(width / ratio)


                val compressedbitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    width,
                    height,
                    false
                )

                val photoFragment = FragPhoto()
                photoFragment.bitmap = compressedbitmap
                //Get supportFragmentManager from Activity
                val fragManager = activity!!.supportFragmentManager
                // Begin the fragment transition using support fragment manager
                val transaction = fragManager.beginTransaction()

                transaction.add(R.id.camera_container, photoFragment)
                transaction.addToBackStack(null)

                // Finishing the transition
                transaction.commit()
            }
            CAMERA_REQUEST_CODE -> {

                if (resultCode == Activity.RESULT_OK && data != null) {
                    val photoFragment = FragPhoto()
                    photoFragment.bitmap = data.extras?.get("data") as Bitmap
                    //Get supportFragmentManager from Activity
                    val fragManager = activity!!.supportFragmentManager
                    // Begin the fragment transition using support fragment manager
                    val transaction = fragManager.beginTransaction()

                    transaction.add(R.id.camera_container, photoFragment)
                    transaction.addToBackStack(null)

                    // Finishing the transition
                    transaction.commit()
                }
            }
            else -> {
                Toast.makeText(activity, "Unrecognized request code", Toast.LENGTH_SHORT).show()
            }
        }
    }
}