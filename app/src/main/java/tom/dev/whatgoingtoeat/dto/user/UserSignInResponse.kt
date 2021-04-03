package tom.dev.whatgoingtoeat.dto.user

data class UserSignInResponse(
    val code: String,
    val message: String,
    val user: User? = null
)
