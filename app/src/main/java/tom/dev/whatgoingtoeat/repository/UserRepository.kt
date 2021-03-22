package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.user.UserResponse
import tom.dev.whatgoingtoeat.dto.user.User

interface UserRepository {

    fun signIn(name: String): Single<UserResponse<User>>

    fun signUp(userRequest: User): Single<UserResponse<User>>
}