package tom.dev.whatgoingtoeat.dto.user

data class UserSignInResponse(
    val code: String,
    val body: User?
)
