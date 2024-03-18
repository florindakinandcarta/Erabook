import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Epub(
    val isAvailable: Boolean? = null
) : Parcelable