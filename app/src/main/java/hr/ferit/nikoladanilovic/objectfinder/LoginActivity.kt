package hr.ferit.nikoladanilovic.objectfinder


import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import hr.ferit.nikoladanilovic.objectfinder.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        //config progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click, open sign up activity
        loginBinding.noAccountTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        loginBinding.loginBtn.setOnClickListener{
            //before logging in, validate data
            validateData()
        }
    }

    private fun validateData() {
        //get data
        email = loginBinding.emailEt.text.toString().trim()
        password = loginBinding.passwordEt.text.toString().trim()

        //validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            loginBinding.emailEt.error = "Invalid email format"
        }
        else if (TextUtils.isEmpty(password)){
            //no password entered
            loginBinding.passwordEt.error = "Please enter password"
        }
        else {
            //data is validated, begin login
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        //show progress
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //login success
                progressDialog.dismiss()
                //get user info
                val firebaseUser =firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"Logged in as $email", Toast.LENGTH_SHORT).show()

                //open user profile
                startActivity(Intent(this, LocationsListActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                //login failed
                progressDialog.dismiss()
                Toast.makeText(this,"Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        //if user is alredy logged in go to profile activity
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user is already logged in
            startActivity(Intent(this, LocationsListActivity::class.java))
            finish()
        }
    }
}