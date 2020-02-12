package io.turntotech.android.instagramclone

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView



class AdapterCloudComments (val bitmap: Bitmap, val comments : ArrayList<Comments>): RecyclerView.Adapter<RecyclerView.ViewHolder> ( ) {

    lateinit var fragment: FragCloudPhoto

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1) {
            val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.cell_layout_preview, parent, false)
            return PreviewViewHolder(view)

        } else {
            val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.cell_layout_comments, parent, false)
            return CommentsViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position==0){
            return 1
        } else {
            return 2
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //holder?.txtName?.text = names.get(position)

        if(position==0){
            val previewViewHolder: PreviewViewHolder = holder as PreviewViewHolder
            previewViewHolder.viewHolderPreview.setImageBitmap(bitmap)
            previewViewHolder.viewHolderLikeCount.text = "Likes: ${fragment.likeCount}"

            if(fragment.userLiked){
                previewViewHolder.viewHolderLikeBtnRed.setVisibility(View.VISIBLE)
                previewViewHolder.viewHolderLikeBtnGray.setVisibility(View.INVISIBLE)

            } else {
                previewViewHolder.viewHolderLikeBtnGray.setVisibility(View.VISIBLE)
                previewViewHolder.viewHolderLikeBtnRed.setVisibility(View.INVISIBLE)
            }

                holder.viewHolderLikeBtnGray.setOnClickListener {
                    previewViewHolder.viewHolderLikeBtnRed.setVisibility(View.VISIBLE)
                    previewViewHolder.viewHolderLikeBtnGray.setVisibility(View.INVISIBLE)
                    fragment.addLike()
            }
                holder.viewHolderLikeBtnRed.setOnClickListener {
                    previewViewHolder.viewHolderLikeBtnGray.setVisibility(View.VISIBLE)
                    previewViewHolder.viewHolderLikeBtnRed.setVisibility(View.INVISIBLE)
                    fragment.removeLike()
            }

        } else {

            val commentsViewHolder: CommentsViewHolder = holder as CommentsViewHolder

            commentsViewHolder.viewHolderUser.text = comments.get(position).username
            commentsViewHolder.viewHolderComments.text = comments.get(position).comment
        }

//            val commentText = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
//            holder.viewHolderPhoto.set(bitmapImage)
    }
}

class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val viewHolderUser = itemView.findViewById<TextView>(R.id.comments_user)
    val viewHolderComments = itemView.findViewById<TextView>(R.id.comments_item)
}

class PreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val viewHolderPreview = itemView.findViewById<ImageView>(R.id.preview_photo_item)
    val viewHolderLikeBtnGray = itemView.findViewById<ImageButton>(R.id.imageButtonLikeGray)
    val viewHolderLikeBtnRed = itemView.findViewById<ImageButton>(R.id.imageButtonLikeRed)
    val viewHolderLikeCount = itemView.findViewById<TextView>(R.id.textViewLikeCount)}
