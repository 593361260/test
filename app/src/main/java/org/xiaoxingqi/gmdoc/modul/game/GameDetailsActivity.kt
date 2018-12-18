package org.xiaoxingqi.gmdoc.modul.game

import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.BitmapImageViewTarget
import kotlinx.android.synthetic.main.activity_game_details.*
import kotlinx.android.synthetic.main.game_head.view.*
import org.xiaoxingqi.gmdoc.R
import org.xiaoxingqi.gmdoc.core.BaseActivity
import org.xiaoxingqi.gmdoc.core.adapter.BaseAdapterHelper
import org.xiaoxingqi.gmdoc.core.adapter.QuickAdapter
import org.xiaoxingqi.gmdoc.entity.BaseSimpleData
import org.xiaoxingqi.gmdoc.entity.game.GameDetailsData
import org.xiaoxingqi.gmdoc.entity.home.HomeUserShareData
import org.xiaoxingqi.gmdoc.impl.game.GameDetailCallBack
import org.xiaoxingqi.gmdoc.parsent.game.GameDetailPersent
import org.xiaoxingqi.gmdoc.tools.AppTools
import org.xiaoxingqi.gmdoc.tools.FastBlur

class GameDetailsActivity : BaseActivity<GameDetailPersent>() {
    private lateinit var adapter: QuickAdapter<HomeUserShareData.ContributeBean>
    private lateinit var headView: View
    private lateinit var gameId: String
    private val mData by lazy {
        ArrayList<HomeUserShareData.ContributeBean>()
    }

    override fun createPresent(): GameDetailPersent {
        return GameDetailPersent(this, object : GameDetailCallBack {
            override fun gameDetails(data: GameDetailsData?) {
                Glide.with(this@GameDetailsActivity)
                        .load(data?.game?.cover)
                        .asBitmap()
                        .into(object : BitmapImageViewTarget(headView.iv_Game_Logo) {
                            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                                super.onResourceReady(resource, glideAnimation)
                                headView.iv_Bluer_Bg.setImageBitmap(FastBlur().fastblur(resource, 60, headView.iv_Bluer_Bg))
                            }
                        })
                headView.expendView.setTvShowText(data!!.game.introduce + data.game.introduce_2)
                headView.tv_GameTitle.text = data.game.game_name
                if (!"ios".equals(data.game.platform, ignoreCase = true)) {
                    headView.tv_GameExtrl.text = data.game.platform + " | " + data.game.developer + " | " + data.game.type + " | " + data.game.sale_time
                } else {
                    headView.tv_GameExtrl.text = data.game.platform + " | " + data.game.developer + " | " + data.game.type
                }
                headView.scoreView_Community.setScore(data.all.score)
                headView.scoreView_Follow.setScore(data.my.score)
                headView.linear_img_Details.removeAllViews()
                tv_Game_Name.text = data.game.game_name

                var descData: ArrayList<BaseSimpleData>? = null
                /**
                 * 组装视屏图片的集合
                 */
                data.video?.let {
                    if (it.size > 0) {
                        descData = it
                    }
                }
                data.pic?.let {
                    if (it.size > 0) {
                        if (null == descData) {
                            descData = it
                        } else {
                            descData?.addAll(it)
                        }
                    }
                }
                val width = ((AppTools.getWindowsWidth(this@GameDetailsActivity) - AppTools.dp2px(this@GameDetailsActivity, 50)) * 1f / 3.1f + 0.5f).toInt()
                descData?.let {
                    for (index in 0 until it.size) {
                        val params = LinearLayout.LayoutParams(width, width)
                        if (index > 4) {
                            break
                        }
                        if (it.size == 1) {
                            params.setMargins(AppTools.dp2px(this@GameDetailsActivity, 14), 0, AppTools.dp2px(this@GameDetailsActivity, 12), 0)
                        } else {
                            if (index == 0) {
                                params.setMargins(AppTools.dp2px(this@GameDetailsActivity, 14), 0, 0, 0)
                            } else if (index == it.size - 1) {
                                if (index == 4) {
                                    params.setMargins(AppTools.dp2px(this@GameDetailsActivity, 12), 0, AppTools.dp2px(this@GameDetailsActivity, 12), 0)
                                } else {
                                    params.setMargins(AppTools.dp2px(this@GameDetailsActivity, 12), 0, AppTools.dp2px(this@GameDetailsActivity, 12), 0)
                                }
                            } else {
                                params.setMargins(AppTools.dp2px(this@GameDetailsActivity, 12), 0, 0, 0)
                            }
                        }
                        val view = View.inflate(this@GameDetailsActivity, R.layout.layout_game_details_desc_img, null)
                        val isVeedio = view.findViewById<View>(R.id.viewIsVedio)
                        if (TextUtils.isEmpty(it[index].url) || !it[index].url.contains(".mp4")) {
                            isVeedio.visibility = View.GONE
                        } else {
                            isVeedio.visibility = View.VISIBLE
                        }
                        val mIvGameDesc = view.findViewById<ImageView>(R.id.iv_game_img)
                        var url = if (it[index].pic.contains("?")) {
                            it[index].pic + "&imageMogr2/auto-orient/thumbnail/!200x200r"
                        } else {
                            it[index].pic + "?imageMogr2/auto-orient/thumbnail/!200x200r"
                        }
                        Glide.with(this@GameDetailsActivity)
                                .load(url)
                                .into(mIvGameDesc)
                        view.layoutParams = params
                        headView.linear_img_Details.addView(view)
                        mIvGameDesc.setOnClickListener { v ->
                            //                            startActivity(Intent(this@GameDetailsActivity, ImageVedioDetailsActivity::class.java)
//                                    .putExtra("data", descData)
//                                    .putExtra("current", finalA)
//                                    .putExtra("title", mData.getGame().getGame_name() +
//                                            " | " + mData.getGame().getPlatform()
//                                            + mData.getGame().getVer_name()))
                        }
                    }
                }
                val params = LinearLayout.LayoutParams(width, width)
                if (!(descData != null && descData?.size != 0)) {
                    params.setMargins(AppTools.dp2px(this@GameDetailsActivity, 14), 0, 0, 0)
                } else params.setMargins(0, 0, AppTools.dp2px(this@GameDetailsActivity, 14), 0)
                if (descData == null || descData?.size!! < 5) {
                    val iv = ImageView(this@GameDetailsActivity)
                    iv.setImageResource(R.drawable.btn_post_pic)
                    headView.linear_img_Details.addView(iv, params)
                    iv.setOnClickListener { v ->
                        /*if (AppTools.isLogin(this)) {
                            startActivity(Intent(this, Act_AddShare::class.java).putExtra("gameId", mGameId))
                        } else {
                            AppTools.startAct(this)
                        }*/
                    }
                } else {
                    val view = View.inflate(this@GameDetailsActivity, R.layout.layout_game_desc_find_all, null)
                    val mTvCount = view.findViewById<TextView>(R.id.tv_AllCount)
                    mTvCount.text = "${data.all_pic_num}张"
                    headView.linear_img_Details.addView(view, params)
//                    view.setOnClickListener { v -> startActivity(Intent(this@GameDetailsActivity, GameImgListActivity::class.java).putExtra("gameId", gameId)) }
                }
            }

            override fun gameDynamic(data: HomeUserShareData?) {
                for (bean in data?.data?.data!!) {
                    mData.add(bean)
                    adapter.notifyItemInserted(adapter.itemCount - 1)
                }
            }
        })
    }

    override fun setContent() {
        setContent(R.layout.activity_game_details)
    }

    override fun initView() {
        gameRecycler.layoutManager = LinearLayoutManager(this)
        headView = View.inflate(this, R.layout.game_head, null)
        val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        headView.layoutParams = params
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowTitleEnabled(false)
        }
    }

    override fun initData() {
        window.decorView.overlay
        gameId = intent.getStringExtra("gameId")
        adapter = object : QuickAdapter<HomeUserShareData.ContributeBean>(this, R.layout.item_dynamic, mData, headView) {
            override fun convert(helper: BaseAdapterHelper?, item: HomeUserShareData.ContributeBean?) {


            }
        }
        gameRecycler.adapter = adapter
        /**
         * 獲取游戲詳情
         */
        persent?.getGameDetail(gameId)
        persent?.getAllDynamic(gameId, "3", 0)
    }

    private var allLength = 0
    private var commentLocation = 500f
    private var bowenLocation = 1080f
    override fun initEvent() {
        gameRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                allLength += dy
                if (allLength <= AppTools.dp2px(this@GameDetailsActivity, 238)) {
                    appbar.setBackgroundColor(Color.argb((allLength / AppTools.dp2px(this@GameDetailsActivity, 238).toFloat() * 255 + 0.5f).toInt(), 0, 0, 0))
                } else {
                    appbar.setBackgroundColor(Color.BLACK)
                }
                if (allLength + AppTools.dp2px(this@GameDetailsActivity, 80) >= commentLocation) {
                    commentTitle.visibility = View.VISIBLE
                } else {
                    commentTitle.visibility = View.GONE
                }
                val dy = allLength + AppTools.dp2px(this@GameDetailsActivity, 80) + AppTools.dp2px(this@GameDetailsActivity, 38)
                if (dy >= bowenLocation) {
                    var alpha = (-(bowenLocation - dy) / AppTools.dp2px(this@GameDetailsActivity, 38).toFloat())
                    if (alpha >= 1) {
                        alpha = 1f
                    }
                    val layoutComment = commentTitle.layoutParams
                    layoutComment.height = (AppTools.dp2px(this@GameDetailsActivity, 38) * (1 - alpha) + 0.5f).toInt()
                    commentTitle.layoutParams = layoutComment

                    val params = frameBowen.layoutParams as RelativeLayout.LayoutParams
                    params.setMargins(0, (AppTools.dp2px(this@GameDetailsActivity, 38) * (1 - alpha) + 0.5f).toInt(), 0, 0)
                    params.height = AppTools.dp2px(this@GameDetailsActivity, 38)
                    frameBowen.layoutParams = params
                } else {
                    val layoutComment = commentTitle.layoutParams
                    layoutComment.height = AppTools.dp2px(this@GameDetailsActivity, 38)
                    commentTitle.layoutParams = layoutComment

                    val params = frameBowen.layoutParams as RelativeLayout.LayoutParams
                    params.setMargins(0, AppTools.dp2px(this@GameDetailsActivity, 38), 0, 0)
                    params.height = AppTools.dp2px(this@GameDetailsActivity, 38)
                    frameBowen.layoutParams = params
                }
            }
        })
        headView.viewTreeObserver.addOnGlobalLayoutListener {
            commentLocation = headView.tabCommentTitle.y
            bowenLocation = headView.tabTitleDyncmia.y
        }
    }

    override fun request() {

    }
}