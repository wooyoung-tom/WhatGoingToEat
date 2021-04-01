package tom.dev.whatgoingtoeat.ui.restaurant

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.databinding.FragmentRestaurantBinding
import tom.dev.whatgoingtoeat.utils.LoadingDialog

@AndroidEntryPoint
class RestaurantFragment : Fragment() {

    private val viewModel: RestaurantViewModel by viewModels()

    private var _binding: FragmentRestaurantBinding? = null
    private val binding get() = _binding!!

    private val category by lazy { RestaurantFragmentArgs.fromBundle(requireArguments()).category }

    private lateinit var restaurantListAdapter: RestaurantListAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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

        observeRestaurantList()
        observeLoading()
    }

    // Destroy 시에 _binding null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun startTrackingLocation() {
        if (checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) showPermissionNotGrantedView()

        fusedLocationClient.lastLocation.addOnSuccessListener {
            viewModel.findRestaurants(category, it.latitude.toString(), it.longitude.toString())
        }
    }

    private fun showPermissionNotGrantedView() {
        binding.recyclerviewRestaurant.visibility = View.GONE
    }

    private fun goToSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${requireContext().packageName}")
        ).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { intent -> startActivity(intent) }
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
    }

    private fun setRestaurantCategoryTitle() {
        binding.tvRestaurantCategoryTitle.text = category
    }

    private fun observeRestaurantList() {
        viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {
            it?.let {
                restaurantListAdapter.submitList(it)
            }
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