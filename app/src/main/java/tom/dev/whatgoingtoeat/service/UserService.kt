package tom.dev.whatgoingtoeat.service

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import tom.dev.whatgoingtoeat.dto.NetworkResponse
import tom.dev.whatgoingtoeat.dto.User

interface UserService {

    @GET("/lunch/users/{name}")
    fun signIn(
        @Path("name") name: String
    ): Single<NetworkResponse<User>>

    @POST("/lunch/users")
    fun signUp(
        @Body user: User
    ): Single<NetworkResponse<User>>
}