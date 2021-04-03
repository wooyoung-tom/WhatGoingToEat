package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.user.UserSignInResponse
import tom.dev.whatgoingtoeat.dto.user.UserSignUpResponse
import tom.dev.whatgoingtoeat.dto.user.UserSigningRequest
import tom.dev.whatgoingtoeat.service.UserService
import javax.inject.Inject

class UserRepositoryImpl
@Inject
constructor(
    private val userService: UserService
) : UserRepository {
    override fun signUp(user: UserSigningRequest): Single<UserSignUpResponse> {
        return userService.signUp(user)
    }

    override fun signIn(user: UserSigningRequest): Single<UserSignInResponse> {
        return userService.signIn(user)
    }
}