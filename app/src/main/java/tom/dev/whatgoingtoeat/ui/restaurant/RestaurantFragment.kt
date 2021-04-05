package tom.dev.whatgoingtoeat.ui.restaurant

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentRestaurantBinding
import tom.dev.whatgoingtoeat.ui.MainViewModel
import tom.dev.whatgoingtoeat.utils.LoadingDialog
import tom.dev.whatgoingtoeat.utils.hide
import tom.dev.whatgoingtoeat.utils.showShortSnackBar

@AndroidEntryPoint
class RestaurantFragment : Fragment() {

    private val viewModel: RestaurantViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentRestaurantBinding? = null
    private val binding get() = _binding!!

    private val category by lazy { RestaurantFragmentArgs.fromBundle(requireArguments()).category }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private lateinit var restaurantListAdapter: RestaurantListAdapter

    private lateinit var loading: LoadingDialog

    private var searchRestaurantJob: Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRestaurantBinding.inflate(inflater, container, false)

        // FusedLocationClient 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                startTrackingLocation()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                showPermissionNotGrantedView()
            }
        }

        TedPermission.with(requireContext())
            .setPermissionListener(permissionListener)
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .check()

        loading = LoadingDialog(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRestaurantListAdapterInit()

        setRestaurantCategoryTitle()
        setFilter()

        setBasketButtonClickListener()
    }

    // Destroy 시에 _binding null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        searchRestaurantJob?.cancel()
    }

    private fun setBasketButtonClickListener() {
        binding.btnRestaurantBasket.setOnClickListener {
            findNavController().navigate(R.id.action_restaurantFragment_to_basketFragment)
        }
    }

    private fun setRestaurantListAdapterInit() {
        restaurantListAdapter = RestaurantListAdapter { restaurantItem ->
            val action = RestaurantFragmentDirections
                .actionRestaurantFragmentToRestaurantInfoFragment(restaurantItem)
            findNavController().navigate(action)
        }.apply {
            addLoadStateListener { loadState ->
                when (loadState.source.refresh) {
                    is LoadState.NotLoading -> loading.dismiss()
                    is LoadState.Loading -> loading.show()
                    is LoadState.Error -> loading.dismiss()
                }

                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error

                errorState?.let { requireView().showShortSnackBar("${it.error}") }
            }
        }

        binding.recyclerviewRestaurant.apply {
            adapter = restaurantListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setRestaurantCategoryTitle() {
        binding.tvRestaurantCategoryTitle.text = category
    }

    private fun setFilter() {
        binding.chipgroupRestaurantFilters.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_restaurant_favorite -> searchFavoriteRestaurant()
                R.id.chip_restaurant_distance -> {

                }
                R.id.chip_restaurant_review -> {

                }
                R.id.chip_restaurant_asc -> searchRestaurantsByLiteralAsc()
                else -> searchRestaurant()
            }
        }
    }

    private fun startTrackingLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) showPermissionNotGrantedView()

        fusedLocationClient.lastLocation.addOnSuccessListener {
            lastLocation = it

            searchRestaurant()
        }
    }

    private fun showPermissionNotGrantedView() {
        binding.recyclerviewRestaurant.hide()
    }

    private fun searchRestaurant() {
        val lat = lastLocation.latitude
        val lng = lastLocation.longitude

        searchRestaurantJob?.cancel()
        searchRestaurantJob = lifecycleScope.launch {
            viewModel.searchRestaurant(category, lat, lng).collectLatest {
                restaurantListAdapter.submitData(it)
            }
        }
    }

    private fun searchFavoriteRestaurant() {
        val lat = lastLocation.latitude
        val lng = lastLocation.longitude

        searchRestaurantJob?.cancel()
        searchRestaurantJob = lifecycleScope.launch {
            val userId = activityViewModel.userInstance?.userId ?: return@launch

            viewModel.searchRestaurant(userId = userId, category = category, lat = lat, lng = lng, favorite = true)
                .collectLatest {
                    restaurantListAdapter.submitData(it)
                }
        }
    }

    private fun searchRestaurantsByLiteralAsc() {
        val lat = lastLocation.latitude
        val lng = lastLocation.longitude

        searchRestaurantJob?.cancel()
        searchRestaurantJob = lifecycleScope.launch {
            viewModel.searchRestaurant(category = category, lat = lat, lng = lng, literal = true)
                .collectLatest {
                    restaurantListAdapter.submitData(it)
                }
        }
    }
}