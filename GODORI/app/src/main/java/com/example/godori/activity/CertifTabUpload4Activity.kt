package com.example.godori.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.godori.GroupRetrofitServiceImpl
import com.example.godori.R
import com.example.godori.data.RequestCertiUpload
import com.example.godori.data.ResponseCertiUpload
import com.example.godori.data.ResponseGroupCreationData
import com.example.godori.fragment.CertifTabFragment
import kotlinx.android.synthetic.main.activity_certif_tab_upload4.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File


class CertifTabUpload4Activity : AppCompatActivity() {
    // 데이터 목록
    var ex_comment: String = ""
    var ex_time: String = ""
    var ex_intensity: String = ""
    var ex_evalu: String = ""
    var certi_sport: String = ""
    lateinit var images: File

    var imageURI: Uri? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certif_tab_upload4)

        //백 버튼 눌렀을 때
        backBtn4.setOnClickListener {
            onBackPressed()
        }

        // 코멘트에 textWatcher
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btn_complete.isEnabled = true
                ex_comment = certi_upload_comment.text.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                btn_complete.isEnabled = false

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btn_complete.isEnabled = true
            }
        }
        certi_upload_comment.addTextChangedListener(textWatcher)

        // 완료 버튼
        btn_complete.setOnClickListener(View.OnClickListener {
            // 1. 데이터 전달 받음
            val secondIntent = getIntent()
            ex_time = secondIntent.getStringExtra("ex_time").toString()
            ex_intensity = secondIntent.getStringExtra("ex_intensity").toString()
            ex_evalu = secondIntent.getStringExtra("ex_intensity").toString()
            certi_sport = secondIntent.getStringExtra("certi_sport").toString()

            // 2. 이미지 Multipart.Part로 변환
            var imageURIString = secondIntent.getStringExtra("imageURI")
            if (imageURIString != null) {
                imageURI = imageURIString.toUri()
            }
            Log.v("certi4", imageURI.toString())

            val options = BitmapFactory.Options()
            val inputStream = imageURI?.let { it1 -> contentResolver.openInputStream(it1) }
            val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            val byteArrayOutputStream = ByteArrayOutputStream()
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream)
            }
            val photoBody =
                RequestBody.create(
                    MediaType.parse("image/jpg"),
                    byteArrayOutputStream.toByteArray()
                )
            val part = MultipartBody.Part.createFormData(
                "image",
                File(imageURI.toString()).name,
                photoBody
            )

            // 3. 인증 업로드 POST
            val call: Call<ResponseCertiUpload> =
                GroupRetrofitServiceImpl.service_ct_upload.postCertiUpload(
                    "김지현",
                    RequestCertiUpload(
                        ex_time = ex_time,
                        ex_intensity = ex_intensity,
                        ex_evalu = ex_evalu,
                        certi_sport = certi_sport,
                        ex_comment = ex_comment
                    )
                )
            call.enqueue(object : Callback<ResponseCertiUpload> {
                override fun onFailure(call: Call<ResponseCertiUpload>, t: Throwable) {
                    // 통신 실패 로직
                    Log.d("업로드 실패 : ", t.message.toString())
                }

                override fun onResponse(
                    call: Call<ResponseCertiUpload>,
                    response: Response<ResponseCertiUpload>
                ) {
                    response.takeIf { it.isSuccessful }
                        ?.body()
                        ?.let { it ->
                            Log.d("업로드 성공 : ", response.body().toString())

                            // 4. 업로드 성공하면 -> 이미지 업로드
                            val call: Call<ResponseGroupCreationData> =
                                GroupRetrofitServiceImpl.service_ct_upload_image.postCertiUpload(
                                    it.data,
                                    images = part
                                )
                            call.enqueue(object : Callback<ResponseGroupCreationData> {
                                override fun onFailure(
                                    call: Call<ResponseGroupCreationData>,
                                    t: Throwable
                                ) {
                                    // 통신 실패 로직
                                    Log.d("업로드 실패 : ", t.message.toString())
                                }

                                override fun onResponse(
                                    call: Call<ResponseGroupCreationData>,
                                    response: Response<ResponseGroupCreationData>
                                ) {
                                    response.takeIf { it.isSuccessful }
                                        ?.body()
                                        ?.let { it ->
                                            Log.d("사진 업로드 성공 : ", response.body().toString())
                                        } ?: showError(response.errorBody())
                                }
                            })

                        } ?: showError(response.errorBody())
                }

            })
            // 5. 이전 뷰 스택 다 지우고 TabBar 액티비티로 돌아가기
            val intent = Intent(application, TabBarActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // 6. 액티비티 시작
            startActivity(intent)
        })

    }


    val certifTabFragment = CertifTabFragment()
    //        btn_complete.setOnClickListener(View.OnClickListener {
//            replaceFragment(certifTabFragment)
//        })


    // Fragment로 이동
    @SuppressLint("RestrictedApi")
    fun replaceFragment(fragment: Fragment?) {
//        val certifTabFragment = CertifTabFragment()
//        val fragmentManager: FragmentManager = supportFragmentManager
//        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.certifTabFrag, certifTabFragment)
//        fragmentTransaction.commit()
        val fragmentManager = supportFragmentManager
//        val fragmentManager: FragmentManager = getActivity().getFragmentManager()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.menu_certi, fragment!!)
        fragmentTransaction.commit()
    }

    // 서버 연동관련 에러 함수
    private fun showError(error: ResponseBody?) {
        val e = error ?: return
//        val ob = JSONObject(e.string())
//        Toast.makeText(this, ob.getString("message"), Toast.LENGTH_SHORT).show()
    }
}
