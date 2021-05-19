package com.example.godori.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.example.godori.GroupRetrofitServiceImpl
import com.example.godori.R
import com.example.godori.adapter.GroupRecruitingInfoAdapter
import com.example.godori.adapter.MyInfoPictureAdapter
import com.example.godori.data.ResponseGroupRecruit
import kotlinx.android.synthetic.main.activity_group_recruiting.*
import kotlinx.android.synthetic.main.fragment_my_info_tab.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class GroupRecruitingActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    var dataList: ResponseGroupRecruit? = null
    var groupList: List<ResponseGroupRecruit.Data.Group>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_recruiting)

        // 이전
        gr_btn_recruiting_back.setOnClickListener {
            onBackPressed()
        }

        // 그룹 정보 recycler view 초기화
        viewManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        viewAdapter = GroupRecruitingInfoAdapter(groupList, this)
        recyclerView = findViewById<RecyclerView>(R.id.gr_rcv_recruiting_info).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }

        // 취향 버튼
        gr_btn_recruiting_modify.setOnClickListener {
            val intent = Intent(this, TasteSettingActivity::class.java)
            startActivity(intent)
        }

        loadData()
    }

    private fun loadData() {
        //Callback 등록하여 통신 요청
        val call: Call<ResponseGroupRecruit> =
            GroupRetrofitServiceImpl.service_gr_recruit.requestList(
                userName = "김지현" //수정하기
            )
        call.enqueue(object : Callback<ResponseGroupRecruit> {
            override fun onFailure(call: Call<ResponseGroupRecruit>, t: Throwable) {
                // 통신 실패 로직
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ResponseGroupRecruit>,
                response: Response<ResponseGroupRecruit>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let { it ->
                        // do something
                        dataList = response.body()
                        Log.d("GroupRecruitingActivity", dataList.toString())
                        groupList = dataList!!.data.group_list
                        Log.d("GroupRecruitingActivity", groupList.toString())

                        //recyclerView adapter에 Group 데이터 넣기
                        // 그룹 정보 recycler view
                        val mAdapter = GroupRecruitingInfoAdapter(
                            groupList!!, this@GroupRecruitingActivity
                        )
                        gr_rcv_recruiting_info.adapter = mAdapter
                        mAdapter.itemClick = object : GroupRecruitingInfoAdapter.ItemClick {
                            override fun onClick(view: View, position: Int) {
                                val groupId = groupList!![position].id

                                val intent = Intent(baseContext, GroupInfoActivity::class.java)
                                intent.putExtra("groupId", groupId)
                                startActivity(intent)
                            }
                        }
                        mAdapter.notifyDataSetChanged()
                        gr_rcv_recruiting_info.setHasFixedSize(true)

                        //recruit에 Group 데이터 넣기
                        gr_tv_recruiting_groupNum.setText(it.data.group_list.size.toString() + "건")

                        //taste에 User 데이터 넣기
                        gr_tv_taste1.setText("주 " + it.data.user.ex_cycle + "회")
                        gr_tv_taste2.setText(it.data.user.ex_intensity)
                        val sportList = it.data.user.sports.split(",")
                        val textArray = arrayOf<TextView>(
                            gr_tv_taste3,
                            gr_tv_taste4,
                            gr_tv_taste5,
                            gr_tv_taste6,
                            gr_tv_taste7,
                            gr_tv_taste8
                        )
                        val layoutArray = arrayOf<View>(
                            gr_lay_taste3,
                            gr_lay_taste4,
                            gr_lay_taste5,
                            gr_lay_taste6,
                            gr_lay_taste7,
                            gr_tv_taste8
                        )

                        for (i in sportList.indices) {
                            layoutArray[i].setVisibility(View.VISIBLE)
                            textArray[i].setText(sportList[i])
                        }
                    } ?: showError(response.errorBody())
            }
        })
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        Toast.makeText(this, ob.getString("message"), Toast.LENGTH_SHORT).show()
    }

}