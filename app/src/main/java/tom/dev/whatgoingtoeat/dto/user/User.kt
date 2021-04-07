package tom.dev.whatgoingtoeat.dto.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Long,
    val userId: String,
    val username: String
) : Parcelable