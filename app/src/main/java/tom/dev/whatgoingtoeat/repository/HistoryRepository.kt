package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.history.History
import tom.dev.whatgoingtoeat.dto.history.HistoryResponse
import tom.dev.whatgoingtoeat.dto.history.HistoryResult

interface HistoryRepository {

    fun saveHistory(history: History): Single<HistoryResponse<History>>

    fun findHistoryResult(teamName: String): Single<HistoryResponse<List<HistoryResult>>>
}