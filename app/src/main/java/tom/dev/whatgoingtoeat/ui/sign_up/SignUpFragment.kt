package tom.dev.whatgoingtoeat.ui.sign_up

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentSignUpBinding
import tom.dev.whatgoingtoeat.utils.disable
import tom.dev.whatgoingtoeat.utils.enable

@AndroidEntryPoint
class SignUpFragment : Fragment() {

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


        }
    }

    private fun getName() = binding.etSignUpName.text.toString()

    private fun getTeamName() = binding.autoSignUpTeamName.text.toString()
}