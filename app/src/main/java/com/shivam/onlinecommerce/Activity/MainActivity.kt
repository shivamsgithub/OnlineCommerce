package com.shivam.onlinecommerce.Activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.shivam.onlinecommerce.Fragment.AccountFragment
import com.shivam.onlinecommerce.Fragment.CartFragment
import com.shivam.onlinecommerce.Fragment.HomeFragment
import com.shivam.onlinecommerce.R


class MainActivity : AppCompatActivity() {

    lateinit var bottomNav: BottomNavigationView
//    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    lateinit var gso : GoogleSignInOptions
    lateinit var gsc : GoogleSignInClient

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<View>(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        toolbar.setTitleTextColor(resources.getColor(R.color.black))
        setSupportActionBar(toolbar)

            var frag = intent.getIntExtra("fragment", 0)
        Log.d("FragmentCheck1", "Fragment value: $frag")

        if (frag == 2) {
            loadFragment(CartFragment())
        }else{
            loadFragment(HomeFragment())
        }
//
//            var accountName = account?.displayName
            var accountName = intent.getStringExtra("SignedInName")
            var accountNameAlreadySignedIn = intent.getStringExtra("alreadySignedName")


        val bottomNav : BottomNavigationView = findViewById(R.id.bottomNav)
        val tvCount : TextView = findViewById(R.id.tv_item_count_main)
        val ivCart : ImageView = findViewById(R.id.iv_cart)
        tvCount.visibility = View.GONE
        ivCart.visibility = View.GONE
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.cart -> {
                    loadFragment(CartFragment())
                    true
                }

                R.id.account -> {
                    loadFragment(AccountFragment())
                    true
                }

                else -> {
                    loadFragment(HomeFragment())
                    true
                }
            }
        }

        val prefs: SharedPreferences = getSharedPreferences("Cart Values", MODE_PRIVATE)
        var count = prefs.getInt("count", 0)
        tvCount.text = "$count"

//        drawerLayout = findViewById(R.id.my_drawer_layout)


        val drawerLayout: DrawerLayout = findViewById(R.id.my_drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val menu = navView.menu

        // Inflate the menu items from the XML file
        menuInflater.inflate(R.menu.drawer, menu)



        // Programmatically add a menu item
        if (accountName != null){
            menu.add("Hi, $accountName")
        }else if (accountNameAlreadySignedIn != null){
            menu.add("Hi, $accountNameAlreadySignedIn")
        }else {
            menu.add("Hi, User")
        }
        menu.add("Account")
        menu.add("Categories")
        menu.add("Settings")
        menu.add("Contact Us")
        menu.add("Sign Out")

        navView.setNavigationItemSelectedListener { item ->
            if (item.title.equals("Sign Out")) {
                if (accountName != null){
                    gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
                    val signInClient = GoogleSignIn.getClient(this, gso)
                    var account = GoogleSignIn.getLastSignedInAccount(this@MainActivity)
                    var googleSignInApiClient = GoogleApiClient.Builder(applicationContext).addApi(Auth.GOOGLE_SIGN_IN_API).build()

                    signInClient.signOut().addOnCompleteListener(this@MainActivity, OnCompleteListener {
                        Toast.makeText(this, "Signed Out Successfully", Toast.LENGTH_SHORT).show()
                    })

                } else {
                    Toast.makeText(this, "You have not signed in", Toast.LENGTH_SHORT).show()
                    drawerLayout.close()
                }
                return@setNavigationItemSelectedListener true
            }
            false
        }

        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        drawerLayout.invalidate()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment).addToBackStack(null)
        transaction.commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    fun customExitDialog() {

        val dialog = Dialog(this@MainActivity)
        dialog.setContentView(R.layout.exit_layout)

        val dialogButtonYes = dialog.findViewById(R.id.tv_yes) as TextView
        val dialogButtonNo = dialog.findViewById(R.id.tv_no) as TextView

        dialogButtonNo.setOnClickListener { // dismiss the dialog
            dialog.dismiss()
        }
        dialogButtonYes.setOnClickListener { // dismiss the dialog and exit the exit
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }

//    override fun onBackPressed() {
////        if(GoogleSignIn.getLastSignedInAccount(this) != null && ){
////            customExitDialog()
////        }else{
////            super.onBackPressed()
////        }
//    }
}