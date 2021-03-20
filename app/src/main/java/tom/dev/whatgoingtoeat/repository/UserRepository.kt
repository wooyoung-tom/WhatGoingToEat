package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.NetworkResponse
import tom.dev.whatgoingtoeat.dto.User

interface UserRepository {

    fun signIn(name: String): Single<NetworkResponse<User>>

    fun signUp(user: User): Single<NetworkResponse<User>>
}