package tom.dev.whatgoingtoeat.dto.user

data class UserSignUpRequest(
    val userId: String,
    val password: String,
    val username: String
)
