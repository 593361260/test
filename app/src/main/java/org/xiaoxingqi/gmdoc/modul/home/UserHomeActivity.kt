package org.xiaoxingqi.gmdoc.modul.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.*
import androidx.viewpager.widget.ViewPager
import android.text.TextUtils
import android.transition.ArcMotion
import android.util.TypedValue
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_user_info.*
import org.xiaoxingqi.gmdoc.R
import org.xiaoxingqi.gmdoc.core.BaseActivity
import org.xiaoxingqi.gmdoc.entity.user.UserInfoData
import org.xiaoxingqi.gmdoc.impl.IConstant
import org.xiaoxingqi.gmdoc.impl.home.UserInfoCallBack
import org.xiaoxingqi.gmdoc.modul.lifeCircle.TypeCircleFragment
import org.xiaoxingqi.gmdoc.presenter.home.HomeUserInfoPersent
import org.xiaoxingqi.gmdoc.tools.CustomChangeBounds
import org.xiaoxingqi.gmdoc.tools.PreferenceTools

/**
 * 用户个人中心界面
 */
class UserHomeActivity : BaseActivity<HomeUserInfoPersent>() {
    private var infoData: UserInfoData? = null
    private var userId: String = ""
    private val chooseType = arrayOf("", "4", "1", "2", "3")
    override fun createPresent(): HomeUserInfoPersent {
        return HomeUserInfoPersent(this, object : UserInfoCallBack() {
            @SuppressLint("SetTextI18n")
            override fun userInfo(info: UserInfoData?) {
                Glide.with(this@UserHomeActivity)
                        .asBitmap()
                        .load(info!!.data.top_image)
                        .into(iv_User_Bg)
                Glide.with(this@UserHomeActivity)
                        .load(info.data.avatar)
                        .apply(RequestOptions().error(R.mipmap.img_avatar_default))
                        .into(iv_UserLogo)


                tv_UserName.text = info.data.username
                if ("1" == info.data.role) {
                    tv_UserType.setImageResource(R.mipmap.img_user_formal)
                } else {
                    tv_UserType.setImageResource(R.mipmap.img_user_general)
                }
                if ("1" == info.data.sex) {//女
                    iv_Sex.setImageResource(R.mipmap.img_user_female)
                } else {//男img_user_male
                    iv_Sex.setImageResource(R.mipmap.img_user_male)
                }
                if (!TextUtils.isEmpty(info.data.desc)) {
                    tv_UserDesc.text = info.data.desc
                    tv_UserDesc.visibility = View.VISIBLE
                } else {
                    tv_UserDesc.visibility = View.GONE
                }
                tv_UserLoveGame.text = "最喜欢的游戏：${info.data.like_game}"
                if (!TextUtils.isEmpty(info.data.like_game)) {
                }
                if (TextUtils.isEmpty(info.data.follow_num)) {
                    tv_Follow.text = "0"
                } else {
                    tv_Follow.text = info.data.follow_num
                }

                if (null != infoData) {

                    if (userId == infoData?.data?.uid) {
                        if (info.data.fans_switch == 0) {//开启
                            if (TextUtils.isEmpty(info.data.fans_num)) {
                                tv_Fans.text = "0"
                            } else {
                                tv_Fans.text = info.data.fans_num
                            }
                        } else {//关闭
                            tv_Fans.text = "未知"
                            tv_Fans.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                        }
                    } else {
                        if (TextUtils.isEmpty(info.data.fans_num)) {
                            tv_Fans.text = "0"
                        } else {
                            tv_Fans.text = info.data.fans_num
                        }
                    }

                } else {
                    if (TextUtils.isEmpty(info.data.fans_num)) {
                        tv_Fans.text = "0"
                    } else {
                        tv_Fans.text = info.data.fans_num
                    }
                }

                if (info.data.is_black == 1) {
                    iv_follow.setImageResource(R.mipmap.btn_blacklist_selected)
                    iv_follow.isClickable = false
                } else {
                    if (0 == info.data.is_sub) {//未关注
                        iv_follow.setImageResource(R.mipmap.btn_follow)
                    } else if (1 == info.data.is_sub) {//已经关注
                        iv_follow.setImageResource(R.mipmap.btn_follow_selected)
                    } else if (2 == info.data.is_sub) {//互相关注
                        iv_follow.setImageResource(R.mipmap.btn_follow_back)
                    }
                }

            }
        })
    }

    private val fragments = arrayOfNulls<TypeCircleFragment>(5)
    override fun setContent() {
        setContent(R.layout.activity_user_info)
    }

    override fun initView() {

    }

    override fun initData() {
        intent.getStringExtra("url")?.let {
            Glide.with(this@UserHomeActivity)
                    .load(it)
                    .apply(RequestOptions().error(R.mipmap.img_avatar_default))
                    .into(iv_UserLogo)
            setMotion(iv_UserLogo)
        }
        infoData = PreferenceTools.getObj(this, IConstant.USERINFO, UserInfoData::class.java)
        intent.getStringExtra("userId")?.let {
            userId = it
        }
        if (TextUtils.isEmpty(userId)) {
            userId = ""
        }
        persent?.getUserInfo(userId)
        for (tag in chooseType.indices) {
            val frag = TypeCircleFragment()
            val bundle = Bundle()
            bundle.putString("chooseType", chooseType[tag])
            bundle.putString("userId", userId)
            frag.arguments = bundle
            fragments[tag] = frag
        }
        viewPager.adapter = DynamicAdapter(supportFragmentManager)
    }

    override fun initEvent() {
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabTitleLayout.setCurrentSelect(position)
            }
        })
        tabTitleLayout.setOnClick {
            val ofChild = tabTitleLayout.indexOfChild(it)
            viewPager.setCurrentItem(ofChild, false)
        }

    }

    override fun request() {

    }

    private inner class DynamicAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return fragments[position]!!
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }


    companion object {
        @JvmStatic
        fun start(context: Activity, url: String?, id: String?, imageView: View) {
            val intent = Intent(context, UserHomeActivity::class.java)
            intent.putExtra("userId", id)
            intent.putExtra("url", url)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                    imageView, "transition_movie_img")//与xml文件对应
            ActivityCompat.startActivity(context, intent, options.toBundle())
        }
    }


    private fun setMotion(imageView: ImageView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //定义ArcMotion
            val arcMotion = ArcMotion()
            arcMotion.minimumHorizontalAngle = 50f
            arcMotion.minimumVerticalAngle = 50f
            //插值器，控制速度
            val interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.fast_out_slow_in)
            //实例化自定义的ChangeBounds
            val changeBounds = CustomChangeBounds()
            changeBounds.pathMotion = arcMotion
            changeBounds.interpolator = interpolator
            changeBounds.addTarget(imageView)
            //将切换动画应用到当前的Activity的进入和返回
            window.sharedElementEnterTransition = changeBounds
            window.sharedElementReturnTransition = changeBounds
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragments.orEmpty()
    }
}