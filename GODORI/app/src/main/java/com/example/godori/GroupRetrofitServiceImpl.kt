package com.example.godori

import com.example.godori.Interface.*
import com.example.godori.data.CertiUploadImage
import com.example.godori.data.ResponseFirstLogin
import com.example.godori.fragment.GroupTabFragment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GroupRetrofitServiceImpl {
    private const val BASE_URL = "http://15.164.186.213:3000/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 그룹 - 모집중인인 그룹
    val service_gr_recruit: GroupRetrofitService = retrofit.create(GroupRetrofitService::class.java)

    // 그룹 - 그룹 생성
    val service_gr_creation: GroupCreation = retrofit.create(GroupCreation::class.java)

    // 그룹 - 가입
    val service_gr_join: GroupJoin = retrofit.create(GroupJoin::class.java)

    // 그룹 - 가입 전 취향설정 x 목록
    val service_gr_before_list: GroupBeforeList = retrofit.create(GroupBeforeList::class.java)

    // 그룹 - 그룹후 화면
    val service_gr_after_tab: GroupAfter = retrofit.create(GroupAfter::class.java)

    // 그룹 - 그룹 탈퇴
    val service_gr_exit: GroupExit = retrofit.create(GroupExit::class.java)

    // 인증 - 업로드
    val service_ct_upload: CertiUpload = retrofit.create(CertiUpload::class.java)

    // 인증 - 업로드 사진
    val service_ct_upload_image: CertiUploadImage = retrofit.create(CertiUploadImage::class.java)

    // 취향 설정
    val service_taste: TasteSetting = retrofit.create(TasteSetting::class.java)

    //그룹 - 가입후 그룹
    val service_gr_after : GroupAfterRetrofitService = retrofit.create(GroupAfterRetrofitService::class.java)

    //그룹 - 가입후 그룹
    val service_gr_search : GroupSearch = retrofit.create(GroupSearch::class.java)

    //그룹 - 가입 전 상세보기
    val service_gr__info : GroupInfoRetrofitService = retrofit.create(GroupInfoRetrofitService::class.java)

    //그룹 - 가입 후 상세보기
    val service_gr__info_after : GroupInfoAfterRetrofitService = retrofit.create(GroupInfoAfterRetrofitService::class.java)

    //인증 - 메인
    val service_ct_tab : CertifRetrofitService = retrofit.create(CertifRetrofitService::class.java)

    //인증 - 상세보기
    val service_ct_detail : CertifDetail = retrofit.create(CertifDetail::class.java)

    //인증 - 좋아요, 좋아요 취소
    val service_ct_like : CertiLike = retrofit.create(CertiLike::class.java)

    // 마이페이지
    val service_mypage : MyPage = retrofit.create(MyPage::class.java)

    // 온보딩뷰 - 사용자 정보 생성
    val service_ob_user_creation: OnBoardingaUserInfo = retrofit.create(OnBoardingaUserInfo::class.java)

    // 로그인 - 첫 로그인 판단
    val service_lg_first: LoginFirst = retrofit.create(LoginFirst::class.java)
}