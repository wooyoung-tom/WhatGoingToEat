package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.CommonSimpleResponse
import tom.dev.whatgoingtoeat.dto.user.UserSignInResponse
import tom.dev.whatgoingtoeat.dto.user.UserSigningRequest

interface UserRepository {
    fun signUp(user: UserSigningRequest): Single<CommonSimpleResponse>
    fun signIn(user: UserSigningRequest): Single<UserSignInResponse>
}