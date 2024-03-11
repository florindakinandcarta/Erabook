import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Epub(
    var isAvailable: Boolean? = null
) : Parcelable