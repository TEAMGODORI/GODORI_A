package com.example.godori.fragment


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.godori.GroupRetrofitServiceImpl
import com.example.godori.R
import com.example.godori.activity.CertifTabDetailActivity
import com.example.godori.activity.CertifTabUpload1Activity
import com.example.godori.adapter.CertifDateAdapter
import com.example.godori.data.ResponseCertiTab
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.prolificinteractive.materialcalendarview.*
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import kotlinx.android.synthetic.main.activity_certif_tab_upload1.*
import kotlinx.android.synthetic.main.activity_certif_tab_upload4.*
import kotlinx.android.synthetic.main.fragment_certif_tab.*
import kotlinx.android.synthetic.main.fragment_certif_tab.view.*
import kotlinx.android.synthetic.main.fragment_group_after_tab.*
import kotlinx.android.synthetic.main.item_certif_tab.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.getInstance


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CertifTabFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CertifTabFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceType", "SetTextI18n")

    var data: ResponseCertiTab? = null
    var certiList: List<ResponseCertiTab.Data>? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    var kakaoId:Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_certif_tab, container, false)
        val materialCalendarView: MaterialCalendarView = view.findViewById(R.id.cal)

        //현재 날짜
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("YYYY-MM-dd", Locale.getDefault())
        var serverDate = dateFormat.format(currentTime)

        materialCalendarView.state().edit()
            .setFirstDayOfWeek(Calendar.MONDAY)
            .setMinimumDate(CalendarDay.from(2020, 1, 1))
            .setMaximumDate(CalendarDay.today())
            .setCalendarDisplayMode(CalendarMode.WEEKS)
            .commit()

        val formatter = SimpleDateFormat("yyyy-M-dd")
        val date = formatter.parse("2021-5-20")
        val cal = Calendar.getInstance()
        cal.time = date

        materialCalendarView.addDecorators(
            SundayDecorator(),
            SaturdayDecorator(),
            OneDayDecorator(materialCalendarView),
            EventDecorator(
                Color.RED,
                Collections.singleton(cal.time)
            )
        )


        //오늘 날짜에 색칠
        materialCalendarView.setDateSelected(Calendar.getInstance(), true)
        materialCalendarView.isDynamicHeightEnabled = true

        //날짜 선택 시
        materialCalendarView.setOnDateChangedListener { widget, date, selected ->
            val weekdayFormat = SimpleDateFormat("EE", Locale.getDefault())
            val dateFormat = SimpleDateFormat("YYYY-MM-dd", Locale.getDefault())

            val weekDay = weekdayFormat.format(date.getDate())
            calendarText.text = "${date.month + 1}월 ${date.day}일 ${weekDay}요일"

            //서버에 보낼 형식
            serverDate = dateFormat.format(date.date)

            // 카카오톡 ID
            var keyHash = activity?.let { Utility.getKeyHash(it) }
            if (keyHash != null) {
                Log.d("KEY_HASH", keyHash)
            }

            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if (error != null) {
                    Log.d("CertiFragment_KAKAOID", "토큰 정보 보기 실패")
                }
                else if (tokenInfo != null) {
                    Log.d("CertiFragment_KAKAOID", "토큰 정보 보기 성공" +
                            "\n회원번호: ${tokenInfo.id}")
                    kakaoId = tokenInfo.id

                    // 마지막에 데이터 로드
                    load(serverDate)
                }
            }
        }

//        load(serverDate)

        return view
    }

    class SundayDecorator : DayViewDecorator { //일요일 데코
        private val calendar = Calendar.getInstance()
        override fun shouldDecorate(day: CalendarDay): Boolean {
            day.copyTo(calendar)
            val weekDay = calendar[Calendar.DAY_OF_WEEK]
            return weekDay == Calendar.SUNDAY
        }

        override fun decorate(view: DayViewFacade) {
//            view.addSpan(ForegroundColorSpan(Color.RED))
        }
    }

    class SaturdayDecorator : DayViewDecorator { //토요일 데코
        private val calendar = Calendar.getInstance()
        override fun shouldDecorate(day: CalendarDay): Boolean {
            day.copyTo(calendar)
            val weekDay = calendar[Calendar.DAY_OF_WEEK]
            return weekDay == Calendar.SATURDAY
        }

        override fun decorate(view: DayViewFacade) {
        }
    }

    class OneDayDecorator(context: MaterialCalendarView) : DayViewDecorator { //오늘 날짜에 표시
        private var date: CalendarDay?
        val drawable: Drawable = context.resources.getDrawable(R.drawable.button_circle)


        override fun shouldDecorate(day: CalendarDay): Boolean { //date가 day오늘 날짜랑 같은지 비교
            return date != null && day == date
        }

        override fun decorate(view: DayViewFacade) {
//            view.addSpan(StyleSpan(Typeface.BOLD))
//            view.addSpan(RelativeSizeSpan(1.4f))
        }

        /**
         * We're changing the internals, so make sure to call [MaterialCalendarView.invalidateDecorators]
         */
        fun setDate(date: Date?) {
            this.date = CalendarDay.from(date)
        }

        init {
            date = CalendarDay.today()
        }
    }

    class EventDecorator(private val color: Int, dates: Collection<CalendarDay>) : DayViewDecorator {
        private val dates: HashSet<CalendarDay> = HashSet(dates)
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return dates.contains(day)
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(DotSpan(8F, color))
        }

    }
    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        uploadBtn1.setOnClickListener {
            val intent = Intent(activity, CertifTabUpload1Activity::class.java)
            startActivity(intent)
        }
        val currentTime = Calendar.getInstance().time
        val dayFormat = SimpleDateFormat("d", Locale.getDefault())
        val monthFormat = SimpleDateFormat("M", Locale.getDefault())
        val weekdayFormat = SimpleDateFormat("EE", Locale.getDefault())

        val month = monthFormat.format(currentTime)
        val day = dayFormat.format(currentTime)
        val weekDay = weekdayFormat.format(currentTime)

        //오늘 날짜 가져오기
        calendarText.text = month + "월 " + day + "일 " + weekDay + "요일"

        viewManager = GridLayoutManager(this.context, 2)
        viewAdapter = CertifDateAdapter(certiList, context)
        recyclerView = certifRecycler.apply {
            setHasFixedSize(true)
            // use a linear layout manager
            layoutManager = viewManager
            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    private fun load(serverDate: String){
        //Callback 등록하여 통신 요청
        val call: Call<ResponseCertiTab> =
            GroupRetrofitServiceImpl.service_ct_tab.requestList(
                kakaoId = kakaoId,
                date = serverDate //처음 date에 오늘 날짜
            )
        Log.d("changeServerDate", serverDate)
        call.enqueue(object : Callback<ResponseCertiTab> {
            override fun onFailure(call: Call<ResponseCertiTab>, t: Throwable) {
                // 통신 실패 로직
            }

            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ResponseCertiTab>,
                response: Response<ResponseCertiTab>
            ) {
                response.takeIf { it.isSuccessful }
                    ?.body()
                    ?.let { it ->
                        // do something
                        data = response.body()

                        Log.d("CertifTabFragment", data.toString())

                        //인증한 adapter에 Member 데이터 넣기
                        setCertifAdapter(it.data)

                    } ?: showError(response.errorBody())
            }
        })
    }

    private fun showError(error: ResponseBody?) {
        val e = error ?: return
        val ob = JSONObject(e.string())
        Toast.makeText(context, ob.getString("message"), Toast.LENGTH_SHORT).show()
    }

    private fun setCertifAdapter(certiList: List<ResponseCertiTab.Data>) {
        val mAdapter = CertifDateAdapter(certiList, context)
        certifRecycler.adapter = mAdapter
        mAdapter.itemClick = object: CertifDateAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                val intent = Intent(activity, CertifTabDetailActivity::class.java)
                //인증탭의 인증 이미지의 id 넘겨주기
                intent.putExtra("certiImgId", certiList[position].id)
                startActivity(intent)
            }
        }
        mAdapter.notifyDataSetChanged()
        certifRecycler.setHasFixedSize(true)
    }

    private fun dateDataLoad(){

    }
}