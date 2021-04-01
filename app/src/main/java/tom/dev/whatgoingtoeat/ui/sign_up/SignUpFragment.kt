package tom.dev.whatgoingtoeat.ui.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentSignUpBinding
import tom.dev.whatgoingtoeat.dto.user.UserSignUpRequest
import tom.dev.whatgoingtoeat.utils.LoadingDialog
import tom.dev.whatgoingtoeat.utils.showShortSnackBar

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val viewModel: SignUpViewModel by viewModels()

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        setSignUpButtonClickListener()

        observeLoading()
        observeSignUpResult()
    }

    // Destroy 시에 _binding null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setSignUpButtonClickListener() {
        binding.btnSignUp.setOnClickListener {
            val name = binding.etSignUpId.text.toString()
            val password = binding.etSignUpPassword.text.toString()
            val passwordCheck = binding.etSignUpPasswordCheck.text.toString()

            if (checkUserInfoValid(name, password, passwordCheck))
                viewModel.signUp(UserSignUpRequest(name, password))
        }
    }

    private fun checkUserInfoValid(name: String, password: String, passwordCheck: String): Boolean {
        if (name.length > 10) {
            binding.tilSignUpId.error = "글자수가 10자를 초과했습니다."
            return false
        }
        if (name.isBlank()) {
            binding.tilSignUpId.error = "아이디를 입력해주세요."
            return false
        }
        if (password.isBlank()) {
            binding.tilSignUpPasswordCheck.error = "비밀번호를 입력해주세요."
            return false
        }
        if (passwordCheck.isBlank()) {
            binding.tilSignUpPasswordCheck.error = "비밀번호를 한번 더 입력해주세요."
            return false
        }
        if (password != passwordCheck) {
            binding.tilSignUpPasswordCheck.error = "비밀번호가 일치하지 않습니다."
            return false
        }

        binding.tilSignUpId.error = null
        return true
    }

    private fun observeLoading() {
        val loading = LoadingDialog(requireContext())
        viewModel.startLoadingDialogEvent.observe(viewLifecycleOwner) {
            loading.show()
        }
        viewModel.stopLoadingDialogEvent.observe(viewLifecycleOwner) {
            loading.dismiss()
        }
    }

    private fun observeSignUpResult() {
        viewModel.failSignUpLiveData.observe(viewLifecycleOwner) {
            requireView().showShortSnackBar("회원 등록에 실패했습니다. 다시 시도해주세요.")
        }
        viewModel.nameDuplicateEvent.observe(viewLifecycleOwner) {
            binding.tilSignUpId.error = "이미 등록된 아이디입니다."
        }
        viewModel.completeSignUpLiveData.observe(viewLifecycleOwner) {
            requireView().showShortSnackBar("회원 등록이 완료되었습니다.")
            findNavController().navigate(R.id.action_signUpFragment_pop)
        }
    }
}