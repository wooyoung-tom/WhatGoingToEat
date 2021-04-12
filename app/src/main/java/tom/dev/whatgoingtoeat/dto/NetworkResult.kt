package tom.dev.whatgoingtoeat.dto

sealed class NetworkResult<out T> {
    data class OK<out T>(val body: T) : NetworkResult<T>()

    object Loading : NetworkResult<Nothing>()

    data class Error(val exception: Throwable) : NetworkResult<Nothing>()
}