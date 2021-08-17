package hr.ferit.nikoladanilovic.objectfinder.model

import com.google.firebase.firestore.Exclude

data class Location(
    private var documentId: String = "",
    private var name: String = "",
    private var latitude: Double = 0.0,
    private var longitude: Double = 0.0
) {
    @Exclude
    fun getDocumentId() : String {
        return documentId
    }

    fun setDocumentId(documentId: String){
        this.documentId = documentId
    }

    fun getName() : String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getLatitude() : Double {
        return latitude
    }

    fun setLatitude(latitude: Double) {
        this.latitude = latitude
    }

    fun getLongitude() : Double {
        return longitude
    }

    fun setLongitude(longitude: Double) {
        this.longitude = longitude
    }
}
