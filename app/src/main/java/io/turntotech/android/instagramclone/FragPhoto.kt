package io.turntotech.android.instagramclone

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*

class FragPhoto : Fragment() {

    lateinit var imgPhoto: ImageView
    lateinit var btnUpload: Button
    lateinit var editTextPhotoDescription: EditText
    lateinit var bitmap: Bitmap
    lateinit var storage: FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_photo, container, false)
        imgPhoto = view.findViewById(R.id.imgPhoto)
        imgPhoto.setImageBitmap(bitmap)
        btnUpload = view.findViewById(R.id.btnUpload)
        editTextPhotoDescription = view.findViewById(R.id.editTextPhotoDescription)

        val fragManager = fragmentManager
        val fireDb = FirebaseFirestore.getInstance()
        val TAG = "Database photos"

        //Create instance of FirebaseStorage
        storage = FirebaseStorage.getInstance()

        // Create a storage reference from our app
        var storageRef = storage.reference

        val filename = UUID.randomUUID().toString()+".jpg"; //generate random filename for each photo

        Log.d(TAG, "Filename: " + filename);

        val takenPhotoRef = storageRef.child("images/$filename")

        // val takenPhotoImagesRef = storageRef.child("images/takenPhoto.jpg")
        //  takenPhotoRef.name == takenPhotoImagesRef.name
        //  takenPhotoRef.path == takenPhotoImagesRef.path

        // Create a child reference
        // imagesRef now points to "images"
        var imagesRef: StorageReference? = storageRef.child("images")

        //Click Upload button to upload photo to Cloud (Firebase)
        btnUpload.setOnClickListener{

            //Upload file from app memory to Cloud (Firebase)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            var uploadTask = takenPhotoRef.putBytes(data)

            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener {
                // taskSnapshot.metadata contains file metadata such as size, content-type, etc.

                val image = hashMapOf(
                    "user" to "Peter",
                    "filename" to filename,
                    "description" to editTextPhotoDescription.text.toString()
                )
                // Add a new document with a generated ID to Firebase Database
                fireDb.collection("photos")
                    .add(image)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }

                Toast.makeText(activity, "Photo Uploaded", Toast.LENGTH_LONG).show()
                var transaction = fragManager!!.beginTransaction()
                transaction.remove(this)
                transaction.commit()
            }
        }
        return view
    }
}