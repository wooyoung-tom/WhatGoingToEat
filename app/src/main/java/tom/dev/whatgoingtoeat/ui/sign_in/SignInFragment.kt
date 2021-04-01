package tom.dev.whatgoingtoeat.ui.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentSignInBinding
import tom.dev.whatgoingtoeat.dto.user.UserSignInRequest
import tom.dev.whatgoingtoeat.ui.MainViewModel
import tom.dev.whatgoingtoeat.utils.LoadingDialog

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val viewModel: SignInViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        setSignUpButtonClickListener()
        setSignInButtonClickListener()

        observeLoading()
        observeSignInResult()
    }

    // Destroy 시에 _binding null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setSignUpButtonClickListener() {
        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun setSignInButtonClickListener() {
        binding.btnSignIn.setOnClickListener {
            val userName = binding.etSignInId.text.toString()
            val password = binding.etSignInPassword.text.toString()

            if (checkUserInfoValid(userName, password))
                viewModel.signIn(UserSignInRequest(userName, password))
        }
    }

    private fun checkUserInfoValid(userName: String, password: String): Boolean {
        if (userName.isBlank()) {
            binding.tilSignInPassword.error = "아이디를 입력해주세요."
            return false
        }

        if (password.isBlank()) {
            binding.tilSignInPassword.error = "비밀번호를 입력해주세요."
            return false
        }

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

    private fun observeSignInResult() {
        viewModel.successEvent.observe(viewLifecycleOwner) {
            activityViewModel.userInstance = it

            if (activityViewModel.userInstance != null) {
                findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
            } else {
                binding.tilSignInPassword.error = "다시 시도해주세요."
            }
        }

        viewModel.nameNotFoundEvent.observe(viewLifecycleOwner) {
            binding.tilSignInPassword.error = "해당 아이디가 존재하지 않습니다."
        }

        viewModel.passwordInvalidEvent.observe(viewLifecycleOwner) {
            binding.tilSignInPassword.error = "비밀번호가 일치하지 않습니다."
        }
    }
}