package io.turntotech.android.instagramclone

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView


class AdapterCloudCollection (val photos : ArrayList<PhotoDetails>): RecyclerView.Adapter<PhotoViewHolder>( ){

    lateinit var fragment: Fragment

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.cell_layout_photos, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        //holder?.txtName?.text = names.get(position)

        val photoDetails = photos.get(position)
        val imageRef = photoDetails.storageReference
        val ONE_MEGABYTE: Long = 1024 * 1024
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { imageByteArray->

            val bitmapImage = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
            holder.viewHolderPhoto.setImageBitmap(bitmapImage)

        }.addOnFailureListener {
            // Handle any errors
        }

        holder.viewHolderPhoto.setOnClickListener{
            val cloudPhotoFrag = FragCloudPhoto()
            val bitmapFromViewHolder = holder.viewHolderPhoto.drawable.toBitmap()
            cloudPhotoFrag.bitmapFromViewholder = bitmapFromViewHolder

            cloudPhotoFrag.photoDetails = photoDetails

            val fragManager = fragment.fragmentManager
            val transaction = fragManager!!.beginTransaction()
            transaction.add(R.id.cloud_collection_container, cloudPhotoFrag)
                .addToBackStack(null)
                .commit()
        }
    }
}

class PhotoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val viewHolderPhoto = itemView.findViewById<ImageView>(R.id.photo_item)
}
