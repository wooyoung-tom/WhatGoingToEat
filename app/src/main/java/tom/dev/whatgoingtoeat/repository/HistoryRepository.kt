package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.history.History
import tom.dev.whatgoingtoeat.dto.history.HistoryResponse

interface HistoryRepository {

    fun saveHistory(history: History): Single<HistoryResponse<History>>
}