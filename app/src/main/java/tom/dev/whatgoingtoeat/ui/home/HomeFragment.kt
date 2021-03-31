package tom.dev.whatgoingtoeat.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.databinding.FragmentHomeBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setKoreanButtonClickListener()
        setChineseButtonClickListener()
        setWesternButtonClickListener()
        setJapaneseButtonClickListener()
        
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
}