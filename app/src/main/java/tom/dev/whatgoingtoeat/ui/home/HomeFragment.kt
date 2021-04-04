package tom.dev.whatgoingtoeat.ui.home

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
import tom.dev.whatgoingtoeat.databinding.FragmentHomeBinding
import tom.dev.whatgoingtoeat.ui.MainViewModel
import tom.dev.whatgoingtoeat.utils.LoadingDialog

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val user by lazy { activityViewModel.userInstance }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUserNameTextView()

        setKoreanButtonClickListener()
        setChineseButtonClickListener()
        setWesternButtonClickListener()
        setJapaneseButtonClickListener()

        setBasketButtonClickListener()

        observeLoading()
    }

    // Destroy 시에 _binding null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUserNameTextView() {
        binding.tvHomeName.text = user?.userName ?: "센디"
    }

    private fun setKoreanButtonClickListener() {
        binding.btnHomeMenuKorean.setOnClickListener {
            val category = binding.tvHomeMenuKorean.text.toString()
            val action = HomeFragmentDirections.actionHomeFragmentToRestaurantFragment(category)
            findNavController().navigate(action)
        }
    }

    private fun setWesternButtonClickListener() {
        binding.btnHomeMenuWestern.setOnClickListener {
            val category = binding.tvHomeMenuWestern.text.toString()
            val action = HomeFragmentDirections.actionHomeFragmentToRestaurantFragment(category)
            findNavController().navigate(action)
        }
    }

    private fun setChineseButtonClickListener() {
        binding.btnHomeMenuChinese.setOnClickListener {
            val category = binding.tvHomeMenuChinese.text.toString()
            val action = HomeFragmentDirections.actionHomeFragmentToRestaurantFragment(category)
            findNavController().navigate(action)
        }
    }

    private fun setJapaneseButtonClickListener() {
        binding.btnHomeMenuJapanese.setOnClickListener {
            val category = binding.tvHomeMenuJapanese.text.toString()
            val action = HomeFragmentDirections.actionHomeFragmentToRestaurantFragment(category)
            findNavController().navigate(action)
        }
    }

    private fun setBasketButtonClickListener() {
        binding.btnHomeBasket.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_basketFragment)
        }
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
}