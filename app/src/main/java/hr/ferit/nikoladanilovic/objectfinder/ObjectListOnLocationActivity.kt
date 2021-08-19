package hr.ferit.nikoladanilovic.objectfinder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.nikoladanilovic.objectfinder.adapter.ObjectListAdapter
import hr.ferit.nikoladanilovic.objectfinder.databinding.ActivityObjectListOnLocationBinding
import hr.ferit.nikoladanilovic.objectfinder.model.ObjectOfInterest

class ObjectListOnLocationActivity : AppCompatActivity() {

    private val TAG = "ObjectListOnLocationsActivity"

    private lateinit var binding: ActivityObjectListOnLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectListOnLocationBinding.inflate(layoutInflater)
        binding.backToLocationsListBtn.setOnClickListener { backToLocationsList() }
        binding.showLocationOnMapBtn.setOnClickListener { showLocOnMap() }
        binding.createNewObjectItemBtn.setOnClickListener { createNewObjectItem() }

        binding.objectListRv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.objectListRv.itemAnimator = DefaultItemAnimator()
        binding.objectListRv.addItemDecoration(DividerItemDecoration(this, RecyclerView.VERTICAL))

        displayData()   //placeholder function

        setContentView(binding.root)
    }

    //placeholder function
    private fun displayData() {
        var exampleMutableListObj = mutableListOf<ObjectOfInterest>()
        val exampleObj1 = ObjectOfInterest("", "random ime","random opis","random uri","w4aseraw42d1")
        val exampleObj2 = ObjectOfInterest("", "random ime2","random opis2","random uri2","w4aseraw42d12")
        val exampleObj3 = ObjectOfInterest("", "random ime3","random opis3","random uri3","w4aseraw42d13")
        exampleMutableListObj.add(exampleObj1)
        exampleMutableListObj.add(exampleObj2)
        exampleMutableListObj.add(exampleObj3)
        exampleMutableListObj.add(exampleObj1)

        binding.objectListRv.adapter = ObjectListAdapter(exampleMutableListObj)
    }

    private fun createNewObjectItem() {
        /*
        startActivity(Intent(this, CreateNewObjectItemActivity::class.java)
            .putExtra("LOCATION_ID_CREATE_OBJECT", intent.getStringExtra("ACCESSED_LOCATION")))
         */
        startActivity(Intent(this, CreateNewObjectItemActivity::class.java))
        //Toast.makeText(this,"Not implemented yet.", Toast.LENGTH_SHORT).show()
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