package tom.dev.whatgoingtoeat.ui.map

import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentMapBinding

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val searchResult by lazy { MapFragmentArgs.fromBundle(requireArguments()).info }
    private val targetLatLng by lazy { LatLng(searchResult.lat.toDouble(), searchResult.lng.toDouble()) }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

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
    }

    @UiThread
    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        setMapUIInit()
        setMapLocationInit()
        setMapSearchResultMarkerInit()
        setMapSearchResultBottomView()

        setPhoneButtonClickListener()
        setDetailButtonClickListener()
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
        val cameraUpdate = CameraUpdate.scrollTo(targetLatLng)
        naverMap.moveCamera(cameraUpdate)
    }

    private fun setMapSearchResultMarkerInit() {
        // 식당의 위치에 마커 표시
        Marker().apply {
            position = targetLatLng
            icon = MarkerIcons.RED
            map = naverMap
        }
    }

    private fun setMapSearchResultBottomView() {
        binding.tvBottomSheetTitle.text = searchResult.placeName
        binding.tvBottomSheetCategory.text = searchResult.categoryName
        binding.tvBottomSheetDistance.text = getDistanceText()
        binding.tvBottomSheetDoro.text = searchResult.roadAddressName
        binding.tvBottomSheetJibun.text = searchResult.addressName

        if (searchResult.phone.isBlank()) {
            binding.buttonBottomSheetPhone.isEnabled = false
            binding.buttonBottomSheetPhone.icon = null
            binding.buttonBottomSheetPhone.text = "등록된 번호가 없습니다."
        } else {
            binding.buttonBottomSheetPhone.text = searchResult.phone
        }
    }

    private fun getDistanceText() = "${searchResult.distance}m"

    private fun setPhoneButtonClickListener() {
        val phoneStrArr = searchResult.phone.split("-")
        val phoneStr = phoneStrArr[0] + phoneStrArr[1] + phoneStrArr[2]
        val phoneUri = "tel:$phoneStr"

        binding.buttonBottomSheetPhone.setOnClickListener {
            val intent = Intent(ACTION_DIAL, Uri.parse(phoneUri))
            startActivity(intent)
        }
    }

    private fun setDetailButtonClickListener() {
        binding.buttonBottomSheetDetail.setOnClickListener {
            val action = MapFragmentDirections.actionMapFragmentToWebViewFragment(searchResult.placeUrl)
            findNavController().navigate(action)
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}