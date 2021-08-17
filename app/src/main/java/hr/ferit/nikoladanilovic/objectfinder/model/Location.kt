package hr.ferit.nikoladanilovic.objectfinder.model

import com.google.firebase.firestore.Exclude

data class Location(
    private var documentId: String = "",
    private var name: String = "",
    private var coordinates: String = ""
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

    fun getCoordinates() : String {
        return coordinates
    }

    fun setCoordinates(coordinates: String) {
        this.coordinates = coordinates
    }
}
