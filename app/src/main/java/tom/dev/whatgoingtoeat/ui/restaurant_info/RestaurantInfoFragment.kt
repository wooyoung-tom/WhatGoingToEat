package tom.dev.whatgoingtoeat.ui.restaurant_info

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentRestaurantInfoBinding
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantMenu
import tom.dev.whatgoingtoeat.ui.MainViewModel
import tom.dev.whatgoingtoeat.utils.LoadingDialog
import tom.dev.whatgoingtoeat.utils.showShortSnackBar

@AndroidEntryPoint
class RestaurantInfoFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: RestaurantInfoViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentRestaurantInfoBinding? = null
    private val binding get() = _binding!!

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private lateinit var restaurantMenuListAdapter: RestaurantMenuListAdapter

    private val restaurant by lazy { RestaurantInfoFragmentArgs.fromBundle(requireArguments()).restaurant }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRestaurantInfoBinding.inflate(inflater, container, false)

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentManager = childFragmentManager
        val mapFragment = fragmentManager.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fragmentManager.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync(this)

        viewModel.checkFavorite(activityViewModel.userInstance?.id, restaurant.restaurantId)

        setRestaurantInfo()
        setRestaurantMenuAdapter()
        setSelectMenuButtonClickListener()

        observeOrderSave()
        observeFavorite()
        observeSaveFavoriteSuccess()
        observeDeleteFavoriteSuccess()
        observeLoading()
    }

    // Destroy 시에 _binding null
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setRestaurantInfo() {
        binding.tvRestaurantInfoName.text = restaurant.restaurantName
        binding.tvRestaurantInfoCategory.text = restaurant.category
        binding.tvRestaurantInfoAddress.text =
            if (!restaurant.roadAddress.isNullOrBlank()) restaurant.roadAddress
            else restaurant.jibunAddress
    }

    private fun setRestaurantMenuAdapter() {
        restaurantMenuListAdapter = RestaurantMenuListAdapter(
            object : RestaurantMenuListAdapter.SelectedItemControlListeners {
                override fun onItemRemoved(item: RestaurantMenu) {
                    viewModel.removeMenu(item)
                }

                override fun onItemSelected(item: RestaurantMenu) {
                    viewModel.selectMenu(item)
                }
            }
        )

        binding.recyclerviewRestaurantMenu.apply {
            adapter = restaurantMenuListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        restaurantMenuListAdapter.submitList(restaurant.menuList)
    }

    private fun setSelectMenuButtonClickListener() {
        binding.btnRestaurantInfoMenuSelect.setOnClickListener {
            if (viewModel.selectedMenuList.isEmpty()) {
                requireView().showShortSnackBar("메뉴를 선택해주세요.")
            } else {
                viewModel.saveOrder(activityViewModel.userInstance?.id, restaurant.restaurantId)
            }
        }
    }

    private fun observeOrderSave() {
        viewModel.orderSaveCompleteEvent.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_restaurantInfoFragment_to_basketFragment)
        }

        viewModel.orderSaveFailedLiveData.observe(viewLifecycleOwner) {
            requireView().showShortSnackBar(it)
        }

        viewModel.orderDuplicatedLiveData.observe(viewLifecycleOwner) {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("주문 확인")
                setMessage("이미 대기중인 주문이 있습니다.\n장바구니로 가시겠습니까?")
                setPositiveButton("확인") { dialog, _ ->
                    findNavController().navigate(R.id.action_restaurantInfoFragment_to_basketFragment)
                    dialog.dismiss()
                }
                setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
            }.show()
        }
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        setMapUIInit()
        setMapLocationInit()
        setMapSearchResultMarkerInit()
    }

    private fun setMapUIInit() {
        // 전부 다 없애자
        naverMap.uiSettings.apply {
            isLocationButtonEnabled = false
            isZoomGesturesEnabled = false
            isZoomControlEnabled = false
            isCompassEnabled = false
            isLogoClickEnabled = false
            isScaleBarEnabled = false

            // 스크롤 제스쳐 -> 맵 이동 block
            isScrollGesturesEnabled = false
        }
    }

    private fun setMapLocationInit() {
        naverMap.locationSource = locationSource
        // 위치 모드 수정
        naverMap.locationTrackingMode = LocationTrackingMode.NoFollow

        // 카메라 위치 수정
        val cameraUpdate = CameraUpdate.scrollTo(getTargetLatLng())
        naverMap.moveCamera(cameraUpdate)
    }

    private fun setMapSearchResultMarkerInit() {
        // 식당의 위치에 마커 표시
        Marker().apply {
            position = getTargetLatLng()
            icon = MarkerIcons.RED
            map = naverMap
        }
    }

    private fun getTargetLatLng() = LatLng(restaurant.latitude.toDouble(), restaurant.longitude.toDouble())

    private fun observeFavorite() {
        viewModel.isMyFavoriteLiveData.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding.tvRestaurantInfoFavorite.text = "즐겨찾기 삭제"
                    binding.btnRestaurantInfoFavorite.setOnClickListener {
                        viewModel.deleteFavorite(activityViewModel.userInstance?.id, restaurant.restaurantId)
                    }
                }
                false -> {
                    binding.tvRestaurantInfoFavorite.text = "즐겨찾기 추가"
                    binding.btnRestaurantInfoFavorite.setOnClickListener {
                        viewModel.saveFavorite(activityViewModel.userInstance?.id, restaurant.restaurantId)
                    }
                }
            }
        }
    }

    private fun observeSaveFavoriteSuccess() {
        viewModel.saveSuccess.observe(viewLifecycleOwner) {
            binding.tvRestaurantInfoFavorite.text = "즐겨찾기 삭제"
            binding.btnRestaurantInfoFavorite.setOnClickListener {
                viewModel.deleteFavorite(activityViewModel.userInstance?.id, restaurant.restaurantId)
            }
        }
    }

    private fun observeDeleteFavoriteSuccess() {
        viewModel.deleteSuccess.observe(viewLifecycleOwner) {
            binding.tvRestaurantInfoFavorite.text = "즐겨찾기 추가"
            binding.btnRestaurantInfoFavorite.setOnClickListener {
                viewModel.saveFavorite(activityViewModel.userInstance?.id, restaurant.restaurantId)
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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}