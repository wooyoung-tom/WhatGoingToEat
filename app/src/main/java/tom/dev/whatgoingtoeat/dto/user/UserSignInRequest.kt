package tom.dev.whatgoingtoeat.dto.user

data class UserSignInRequest(
    val userId: String,
    val password: String,
)