package hr.ferit.nikoladanilovic.objectfinder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.nikoladanilovic.objectfinder.R
import hr.ferit.nikoladanilovic.objectfinder.model.ObjectOfInterest

class ObjectListAdapter (objects: MutableList<ObjectOfInterest>) : RecyclerView.Adapter<ObjectListHolder>() {
    private val objects: MutableList<ObjectOfInterest>

    init {
        this.objects = mutableListOf()
        this.objects.addAll(objects)
    }

    fun refreshData(objects: MutableList<ObjectOfInterest>){
        this.objects.clear()
        this.objects.addAll(objects)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectListHolder {
        val objectsView = LayoutInflater.from(parent.context).inflate(R.layout.object_item, parent, false)
        val objectsHolder = ObjectListHolder(objectsView)
        return objectsHolder
    }

    override fun onBindViewHolder(holder: ObjectListHolder, position: Int) {
        val object1 = objects[position]
        holder.bind(object1)
    }

    override fun getItemCount(): Int {
        return objects.size
    }

}