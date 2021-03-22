package tom.dev.whatgoingtoeat.ui.select_menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tom.dev.whatgoingtoeat.dto.history.History
import tom.dev.whatgoingtoeat.dto.user.User
import tom.dev.whatgoingtoeat.repository.HistoryRepository
import tom.dev.whatgoingtoeat.utils.SingleLiveEvent
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SelectCategoryViewModel
@Inject
constructor(
    private val historyRepository: HistoryRepository
): ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    var currentSelectedCategory: String? = null

    private val _exceptionOccurred: SingleLiveEvent<String> = SingleLiveEvent()
    val exceptionOccurred: LiveData<String> get() = _exceptionOccurred

    private val _saveCategoryHistoryComplete: SingleLiveEvent<History> = SingleLiveEvent()
    val saveCategoryHistoryComplete: LiveData<History> get() = _saveCategoryHistoryComplete

    fun completeSelectCategory(user: User, date: String) {
        val newHistory = History(
            name = user.name,
            teamName = user.teamName,
            historyDate = date,
            category = currentSelectedCategory!!
        )

        compositeDisposable.add(
            historyRepository.saveHistory(newHistory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _saveCategoryHistoryComplete.postValue(it.body)
                }, {
                    when (it.cause) {
                        is UnknownHostException -> _exceptionOccurred.postValue("네트워크 오류가 발생했습니다.")
                    }
                })
        )
    }
}