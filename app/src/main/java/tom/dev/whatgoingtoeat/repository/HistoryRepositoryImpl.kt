package tom.dev.whatgoingtoeat.repository

import io.reactivex.Single
import tom.dev.whatgoingtoeat.dto.history.History
import tom.dev.whatgoingtoeat.dto.history.HistoryResponse
import tom.dev.whatgoingtoeat.dto.history.HistoryResult
import tom.dev.whatgoingtoeat.service.HistoryService
import javax.inject.Inject

class HistoryRepositoryImpl
@Inject
constructor(
    private val historyService: HistoryService
) : HistoryRepository {

    override fun saveHistory(history: History): Single<HistoryResponse<History>> {
        return historyService.saveHistory(history)
    }

    override fun findHistoryResult(teamName: String): Single<HistoryResponse<List<HistoryResult>>> {
        return historyService.findHistoryResult(teamName)
    }
}