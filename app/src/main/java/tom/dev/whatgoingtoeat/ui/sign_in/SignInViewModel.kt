package tom.dev.whatgoingtoeat.ui.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tom.dev.whatgoingtoeat.dto.user.User
import tom.dev.whatgoingtoeat.utils.SingleLiveEvent
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private val _exceptionOccurred: SingleLiveEvent<String> = SingleLiveEvent()
    val exceptionOccurred: LiveData<String> get() = _exceptionOccurred

    private val _failedToSignIn: SingleLiveEvent<Any> = SingleLiveEvent()
    val failedToSignIn: LiveData<Any> get() = _failedToSignIn

    private val _needToRegisterHistory: SingleLiveEvent<User> = SingleLiveEvent()
    val needToRegisterHistory: LiveData<User> get() = _needToRegisterHistory

    private val _successToSignIn: SingleLiveEvent<User> = SingleLiveEvent()
    val successToSignIn: LiveData<User> get() = _successToSignIn

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

    // 로그인 함수
    fun signIn(name: String) {
        compositeDisposable.add(
            userRepository.signIn(name)
                .doOnSubscribe { startLoading() }
                .doOnSuccess { stopLoading() }
                .doOnError { stopLoading() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // body 가 null 이면 찾지 못해서 가입 해야 하는 것
                    if (it.body == null) _failedToSignIn.call()
                    else {
                        if (it.code == "SUCCESS") _successToSignIn.postValue(it.body)
                        else _needToRegisterHistory.postValue(it.body)
                    }
                }, {
                    when (it.cause) {
                        is UnknownHostException -> _exceptionOccurred.postValue("네트워크 오류가 발생했습니다.")
                    }
                })
        )
    }
}