package tom.dev.whatgoingtoeat.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.InfoWindow
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
        }
    }

    private fun setMapLocationInit() {
        naverMap.locationSource = locationSource
        // 위치 모드 수정
        naverMap.locationTrackingMode = LocationTrackingMode.NoFollow
        // 위치 변경 이벤트 등록
        naverMap.addOnLocationChangeListener(locationChangeListener)
    }

    private val locationChangeListener = NaverMap.OnLocationChangeListener { location ->
        // 내 현재 위치로 카메라를 옮긴다.
        val cameraUpdate = CameraUpdate.scrollTo(LatLng(location.latitude, location.longitude))
        naverMap.moveCamera(cameraUpdate)
    }

    private fun setMapSearchResultMarkerInit() {
        // 식당의 위치에 마커 표시
        val marker = Marker().apply {
            position = targetLatLng
            icon = MarkerIcons.RED
            map = naverMap
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}