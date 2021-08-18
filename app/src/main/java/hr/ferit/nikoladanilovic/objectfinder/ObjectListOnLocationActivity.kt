package hr.ferit.nikoladanilovic.objectfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import hr.ferit.nikoladanilovic.objectfinder.databinding.ActivityObjectListOnLocationBinding

class ObjectListOnLocationActivity : AppCompatActivity() {

    private val TAG = "ObjectListOnLocationsActivity"

    private lateinit var binding: ActivityObjectListOnLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectListOnLocationBinding.inflate(layoutInflater)
        binding.backToLocationsListBtn.setOnClickListener { backToLocationsList() }
        binding.showLocationOnMapBtn.setOnClickListener { showLocOnMap() }

        setContentView(binding.root)
    }

    private fun showLocOnMap() {
        startActivity(Intent(this, ShowLocationOnMapActivity::class.java).putExtra("LOCATION_ID_SHOW_MAP", intent.getStringExtra("ACCESSED_LOCATION")))
    }

    private fun backToLocationsList() {
        startActivity(Intent(this, LocationsListActivity::class.java))
        finish()
    }
}