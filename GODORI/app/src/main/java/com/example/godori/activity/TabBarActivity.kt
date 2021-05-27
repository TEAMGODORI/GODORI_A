package com.example.godori.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.godori.GroupRetrofitServiceImpl
import com.example.godori.R
import com.example.godori.adapter.TabBarViewPagerAdapter
import com.example.godori.data.ResponseGroupAfterTab
import com.example.godori.fragment.CertifTabFragment
import com.kakao.sdk.user.UserApiClient
import kotlinx.android.synthetic.main.activity_tab_bar.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates


class TabBarActivity : AppCompatActivity() {
    private lateinit var viewpagerAdapter: TabBarViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_bar)

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Log.d("TabBar_KAKAOID", "토큰 정보 보기 실패")
            }
            else if (tokenInfo != null) {
                Log.d(
                    "TabBar_KAKAOID", "토큰 정보 보기 성공" +
                            "\n회원번호: ${tokenInfo.id}"
                )
                // fragment
                loadData(tokenInfo.id)
            }
        }

        tabbar_viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                tabbar.menu.getItem(position).isChecked = true
            }
        })

        // 바텀 네비게이션 세팅
        tabbar.setOnNavigationItemSelectedListener {
            var index by Delegates.notNull<Int>()
            when (it.itemId) {
                R.id.menu_group -> index = 0
                R.id.menu_certi -> index = 1
                R.id.menu_myinfo -> index = 2
            }
            tabbar_viewpager.currentItem = index
            true
        }
    }
    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        Toast.makeText(this, ob.getString("message"), Toast.LENGTH_SHORT).show()
    }

    fun loadData(kakaoId: Long){
        val call: Call<ResponseGroupAfterTab> =
            GroupRetrofitServiceImpl.service_gr_after_tab.requestList(
                kakaoId = kakaoId
            )
        call.enqueue(object : Callback<ResponseGroupAfterTab> {
            override fun onFailure(call: Call<ResponseGroupAfterTab>, t: Throwable) {
                // 통신 실패 로직
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ResponseGroupAfterTab>,
                response: Response<ResponseGroupAfterTab>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let { it ->
                        Log.v("kakaoId-tabbar-loaddata", kakaoId.toString())
                        // 1. 그룹아이디
                        var group = it.data.group_id

                        // 2. 뷰 페이저 세팅
                        Log.v("group_string", group.toString())
                        viewpagerAdapter = TabBarViewPagerAdapter(supportFragmentManager, group)
                        tabbar_viewpager.adapter = viewpagerAdapter
                        Log.v("onResponse", group.toString())

                        // 3. 인증 올렸을 시 인증탭으로
                        val secondIntent = getIntent()
                        var certiUpload = secondIntent.getBooleanExtra("certiUpload", false)
                        if (certiUpload) {
                            tabbar_viewpager.currentItem = 1;
                        } else {
                            tabbar_viewpager.currentItem = 0;
                        }
                    } ?: showError(response.errorBody())
            }
        })
    }
}
