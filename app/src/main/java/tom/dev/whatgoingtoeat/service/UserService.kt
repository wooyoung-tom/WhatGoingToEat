package tom.dev.whatgoingtoeat.service

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import tom.dev.whatgoingtoeat.dto.CommonSimpleResponse
import tom.dev.whatgoingtoeat.dto.user.UserSignInRequest
import tom.dev.whatgoingtoeat.dto.user.UserSignInResponse
import tom.dev.whatgoingtoeat.dto.user.UserSignUpRequest

interface UserService {

    @POST("/users/register")
    fun signUp(
        @Body user: UserSignUpRequest
    ): Single<CommonSimpleResponse>

    @POST("/users/sign-in")
    fun signIn(
        @Body user: UserSignInRequest
    ): Single<UserSignInResponse>
}