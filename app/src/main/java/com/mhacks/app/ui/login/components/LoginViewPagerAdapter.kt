package com.mhacks.app.ui.login.components

/**
 * Created by jeffreychang on 5/31/17.
 */

//class LoginViewPagerAdapter constructor(val context: Context) : PagerAdapter() {
//
//    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
//        val enum = LoginViewPagerEnum.values()[position]
//        val layout = LayoutInflater.from(context)
//                .inflate(enum.view, container, false)
//        container!!.addView(layout)
//        return layout
//    }
//
//    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
//        container?.removeView(`object` as View)
//    }
//
//    override fun isViewFromObject(view: View?, `object`: Any?): Boolean = (view == `object`)
//
//    override fun getCount(): Int = LoginViewPagerEnum.values().size
//
//    enum class LoginViewPagerEnum(val view: Int) {
//        SLACK(R.layout.viewpager_slack),
//        FACEBOOK(R.layout.viewpager_facebook),
//        EMAIL(R.layout.viewpager_email)
//    }
//}