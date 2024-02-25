package com.shivam.onlinecommerce.Activity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.shivam.onlinecommerce.R

class SignInActivity : AppCompatActivity() {

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private var REQ_ONE_TAP = 100
    lateinit var tvSignUp: TextView
    lateinit var btnSkiptoMain : Button
    lateinit var tvGotoSignUp : TextView
    lateinit var gso : GoogleSignInOptions

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        tvSignUp = findViewById(R.id.tv_sign_up)
        btnSkiptoMain = findViewById(R.id.btn_skip_to_main_activity)
        tvGotoSignUp = findViewById(R.id.tv_sign_up)

        btnSkiptoMain.setOnClickListener{
            intent = Intent (this@SignInActivity, MainActivity::class.java)
            startActivity(intent)
        }

        tvGotoSignUp.setOnClickListener{
            finish()
            intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        if(GoogleSignIn.getLastSignedInAccount(this@SignInActivity)?.isExpired == false) {
            finish()
            intent = Intent (this@SignInActivity, MainActivity::class.java)
            intent.putExtra("alreadySignedName",
                GoogleSignIn.getLastSignedInAccount(this)?.email.toString()
            )
            startActivity(intent)
        }


        val llGoogleSignIn: LinearLayout = findViewById(R.id.ll_sign_in_with_google)

        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId("629543722686-cr35v5ju54fnb4nnte9lc5735luioj1k.apps.googleusercontent.com")
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()


        llGoogleSignIn.setOnClickListener{
            SignIn()
        }

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        val signInClient = GoogleSignIn.getClient(this, gso)

    }

    fun SignIn(){
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0, null)
                    Log.d("Sign In Success", "Successfully Signed In")
                } catch (e: IntentSender.SendIntentException) {
                    Log.e("Sign In Success Catch", "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener(this) { e ->
                Log.d("Sign In Failure", e.localizedMessage)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    val username = credential.id
                    val password = credential.password
                    val name = credential.displayName

                    if (name != null) {
                        goToMainActivity(name)
                    }else{
                        goToMainActivity("user")
                    }

                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with your backend.
                            Log.d(TAG, "Got ID token.")
                        }
                        password != null -> {
                            // Got a saved username and password. Use them to authenticate
                            // with your backend.
                            Log.d(TAG, "Got password.")
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token or password!")
                        }
                    }
                } catch (e: ApiException) {
                    // ...
                }
            }
        }
    }

    private fun goToMainActivity(user: String) {
        finish()
        intent = Intent(this@SignInActivity, MainActivity::class.java)
        intent.putExtra("SignedInName", user)
        startActivity(intent)
    }
}