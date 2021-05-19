package com.example.godori.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.godori.GroupRetrofitServiceImpl
import com.example.godori.R
import com.example.godori.activity.GroupInfoAfterActivity
import com.example.godori.data.ResponseGroupInfoAfter
import kotlinx.android.synthetic.main.activity_group_info_after.*
import kotlinx.android.synthetic.main.fragment_group_info1.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GroupInfo1Flagment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GroupInfo1Flagment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var data: ResponseGroupInfoAfter? = null
    var groupData: ResponseGroupInfoAfter.Data.GroupDetail? = null
    var memberList: List<ResponseGroupInfoAfter.Data.GroupMember>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_group_info1, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //group_id로 그룹 정보 가져오기 GroupInfo1Fragment
        val call: Call<ResponseGroupInfoAfter> =
            GroupRetrofitServiceImpl.service_gr__info_after.requestList(
                groupId = (activity as GroupInfoAfterActivity?)!!.groupId()
            ).apply {
                enqueue(object : Callback<ResponseGroupInfoAfter> {
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
                                data = response.body()
                                groupData = data!!.data.group_detail
                                memberList = data!!.data.group_member

                                //recruited_num
                                gr_tv_info_after_member_recruit_num.setText(groupData!!.recruited_num.toString() + "/")
                                //recruit_num
                                gr_tv_info_after_member_total_num.setText(groupData!!.recruit_num.toString())
                                //ex_cycle
                                gr_tv_info_after_cycle_num.setText("주" + groupData!!.ex_cycle.toString() + "회")
                                //ex_intensity
                                gr_tv_info_after_level_num.setText(groupData!!.ex_intensity)
                                //achive_rate
                                gr_tv_info_after_percent_num.setText(groupData!!.achive_rate.toString() + "%")
                                //group_sport
                                gr_tv_info_after_exercise_num.setText(groupData!!.group_sport)

                            } ?: showError(response.errorBody())
                    }
                })
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GroupInfo1Flagment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GroupInfo1Flagment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    // api 호출 에러 함수
    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
    }
}