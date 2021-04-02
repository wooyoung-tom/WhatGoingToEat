package tom.dev.whatgoingtoeat.ui.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentRestaurantBinding
import tom.dev.whatgoingtoeat.utils.LoadingDialog

@AndroidEntryPoint
class RestaurantFragment : Fragment() {

    private val viewModel: RestaurantViewModel by viewModels()

    private var _binding: FragmentRestaurantBinding? = null
    private val binding get() = _binding!!

    private val category by lazy { RestaurantFragmentArgs.fromBundle(requireArguments()).category }
    private val restaurantList by lazy { RestaurantFragmentArgs.fromBundle(requireArguments()).restaurantList }

    private lateinit var restaurantListAdapter: RestaurantListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRestaurantBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRestaurantListAdapterInit()

        setRestaurantCategoryTitle()

        setBasketButtonClickListener()

        observeLoading()
    }

    // Destroy 시에 _binding null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setBasketButtonClickListener() {
        binding.btnRestaurantBasket.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantFragment_to_basketFragment)
        }
    }

    private fun setRestaurantListAdapterInit() {
        restaurantListAdapter = RestaurantListAdapter { restaurantItem ->
            val action = RestaurantFragmentDirections
                .actionRestaurantFragmentToRestaurantInfoFragment(restaurantItem.restaurant)
            findNavController().navigate(action)
        }
        binding.recyclerviewRestaurant.apply {
            adapter = restaurantListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        restaurantListAdapter.submitList(restaurantList.list)
    }

    private fun setRestaurantCategoryTitle() {
        binding.tvRestaurantCategoryTitle.text = category
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