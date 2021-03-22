package tom.dev.whatgoingtoeat.service

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import tom.dev.whatgoingtoeat.dto.history.History
import tom.dev.whatgoingtoeat.dto.history.HistoryResponse

interface HistoryService {

    @POST("/lunch/history")
    fun saveHistory(
        @Body history: History
    ): Single<HistoryResponse<History>>
}