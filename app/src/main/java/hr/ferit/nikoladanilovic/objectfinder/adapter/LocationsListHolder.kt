package hr.ferit.nikoladanilovic.objectfinder.adapter

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.nikoladanilovic.objectfinder.LoginActivity
import hr.ferit.nikoladanilovic.objectfinder.databinding.LocationsItemBinding
import hr.ferit.nikoladanilovic.objectfinder.model.Location

class LocationsListHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersRef : CollectionReference = db.collection("Users")

    private val TAG = "LocationsListHolder"

    fun bind(location: Location){
        val locationsItemBinding = LocationsItemBinding.bind(itemView)
        locationsItemBinding.locationNameTv.text = location.getName()
        locationsItemBinding.deleteImgBtn.setOnClickListener { deleteLocation(location) }
        locationsItemBinding.enterLocationImgBtn.setOnClickListener { enterSpecificLocationObjectFinder() }

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

    }


    private fun enterSpecificLocationObjectFinder() {
        TODO("Implement entering object list of specific location")
    }

    private fun deleteLocation(deletedLocation: Location) {
        //check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user not null, user is logged int
            val email = firebaseUser.email

            usersRef.whereEqualTo("email", email).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        document.reference.collection("location").document(deletedLocation.getDocumentId()).delete()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }

        //TREBAT CE SE OVJDE IMPLEMENTIRATI I BRISANJE SVIH OBJEKATA KOJE OPISUJE PRIPADNA LOKACIJA KOJA SE BRISE!!

    }






}