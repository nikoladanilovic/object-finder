package hr.ferit.nikoladanilovic.objectfinder

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.nikoladanilovic.objectfinder.databinding.ActivityShowLocationOnMapBinding
import hr.ferit.nikoladanilovic.objectfinder.model.Location

class ShowLocationOnMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "ShowLocationOnMapActivity"
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityShowLocationOnMapBinding
    private lateinit var locationCoordinate : LatLng
    private lateinit var shownLocation : Location
    private lateinit var firebaseAuth: FirebaseAuth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersRef : CollectionReference = db.collection("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShowLocationOnMapBinding.inflate(layoutInflater)
        binding.goBackToObjectListBtn.setOnClickListener { goBackToObjList() }
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        aquireLocationCoordinates()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map2) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun goBackToObjList() {
        startActivity(Intent(this, ObjectListOnLocationActivity::class.java))
        finish()
    }

    private fun aquireLocationCoordinates() {
        val locationId = loadLocationIdData()      //Rjesi za slucaj kada je locationId = null

        //check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user not null, user is logged int
            val email = firebaseUser.email

            usersRef.whereEqualTo("email", email).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        document.reference.collection("location").document(locationId).get()
                            .addOnSuccessListener { documentResult ->
                                shownLocation = documentResult.toObject(Location::class.java)!!
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }
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

    private fun loadLocationIdData(): String {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("ACCESSED_LOCATION_IN_PREFS", "")!!  //provjeri
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val locationId = intent.getStringExtra("LOCATION_ID_SHOW_MAP")      //Rjesi za slucaj kada je locationId = null

        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user not null, user is logged int
            val email = firebaseUser.email

            usersRef.whereEqualTo("email", email).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        document.reference.collection("location").document(locationId!!).get()
                            .addOnSuccessListener { documentResult ->
                                shownLocation = documentResult.toObject(Location::class.java)!!
                                val locationOnMap = LatLng(shownLocation.getLatitude(), shownLocation.getLongitude())
                                mMap.addMarker(MarkerOptions().position(locationOnMap).title("Location of: ${shownLocation.getName()}"))
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(locationOnMap))
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }
    }
}