package tom.dev.whatgoingtoeat.ui.search_result

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.Flow
import tom.dev.whatgoingtoeat.dto.search.SearchDocument
import tom.dev.whatgoingtoeat.repository.SearchRepository
import tom.dev.whatgoingtoeat.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel
@Inject
constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private val _exceptionOccurred: SingleLiveEvent<String> = SingleLiveEvent()
    val exceptionOccurred: LiveData<String> get() = _exceptionOccurred

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<SearchDocument>>? = null

    fun searchByKeyword(query: String, latitude: String, longitude: String): Flow<PagingData<SearchDocument>> {
        startLoading()

        val lastResult = currentSearchResult
        if (query == currentQueryValue && lastResult != null) return lastResult
        currentQueryValue = query
        val newResult = searchRepository.searchByKeyword(query, latitude, longitude).cachedIn(viewModelScope)
        currentSearchResult = newResult

        stopLoading()
        return newResult
    }

    private val _startLoadingDialogEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val startLoadingDialogEvent: LiveData<Any> get() = _startLoadingDialogEvent

    private val _stopLoadingDialogEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val stopLoadingDialogEvent: LiveData<Any> get() = _stopLoadingDialogEvent

    private fun startLoading() {
        _startLoadingDialogEvent.call()
    }

    private fun stopLoading() {
        _stopLoadingDialogEvent.call()
    }
}