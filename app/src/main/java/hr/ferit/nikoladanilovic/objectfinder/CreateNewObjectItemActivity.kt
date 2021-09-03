package hr.ferit.nikoladanilovic.objectfinder

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.nikoladanilovic.objectfinder.databinding.ActivityCreateNewObjectItemBinding
import hr.ferit.nikoladanilovic.objectfinder.model.Location
import hr.ferit.nikoladanilovic.objectfinder.model.ObjectOfInterest
import java.lang.Exception

class CreateNewObjectItemActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCreateNewObjectItemBinding
    private val TAG = "CreateNewObjectItemActivity"
    private lateinit var firebaseAuth: FirebaseAuth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersRef : CollectionReference = db.collection("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewObjectItemBinding.inflate(layoutInflater)

        binding.captureImageBtn.setOnClickListener { captureImg() }
        binding.createNewObjectBtn.setOnClickListener { createNewObject() }

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        loadDataForEditViews()
    }

    override fun onStop() {
        super.onStop()
        saveDataForEditViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.objectNameEt.setText("")
        binding.objectDescEt.setText("")
        resetImageUri()
        saveDataForEditViews()
    }


    private fun createNewObject() {

        //check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user not null, user is logged int
            val email = firebaseUser.email
            val objectName = binding.objectNameEt.text.toString()
            val objectDesc = binding.objectDescEt.text.toString()
            val objectImgUri = loadImgUriData()
            val objectLocationId = loadLocationIdData()

            val newObjectData = ObjectOfInterest("", objectName, objectDesc, objectImgUri, objectLocationId)

            if(objectName != "" && objectDesc != "" && objectImgUri != "" && objectLocationId != "") {
                usersRef.whereEqualTo("email", email).get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            document.reference.collection("objects").add(newObjectData)
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
        
        startActivity(Intent(this, ObjectListOnLocationActivity::class.java))
        finish()
    }

    private fun loadLocationIdData(): String {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("ACCESSED_LOCATION_IN_PREFS", "")!!  //provjeri
    }

    private fun captureImg() {
        startActivity(Intent(this, TakeObjectPhotoActivity::class.java))
    }

    private fun loadImgUriData() : String{
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("CAPTURED_PHOTO_URI", "")!!  //provjeri
    }

    private fun checkUser() {
        //check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null){
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun saveDataForEditViews(){
        val objectName = binding.objectNameEt.text.toString()
        val objectDesc = binding.objectDescEt.text.toString()
        binding.objectNameEt.setText(objectName)
        binding.objectDescEt.setText(objectDesc)

        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("OBJECT_NAME_CREATE_NEW_OBJECT_ACTIVITY", objectName)
            putString("OBJECT_DESC_CREATE_NEW_OBJECT_ACTIVITY", objectDesc)
        }.apply()
    }

    private fun loadDataForEditViews(){
        try {
            val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
            var savedString = sharedPreferences.getString("OBJECT_NAME_CREATE_NEW_OBJECT_ACTIVITY", "")
            binding.objectNameEt.setText(savedString)
            savedString = sharedPreferences.getString("OBJECT_DESC_CREATE_NEW_OBJECT_ACTIVITY", "")
            binding.objectDescEt.setText(savedString)
        }catch (e : Exception){
            binding.objectNameEt.setText("")
            binding.objectDescEt.setText("")
        }
    }


    private fun resetImageUri() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("CAPTURED_PHOTO_URI", "")
        }.apply()
    }
}