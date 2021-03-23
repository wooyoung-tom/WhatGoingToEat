package tom.dev.whatgoingtoeat.service

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import tom.dev.whatgoingtoeat.dto.history.History
import tom.dev.whatgoingtoeat.dto.history.HistoryResponse
import tom.dev.whatgoingtoeat.dto.history.HistoryResult

interface HistoryService {

    @POST("/lunch/history")
    fun saveHistory(
        @Body history: History
    ): Single<HistoryResponse<History>>

    @GET("/lunch/history")
    fun findHistoryResult(
        @Query("teamName") teamName: String
    ): Single<HistoryResponse<List<HistoryResult>>>
}