package io.turntotech.android.instagramclone

import com.google.firebase.storage.StorageReference

class PhotoDetails (val id: String, val storageReference: StorageReference, val filename: String, val description: String, val user: String) {

}