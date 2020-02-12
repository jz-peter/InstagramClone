package io.turntotech.android.instagramclone

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class FragCloudCollection : Fragment () {

    val TAG = "FragCloudCollection"
    //Create an array containing photos
    val photos: ArrayList<PhotoDetails> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_cloud_collection, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.photo_recycler_view);

        //Using GridLayoutManager to specify number of photos per row in RecyclerView
        recyclerView.layoutManager = GridLayoutManager(context, 3);

        val adapterCloudCollection = AdapterCloudCollection(photos)
        adapterCloudCollection.fragment = this

        recyclerView.adapter = adapterCloudCollection

        //Using ListAll API to retrieve list of items from Firebase
        val storage = FirebaseStorage.getInstance()
        val listRef = storage.reference.child("images")
        val db = FirebaseFirestore.getInstance()

        //If you want to clear local cache
//        db.clearPersistence().addOnSuccessListener {
//        }

        //Using SnapshotListener to detect realtime changes in Firebase Database
        db.collection("photos").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            photos.clear()
            for (document in snapshot!!) {
                val storageReference =  listRef.child("${document.data.get("filename")}")
                Log.d(TAG, "${document.id} => ${document.data.get("filename")}")

                val photoDetails = PhotoDetails (
                    document.id,
                    storageReference,
                    document.data.get("filename") as String,
                    document.data.get("description") as String,
                    document.data.get("user") as String )
                photos.add(photoDetails)
            }
            adapterCloudCollection.notifyDataSetChanged()
        }


        /*

        listRef.listAll()
            .addOnSuccessListener { listResult ->

                photos.clear()
                listResult.items.forEach { item ->
                    // All the items under listRef.
                    photos.add(item)

                }
                adapterCloudPhotos.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // Uh-oh, an error occurred!
            }

        */

        /*  Java version
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

             MyAdapter mAdapter = new MyAdapter(myDataset);
        recyclerView.setAdapter(mAdapter);

        //myDataset = new ArrayList<>();
        //myDataset.add( "Cell Main " + 5 );

        mAdapter.notifyDataSetChanged();

         */

        return view
    }


}
