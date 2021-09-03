package hr.ferit.nikoladanilovic.objectfinder

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hr.ferit.nikoladanilovic.objectfinder.databinding.ActivityMapSetLocationBinding

class MapSetLocationActivity : AppCompatActivity(), GoogleMap.OnMapClickListener, OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener,
    ActivityCompat.OnRequestPermissionsResultCallback { //zadnja 3 dodana

    private val TAG = "MapSetLocationActivity"
    
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapSetLocationBinding
    private lateinit var chosenPoint: LatLng

    private var permissionDenied = false    //dodano

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapSetLocationBinding.inflate(layoutInflater)
        binding.confirmLocationBtn.setOnClickListener { confirmLocation() }
        setContentView(binding.root)

        chosenPoint = LatLng(-34.0, 151.0)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun confirmLocation() {
        val latLongArray = doubleArrayOf(chosenPoint.latitude, chosenPoint.longitude)

        val intentt = Intent(this, NewLocationActivity::class.java)
        intentt.putExtra("COORDINATES_ID", latLongArray)
        startActivity(intentt)
        finish()
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

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.setOnMyLocationButtonClickListener(this) //dodano
        mMap.setOnMyLocationClickListener(this)    //dodano
        enableMyLocation()  //dodano
        mMap.setOnMapClickListener(this)
    }

    override fun onMapClick(p0: LatLng) {
        Log.d(TAG, "onMapClick: tapped, point=$p0")
        chosenPoint = p0

        mMap.clear()
        val tappedPlace = LatLng(p0.latitude, p0.longitude)
        mMap.addMarker(MarkerOptions().position(tappedPlace).title("Location of potential objects"))
    }

    //sve ispod je dodano

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (!::mMap.isInitialized) return
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        //Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        //Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            // Enable the my location layer if the permission has been granted.
            if (allPermissionsGranted()) {
                enableMyLocation()
            }else {
                // Permission was denied. Display an error message
                // Display the missing permission error dialog when the fragments resume.
                permissionDenied = true
            }

        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
        Toast.makeText(this, "Permission for device location is missing! You can manually allow permission for location in application settings!", Toast.LENGTH_SHORT).show()
    }

    private fun allPermissionsGranted() = MapSetLocationActivity.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


}