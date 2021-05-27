package com.example.godori.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.example.godori.GroupRetrofitServiceImpl
import com.example.godori.R
import com.example.godori.activity.*
import com.example.godori.adapter.CertifDateAdapter
import com.example.godori.adapter.GroupInfoMemberAdapter
import com.example.godori.adapter.MyInfoPictureAdapter
import com.example.godori.data.ResponseGroupInfoAfter
import com.example.godori.data.ResponseMypage
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_on_boarding1.*
import kotlinx.android.synthetic.main.fragment_group_info2.*
import kotlinx.android.synthetic.main.fragment_my_info_tab.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInfoTabFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    var kakaoId:Long = 0

    var certiList: List<ResponseMypage.Data.Certi>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_my_info_tab, container, false)

        // 카카오톡 ID
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.d("MyInfoFragment_KAKAOID", "토큰 정보 보기 실패")
            }
            else if (tokenInfo != null) {
                Log.d("MyInfoFragment_KAKAOID", "토큰 정보 보기 성공" +
                        "\n회원번호: ${tokenInfo.id}")
                kakaoId = tokenInfo.id

                // 마이페이지 서버 연결
                loadData()
            }
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 피드 사진 recycler view
        viewManager = GridLayoutManager(this.context, 3)
        viewAdapter = MyInfoPictureAdapter(certiList, context)
        recyclerView = my_rcv_picture.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }

        // 설정 버튼
        my_btn_setting.setOnClickListener {
            val intent = Intent(activity, SettingActivity::class.java)
            startActivity(intent)
        }

        // 취향 버튼
        my_btn_taste.setOnClickListener {
            val intent = Intent(activity, TasteSettingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadData() {
        //Callback 등록하여 통신 요청
        val call: Call<ResponseMypage> =
            GroupRetrofitServiceImpl.service_mypage.requestList(
                kakaoId = kakaoId //수정하기
            )
        call.enqueue(object : Callback<ResponseMypage> {
            override fun onFailure(call: Call<ResponseMypage>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<ResponseMypage>,
                response: Response<ResponseMypage>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let { it ->
                        // Response 로그
                        var dataList = response.body()
                        Log.d("MyInfoTabFragment", dataList.toString())

                        // message 확인
                        var message = it.message
                        Log.d("Mypage", message)

                        when (message) {
                            "마이페이지 정보 불러오기 성공" -> {
                                // 달성률
                                var percent = it.data.join.achive_rate
                                my_tv_percent.setText("$percent%")

                                // 이번주 인증 횟수
                                var count = it.data.certi_list.size
                                my_tv_week_count.setText("${count}회")

                                // 사용자 프로필 닉네임 - 수정 된 건지?
                                var nickName = it.data.profile.name
                                my_tv_userName.text = nickName

                                // 사용자 프로필 이미지 - 수정 된 건지?
                                var userImg = it.data.profile.image
                                @GlideModule
                                if (userImg != null) {
                                    Glide.with(this@MyInfoTabFragment)
                                        .load(userImg)
                                        .circleCrop()
                                        .error(android.R.drawable.stat_notify_error)
                                        .into(my_iv_profile)

                                } else {
                                    Glide.with(this@MyInfoTabFragment)
                                        .load(R.drawable.gr_img_profile_basic)
                                        .circleCrop()
                                        .error(android.R.drawable.stat_notify_error)
                                        .into(my_iv_profile)
                                }


                                // 사진 어댑터에 데이터 전달
                                setMyPageAdapter(it.data.certi_list)
                            }
                            else -> {
                                my_tv_percent.setText("-%")
                                my_tv_week_count.setText("-회")
                            }
                        }

                    } ?: showError(response.errorBody())
            }
        })
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        Toast.makeText(context, ob.getString("message"), Toast.LENGTH_SHORT).show()
    }

    private fun setMyPageAdapter(certiList: List<ResponseMypage.Data.Certi>) {
        val mAdapter = MyInfoPictureAdapter(certiList, context)
        my_rcv_picture.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
        mAdapter.itemClick = object : MyInfoPictureAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(activity, CertifTabDetailActivity::class.java)
                //인증탭의 인증 이미지의 id 넘겨주기
                intent.putExtra("certiImgId", certiList[position].id)
                startActivity(intent)
            }
        }
        my_rcv_picture.setHasFixedSize(true)
    }
}