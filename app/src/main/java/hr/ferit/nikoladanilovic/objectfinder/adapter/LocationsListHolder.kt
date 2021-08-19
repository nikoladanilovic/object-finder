package hr.ferit.nikoladanilovic.objectfinder.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.nikoladanilovic.objectfinder.LocationsListActivity
import hr.ferit.nikoladanilovic.objectfinder.LoginActivity
import hr.ferit.nikoladanilovic.objectfinder.NewLocationActivity
import hr.ferit.nikoladanilovic.objectfinder.ObjectListOnLocationActivity
import hr.ferit.nikoladanilovic.objectfinder.databinding.LocationsItemBinding
import hr.ferit.nikoladanilovic.objectfinder.model.Location
import hr.ferit.nikoladanilovic.objectfinder.model.ObjectOfInterest
import java.io.File

class LocationsListHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersRef : CollectionReference = db.collection("Users")

    private lateinit var context: Context

    private val TAG = "LocationsListHolder"

    fun bind(location: Location){
        val locationsItemBinding = LocationsItemBinding.bind(itemView)
        locationsItemBinding.locationNameTv.text = location.getName()
        locationsItemBinding.deleteImgBtn.setOnClickListener { deleteLocation(location) }
        locationsItemBinding.enterLocationImgBtn.setOnClickListener { enterSpecificLocationObjectFinder(location) }

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

    }


    private fun enterSpecificLocationObjectFinder(accessLocation: Location) {
        context = itemView.context
        val intentt = Intent(context, ObjectListOnLocationActivity::class.java)
        intentt.putExtra("ACCESSED_LOCATION", accessLocation.getDocumentId())
        context.startActivity(intentt)
        //finish()  //provjeri!!
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

            //TREBAT CE SE OVJDE IMPLEMENTIRATI I BRISANJE SVIH OBJEKATA KOJE OPISUJE PRIPADNA LOKACIJA KOJA SE BRISE!!

            usersRef.whereEqualTo("email", email).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        document.reference.collection("objects").whereEqualTo("locationId", deletedLocation.getDocumentId()).get()
                            .addOnSuccessListener { result2 ->
                                for (document2 in result2) {
                                    val objectFromFirebase = document2.toObject(ObjectOfInterest::class.java)
                                    val imageUri = Uri.parse(objectFromFirebase.getUriOfImage())
                                    val fdelete = File(imageUri.path!!)
                                    fdelete.delete()

                                    document2.reference.delete()
                                }
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }

        }
        }
    }
