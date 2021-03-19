package tom.dev.whatgoingtoeat.ui.sign_in

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentSignInBinding
import tom.dev.whatgoingtoeat.utils.disable
import tom.dev.whatgoingtoeat.utils.enable

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private val viewModel: SignInViewModel by viewModels()

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        setNameTextWatcher()
        setSignInButtonClickListener()
        setSignUpButtonClickListener()

        observeSignInResult()
    }

    // Destroy 시에 _binding null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    // Text Watcher Register
    private fun setNameTextWatcher() {
        binding.etSignInName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val length = s?.length ?: 0

                if (length == 0 || length > 10) binding.btnSignIn.disable()
                else binding.btnSignIn.enable()
            }
        })
    }

    // Sign In Button 눌렀을 때
    private fun setSignInButtonClickListener() {
        binding.btnSignIn.setOnClickListener {
            val name = getNameEditTextString()

            viewModel.signIn(name)
        }
    }

    // Sign Up Button 누르면 이동
    private fun setSignUpButtonClickListener() {
        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    // Name EditText String 가져오는 함수
    private fun getNameEditTextString(): String = binding.etSignInName.text.toString()

    // Sign In Observing
    private fun observeSignInResult() {
        // 성공 시
        viewModel.successToSignIn.observe(viewLifecycleOwner) {
            // 다음 화면으로 이동
        }

        // 실패 시
        viewModel.failedToSignIn.observe(viewLifecycleOwner) {
            binding.etSignInName.error = "Cannot found user, please sign up."
            binding.btnSignIn.disable()
        }
    }
}