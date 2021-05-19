package com.example.godori.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.godori.GroupRetrofitServiceImpl
import com.example.godori.R
import com.example.godori.activity.GroupInfoAfterActivity
import com.example.godori.adapter.GroupInfoMemberAdapter
import com.example.godori.data.ResponseGroupInfoAfter
import kotlinx.android.synthetic.main.fragment_group_info2.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupInfo2Fragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    var memberList: List<ResponseGroupInfoAfter.Data.GroupMember>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_group_info2, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 그룹원 recycler view
        viewManager = GridLayoutManager(activity, 4)
        viewAdapter = GroupInfoMemberAdapter(memberList, context)
        recyclerView = gr_rcv_info_member.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }

        //group_id로 그룹 정보 가져오기
        val call: Call<ResponseGroupInfoAfter> =
            GroupRetrofitServiceImpl.service_gr__info_after.requestList(
                groupId = (activity as GroupInfoAfterActivity?)!!.groupId()
            )
        call.enqueue(object : Callback<ResponseGroupInfoAfter> {
            override fun onFailure(call: Call<ResponseGroupInfoAfter>, t: Throwable) {
                // 통신 실패 로직
            }

            @SuppressLint("SetTextI18n", "SimpleDateFormat")
            override fun onResponse(
                call: Call<ResponseGroupInfoAfter>,
                response: Response<ResponseGroupInfoAfter>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let { it ->
                        // do something
                        memberList = response.body()!!.data.group_member
                        Log.d("GroupRecruitingActivity", memberList.toString())
                        setGroupInfoMemAdapter(memberList!!)

                    } ?: showError(response.errorBody())
            }
        })

    }

    private fun setGroupInfoMemAdapter(memberList: List<ResponseGroupInfoAfter.Data.GroupMember>) {
        gr_rcv_info_member.layoutManager =
            GridLayoutManager(activity, 4)
        val mAdapter = GroupInfoMemberAdapter(memberList, context)
        gr_rcv_info_member.adapter = mAdapter
        mAdapter.notifyDataSetChanged()
        gr_rcv_info_member.setHasFixedSize(true)
    }

    // api 호출 에러 함수
    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
    }
}