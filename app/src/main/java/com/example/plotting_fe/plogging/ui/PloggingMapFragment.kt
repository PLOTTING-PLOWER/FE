package com.example.plotting_fe.plogging.ui

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.RetrofitImpl
import com.example.plotting_fe.plogging.presentation.PloggingController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.naver.maps.map.overlay.Marker
import com.plotting.server.plogging.dto.response.PloggingMapResponse

class PloggingMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var naverMap: NaverMap

    // Retrofit 서비스 인스턴스
    private val ploggingController: PloggingController by lazy {
        RetrofitImpl.retrofit.create(PloggingController::class.java)
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
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        // 초기 카메라 위치 설정
        naverMap.moveCamera(CameraUpdate.toCameraPosition(CameraPosition(LatLng(37.5665, 126.9780), 12.0)))

        // 지도 이동이나 줌 레벨이 변경될 때마다 좌표 가져오기
        naverMap.addOnCameraChangeListener { _, _ ->
            val bounds = naverMap.contentBounds

            // 좌측 상단과 우측 하단 좌표
            val lat1 = bounds.northWest.latitude
            val lon1 = bounds.northWest.longitude
            val lat2 = bounds.southEast.latitude
            val lon2 = bounds.southEast.longitude

            // 줌 레벨 가져오기
            val zoom = naverMap.cameraPosition.zoom.toInt()

            // 좌표와 줌 레벨을 서버에 요청하는 로직 추가
            sendBoundsToServer(lat1, lon1, lat2, lon2, zoom)
        }
    }

    private fun sendBoundsToServer(lat1: Double, lon1: Double, lat2: Double, lon2: Double, zoom: Int) {
        ploggingController.getPloggingInBounds(lat1, lon1, lat2, lon2, zoom).enqueue(object : Callback<ResponseTemplate<List<PloggingMapResponse>>> {
            override fun onResponse(
                call: Call<ResponseTemplate<List<PloggingMapResponse>>>,
                response: Response<ResponseTemplate<List<PloggingMapResponse>>>
            ) {
                if (response.isSuccessful) {
                    // 서버로부터 받은 플로깅 데이터 처리
                    response.body()?.results?.let { ploggings ->
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

    // 마커를 저장할 리스트
    private val markers = mutableListOf<Marker>()

    private fun updateMapWithPloggings(ploggings: List<PloggingMapResponse>) {
        // 기존 지도상의 모든 마커를 지우기
        clearAllMarkers()

        // 플로깅 데이터로 지도에 마커 추가
        ploggings.forEach { plogging ->
            val latLng = LatLng(plogging.startLatitude.toDouble(), plogging.startLongitude.toDouble())
            val marker = Marker().apply {
                position = latLng
                captionText = plogging.title
                // 추가로 마커의 다른 속성 설정 가능 (예: 색상, 아이콘 등)
            }
            marker.map = naverMap

            // 마커 리스트에 추가
            markers.add(marker)
        }
    }

    // 지도상의 모든 마커를 지우는 함수
    private fun clearAllMarkers() {
        // 마커 리스트를 순회하며 모든 마커 제거
        markers.forEach { marker ->
            marker.map = null
        }

        // 마커 리스트 초기화
        markers.clear()
    }

}


