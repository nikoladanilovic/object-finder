package hr.ferit.nikoladanilovic.objectfinder

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import hr.ferit.nikoladanilovic.objectfinder.adapter.LocationsListAdapter
import hr.ferit.nikoladanilovic.objectfinder.adapter.ObjectListAdapter
import hr.ferit.nikoladanilovic.objectfinder.databinding.ActivityObjectListOnLocationBinding
import hr.ferit.nikoladanilovic.objectfinder.model.Location
import hr.ferit.nikoladanilovic.objectfinder.model.ObjectOfInterest

class ObjectListOnLocationActivity : AppCompatActivity() {

    private val TAG = "ObjectListOnLocationsActivity"

    private lateinit var binding: ActivityObjectListOnLocationBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var listOfObjects : MutableList<ObjectOfInterest> = mutableListOf()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersRef : CollectionReference = db.collection("Users")
    private lateinit var objectItemListener : ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectListOnLocationBinding.inflate(layoutInflater)
        binding.backToLocationsListBtn.setOnClickListener { backToLocationsList() }
        binding.showLocationOnMapBtn.setOnClickListener { showLocOnMap() }
        binding.createNewObjectItemBtn.setOnClickListener { createNewObjectItem() }

        setCurrentLocationIdInSharedPrefs()

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.objectListRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.objectListRv.itemAnimator = DefaultItemAnimator()
        binding.objectListRv.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

        setContentView(binding.root)
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
                        objectItemListener = document.reference.collection("objects").whereEqualTo("locationId", loadLocationIdData()).addSnapshotListener { value, e ->
                            if(e != null){
                                Log.w(TAG, "Listen failed ", e)
                                return@addSnapshotListener
                            }

                            listOfObjects.clear()

                            for (doc in value!!){
                                val objectsFromFirebase = doc.toObject(ObjectOfInterest::class.java)
                                objectsFromFirebase.setDocumentId(doc.id)
                                listOfObjects.add(objectsFromFirebase)
                            }

                            binding.objectListRv.adapter = ObjectListAdapter(listOfObjects)
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
        objectItemListener.remove()
    }

    private fun setCurrentLocationIdInSharedPrefs() {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val locationId = intent.getStringExtra("ACCESSED_LOCATION")
        editor.apply {
            if(locationId != null && locationId != "") {
                putString("ACCESSED_LOCATION_IN_PREFS", locationId)
            }
        }.apply()
    }

    private fun loadLocationIdData(): String {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("ACCESSED_LOCATION_IN_PREFS", "")!!  //provjeri
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

    private fun createNewObjectItem() {
        startActivity(Intent(this, CreateNewObjectItemActivity::class.java))
    }

    private fun showLocOnMap() {
        startActivity(Intent(this, ShowLocationOnMapActivity::class.java)
            .putExtra("LOCATION_ID_SHOW_MAP", intent.getStringExtra("ACCESSED_LOCATION")))
    }

    private fun backToLocationsList() {
        startActivity(Intent(this, LocationsListActivity::class.java))
        finish()
    }
}