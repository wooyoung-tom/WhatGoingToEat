package tom.dev.whatgoingtoeat.dto.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userId: Long,
    val userName: String
) : Parcelable