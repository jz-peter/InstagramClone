package io.turntotech.android.instagramclone

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragCloudPhoto: Fragment() {

    val TAG = "Database photos"

    lateinit var editTextAddComment: EditText
    lateinit var btnSend: Button
    lateinit var commentsRecyclerView: RecyclerView

    var likeCount = 0
    var userLiked = false


    lateinit var bitmapFromViewholder: Bitmap
    lateinit var photoDetails: PhotoDetails

    //Create comments Arraylist with data from Comments class
    var comments = ArrayList<Comments>()
    val fireDb = FirebaseFirestore.getInstance()

    lateinit var adapterCloudComments: AdapterCloudComments

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.title = "InstagramClone"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_cloud_photo, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = photoDetails.description

        setup(view)
        readComments()

        btnSend.setOnClickListener {
            addComment()
        }
        return view
    }

    private fun setup(view: View) {

        editTextAddComment = view.findViewById(R.id.editTextAddComment)
        btnSend = view.findViewById(R.id.btnSend)
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView)

        val recyclerView: RecyclerView = view.findViewById(R.id.commentsRecyclerView);
        recyclerView.layoutManager = GridLayoutManager(context, 1);

        adapterCloudComments = AdapterCloudComments(bitmapFromViewholder, comments)
        adapterCloudComments.fragment = this

        recyclerView.adapter = adapterCloudComments
    }

    private fun addComment() {
        val auth = FirebaseAuth.getInstance()
        val commentData = hashMapOf(
            "user" to auth.currentUser?.displayName,
//                "filename" to filename,
//                "description" to editTextPhotoDescription.text.toString(),
            "comment" to editTextAddComment.text.toString()
        )
        // Add a new document with a generated ID to Firebase Database
        fireDb.collection("photos/${photoDetails.id}/comments")
            .add(commentData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
        editTextAddComment.text.clear()
        editTextAddComment.onEditorAction(EditorInfo.IME_ACTION_DONE);
        Toast.makeText(activity, "Comment Posted", Toast.LENGTH_LONG).show()
    }

    //add comments data to Comments class
    private fun readComments() {
        //comments.forEach { comment ->
        //fireDb.collection("photos/${photoDetails.id}/comments")
        // comments.add()

        //Using SnapshotListener to detect realtime changes in Firebase Database
        fireDb.collection("photos/${photoDetails.id}/comments").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            comments.clear()
            comments.add(Comments("",""))

            for (document in snapshot!!) {
                val photoComments = Comments (
                    document.data.get("user") as String,
                    document.data.get("comment") as String )
                comments.add(photoComments)
            }
            adapterCloudComments.notifyDataSetChanged()
        }

        val auth = FirebaseAuth.getInstance()
        val displayName = auth.currentUser!!.displayName

        fireDb.collection("photos/${photoDetails.id}/likes").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            likeCount = snapshot!!.documents.size
            userLiked = false

            for (document in snapshot!!) {
                 if( document.id == displayName ){
                     userLiked = true
                 }
            }
            adapterCloudComments.notifyDataSetChanged()
        }
    }

    fun removeLike() {
        val likeData = hashMapOf(
            "liked" to 1)

        val auth = FirebaseAuth.getInstance()
        auth.currentUser?.displayName?.let {
            fireDb.collection("photos/${photoDetails.id}/likes")
                .document(it)
                .delete()
                .addOnSuccessListener { documentReference ->
                    //Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error removing document", e)
                }
        }
        Toast.makeText(activity, "Like Removed", Toast.LENGTH_SHORT).show()
    }

    fun addLike(){
        val likeData = hashMapOf(
             "liked" to 1)

        val auth = FirebaseAuth.getInstance()
        auth.currentUser?.displayName?.let {
            fireDb.collection("photos/${photoDetails.id}/likes")
                .document(it)
                .set(HashMap<String,String>())
                .addOnSuccessListener { documentReference ->
                    //Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                }
        }
        Toast.makeText(activity, "Liked", Toast.LENGTH_SHORT).show()
    }
}


