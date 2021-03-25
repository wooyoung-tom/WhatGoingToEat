package tom.dev.whatgoingtoeat.ui.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tom.dev.whatgoingtoeat.dto.user.User
import tom.dev.whatgoingtoeat.repository.UserRepository
import tom.dev.whatgoingtoeat.utils.SingleLiveEvent
import java.net.UnknownHostException
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

    private val _exceptionOccurred: SingleLiveEvent<String> = SingleLiveEvent()
    val exceptionOccurred: LiveData<String> get() = _exceptionOccurred

    private val _nameLengthInvalidEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val nameLengthInvalidEvent: LiveData<Any> get() = _nameLengthInvalidEvent

    private val _teamNameNotSelectedEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val teamNameNotSelectedEvent: LiveData<Any> get() = _teamNameNotSelectedEvent

    private val _nameDuplicationEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val nameDuplicationEvent: LiveData<Any> get() = _nameDuplicationEvent

    private val _completeSignUpEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val completeSignUpEvent: LiveData<Any> get() = _completeSignUpEvent

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

    fun signUp(name: String, teamName: String) {
        // 이름 길이가 10자를 넘어가게되면 invalid
        if (name.length > 10) {
            _nameLengthInvalidEvent.call()
            return
        }
        // 팀 이름 고르지 않았다면
        if (teamName.isEmpty() || teamName.isBlank()) {
            _teamNameNotSelectedEvent.call()
            return
        }

        val newUser = User(name, teamName)

        compositeDisposable.add(
            userRepository.signUp(newUser)
                .doOnSubscribe { startLoading() }
                .doOnSuccess { stopLoading() }
                .doOnError { stopLoading() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // 중복 아이디 존재
                    if (it.body == null) _nameDuplicationEvent.call()
                    else _completeSignUpEvent.call()
                }, {
                    when (it.cause) {
                        is UnknownHostException -> _exceptionOccurred.postValue("네트워크 오류가 발생했습니다.")
                    }
                })
        )
    }
}