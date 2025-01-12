package com.example.plotting_fe.user.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.plotting_fe.BuildConfig
import com.example.plotting_fe.MainActivity
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.application.TokenApplication
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.user.dto.request.LoginRequest
import com.example.plotting_fe.user.dto.response.LoginResponse
import com.example.plotting_fe.user.presentation.AuthController
import com.example.plotting_fe.global.util.ClickUtil
import com.example.plotting_fe.user.dto.request.AccessTokenRequest
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthIntent
import com.navercorp.nid.oauth.NidOAuthLogin
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val authController: AuthController by lazy {
        ApiClient.getApiClient().create(AuthController::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById<View>(R.id.login)) { v, insets ->
            val systemBars: Insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 네이버 로그인 초기화
        NaverIdLoginSDK.initialize(
            context = this,
            clientId = BuildConfig.NAVER_CLIENT_ID,
            clientSecret = BuildConfig.NAVER_CLIENT_SECRET,
            clientName = getString(R.string.naver_client_name)
        )

        val emailInput = findViewById<EditText>(R.id.et_email)
        val passwordInput = findViewById<EditText>(R.id.et_pw)
        val loginBtn = findViewById<Button>(R.id.btn_login)
        val naverLoginBtn = findViewById<ImageView>(R.id.iv_naver)

        // 일반 로그인
        loginBtn.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            handleLogin(email, password)
        }

        // 네이버 로그인
        naverLoginBtn.setOnClickListener {
            handleNaverLogin()
        }

        // 로그인 버튼에 클릭 리스너 설정
        findViewById<View>(R.id.tv_gojoin).setOnClickListener {
            ClickUtil.onJoinClick(this)
        }
    }

    // 일반 로그인
    private fun handleLogin(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val request = LoginRequest(email, password)
        authController.login(request).enqueue(object : Callback<ResponseTemplate<LoginResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<LoginResponse>>,
                response: Response<ResponseTemplate<LoginResponse>>
            ) {
                if (response.isSuccessful) {
                    val responseTemplate = response.body()
                    checkResponse(responseTemplate)
                } else {
                    Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ResponseTemplate<LoginResponse>>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    // ActivityResultLauncher 등록
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        when (result.resultCode) {
            RESULT_OK -> {
                val accessToken = NaverIdLoginSDK.getAccessToken()
                Log.d("launcher", "accessToken: " + accessToken)

                if (!accessToken.isNullOrEmpty()) {
                    sendTokenToServer(accessToken)      // 서버에 accessToken 전송
                } else {
                    Toast.makeText(this, "액세스 토큰을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            RESULT_CANCELED -> {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(
                    this,
                    "로그인 실패: errorCode:$errorCode, errorDesc:$errorDescription",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleNaverLogin() {
        // 네이버 로그인 인증 호출 (ActivityResultLauncher 사용)
        NaverIdLoginSDK.authenticate(this, launcher)
    }

    private fun sendTokenToServer(accessToken: String) {
        val request = AccessTokenRequest(accessToken)
        authController.loginWithNaver(request).enqueue(object : Callback<ResponseTemplate<LoginResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<LoginResponse>>,
                response: Response<ResponseTemplate<LoginResponse>>
            ) {
                if (response.isSuccessful) {
                    Log.d("get", "onResponse 성공: " + response.body().toString())
                    val responseTemplate = response.body()
                    checkResponse(responseTemplate)
                } else {
                    Log.d("get", "onResponse 실패: " + response.code())
                    Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<LoginResponse>>, t: Throwable) {
                Log.d("get", "onFailure 에러: " +  t.message.toString())

                Toast.makeText(
                    this@LoginActivity,
                    "네트워크 오류: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun checkResponse(responseTemplate: ResponseTemplate<LoginResponse>?){
        if (responseTemplate != null && responseTemplate.isSuccess == true) {
            val loginResponse = responseTemplate.results

            if(loginResponse != null){
                saveToken(loginResponse.token, loginResponse.refreshToken)
                Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                goToMainScreen()
            }else {
                Toast.makeText(this@LoginActivity, "회원 정보가 없습니다.", Toast.LENGTH_SHORT).show()
            }

        }else{
            Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()

        }
    }

    private fun saveToken(token: String, refreshToken: String) {
        TokenApplication.saveTokens(token, refreshToken)
    }

    private fun goToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
