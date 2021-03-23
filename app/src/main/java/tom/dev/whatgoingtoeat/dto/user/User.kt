package tom.dev.whatgoingtoeat.dto.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val teamName: String
): Parcelable
