package xyz.manzodev.thecoffeehouse.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.ActivityLoginBinding
import xyz.manzodev.thecoffeehouse.user.model.User
import java.util.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        var activityLoginBinding = DataBindingUtil.setContentView<ActivityLoginBinding>(this,
            R.layout.activity_login
        )


        activityLoginBinding.btnGoogle.setOnClickListener { signInGoogle() }

        activityLoginBinding.btnFacebook.setOnClickListener { signInFacebook() }
    }

    private fun signInGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, 112)
    }

    var callbackManager : CallbackManager? = null

    private fun signInFacebook() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    val authCredential =
                        FacebookAuthProvider.getCredential(loginResult.accessToken.token)
                    signIn(authCredential)
                }

                override fun onCancel() {}

                override fun onError(error: FacebookException) {Log.d("OK",error.toString())}
            })
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("email", "public_profile","user_friends"))
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.let { it.onActivityResult(requestCode, resultCode, data) }

        if (requestCode === 112) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
                signIn(credential)

            } catch (e: ApiException) {
                //Todo : Handle Google sign in fail
                //Sign in Fail
                e.printStackTrace()
            }

        }
    }

    private fun signIn(authCredential: AuthCredential){
        var firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener{
            if (it.isSuccessful()){
                var isNews= it.result!!.additionalUserInfo!!.isNewUser
                if (isNews){
                    var user = User.convertUser(firebaseAuth.currentUser!!)
                    FirebaseDatabase.getInstance().getReference("User").child(user.uid).setValue(user)
                }
                finish()
            }
            else{
                //Login Fail
            }
        }
    }
}
