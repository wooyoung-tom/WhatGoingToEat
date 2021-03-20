package tom.dev.whatgoingtoeat.ui.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tom.dev.whatgoingtoeat.dto.User
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

    private val _exceptionOccurred: SingleLiveEvent<String> = SingleLiveEvent()
    val exceptionOccurred: LiveData<String> get() = _exceptionOccurred

    private val _nameDuplicationEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val nameDuplicationEvent: LiveData<Any> get() = _nameDuplicationEvent

    private val _completeSignUpEvent: SingleLiveEvent<Any> = SingleLiveEvent()
    val completeSignUpEvent: LiveData<Any> get() = _completeSignUpEvent

    fun signUp(name: String, teamName: String) {
        val newUser = User(name, teamName)

        compositeDisposable.add(
            userRepository.signUp(newUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    // 중복 아이디 존재
                    if (it.body == null) _nameDuplicationEvent.call()
                    else _completeSignUpEvent.call()
                }, {
                    when (it.cause) {
                        is UnknownHostException -> _exceptionOccurred.postValue("네트워크 ㅂㅅ됨")
                    }
                })
        )
    }
}