import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.erabook.R
import com.example.erabook.data.firebasedb.UserDataRemote
import com.google.firebase.Timestamp
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class UserInfoViewModel : ViewModel() {
    private val _userInfo = MutableLiveData<UserDataRemote>()
    private val db = Firebase.firestore
    private val _documentId = MutableLiveData<String>()
    private val _errorMessage = MutableLiveData<Int>()

    val errorMessage: LiveData<Int>
        get() = _errorMessage

    val userInfo: LiveData<UserDataRemote>
        get() = _userInfo

    init {
        fetchUserInfo()
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.collection("erabook-users")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            _documentId.postValue(document.id)
                            val userBirthdayTimestamp = document.data["userBirthday"] as? Timestamp
                            val userBirthday = userBirthdayTimestamp ?: Timestamp.now()
//                    val userLocation = parseCoordinates(userData["user-location"] as Map<*, *>?)
                            val parsedUserData = UserDataRemote(
                                document.data["userUid"].toString(),
                                document.data["userName"].toString(),
                                document.data["userEmail"].toString(),
                                (document.data["userMobile"] as? Long)?.toInt() ?: 0,
                                "",
                                document.data["userUsername"].toString(),
                                userBirthday.toDate(),
                                ArrayList()
                            ).apply {
                                this.userUid = document.data["userUid"].toString()
                            }

                            _userInfo.postValue(parsedUserData)
                        }
                    }
                    .addOnFailureListener { exception ->
                        _errorMessage.postValue(R.string.error_fetching_data)
                        println("Error getting documents $exception")
                    }
            }
        }
    }

    fun updateUserDataByEmail(
        updatedUserData: UserDataRemote,
        userEmail: String
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.collection("erabook-users")
                    .whereEqualTo("userEmail", userEmail)
                    .get()
                    .addOnSuccessListener { documents ->
                        val batch = db.batch()
                        for (document in documents) {
                            val documentRef = db.collection("erabook-users").document(document.id)
                            batch.update(
                                documentRef, mapOf(
                                    "userName" to updatedUserData.userName,
                                    "userUsername" to updatedUserData.userUsername,
                                    "userMobile" to updatedUserData.userMobile,
                                    "userBirthday" to updatedUserData.userBirthday
                                )
                            )
                        }
                        batch.commit().addOnSuccessListener {
                            fetchUserInfo()
                            println("Data updated successfully")
                        }
                            .addOnFailureListener { exception ->
                                println("Error updating documents: $exception")
                            }
                    }
                    .addOnFailureListener { exception ->
                        println("Error getting documents: $exception")
                    }
            }
        }
    }

    fun addUserData(newUserDataRemote: UserDataRemote) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                db.collection("erabook-users")
                    .document()
                    .set(newUserDataRemote, SetOptions.merge())
            }
        }
    }
    fun updateUserBirthday(newBirthday: Date){
        val currentUser = userInfo.value
        requireNotNull(currentUser){
            "User info must not be null"
        }
        val updatedBirthday = currentUser.copy(userBirthday = newBirthday)
        _userInfo.postValue(updatedBirthday)
    }
    //TODO(getUserLocation)
//    private fun parseCoordinates(coordinatesMap: Map<*, *>?): Coordinates? {
//        return if (coordinatesMap != null) {
//            Coordinates(coordinatesMap["lat"] as Double?, coordinatesMap["lon"] as Double?)
//        } else {
//            null
//        }
//    }
}
