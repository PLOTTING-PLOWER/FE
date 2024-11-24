package com.example.plotting_fe.plogging.ui

import android.Manifest
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.RetrofitImpl
import com.example.plotting_fe.plogging.presentation.PloggingController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.gson.Gson
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.clustering.Cluster
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.plotting.server.plogging.dto.response.PloggingMapResponse
import java.math.BigDecimal
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class PloggingMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var naverMap: NaverMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastCenterPosition: LatLng? = null
    private val DISTANCE_THRESHOLD = 1000.0  // 1km
    // 그리드 크기 설정
    private val GRID_SIZE = 0.03 // 약 3km (위도/경도 기준)

    // 권한 요청을 위한 ActivityResultLauncher 선언
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                // 권한이 승인되면 위치 추적 시작
                getLastKnownLocation()
            } else {
                // 권한이 거부되면 사용자에게 안내
                Toast.makeText(context, "위치 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_plogging_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // MapFragment 설정
        val fm = childFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)

        // 위치 서비스 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // 위치 추적 아이콘 클릭 이벤트
        val locationTrackingIcon: ImageView = view.findViewById(R.id.location_tracking_icon)
        locationTrackingIcon.setOnClickListener {
            getLastKnownLocation()
        }

    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        // 초기 카메라 위치 설정
        naverMap.moveCamera(
            CameraUpdate.toCameraPosition(
                CameraPosition(
                    LatLng(37.5263886632, 126.933612357),
                    14.0
                )
            )
        )

        // 지도 이동이나 줌 레벨이 변경될 때마다 좌표 가져오기
        naverMap.addOnCameraChangeListener { _, _ ->
            val currentCenter = naverMap.cameraPosition.target

            // 마지막 중심 좌표와 현재 중심 좌표 간 거리 계산
            if (lastCenterPosition == null || calculateDistance(
                    lastCenterPosition!!,
                    currentCenter
                ) > DISTANCE_THRESHOLD
            ) {
                lastCenterPosition = currentCenter  // 마지막 중심 위치 갱신

                // 중심 좌표와 줌 레벨 가져오기
                val latitude = currentCenter.latitude
                val longitude = currentCenter.longitude
                val zoom = naverMap.cameraPosition.zoom.toInt()

                // 중심 좌표와 줌 레벨을 서버에 요청
                sendBoundsToServer(latitude, longitude, zoom)
            }
        }
    }

    private fun sendBoundsToServer(lat1: Double, lon1: Double, zoom: Int) {


        // Retrofit 서비스 인스턴스
        val ploggingController: PloggingController by lazy {
            RetrofitImpl.retrofit.create(PloggingController::class.java)
        }

        ploggingController.getPloggingInBounds(lat1, lon1, zoom).enqueue(object : Callback<ResponseTemplate<List<PloggingMapResponse>>> {
            override fun onResponse(
                call: Call<ResponseTemplate<List<PloggingMapResponse>>>,
                response: Response<ResponseTemplate<List<PloggingMapResponse>>>
            ) {
                if (response.isSuccessful) {
                    // 서버로부터 받은 플로깅 데이터 처리
                    response.body()?.results?.let { ploggings ->
                        // 받은 데이터를 로그로 출력
                        Log.d(TAG, "Received ploggings: $ploggings")
                        updateMapWithPloggings(ploggings)
                    }
                } else {
                    Log.e(TAG, "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<List<PloggingMapResponse>>>, t: Throwable) {
                Log.e(TAG, "Network error: ", t)
                Toast.makeText(context, "Network error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // 그리드에 맞게 데이터를 클러스터링하는 함수
    private fun clusterPloggings(ploggings: List<PloggingMapResponse>): Map<String, List<PloggingMapResponse>> {
        val clusters = mutableMapOf<String, MutableList<PloggingMapResponse>>()

        ploggings.forEach { plogging ->
            val lat = plogging.startLatitude.toDouble()
            val lon = plogging.startLongitude.toDouble()

            // 그리드 좌표 계산
            val gridLat = (lat / GRID_SIZE).toInt() * GRID_SIZE
            val gridLon = (lon / GRID_SIZE).toInt() * GRID_SIZE

            val gridKey = "$gridLat,$gridLon" // 각 그리드의 좌표를 키로 사용

            // 해당 그리드에 포함되는 플로깅 데이터를 추가
            if (clusters.containsKey(gridKey)) {
                clusters[gridKey]?.add(plogging)
            } else {
                clusters[gridKey] = mutableListOf(plogging)
            }
        }

        return clusters
    }

    // 클러스터링된 데이터로 지도에 마커 추가
    private fun updateMapWithClusters(clusters: Map<String, List<PloggingMapResponse>>) {

        // 기존 마커 제거
        clearMarkers()

        clusters.forEach { (gridKey, ploggings) ->
            // 그리드의 중심 좌표 계산
            val avgLat = ploggings.map { it.startLatitude.toDouble() }.average()
            val avgLon = ploggings.map { it.startLongitude.toDouble() }.average()

            // 가장 먼저 온 데이터의 타이틀 가져오기
            val firstTitle = ploggings.firstOrNull()?.title ?: "Unknown"

            // 마커 위치 설정
            val clusterLatLng = LatLng(avgLat, avgLon)
            val marker = Marker().apply {
                position = clusterLatLng
                captionText = "${firstTitle} 포함 ${ploggings.size}개"
                icon = OverlayImage.fromResource(R.drawable.ic_location_not_click) // 클러스터 마커 이미지 설정
            }

            // 마커 클릭 이벤트 리스너 설정
            marker.setOnClickListener {

                // 클릭 시 해당 클러스터의 플로깅 데이터 목록을 JSON 문자열로 변환
                val ploggingsJson = Gson().toJson(ploggings)

                // JSON 문자열을 PloggingInfoMapFragment로 전달
                val fragment = PloggingInfoMapFragment.newInstance(ploggingsJson)

                // Fragment를 Dialog로 띄우기
                fragment.show(childFragmentManager, "PloggingInfoMapFragment")

                true // 클릭 이벤트 처리 완료
            }
            Log.d(TAG, "cluster")
            marker.map = naverMap
            markerList.add(marker)  // 추가된 마커를 리스트에 저장
        }
    }

    private var selectedMarker: Marker? = null // 현재 선택된 마커를 저장
    private val markerList = mutableListOf<Marker>()  // 마커들을 관리할 리스트

    private fun updateMapWithPloggings(ploggings: List<PloggingMapResponse>) {
        // 현재 줌 레벨 가져오기
        val zoomLevel = naverMap.cameraPosition.zoom

        // 기존 마커 제거
        clearMarkers()

        //  클러스터링을 적용
        if (zoomLevel <= 13) {

            // 클러스터링 로직을 호출
            val clusters = clusterPloggings(ploggings)
            updateMapWithClusters(clusters)
        } else {
            //  개별 마커로 표시
            ploggings.forEach { plogging ->
                val latLng = LatLng(plogging.startLatitude.toDouble(), plogging.startLongitude.toDouble())
                val marker = Marker().apply {
                    position = latLng
                    captionText = plogging.title
                    icon = OverlayImage.fromResource(R.drawable.ic_location_not_click) // 초기 마커 이미지 설정
                }

                // 마커 클릭 이벤트 리스너 설정
                marker.setOnClickListener {
                    // 이전에 선택된 마커가 있다면 원래 이미지로 복원
                    selectedMarker?.icon = OverlayImage.fromResource(R.drawable.ic_location_not_click)

                    // 현재 마커를 선택된 마커로 설정
                    selectedMarker = marker
                    marker.icon = OverlayImage.fromResource(R.drawable.ic_location_on_click)

                    // 데이터 전달
                    // 클릭 시 해당 클러스터의 플로깅 데이터 목록을 JSON 문자열로 변환
                    val ploggingsJson = Gson().toJson(ploggings)

                    // JSON 문자열을 PloggingInfoMapFragment로 전달
                    val fragment = PloggingInfoMapFragment.newInstance(ploggingsJson)

                    // Fragment를 Dialog로 띄우기
                    fragment.show(childFragmentManager, "PloggingInfoMapFragment")

                    true // 클릭 이벤트 처리 완료
                }
                Log.d(TAG, "invidisual")
                marker.map = naverMap
                markerList.add(marker)  // 추가된 마커를 리스트에 저장
            }
        }
    }


    // 두 좌표 간 거리 계산 (단위: 미터)
    private fun calculateDistance(start: LatLng, end: LatLng): Double {
        val earthRadius = 6371000.0  // 지구 반지름 (미터)
        val dLat = Math.toRadians(end.latitude - start.latitude)
        val dLon = Math.toRadians(end.longitude - start.longitude)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(start.latitude)) * cos(Math.toRadians(end.latitude)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c
    }

    private fun getLastKnownLocation() {
        // 위치 권한이 있는지 확인
        if (ActivityCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {

            // 권한이 있을 경우 위치 정보 가져오기
            try {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener(requireActivity(), object : OnSuccessListener<Location> {
                        override fun onSuccess(location: Location?) {
                            if (location != null) {
                                val currentLatLng = LatLng(location.latitude, location.longitude)
                                val cameraPosition = CameraPosition(currentLatLng, 16.0)
                                naverMap.moveCamera(CameraUpdate.toCameraPosition(cameraPosition))

                                val marker = Marker().apply {
                                    position = currentLatLng
                                    map = naverMap
                                }
                            } else {
                                Toast.makeText(context, "현재 위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
            } catch (e: SecurityException) {
                // 위치 권한이 없을 경우 예외 처리
                Toast.makeText(context, "위치 권한이 없습니다. 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // 권한이 없으면 권한 요청
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    fun resetSelectedMarker() {
        selectedMarker?.icon = OverlayImage.fromResource(R.drawable.ic_location_not_click)
        selectedMarker = null // 선택된 마커 초기화
    }

    // 기존 마커 제거 메서드
    private fun clearMarkers() {
        markerList.forEach { it.map = null } // 지도에서 마커 제거
        markerList.clear() // 리스트 초기화
    }


}





