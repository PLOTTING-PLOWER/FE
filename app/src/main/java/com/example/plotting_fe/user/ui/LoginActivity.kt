package com.example.plotting_fe.user.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.plotting_fe.MainActivity
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.RetrofitImpl
import com.example.plotting_fe.user.dto.request.LoginRequest
import com.example.plotting_fe.user.dto.response.LoginResponse
import com.example.plotting_fe.user.presentation.AuthController
import com.example.plotting_fe.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val authController: AuthController by lazy {
        RetrofitImpl.retrofit.create(AuthController::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById<View>(R.id.login)) { v, insets ->
            val systemBars: Insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
            Utils.onJoinClick(this)
        }
    }

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
                    val token = response.body()?.results?.token
                    saveToken(token)
                    goToMainScreen()
                } else {
                    Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<LoginResponse>>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun handleNaverLogin() {
        val naverAuthUrl = "https://nid.naver.com/oauth2.0/authorize" +
                "?client_id=WehW4LZAFENKHdQImWWS" +
                "&redirect_uri=http://10.0.2.2:8080/login/oauth2/code/naver" +
                "&response_type=code"
        Log.d("LoginActivity", "Naver Auth URL: $naverAuthUrl")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(naverAuthUrl))
        startActivity(intent)
    }

    override fun onNewIntent(intent: Intent) {
        if (intent != null) {
            super.onNewIntent(intent)
        }

        Log.d("LoginActivity", "onNewIntent called with intent: $intent")

        intent?.let {
            val uri = it.data
            Log.d("LoginActivity", "URI: $uri")
            handleIntent(it)
        }
    }

    private fun handleIntent(intent: Intent?) {
        val uri = intent?.data
        if (uri != null && uri.path == "/login/oauth2/code/naver") {
            val code = uri.getQueryParameter("code")
            Log.d("LoginActivity", "Extracted code: $code")

            if (!code.isNullOrEmpty()) {
                handleNaverTokenExchange(code)
            } else {
                Log.e("LoginActivity", "Code not found in URI")
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }else{
            Log.e("LoginActivity", "Invalid URI path: ${uri?.path}")
        }
    }

    private fun handleNaverTokenExchange(authCode: String) {
        val apiService = RetrofitImpl.retrofit.create(AuthController::class.java)
        apiService.exchangeNaverCode(authCode).enqueue(object : Callback<ResponseTemplate<LoginResponse>> {
            override fun onResponse(
                call: Call<ResponseTemplate<LoginResponse>>,
                response: Response<ResponseTemplate<LoginResponse>>
            ) {
                if (response.isSuccessful) {
                    val token = response.body()?.results?.token
                    saveToken(token)
                    goToMainScreen()
                } else {
                    Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<LoginResponse>>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveToken(token: String?) {
        if (token != null) {
            val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
            sharedPreferences.edit().putString("jwt_token", token).apply()
        }
    }

    private fun goToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
