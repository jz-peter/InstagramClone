package io.turntotech.android.instagramclone

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class FragSignIn: Fragment () {

    lateinit var auth: FirebaseAuth
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var btnSignIn: Button
    lateinit var btnCreateUser: Button
    val FragName = "Login Success"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_sign_in, container, false)
        editTextEmail = view.findViewById(R.id.editTextEmail)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        btnSignIn = view.findViewById(R.id.btnSignIn)
        btnCreateUser = view.findViewById(R.id.btnCreateUser)
        auth = FirebaseAuth.getInstance()

        btnSignIn.setOnClickListener {
            auth.signInWithEmailAndPassword(
                editTextEmail.text.toString(),
                editTextPassword.text.toString()
            )
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        var intent = Intent(activity, MainActivity::class.java).apply {}
                        startActivity(intent)
                        activity!!.finish()

                        //Log.d(AppCompatActivity.TAG, "Login Success")

                         Log.d(FragName, "Login Success")
                         val user = auth.currentUser
                        //updateUI(user)

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(FragName, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        //updateUI(null)
                    }
                }
        }

        btnCreateUser.setOnClickListener{
            val fragManager = activity!!.supportFragmentManager
            val createUserFrag = FragCreateUser()
            var transaction = fragManager.beginTransaction()
            transaction.add(R.id.user_container, createUserFrag)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return view
    }
}