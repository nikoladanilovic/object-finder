package hr.ferit.nikoladanilovic.objectfinder.adapter

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import hr.ferit.nikoladanilovic.objectfinder.databinding.ObjectItemBinding
import hr.ferit.nikoladanilovic.objectfinder.model.ObjectOfInterest

class ObjectListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(object1: ObjectOfInterest){
        val itemBinding = ObjectItemBinding.bind(itemView)
        itemBinding.objectNameTv.text = object1.getName()
        itemBinding.objectDescTv.text = object1.getDescription()
        val imageUri = Uri.parse(object1.getUriOfImage())
        itemBinding.objectItemIv.setImageURI(null)
        itemBinding.objectItemIv.setImageURI(imageUri)
    }
}