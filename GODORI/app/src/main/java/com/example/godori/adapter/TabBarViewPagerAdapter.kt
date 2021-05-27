package com.example.godori.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.godori.fragment.CertifTabFragment
import com.example.godori.fragment.GroupAfterTabFragment
import com.example.godori.fragment.GroupTabFragment
import com.example.godori.fragment.MyInfoTabFragment
import java.util.*

class TabBarViewPagerAdapter(fm: FragmentManager, group: Int) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var group = group
    // 뷰페이지의 어댑터는 FragmentManager를 필요로 한다
    // ViewPagerAdapter의 역할을 하기 위해 FragmentStatePagerAdapter 상속
    // FragmentStatePagerAdapter vs FragmentPagerAdapter
    // => 전자는 보여지는 화면 기준 양 옆 프래그먼트를 제외한 나머지를 완전히 파괴하며 메모리 누수 관리에 효과적이다
    // 후자는 프래그먼트의 인스턴스를 완전히 파괴하지 않고 onDestroyView()만 호출한다. 프래그먼트 개수가 고정적일 때 효과적

    override fun getItem(position: Int): Fragment {
        return if (group !=0) {
            when (position) {
                0 -> GroupAfterTabFragment()
                1 -> CertifTabFragment()
                2 -> MyInfoTabFragment()
                else -> throw IllegalStateException("Unexpected position $position")
            }
        } else {
            when (position) {
                0 -> GroupTabFragment()
                1 -> CertifTabFragment()
                2 -> MyInfoTabFragment()
                else -> throw IllegalStateException("Unexpected position $position")
            }
        }
    }

    override fun getCount(): Int = 3
    // 뷰페이저어댑터는 위 두가지 메소드를 반드시 오버라이드 해야한다
    // getItem() -> 리스트에 있는 프래그먼트의 인스턴스를 새 페이지로 제공하는 함수
    // getCount() -> 어댑터에서 만들 페이지 수를 반환하는 함수

}

