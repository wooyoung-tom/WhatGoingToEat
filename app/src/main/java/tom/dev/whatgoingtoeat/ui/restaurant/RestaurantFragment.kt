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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import tom.dev.whatgoingtoeat.utils.LoadingDialog
import tom.dev.whatgoingtoeat.utils.hide

@AndroidEntryPoint
class RestaurantFragment : Fragment() {

    private val viewModel: RestaurantViewModel by viewModels()

    private var _binding: FragmentRestaurantBinding? = null
    private val binding get() = _binding!!

    private val category by lazy { RestaurantFragmentArgs.fromBundle(requireArguments()).category }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private lateinit var restaurantListAdapter: RestaurantListAdapter

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRestaurantListAdapterInit()

        setRestaurantCategoryTitle()
        setFilter()

        setBasketButtonClickListener()

        observeLoading()
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
        }
        binding.recyclerviewRestaurant.apply {
            adapter = restaurantListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
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

    private fun setFilter() {
        binding.chipgroupRestaurantFilters.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chip_restaurant_favorite -> {

                }
                R.id.chip_restaurant_distance -> {

                }
                R.id.chip_restaurant_review -> {

                }
                R.id.chip_restaurant_asc -> {

                }
                else -> {

                }
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
}