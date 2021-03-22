package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.user.UserResponse
import tom.dev.whatgoingtoeat.dto.user.User
import tom.dev.whatgoingtoeat.service.UserService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl
@Inject
constructor(
    private val userService: UserService
) : UserRepository {

    override fun signIn(name: String): Single<UserResponse<User>> {
        return userService.signIn(name)
    }

    override fun signUp(userRequest: User): Single<UserResponse<User>> {
        return userService.signUp(userRequest)
    }
}