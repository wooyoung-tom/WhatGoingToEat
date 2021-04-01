package tom.dev.whatgoingtoeat.ui.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tom.dev.whatgoingtoeat.dto.user.UserSignUpRequest
import tom.dev.whatgoingtoeat.repository.UserRepository
import tom.dev.whatgoingtoeat.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject
constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
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

    private val _completeSignUpLiveData: SingleLiveEvent<Any> = SingleLiveEvent()
    val completeSignUpLiveData: LiveData<Any> get() = _completeSignUpLiveData

    private val _failSignUpLiveData: SingleLiveEvent<Any> = SingleLiveEvent()
    val failSignUpLiveData: LiveData<Any> get() = _failSignUpLiveData

    private val _nameDuplicateEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val nameDuplicateEvent: LiveData<Any> get() = _nameDuplicateEvent

    fun signUp(userSignUpRequest: UserSignUpRequest) {
        compositeDisposable.add(
            userRepository.signUp(userSignUpRequest)
                .doOnSubscribe { startLoading() }
                .doOnSuccess { stopLoading() }
                .doOnError { stopLoading() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.code) {
                        "FAILED" -> _failSignUpLiveData.call()
                        "SUCCESS" -> _completeSignUpLiveData.call()
                        "DUPLICATED" -> _nameDuplicateEvent.call()
                    }
                }, {
                    it.printStackTrace()
                })
        )
    }
}