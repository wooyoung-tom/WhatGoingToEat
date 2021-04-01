package tom.dev.whatgoingtoeat.service

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import tom.dev.whatgoingtoeat.dto.user.UserSignInRequest
import tom.dev.whatgoingtoeat.dto.user.UserSignInResponse
import tom.dev.whatgoingtoeat.dto.user.UserSignUpRequest
import tom.dev.whatgoingtoeat.dto.user.UserSignUpResponse

interface UserService {

    @POST("/market/users/signup")
    fun signUp(
        @Body user: UserSignUpRequest
    ): Single<UserSignUpResponse>

    @POST("/market/users/signin")
    fun signIn(
        @Body user: UserSignInRequest
    ): Single<UserSignInResponse>
}