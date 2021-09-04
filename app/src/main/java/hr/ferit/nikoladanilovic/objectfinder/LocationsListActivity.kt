package hr.ferit.nikoladanilovic.objectfinder


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import hr.ferit.nikoladanilovic.objectfinder.adapter.LocationsListAdapter
import hr.ferit.nikoladanilovic.objectfinder.databinding.ActivityLocationsListBinding
import hr.ferit.nikoladanilovic.objectfinder.model.Location


class LocationsListActivity : AppCompatActivity() {
    private val KEY_USER = "email"
    private val TAG = "LocationsListActivity"
    private var isAlredyInDatabase = false
    private var listOfLocations : MutableList<Location> = mutableListOf()
    private lateinit var binding: ActivityLocationsListBinding
    //private lateinit var actionBar: ActionBar
    private lateinit var firebaseAuth: FirebaseAuth

    //Firestore database
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersRef : CollectionReference = db.collection("Users")
    private lateinit var locationListener : ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationsListBinding.inflate(layoutInflater)

        binding.locationsRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.locationsRv.itemAnimator = DefaultItemAnimator()
        binding.locationsRv.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

        setContentView(binding.root)

        //configure ActionBar
        //actionBar = supportActionBar!!
        //actionBar.title = "Profile"

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        addUserToDatabase()

        //handle click, logout
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()

        }

        binding.newLocationBtn.setOnClickListener {
            startActivity(Intent(this, NewLocationActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()

        //check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user not null, user is logged int
            val email = firebaseUser.email

            usersRef.whereEqualTo("email", email).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        locationListener = document.reference.collection("location").addSnapshotListener { value, e ->
                            if(e != null){
                                Log.w(TAG, "Listen failed ", e)
                                return@addSnapshotListener
                            }

                            listOfLocations.clear()

                            for (doc in value!!){
                                val locationFromFirebase = doc.toObject(Location::class.java)
                                locationFromFirebase.setDocumentId(doc.id)
                                listOfLocations.add(locationFromFirebase)
                            }

                            binding.locationsRv.adapter = LocationsListAdapter(listOfLocations)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }

        }
        else {
            //user is null, user is not logged in
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        locationListener.remove()
    }

    private fun checkUser() {
        //check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser == null){
            //user is null, user is not logged in
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun addUserToDatabase() {
        //check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user not null, user is logged int
            val email = firebaseUser.email

            val userData: MutableMap<String, Any> = hashMapOf()
            userData.put(KEY_USER, email!!)     //provjeri!

            var numOfSameUserInDatabase = 0
            usersRef.whereEqualTo(KEY_USER, email).get()
                .addOnSuccessListener { result ->
                    for(document in result){
                        Log.d(TAG, "${document.id} => ${document.data}")
                        //signatureStringg = "promjenilo se"
                        numOfSameUserInDatabase += 1
                        //isAlredyInDatabase = true

                        //return@addOnSuccessListener
                    }

                    if(numOfSameUserInDatabase == 0){
                        usersRef.add(userData)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                    //isAlredyInDatabase = false
                }


        }
        else {
            //user is null, user is not logged in
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}

