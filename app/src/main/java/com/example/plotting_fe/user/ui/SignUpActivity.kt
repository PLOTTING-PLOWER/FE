package com.example.plotting_fe.user.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.user.dto.request.SignUpRequest
import com.example.plotting_fe.user.presentation.AuthController
import com.example.plotting_fe.global.util.ClickUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private var checkedNickname: String? = null
    private lateinit var alertCheckTextView: TextView

    private val authController: AuthController by lazy{
        ApiClient.getApiClient().create(AuthController::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById<View>(R.id.Join)) { v, insets ->
            val systemBars: Insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailInput = findViewById<EditText>(R.id.et_email)
        val nicknameInput = findViewById<EditText>(R.id.et_nickname)
        val passwordInput = findViewById<EditText>(R.id.et_pw)
        val passWordConfirmInput = findViewById<EditText>(R.id.et_pwConfirm)
        val nickNameCheckBtn = findViewById<Button>(R.id.btn_nicknameCheck)
        val joinBtn = findViewById<Button>(R.id.btn_join)
        alertCheckTextView.visibility = View.GONE // 텍스트뷰 숨기기


        joinBtn.setOnClickListener{
            val nickname = nicknameInput.text.toString()
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            val passwordConfirm = passWordConfirmInput.text.toString()
            handleSignUp(nickname, email, password, passwordConfirm)
        }

        // 닉네임 중복 확인
        nickNameCheckBtn.setOnClickListener{
            val nickname = nicknameInput.text.toString()
            checkNickname(nickname)
        }

        // 로그인 버튼에 클릭 리스너 설정
        findViewById<View>(R.id.tv_gologin).setOnClickListener {
            ClickUtil.onLoginClick(this)
        }
    }

    private fun handleSignUp(nickname: String, email: String, password: String, passwordConfirm: String) {
        if (!checkInput(nickname, email, password, passwordConfirm)) return

        val request = SignUpRequest(nickname, email, password)
        authController.signUp(request).enqueue(object : Callback<ResponseTemplate<Void>> {
            override fun onResponse(call: Call<ResponseTemplate<Void>>, response: Response<ResponseTemplate<Void>>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.isSuccess == true) {
                        Toast.makeText(this@SignUpActivity, "회원가입이 완료 되었습니다.", Toast.LENGTH_SHORT).show()

                        // 회원 가입 성공 시 LoginActivity로 이동
                        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                        startActivity(intent)

                        finish() // 회원가입 성공 후 화면 종료
                    } else {
                        Toast.makeText(this@SignUpActivity, "회원가입 실패: ${responseBody?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignUpActivity, "서버 오류: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<Void>>, t: Throwable) {
                Toast.makeText(this@SignUpActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkNickname(nickname: String){
        if(nickname.isEmpty()){
            Toast.makeText(this@SignUpActivity, "닉네임을 입력해 주세요", Toast.LENGTH_SHORT).show()
            return
        }

        authController.checkNickname(nickname).enqueue(object: Callback<ResponseTemplate<Boolean>>{
            override fun onResponse(
                call: Call<ResponseTemplate<Boolean>>,
                response: Response<ResponseTemplate<Boolean>>
            ) {
                if(response.isSuccessful){
                    val responseTemplate = response.body()
                    if(responseTemplate?.isSuccess ==true){
                        val isAvailable = responseTemplate.results ?: false
                        if(isAvailable){
                            checkedNickname = nickname
                            alertCheckTextView.text = "사용 가능한 닉네임 입니다."
                        }else{
                            alertCheckTextView.text = "이미 사용중인 닉네임 입니다."
                        }
                        alertCheckTextView.visibility = View.VISIBLE // 텍스트뷰 보이기
                        Log.d("get", "onResponse 성공: " + response.body().toString())
                    }else{
                        Toast.makeText(this@SignUpActivity, "닉네임 확인 실패", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Log.d("get", "onResponse 실패: " + response.code())
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<Boolean>>, t: Throwable) {
                Log.d("get", "onFailure 에러: " +  t.message.toString())
            }
        })
    }

    private fun checkInput(nickname: String, email: String,password: String, passwordConfirm: String ): Boolean {
        if(nickname.isEmpty() || email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()){
            Toast.makeText(this, "모든 란을 작성해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        // 이메일 유효성 검사
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "유효한 이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return false
        }

        // 비밀번호 최소 길이 확인
        if (password.length < 4) {
            Toast.makeText(this, "비밀번호는 4자리 이상이어야 합니다.", Toast.LENGTH_SHORT).show()
            return false
        }

        // 비밀번호 확인 일치 여부
        if(password!=passwordConfirm){
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return false
        }

        if(checkedNickname == null || checkedNickname != nickname){
            alertCheckTextView.text = "중복 확인을 해주세요"
            alertCheckTextView.visibility = View.VISIBLE // 텍스트뷰 보이기
            return false
        }
        alertCheckTextView.visibility = View.GONE // 텍스트뷰 숨기기
        return true
    }
}
