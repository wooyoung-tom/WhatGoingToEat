package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.NetworkResponse
import tom.dev.whatgoingtoeat.dto.User
import tom.dev.whatgoingtoeat.service.UserService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userService: UserService
) {

    fun signIn(name: String): Single<NetworkResponse<User>> {
        return userService.signIn(name)
    }
}