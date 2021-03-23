package tom.dev.whatgoingtoeat.ui.select_result

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tom.dev.whatgoingtoeat.dto.history.HistoryResponse
import tom.dev.whatgoingtoeat.dto.history.HistoryResult
import tom.dev.whatgoingtoeat.repository.HistoryRepository
import tom.dev.whatgoingtoeat.utils.SingleLiveEvent
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SelectResultViewModel
@Inject
constructor(
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private val _exceptionOccurred: SingleLiveEvent<String> = SingleLiveEvent()
    val exceptionOccurred: LiveData<String> get() = _exceptionOccurred

    private val _selectResultLiveData: SingleLiveEvent<HistoryResponse<List<HistoryResult>>> = SingleLiveEvent()
    val selectResultLiveData: LiveData<HistoryResponse<List<HistoryResult>>> get() = _selectResultLiveData

    fun findHistoryResult(teamName: String) {
        compositeDisposable.add(
            historyRepository.findHistoryResult(teamName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _selectResultLiveData.postValue(it)
                }, {
                    when (it.cause) {
                        is UnknownHostException -> _exceptionOccurred.postValue("네트워크 오류가 발생했습니다.")
                    }
                })
        )
    }
}