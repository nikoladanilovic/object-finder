package hr.ferit.nikoladanilovic.objectfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.nikoladanilovic.objectfinder.databinding.ActivityNewLocationBinding
import hr.ferit.nikoladanilovic.objectfinder.model.Location
import java.time.Duration

class NewLocationActivity : AppCompatActivity() {
    private val TAG = "NewLocationActivity"

    private lateinit var binding: ActivityNewLocationBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersRef : CollectionReference = db.collection("Users")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewLocationBinding.inflate(layoutInflater)
        binding.CreateLocBtn.setOnClickListener { addLocation() }
        binding.openMapBtn.setOnClickListener { openMap() }

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()



        setContentView(binding.root)
    }

    private fun openMap() {
        startActivity(Intent(this, MapSetLocationActivity::class.java))
    }

    private fun checkUser() {
        //check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user not null, user is logged int
            val email = firebaseUser.email

            Log.d(TAG, "Currently logged account email is: $email")

        }
        else {
            //user is null, user is not logged in
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun addLocation() {
        //check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user not null, user is logged int
            val email = firebaseUser.email
            val locationName = binding.locNameEt.text.toString()

            var coordinatesArray = doubleArrayOf(0.0, 0.0)

            if(intent.getDoubleArrayExtra("COORDINATES_ID") != null){
                coordinatesArray = intent.getDoubleArrayExtra("COORDINATES_ID")!!
            }

            val locationData = Location("", locationName, coordinatesArray[0], coordinatesArray[1])

            if(locationName != "" && coordinatesArray[0] != 0.0 && coordinatesArray[1] != 0.0) {
                usersRef.whereEqualTo("email", email).get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            document.reference.collection("location").add(locationData)
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }
            }
            else{
                Toast.makeText(this, "You must fill all necessary information!", Toast.LENGTH_SHORT).show()
                return
            }
        }
        else {
            //user is null, user is not logged in
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        startActivity(Intent(this, LocationsListActivity::class.java))
        finish()
    }



}