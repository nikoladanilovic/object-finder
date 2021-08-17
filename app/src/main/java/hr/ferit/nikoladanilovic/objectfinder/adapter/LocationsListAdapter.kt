package hr.ferit.nikoladanilovic.objectfinder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.nikoladanilovic.objectfinder.R
import hr.ferit.nikoladanilovic.objectfinder.databinding.LocationsItemBinding
import hr.ferit.nikoladanilovic.objectfinder.model.Location

class LocationsListAdapter (locations : MutableList<Location>) : RecyclerView.Adapter<LocationsListHolder>() {
    private val locations: MutableList<Location>

    init {
        this.locations = mutableListOf()
        this.locations.addAll(locations)
    }

    fun refreshData(locations: MutableList<Location>){
        this.locations.clear()
        this.locations.addAll(locations)
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return locations.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsListHolder {
        val locationsView = LayoutInflater.from(parent.context).inflate(R.layout.locations_item, parent, false)
        val locationsHolder = LocationsListHolder(locationsView)
        return locationsHolder
    }

    override fun onBindViewHolder(holder: LocationsListHolder, position: Int) {
        val location = locations[position]
        holder.bind(location)
    }

}