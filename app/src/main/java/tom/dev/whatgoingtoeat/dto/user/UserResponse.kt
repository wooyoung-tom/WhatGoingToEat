package tom.dev.whatgoingtoeat.dto.user

data class UserResponse<out T>(
    val selected: Boolean,
    val message: String,
    val body: T?
)