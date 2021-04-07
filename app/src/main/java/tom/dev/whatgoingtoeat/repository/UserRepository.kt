package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.CommonSimpleResponse
import tom.dev.whatgoingtoeat.dto.user.UserSignInRequest
import tom.dev.whatgoingtoeat.dto.user.UserSignInResponse
import tom.dev.whatgoingtoeat.dto.user.UserSignUpRequest

interface UserRepository {
    fun signUp(user: UserSignUpRequest): Single<CommonSimpleResponse>
    fun signIn(user: UserSignInRequest): Single<UserSignInResponse>
}