package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.user.UserSignInRequest
import tom.dev.whatgoingtoeat.dto.user.UserSignInResponse
import tom.dev.whatgoingtoeat.dto.user.UserSignUpRequest
import tom.dev.whatgoingtoeat.dto.user.UserSignUpResponse

interface UserRepository {
    fun signUp(user: UserSignUpRequest): Single<UserSignUpResponse>
    fun signIn(user: UserSignInRequest): Single<UserSignInResponse>
}