package tom.dev.whatgoingtoeat.service

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import tom.dev.whatgoingtoeat.dto.UserResponse
import tom.dev.whatgoingtoeat.dto.UserRequest

interface UserService {

    @GET("/lunch/users/{name}")
    fun signIn(
        @Path("name") name: String
    ): Single<UserResponse<UserRequest>>

    @POST("/lunch/users")
    fun signUp(
        @Body userRequest: UserRequest
    ): Single<UserResponse<UserRequest>>
}