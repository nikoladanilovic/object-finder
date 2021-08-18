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

        Log.d(TAG, "onCreate: ${intent.getStringExtra("ACCESSED_LOCATION")}")

        setContentView(binding.root)
    }

    private fun backToLocationsList() {
        startActivity(Intent(this, LocationsListActivity::class.java))
        finish()
    }
}