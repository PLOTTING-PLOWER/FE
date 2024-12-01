package com.example.plotting_fe.mypage.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.example.plotting_fe.R
import com.example.plotting_fe.global.ResponseTemplate
import com.example.plotting_fe.global.util.ApiClient
import com.example.plotting_fe.global.util.ClickUtil
import com.example.plotting_fe.mypage.dto.request.MyProfileRequest
import com.example.plotting_fe.mypage.presentation.MypageController
import com.example.plotting_fe.user.presentation.AuthController
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class EditProfileActivity : AppCompatActivity() {

    private lateinit var profileImageView: ImageView
    private lateinit var nicknameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var profileMessageTextView: TextView
    private lateinit var publicSpinner: Spinner
    private lateinit var alertCheckTextView: TextView

    private var checkedNickname: String? = null
    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage_edit_profile)

        // UI 요소 초기화
        profileImageView = findViewById(R.id.iv_image)
        nicknameTextView = findViewById(R.id.et_nickname_value)
        emailTextView = findViewById(R.id.tv_email_value)
        profileMessageTextView = findViewById(R.id.et_intro_value)
        publicSpinner = findViewById(R.id.visibility_spinner)
        alertCheckTextView = findViewById(R.id.tv_alert_check)
        alertCheckTextView.visibility = View.GONE // 텍스트뷰 숨기기

        // 뒤로가기 버튼 설정
        val backButton: ImageView = findViewById(R.id.iv_back)
        ClickUtil.onBackButtonClick(this, backButton)

        // Intent에서 데이터 가져오기
        val nickname = intent.getStringExtra("nickname")
        val email = intent.getStringExtra("email")
        val profileMessage = intent.getStringExtra("profileMessage")
        val profileImageUrl = intent.getStringExtra("profileImageUrl")
        val isProfilePublic = intent.getBooleanExtra("isProfilePublic", false)

        // UI에 데이터 반영
        setupSpinner()
        setupUI(nickname, email, profileMessage, profileImageUrl, isProfilePublic)

        // 프로필 이미지 버튼 클릭
        profileImageView.setOnClickListener {
            openImagePicker()
        }

        // 닉네임 중복 체크
        val nicknameCheckBtn :Button = findViewById(R.id.btn_nicknameCheck)
        nicknameCheckBtn.setOnClickListener{
            checkNickname(nicknameTextView.text.toString())
        }

        // 수정 버튼 클릭
        val summitEditButton: Button = findViewById(R.id.btn_edit)
        summitEditButton.setOnClickListener{
            Log.d("EditProfileActivity", "summitEditButton")
            handleEdit()
        }

    }

    private fun setupUI(
        nickname: String?,
        email: String?,
        profileMessage: String?,
        profileImageUrl: String?,
        isProfilePublic: Boolean
    ) {
        nicknameTextView.text = nickname ?: "unknown"
        emailTextView.text = email ?: "unknown"
        profileMessageTextView.text = profileMessage ?: ""
        // Spinner 선택 항목 설정 (0: 공개, 1: 비공개)
        publicSpinner.setSelection(if (isProfilePublic) 0 else 1)

        // Glide를 사용해 프로필 이미지 로드
        Glide.with(this)
            .load(profileImageUrl?.takeIf { it.isNotEmpty() }) // URL이 비어있지 않은 경우만 로드
            .apply(RequestOptions().circleCrop()) // 이미지를 원형으로 변환
            .placeholder(R.drawable.ic_flower) // 로드 중 기본 이미지
            .error(R.drawable.ic_flower) // 로드 실패 시 기본 이미지
            .skipMemoryCache(true) // 메모리 캐시 비활성화
            .diskCacheStrategy(DiskCacheStrategy.NONE) // 디스크 캐시 비활성화
            .into(profileImageView) // 이미지뷰에 로드
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.profile_visibility_options, // strings.xml에 정의된 배열 사용
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        publicSpinner.adapter = adapter
    }


    private fun openImagePicker() {
        if (checkPermissions()) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        } else {
            // 권한 요청후 수락하면-> 한번 더 열기
            requestPermissions()
            if(checkPermissions()){
                openImagePicker()
            }
        }

    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            100
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            selectedImageUri = data?.data
            selectedImageUri?.let { uri ->
                // 이미지 리사이즈 및 압축 후 이미지뷰에 설정
                val compressedFile = resizeAndCompressImage(uri)

                // 파일 크기 확인
                if (!checkFileSize(compressedFile)) {
                    Toast.makeText(this, "파일 크기가 너무 큽니다. 최대 2MB로 제한됩니다.", Toast.LENGTH_SHORT).show()
                    return
                }

                Glide.with(this)
                    .load(compressedFile) // 리사이즈 및 압축된 이미지
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 디스크 캐시 비활성화
                    .skipMemoryCache(true) // 메모리 캐시 비활성화
                    .into(profileImageView)  // 새로운 이미지 로드
            }
        }
    }

    private fun resizeAndCompressImage(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)

        // 이미지 크기를 800x800으로 리사이징 (너무 큰 이미지일 경우 크기를 줄임)
        val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 800, 800, false)
        originalBitmap.recycle()

        // 압축 파일 저장 위치
        val compressedFile = File(cacheDir, "compressed_image.jpg")

        var quality = 50
        var fileSizeInKB: Long

        // 처음 압축하고 크기를 체크
        var outputStream = FileOutputStream(compressedFile)
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        outputStream.flush()
        outputStream.close()

        // 파일 크기 확인
        fileSizeInKB = compressedFile.length() / 1024  // KB 단위로 변환

        // 파일 크기가 100KB보다 크면 반복해서 압축
        while (fileSizeInKB > 30 && quality > 10) {
            // 압축률을 낮추고 새로운 OutputStream을 생성하여 압축
            quality -= 5
            outputStream = FileOutputStream(compressedFile) // 새로운 outputStream 생성
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            outputStream.flush()
            outputStream.close()

            // 파일 크기 재확인
            fileSizeInKB = compressedFile.length() / 1024  // KB 단위로 변환
        }

        scaledBitmap.recycle()

        // 파일 크기가 여전히 큰 경우
        if (fileSizeInKB > 30) {
            Log.d("Image Compression", "파일 크기가 여전히 큽니다: $fileSizeInKB KB")
        }

        return compressedFile
    }

    private fun checkFileSize(file: File): Boolean {
        val fileSize = file.length()
        val maxFileSize = 2 * 1024 * 1024 // 2MB로 제한

        Log.d("FileSizeCheck", "파일 크기: ${fileSize / 1024} KB")  // KB 단위로 파일 크기 출력

        return fileSize <= maxFileSize
    }

    private fun checkNickname(nickname: String){
        val authController: AuthController = ApiClient.getApiClient().create(AuthController::class.java)
        if(nickname.isEmpty()){
            Toast.makeText(this@EditProfileActivity, "닉네임을 입력해 주세요", Toast.LENGTH_SHORT).show()
            return
        }

        authController.checkNickname(nickname).enqueue(object: Callback<ResponseTemplate<Boolean>> {
            override fun onResponse(
                call: Call<ResponseTemplate<Boolean>>,
                response: Response<ResponseTemplate<Boolean>>
            ) {
                if(response.isSuccessful){
                    val responseTemplate = response.body()
                    val isAvailable = responseTemplate?.results ?: false
                    if(isAvailable){
                        checkedNickname = nickname
                        alertCheckTextView.text = "사용 가능한 닉네임 입니다."
                    }else{
                        alertCheckTextView.text = "이미 사용중인 닉네임 입니다."
                    }
                    alertCheckTextView.visibility = View.VISIBLE // 텍스트뷰 보이기
                    Log.d("get", "onResponse 성공: " + response.body().toString())
                }else{
                    Log.d("get", "onResponse 실패: " + response.code())
                }
            }
            override fun onFailure(call: Call<ResponseTemplate<Boolean>>, t: Throwable) {
                Log.d("get", "onFailure 에러: " +  t.message.toString())
                if(t.message.toString()=="413"){
                    Toast.makeText(this@EditProfileActivity, "파일 사이즈가 너무 큽니다", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun handleEdit(){
        val nickname = nicknameTextView.text.toString()
        val profileMessage = profileMessageTextView.text.toString()
        val isPublic = publicSpinner.selectedItemPosition == 0 // 0: 공개, 1: 비공개

        if(nickname.isEmpty()){
            Toast.makeText(this, "닉네임을 작성해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        if(nickname!=intent.getStringExtra("nickname")){     // 닉네임 변경시
            if( checkedNickname == null || checkedNickname!=nickname){      // 중복체크 안했다면..!
                alertCheckTextView.text = "중복 확인을 해주세요"
                alertCheckTextView.visibility = View.VISIBLE // 텍스트뷰 보이기
                return
            }else{
                alertCheckTextView.visibility = View.GONE // 텍스트뷰 숨기기
            }
        }

        val profileRequest = MyProfileRequest(nickname, profileMessage, isPublic)

        // 이미지가 변경되었는지 확인
        val profileImagePart = if (selectedImageUri != null) {
            prepareImagePart(selectedImageUri!!)
        } else {
            null // 이미지 변경이 없으면 null
        }

        // 서버에 전송
        val mypageController : MypageController =  ApiClient.getApiClient().create(MypageController::class.java)
        mypageController.updateMyProfile(profileImagePart, profileRequest).enqueue(object :Callback<ResponseTemplate<Void>>{
            override fun onResponse(
                call: Call<ResponseTemplate<Void>>,
                response: Response<ResponseTemplate<Void>>
            ) {
                if(response.isSuccessful){
                    Log.d("get", "onResponse 성공: " + response.body().toString())
                    finish()
                }else{
                    Log.d("get", "onResponse 실패: " + response.code())
                }
            }

            override fun onFailure(call: Call<ResponseTemplate<Void>>, t: Throwable) {
                Log.d("get", "onFailure 에러: " +  t.message.toString())
            }
        });
    }

    private fun prepareImagePart(imageUri: Uri): MultipartBody.Part {
        val filePath = getRealPathFromURI(imageUri)
        val file = File(filePath)
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("profileImage", file.name, requestFile)
    }

    private fun getRealPathFromURI(uri: Uri): String {
        var path = ""
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                path = it.getString(columnIndex)
            }
        }
        return path
    }

}