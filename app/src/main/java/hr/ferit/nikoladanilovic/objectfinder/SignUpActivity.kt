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
import hr.ferit.nikoladanilovic.objectfinder.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating account...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firabase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //handle click, begin signup
        binding.signUpBtn.setOnClickListener {
            //validate data
            validateData()
        }

        binding.goBackToLoginButton.setOnClickListener {
            goBackToLogin()
        }

    }

    private fun goBackToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun validateData() {
        //get data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.emailEt.error = "Invalid email format"
        }
        else if(TextUtils.isEmpty(password)){
            //password isn't entered
            binding.passwordEt.error = "Please enter password"
        }
        else if(password.length < 6){
            //password length is less than 6
            binding.passwordEt.error = "Password must atleast be 6 characters long"
        }
        else {
            //data is valid, continue signup
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        //show progress
        progressDialog.show()

        //create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //signup success
                progressDialog.dismiss()
                //get current user
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Account created with $email", Toast.LENGTH_SHORT).show()

                //open profile
                startActivity(Intent(this, LocationsListActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                //signup failed
                progressDialog.dismiss()
                Toast.makeText(this, "Sign Up Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}

