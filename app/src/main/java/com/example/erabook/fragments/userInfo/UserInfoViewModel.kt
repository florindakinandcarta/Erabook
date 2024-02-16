import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.erabook.data.firebasedb.UserDataRemote
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserInfoViewModel : ViewModel() {
    private val _userInfo = MutableLiveData<UserDataRemote>()
    private val db = Firebase.firestore
    private val _documentId = MutableLiveData<String>()

    val documentId: LiveData<String>
        get() = _documentId

    val userInfo: LiveData<UserDataRemote>
        get() = _userInfo

    init {
        fetchUserInfo()
    }

    fun fetchUserInfo() {
        db.collection("erabook-users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    _documentId.postValue(document.id)
                    val userBirthdayTimestamp = document.data["user-birthday"] as? Timestamp
                    val userBirthday = userBirthdayTimestamp ?: Timestamp.now()
//                    val userLocation = parseCoordinates(userData["user-location"] as Map<*, *>?)
                    val parsedUserData = UserDataRemote(
                        document.data["user-uid"].toString(),
                        document.data["user-name"].toString(),
                        document.data["user-email"].toString(),
                        (document.data["user-mobile"] as? Long)?.toInt() ?: 0,
                        "",
                        document.data["user-username"].toString(),
                        userBirthday,
                        ArrayList()
                    ).apply {
                        this.userUid = document.data["user-uid"].toString()
                    }

                    _userInfo.postValue(parsedUserData)
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents $exception")
            }
    }

    fun updateUserDataByDocumentId( updatedUserData: UserDataRemote) {
        val documentId = _documentId.value
        if (documentId != null) {
            val documentRef = db.collection("erabook-users").document(documentId)
            documentRef.update(
                "userName", updatedUserData.userName,
                "userUsername", updatedUserData.userUsername,
                "userMobile", updatedUserData.userMobile,
                "userBirthday", updatedUserData.userBirthday
            )
                .addOnSuccessListener {
                    println("Data updated successfully")
                }
                .addOnFailureListener { exception ->
                    println("Error updating document: $exception")
                }
        }else{
            println("Document id null")
        }
    }

//    private fun parseCoordinates(coordinatesMap: Map<*, *>?): Coordinates? {
//        return if (coordinatesMap != null) {
//            Coordinates(coordinatesMap["lat"] as Double?, coordinatesMap["lon"] as Double?)
//        } else {
//            null
//        }
//    }
}
