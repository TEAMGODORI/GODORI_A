package com.example.godori

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        //카카오 SDK를 초기화
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
    }
}