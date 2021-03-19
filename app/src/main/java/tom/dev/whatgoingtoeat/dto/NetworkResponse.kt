package tom.dev.whatgoingtoeat.dto

data class NetworkResponse<out T>(
    val message: String,
    val body: T?
)