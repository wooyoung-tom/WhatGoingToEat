package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.user.UserSignInRequest
import tom.dev.whatgoingtoeat.dto.user.UserSignInResponse
import tom.dev.whatgoingtoeat.dto.user.UserSignUpRequest
import tom.dev.whatgoingtoeat.dto.user.UserSignUpResponse
import tom.dev.whatgoingtoeat.service.UserService
import javax.inject.Inject

class UserRepositoryImpl
@Inject
constructor(
    private val userService: UserService
) : UserRepository {
    override fun signUp(user: UserSignUpRequest): Single<UserSignUpResponse> {
        return userService.signUp(user)
    }

    override fun signIn(user: UserSignInRequest): Single<UserSignInResponse> {
        return userService.signIn(user)
    }
}