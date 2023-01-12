package com.example.advancenoteapplication

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.advancenoteapplication.database.AppDatabase
import com.example.advancenoteapplication.databinding.ActivityMainBinding
import com.example.advancenoteapplication.databinding.DrawerHeaderLayoutBinding
import com.example.advancenoteapplication.viewmodel.NoteViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private val viewModel: NoteViewModel by viewModels()
    lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var ImgFromStore: String
    private lateinit var fname:String
    private lateinit var lname:String
    private lateinit var mail:String
    //var drawerLayoutHeaderLayoutBinding:DrawerHeaderLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        viewModel.init(AppDatabase.getDatabase(this))
        setupNavigationComponent()
        initDrawerLayoutHeader()
    }

    private fun setupNavigationComponent() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        //top toolbar config
        //راه اول برای drawer layout
        appBarConfiguration =
            AppBarConfiguration(navController.graph, activityMainBinding.drawerLayout)

        // second way for create navigation drawer
//        appBarConfiguration = AppBarConfiguration.Builder(setOf(R.id.profileFragment,R.id.settingFragment)
//            ).setOpenableLayout(activityMainBinding.drawerLayout).build()
//
        activityMainBinding.navView.setupWithNavController(navController)

        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()

    }

    fun hideKeyboard(view: View) {
        val imm: InputMethodManager =
            application.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard() {
        val imm: InputMethodManager =
            application.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun initDrawerLayoutHeader() {
        val sharedPref: SharedPreferences? = this.getSharedPreferences("com.example.advancenoteapplication",Context.MODE_PRIVATE)
        ImgFromStore = sharedPref?.getString("profileImg",null).toString();
        fname = sharedPref?.getString("firstname","").toString();
        lname = sharedPref?.getString("lastname","").toString();
        mail = sharedPref?.getString("male","").toString();


        //set profile image
        if (ImgFromStore == null){
            activityMainBinding.drawerLayoutHeader.drawerHeaderImage.setImageResource(R.drawable.profile_placeholder);
        }else {
            activityMainBinding.drawerLayoutHeader.drawerHeaderImage.setImageBitmap(decodeBase64(ImgFromStore));
        }
        //set firstname and lastname
        if (fname.equals("") || lname.equals("")){
            activityMainBinding.drawerLayoutHeader.drawerheaderName.text = "set your name in profile";
        }else{
            activityMainBinding.drawerLayoutHeader.drawerheaderName.text = fname + lname;
        }
        //set mail
        if (mail.equals("")){
            activityMainBinding.drawerLayoutHeader.drawerheaderMail.text = "example@mail.com";
        }else{
            activityMainBinding.drawerLayoutHeader.drawerheaderMail.text = mail;
        }
    }

    // decode string to bitmap
    fun decodeBase64(input: String?): Bitmap? {
        val decodedByte: ByteArray = Base64.decode(input, 0)
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
    }
}




