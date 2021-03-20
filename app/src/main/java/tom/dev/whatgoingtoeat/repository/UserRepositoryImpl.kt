package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.NetworkResponse
import tom.dev.whatgoingtoeat.dto.User
import tom.dev.whatgoingtoeat.service.UserService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl
@Inject
constructor(
    private val userService: UserService
) : UserRepository {

    override fun signIn(name: String): Single<NetworkResponse<User>> {
        return userService.signIn(name)
    }

    override fun signUp(user: User): Single<NetworkResponse<User>> {
        return userService.signUp(user)
    }
}