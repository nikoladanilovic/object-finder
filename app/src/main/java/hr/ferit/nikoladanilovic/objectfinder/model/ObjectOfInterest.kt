package hr.ferit.nikoladanilovic.objectfinder.model

import com.google.firebase.firestore.Exclude

data class ObjectOfInterest(
    private var documentId: String = "",
    private var name: String = "",
    private var description: String = "",
    private var uriOfImage: String = "",
    private var locationId: String = ""
){
    @Exclude
    fun getDocumentId() : String {
        return documentId
    }

    fun setDocumentId(documentId: String){
        this.documentId = documentId
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String){
        this.name = name
    }

    fun getDescription(): String {
        return description
    }

    fun setDescription(description: String){
        this.description = description
    }

    fun getUriOfImage(): String {
        return uriOfImage
    }

    fun setUriOfImage(uriOfImage: String){
        this.uriOfImage = uriOfImage
    }

    fun getLocationId(): String {
        return locationId
    }

    fun setLocationId(locationId: String){
        this.locationId = locationId
    }
}
