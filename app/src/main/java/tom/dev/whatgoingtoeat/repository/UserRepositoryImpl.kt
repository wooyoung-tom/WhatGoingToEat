package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.UserResponse
import tom.dev.whatgoingtoeat.dto.UserRequest
import tom.dev.whatgoingtoeat.service.UserService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl
@Inject
constructor(
    private val userService: UserService
) : UserRepository {

    override fun signIn(name: String): Single<UserResponse<UserRequest>> {
        return userService.signIn(name)
    }

    override fun signUp(userRequest: UserRequest): Single<UserResponse<UserRequest>> {
        return userService.signUp(userRequest)
    }
}