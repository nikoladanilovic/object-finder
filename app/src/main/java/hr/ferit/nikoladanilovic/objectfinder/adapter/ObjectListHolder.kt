package hr.ferit.nikoladanilovic.objectfinder.adapter

import android.net.Uri
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.nikoladanilovic.objectfinder.databinding.ObjectItemBinding
import hr.ferit.nikoladanilovic.objectfinder.model.ObjectOfInterest
import java.io.File

class ObjectListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var firebaseAuth: FirebaseAuth
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val usersRef: CollectionReference = db.collection("Users")
    private val TAG = "ObjectListHolder"

    fun bind(object1: ObjectOfInterest) {
        val itemBinding = ObjectItemBinding.bind(itemView)
        itemBinding.objectNameTv.text = object1.getName()
        itemBinding.objectDescTv.text = object1.getDescription()
        val imageUri = Uri.parse(object1.getUriOfImage())
        itemBinding.objectItemIv.setImageURI(null)
        itemBinding.objectItemIv.setImageURI(imageUri)
        itemBinding.deleteObjectIv.setOnClickListener { deleteObjectItem(object1) }

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
    }

    private fun deleteObjectItem(deletedObject: ObjectOfInterest) {
        //check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            //user not null, user is logged int
            val email = firebaseUser.email

            usersRef.whereEqualTo("email", email).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val imageUri = Uri.parse(deletedObject.getUriOfImage())
                        val fdelete = File(imageUri.path!!)
                        fdelete.delete()
                        document.reference.collection("objects")
                            .document(deletedObject.getDocumentId()).delete()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                }
        }
    }
}