package tom.dev.whatgoingtoeat.dto.user

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserSigningRequest(
    val name: String,
    val password: String
)
