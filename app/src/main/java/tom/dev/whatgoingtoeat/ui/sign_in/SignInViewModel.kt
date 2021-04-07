package tom.dev.whatgoingtoeat.ui.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tom.dev.whatgoingtoeat.dto.user.User
import tom.dev.whatgoingtoeat.dto.user.UserSignInRequest
import tom.dev.whatgoingtoeat.repository.UserRepository
import tom.dev.whatgoingtoeat.utils.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class SignInViewModel
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

    private val _nameNotFoundEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val nameNotFoundEvent: LiveData<Any> get() = _nameNotFoundEvent

    private val _passwordInvalidEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val passwordInvalidEvent: LiveData<Any> get() = _passwordInvalidEvent

    private val _successEvent: SingleLiveEvent<User> = SingleLiveEvent()
    val successEvent: LiveData<User> get() = _successEvent

    fun signIn(user: UserSignInRequest) {
        compositeDisposable.add(
            userRepository.signIn(user)
                .doOnSubscribe { startLoading() }
                .doOnSuccess { stopLoading() }
                .doOnError { stopLoading() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.code) {
                        "NotFound" -> _nameNotFoundEvent.call()
                        "Failed" -> _passwordInvalidEvent.call()
                        "Success" -> _successEvent.postValue(it.user)
                    }
                }, {
                    it.printStackTrace()
                })
        )
    }
}