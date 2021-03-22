package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.UserResponse
import tom.dev.whatgoingtoeat.dto.UserRequest

interface UserRepository {

    fun signIn(name: String): Single<UserResponse<UserRequest>>

    fun signUp(userRequest: UserRequest): Single<UserResponse<UserRequest>>
}