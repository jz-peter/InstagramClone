package io.turntotech.android.instagramclone

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import androidx.fragment.app.Fragment

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest


class FragCreateUser: Fragment() {

    lateinit var textViewUsername: TextView
    lateinit var editTextUsername: EditText
    lateinit var textViewEmail: TextView
    lateinit var editTextEmail: EditText
    lateinit var textViewPassword: TextView
    lateinit var editTextPassword: EditText
    lateinit var btnCreateUser: Button
    lateinit var auth: FirebaseAuth
    val create = "User Created"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_create_user, container, false)
            textViewUsername = view.findViewById(R.id.textViewUsername)
            editTextUsername = view.findViewById(R.id.editTextUsername)
            textViewEmail = view.findViewById(R.id.textViewEmail)
            editTextEmail = view.findViewById(R.id.editTextEmail)
            textViewPassword = view.findViewById(R.id.textViewPassword)
            editTextPassword = view.findViewById(R.id.editTextPassword)
            btnCreateUser = view.findViewById(R.id.btnCreateUser)

        btnCreateUser.setOnClickListener{
            auth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(
                editTextEmail.text.toString(),
                editTextPassword.text.toString())
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(context, "User Created Successful.",
                            Toast.LENGTH_LONG).show()
                        Log.d(create, "User Create Successful")
                        val user = auth.currentUser
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(editTextUsername.text.toString())
                              .build()

                        user?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Log.d(create, "User profile updated.")
                                }

                                val fragManager = activity!!.supportFragmentManager
                                var transaction = fragManager!!.beginTransaction()
                                transaction.remove(this)
                                transaction.commit()
                            }

                    } else {
                        // If create user fails, display a message to the user.
                        Log.w(create, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(context, "Create user failed. Please try again",
                            Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }
                }
        }
        return view
    }
}
