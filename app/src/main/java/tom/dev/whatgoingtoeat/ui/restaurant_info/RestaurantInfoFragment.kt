package tom.dev.whatgoingtoeat.ui.restaurant_info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentRestaurantInfoBinding

@AndroidEntryPoint
class RestaurantInfoFragment : Fragment(), OnMapReadyCallback {

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

        setRestaurantInfo()
        setRestaurantMenuAdapter()
    }

    private fun setRestaurantInfo() {
        binding.tvRestaurantInfoName.text = restaurant.restaurantName
        binding.tvRestaurantInfoCategory.text = restaurant.category
        binding.tvRestaurantInfoAddress.text =
            if (!restaurant.roadAddress.isNullOrBlank()) restaurant.roadAddress
            else restaurant.jibunAddress
    }

    private fun setRestaurantMenuAdapter() {
        restaurantMenuListAdapter = RestaurantMenuListAdapter()
        binding.recyclerviewRestaurantMenu.apply {
            adapter = restaurantMenuListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        restaurantMenuListAdapter.submitList(restaurant.menuList)
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

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}