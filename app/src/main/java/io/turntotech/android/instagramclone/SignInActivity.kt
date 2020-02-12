package io.turntotech.android.instagramclone

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class SignInActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser!=null){

            var intent = Intent(this, MainActivity::class.java).apply {}
            startActivity(intent)
            finish()

        } else {

            setContentView(R.layout.activity_signin)

            val fragManager = supportFragmentManager

            val signInFrag = FragSignIn()
            var transaction = fragManager.beginTransaction()
            transaction.replace(R.id.user_container, signInFrag)
            // transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}