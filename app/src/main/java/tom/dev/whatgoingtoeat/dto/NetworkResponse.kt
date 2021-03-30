package tom.dev.whatgoingtoeat.dto

data class NetworkResponse<out T>(
    val code: String,
    val message: String,
    val body: T?
)