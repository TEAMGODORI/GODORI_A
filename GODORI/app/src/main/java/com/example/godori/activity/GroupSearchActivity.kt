package com.example.godori.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.godori.GroupRetrofitServiceImpl
import com.example.godori.R
import com.example.godori.adapter.GroupRecruitingInfoAdapter
import com.example.godori.adapter.GroupSearchFileListAdapter
import com.example.godori.data.RequestGroupCreationData
import com.example.godori.data.ResponseGroupCreationData
import com.example.godori.data.ResponseGroupSearch
import kotlinx.android.synthetic.main.activity_certif_tab_upload4.*
import kotlinx.android.synthetic.main.activity_group_search.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class GroupSearchActivity : AppCompatActivity() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: RecyclerView.Adapter<*>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    lateinit var group_list: List<ResponseGroupSearch.Data>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_search)

        // 이전
        gr_btn_search_back.setOnClickListener {
            onBackPressed()
        }

        // 검색 기능
        val call: Call<ResponseGroupSearch> =
            GroupRetrofitServiceImpl.service_gr_search.groupSearch(
            )
        call.enqueue(object : Callback<ResponseGroupSearch> {
            override fun onFailure(call: Call<ResponseGroupSearch>, t: Throwable) {
                // 통신 실패 로직
            }

            override fun onResponse(
                call: Call<ResponseGroupSearch>,
                response: Response<ResponseGroupSearch>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let { it ->
                        Log.v("그룹 목록: ", it.data.toString())
                        Log.v("그룹 검색: ", "성공!")
                        group_list = it.data
                        recycler()
                    } ?: showError(response.errorBody())
            }
        })

        // 검색 edit text
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                (mAdapter as GroupSearchFileListAdapter)?.filter?.filter(s)
            }
        }
        gr_et_search.addTextChangedListener(textWatcher)
    }

    // 서버 연동관련 에러 함수
    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        Toast.makeText(this, ob.getString("message"), Toast.LENGTH_SHORT).show()
    }

    // 리사이클러 build
    private fun recycler(){
        mLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mAdapter = GroupSearchFileListAdapter(group_list, this)
        mRecyclerView = findViewById<RecyclerView>(R.id.gr_rcv_search).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = mLayoutManager

            // specify an viewAdapter (see also next example)
            adapter = mAdapter
        }

        (mAdapter as GroupSearchFileListAdapter).itemClick = object : GroupRecruitingInfoAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val groupId = group_list[position].id

                val intent = Intent(baseContext, GroupInfoActivity::class.java)
                intent.putExtra("groupId", groupId)
                startActivity(intent)
            }
        }
    }
}