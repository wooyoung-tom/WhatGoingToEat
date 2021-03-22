package tom.dev.whatgoingtoeat.service

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import tom.dev.whatgoingtoeat.dto.user.UserResponse
import tom.dev.whatgoingtoeat.dto.user.User

interface UserService {

    @GET("/lunch/users/{name}")
    fun signIn(
        @Path("name") name: String
    ): Single<UserResponse<User>>

    @POST("/lunch/users")
    fun signUp(
        @Body userRequest: User
    ): Single<UserResponse<User>>
}