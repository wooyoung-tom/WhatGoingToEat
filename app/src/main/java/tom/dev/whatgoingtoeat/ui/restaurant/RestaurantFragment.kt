package tom.dev.whatgoingtoeat.ui.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentRestaurantBinding
import tom.dev.whatgoingtoeat.ui.MainViewModel
import tom.dev.whatgoingtoeat.utils.LoadingDialog

@AndroidEntryPoint
class RestaurantFragment : Fragment() {

    private val viewModel: RestaurantViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentRestaurantBinding? = null
    private val binding get() = _binding!!

    private val category by lazy { RestaurantFragmentArgs.fromBundle(requireArguments()).category }
    private val restaurantList by lazy { RestaurantFragmentArgs.fromBundle(requireArguments()).restaurantList }
    private val latitude by lazy { RestaurantFragmentArgs.fromBundle(requireArguments()).latitude }
    private val longitude by lazy { RestaurantFragmentArgs.fromBundle(requireArguments()).longitude }

    private lateinit var restaurantListAdapter: RestaurantListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRestaurantBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRestaurantListAdapterInit()

        setRestaurantCategoryTitle()
        setFilter()

        setBasketButtonClickListener()

        observeLoading()
        observeFavoriteList()
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

        restaurantListAdapter.update(restaurantList.list)
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

    private fun observeFavoriteList() {
        viewModel.favoriteListLiveData.observe(viewLifecycleOwner) {
            it?.let {
                restaurantListAdapter.update(it)
            }
        }
    }

    private fun setFilter() {
        binding.chipgroupRestaurantFilters.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_restaurant_favorite -> {
                    viewModel.findFavoriteRestaurants(activityViewModel.userInstance?.id, category, latitude, longitude)
                }
                R.id.chip_restaurant_distance -> {
                    val newList = restaurantList.list.sortedBy { it.distance }
                    restaurantListAdapter.update(newList)
                }
                R.id.chip_restaurant_review -> {
                    val newList = restaurantList.list.sortedByDescending { it.review }
                    restaurantListAdapter.update(newList)
                }
                R.id.chip_restaurant_asc -> {
                    val newList = restaurantList.list.sortedBy { it.restaurant.restaurantName }
                    restaurantListAdapter.update(newList)
                }
                else -> {
                    restaurantListAdapter.update(restaurantList.list)
                }
            }
        }
    }
}