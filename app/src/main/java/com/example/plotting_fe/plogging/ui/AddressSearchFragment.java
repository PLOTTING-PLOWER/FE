package com.example.plotting_fe.plogging.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.plotting_fe.BuildConfig;
import com.example.plotting_fe.R;
import com.example.plotting_fe.global.NCPApiService;
import com.example.plotting_fe.global.dto.response.GeocodeResponse;
import com.example.plotting_fe.global.util.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressSearchFragment extends Fragment {

    private EditText editQuery;
    private Button btnSearch, btnCancel;
    private OnAddressSelectedListener listener;

    // 인터페이스를 통해 Activity와 통신
    public interface OnAddressSelectedListener {
        void onAddressSelected(String address);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.a_fragment_location_find, container, false);

        editQuery = rootView.findViewById(R.id.edit_query);
        btnSearch = rootView.findViewById(R.id.btn_search);
        btnCancel = rootView.findViewById(R.id.btn_cancel);

        // 검색 버튼 클릭 시
        btnSearch.setOnClickListener(v -> {
            String query = editQuery.getText().toString();
            if (!query.isEmpty()) {
                // 주소 검색 시작
                searchAddress(query);
            } else {
                Toast.makeText(getContext(), "주소를 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        // 취소 버튼 클릭 시
        btnCancel.setOnClickListener(v -> {
            if (getActivity() != null) {
                // 프래그먼트를 백 스택에서 제거하고 이전 화면으로 돌아감
                getActivity().getSupportFragmentManager().beginTransaction().remove(AddressSearchFragment.this).commit();
            }
        });

        return rootView;
    }

    private void searchAddress(String query) {
        // 네이버 주소 검색 API 호출
        NCPApiService apiService = ApiClient.INSTANCE.getNCPApiService();
        Call<GeocodeResponse> call = apiService.getGeocode(BuildConfig.NAVER_CLIENT_ID, BuildConfig.NAVER_CLIENT_SECRET, query);

        call.enqueue(new Callback<GeocodeResponse>() {
            @Override
            public void onResponse(Call<GeocodeResponse> call, Response<GeocodeResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    List<GeocodeResponse.Addresses> addresses = response.body().getAddresses();
                    if (!addresses.isEmpty()) {
                        // 주소 전달
                        String roadAddress = addresses.get(0).getRoadAddress();
                        if (listener != null) {
                            listener.onAddressSelected(roadAddress);
                        }
                        // 프래그먼트를 백 스택에서 제거하고 이전 화면으로 돌아감
                        getActivity().getSupportFragmentManager().beginTransaction().remove(AddressSearchFragment.this).commit();
                    } else {
                        Toast.makeText(getContext(), "주소를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GeocodeResponse> call, Throwable t) {
                // 요청 후 로딩 표시 종료
                // hideLoadingIndicator();

                Log.e("AddressSearch", "Failure: " + t.getMessage());
                Toast.makeText(getContext(), "서버 요청에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddressSelectedListener) {
            listener = (OnAddressSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnAddressSelectedListener");
        }
    }
}
