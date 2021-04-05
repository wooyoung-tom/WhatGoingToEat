package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.CommonSimpleResponse
import tom.dev.whatgoingtoeat.dto.user.UserSignInResponse
import tom.dev.whatgoingtoeat.dto.user.UserSigningRequest
import tom.dev.whatgoingtoeat.service.UserService
import javax.inject.Inject

class UserRepositoryImpl
@Inject
constructor(
    private val userService: UserService
) : UserRepository {
    override fun signUp(user: UserSigningRequest): Single<CommonSimpleResponse> {
        return userService.signUp(user)
    }

    override fun signIn(user: UserSigningRequest): Single<UserSignInResponse> {
        return userService.signIn(user)
    }
}