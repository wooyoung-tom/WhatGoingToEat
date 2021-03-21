package tom.dev.whatgoingtoeat.ui.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentSignUpBinding
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

        setTeamNameDropDownMenu()
        setSignUpButtonClickListener()

        // Observer Register
        setNameDuplicateEventObserver()
        setNameLengthInvalidEventObserver()
        setTeamNameNotSelectedEventObserver()
        setCompleteSignUpEventObserver()
    }

    // Destroy 시에 _binding null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    // 팀 이름 입력 칸에서 DropDown Menu 지정
    private fun setTeamNameDropDownMenu() {
        val items = listOf("Product")
        val adapter = ArrayAdapter(requireContext(), R.layout.item_team_name, items)
        binding.autoSignUpTeamName.setAdapter(adapter)
    }

    private fun setSignUpButtonClickListener() {
        binding.btnSignUp.setOnClickListener {
            val name = getName()
            val teamName = getTeamName()

            viewModel.signUp(name, teamName)
        }
    }

    private fun getName() = binding.etSignUpName.text.toString()

    private fun getTeamName() = binding.autoSignUpTeamName.text.toString()

    private fun setNameDuplicateEventObserver() {
        viewModel.nameDuplicationEvent.observe(viewLifecycleOwner) {
            binding.tilSignUpName.error = "중복된 이름이 존재합니다. 다른 이름으로 시도해주세요."
        }
    }

    private fun setNameLengthInvalidEventObserver() {
        viewModel.nameLengthInvalidEvent.observe(viewLifecycleOwner) {
            binding.tilSignUpName.error = "글자수가 10자를 초과하였습니다."
        }
    }

    private fun setTeamNameNotSelectedEventObserver() {
        viewModel.teamNameNotSelectedEvent.observe(viewLifecycleOwner) {
            binding.tilSignUpTeamName.error = "팀 이름을 선택하지 않았습니다."
        }
    }

    private fun setCompleteSignUpEventObserver() {
        viewModel.completeSignUpEvent.observe(viewLifecycleOwner) {
            view?.showShortSnackBar("가입이 완료되었습니다.")
            findNavController().navigate(R.id.action_signUpFragment_pop)
        }
    }
}