package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.user.UserSignInResponse
import tom.dev.whatgoingtoeat.dto.user.UserSignUpResponse
import tom.dev.whatgoingtoeat.dto.user.UserSigningRequest

interface UserRepository {
    fun signUp(user: UserSigningRequest): Single<UserSignUpResponse>
    fun signIn(user: UserSigningRequest): Single<UserSignInResponse>
}