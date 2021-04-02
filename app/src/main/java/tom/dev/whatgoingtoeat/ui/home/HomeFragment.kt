package tom.dev.whatgoingtoeat.ui.home

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
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentHomeBinding
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantList
import tom.dev.whatgoingtoeat.ui.MainViewModel
import tom.dev.whatgoingtoeat.ui.restaurant.RestaurantListAdapter
import tom.dev.whatgoingtoeat.utils.LoadingDialog

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val user by lazy { activityViewModel.userInstance }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

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

        setUserNameTextView()

        setKoreanButtonClickListener()
        setChineseButtonClickListener()
        setWesternButtonClickListener()
        setJapaneseButtonClickListener()

        setBasketButtonClickListener()

        observeRestaurantList()
        observeLoading()
    }

    // Destroy 시에 _binding null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun startTrackingLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) showPermissionNotGrantedView()

        fusedLocationClient.lastLocation.addOnSuccessListener {
            lastLocation = it
        }
    }

    private fun showPermissionNotGrantedView() {
        binding.btnHomeMenuKorean.isEnabled = false
        binding.btnHomeMenuJapanese.isEnabled = false
        binding.btnHomeMenuChinese.isEnabled = false
        binding.btnHomeMenuWestern.isEnabled = false
    }

    private fun setUserNameTextView() {
        binding.tvHomeName.text = user?.name ?: "센디"
    }

    private fun setKoreanButtonClickListener() {
        binding.btnHomeMenuKorean.setOnClickListener {
            val category = binding.tvHomeMenuKorean.text.toString()
            viewModel.findRestaurants(category, lastLocation.latitude.toString(), lastLocation.longitude.toString())
        }
    }

    private fun setWesternButtonClickListener() {
        binding.btnHomeMenuWestern.setOnClickListener {
            val category = binding.tvHomeMenuWestern.text.toString()
            viewModel.findRestaurants(category, lastLocation.latitude.toString(), lastLocation.longitude.toString())
        }
    }

    private fun setChineseButtonClickListener() {
        binding.btnHomeMenuChinese.setOnClickListener {
            val category = binding.tvHomeMenuChinese.text.toString()
            viewModel.findRestaurants(category, lastLocation.latitude.toString(), lastLocation.longitude.toString())
        }
    }

    private fun setJapaneseButtonClickListener() {
        binding.btnHomeMenuJapanese.setOnClickListener {
            val category = binding.tvHomeMenuJapanese.text.toString()
            viewModel.findRestaurants(category, lastLocation.latitude.toString(), lastLocation.longitude.toString())
        }
    }

    private fun setBasketButtonClickListener() {
        binding.btnHomeBasket.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_basketFragment)
        }
    }

    private fun observeRestaurantList() {
        viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {
            val parceledList = RestaurantList(it.body)
            val action = HomeFragmentDirections.actionHomeFragmentToRestaurantFragment(
                it.category,
                parceledList,
                lastLocation.latitude.toString(),
                lastLocation.longitude.toString()
            )
            findNavController().navigate(action)
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