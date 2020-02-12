package io.turntotech.android.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    lateinit var imgBtnHome: ImageButton
    lateinit var imgBtnCamera: ImageButton


    //inflate the menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imgBtnHome = findViewById(R.id.imgBtnHome)
        imgBtnCamera = findViewById(R.id.imgBtnCamera)

        val fragManager = supportFragmentManager

        val cloudCollectionFrag = FragCloudCollection()
        var transaction = fragManager.beginTransaction()
        transaction.replace(R.id.cloud_collection_container, cloudCollectionFrag)
        //transaction.addToBackStack(null)
        transaction.commit()

        val cameraFrag = FragCamera()
        transaction = fragManager.beginTransaction()
        transaction.replace(R.id.camera_container, cameraFrag)
        //transaction.addToBackStack(null)
        transaction.commit()

        imgBtnHome.setOnClickListener {
            cloud_collection_container.visibility = View.VISIBLE
            camera_container.visibility = View.INVISIBLE
        }

        imgBtnCamera.setOnClickListener {
            cloud_collection_container.visibility = View.INVISIBLE
            camera_container.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logoff -> {

                FirebaseAuth.getInstance().signOut()

                var intent = Intent(this, SignInActivity::class.java).apply {}
                startActivity(intent)
                finish()

                return true
            }
        }
        return false
    }
}
