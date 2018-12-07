package ir.ncbox.libarary

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.LayoutAnimationController
import android.widget.LinearLayout
import ir.ncbox.libarary.adapter.AdvanceRecyclerViewAdapter
import ir.ncbox.libarary.animations.Animations
import ir.ncbox.libarary.enums.ItemAnimations
import ir.ncbox.libarary.liseners.OnRefreshListener
import ir.ncbox.libarary.liseners.OnScrollEndListener
import ir.ncbox.libarary.util.Logger
import ir.ncbox.libarary.util.SwipeToDismiss
import kotlinx.android.synthetic.main.advance_recycler_view.view.*
import java.lang.Exception
import java.lang.RuntimeException


class AdvanceRecyclerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: Adapter<*>? = null

    /*inflate layout*/
    var moduleView: View = LayoutInflater.from(context)
            .inflate(R.layout.advance_recycler_view, this, true)

    /*fields*/
    var scrollEndListener: OnScrollEndListener? = null
    var onRefreshListener: OnRefreshListener? = null

    private var pastVisibleItems: Int = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: Int = 0
    private var isLoading: Boolean = false
    private var isEnableEndless: Boolean = true
    private var previousItemCount: Int = 0
    private var visibleThreshold: Int = 0
    private var footerViewLayout: Int? = null
    private var footerView: View? = null
    private var contentMessageView: View? = null
    private var contentLoadingView: View? = null
    private var toRightSwipeIcon: Int? = null
    private var toLeftSwipeIcon: Int? = null
    private var toRightSwipeBackColor: Int? = null
    private var toLeftSwipeBackColor: Int? = null

    init {


        /* scroll listener*/
        moduleView.rv_advance_recycler_view_lib.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {

                if (dy > 0)
                //check for scroll down
                {
                    // linearLayouts
                    if (moduleView.rv_advance_recycler_view_lib.layoutManager is LinearLayoutManager) {

                        visibleItemCount = moduleView.rv_advance_recycler_view_lib.layoutManager.childCount
                        totalItemCount = moduleView.rv_advance_recycler_view_lib.layoutManager.itemCount
                        pastVisibleItems = (moduleView.rv_advance_recycler_view_lib.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        if (visibleItemCount + pastVisibleItems >= totalItemCount - 1) {
                            if (isEnableEndless) {

                                if (scrollEndListener != null) {
                                    scrollEndListener!!.onEndPage()
                                }


                            }
                        }

                    }
                }


                // grid Layout Manager
                if (moduleView.rv_advance_recycler_view_lib.layoutManager is GridLayoutManager) {

                    val layoutManager = moduleView.rv_advance_recycler_view_lib
                            .layoutManager as GridLayoutManager

                    val lastItem = layoutManager.findLastCompletelyVisibleItemPosition()
                    val currentTotalCount = layoutManager.itemCount

                    if (currentTotalCount <= lastItem + visibleThreshold) {
                        if (isEnableEndless) {

                            if (previousItemCount == 0)
                                previousItemCount = currentTotalCount

                            if (previousItemCount != 0 && (previousItemCount == currentTotalCount))
                                return
                            if (scrollEndListener != null) {
                                scrollEndListener!!.onEndPage()
                            }

                        }

                    }
                }


            }
        })

        /*End scroll listener*/

        /*refresh layout*/

        moduleView.srl_advance_recycler_view_lib.setColorSchemeColors(ContextCompat.getColor(getContext(), android.R.color.black))
        moduleView.srl_advance_recycler_view_lib.setOnRefreshListener {
            if (onRefreshListener != null) {
                onRefreshListener!!.onRefresh()
            }
        }

        /*End refresh layout*/


    }

    /*get RecyclerView*/
    fun getBase() = moduleView.rv_advance_recycler_view_lib

    /*
    * set layout manager
     */
    fun setLayoutManager(layoutManager: LinearLayoutManager) {
        this.layoutManager = layoutManager
        getBase().layoutManager = layoutManager

    }

    /*
    * set adapter
    * */

    fun setAdapter(adapter: Adapter<*>) {
        this.adapter = adapter
        getBase().adapter = adapter
    }

    fun setAdapter(adapter: AdvanceRecyclerViewAdapter<*, *>) {
        this.adapter = adapter
        getBase().adapter = adapter
    }

    fun getAdapter() = adapter
    fun getLayoutManager() = adapter

    /*
    * enable/disable refreshing
    *
    * */
    fun setEnableRefreshing(isEnable: Boolean) {
        // must be edit
        moduleView.srl_advance_recycler_view_lib.isEnabled = isEnable
        if (!isEnable){
            moduleView.srl_advance_recycler_view_lib.setDistanceToTriggerSync(999999)
        }
    }

    /*
    *
    * cancel refreshing
    * */
    fun cancelRefreshing() {
        moduleView.srl_advance_recycler_view_lib.isRefreshing = false
    }

    /*
    * set refresh colors
    *
    * */
    fun setRefreshColorScheme(@ColorInt color: Int) {
        moduleView.srl_advance_recycler_view_lib.setColorSchemeColors(color)
    }

    /*
   * set background refresh colors
   *
   * */
    fun setBackgrounRefreshColorScheme(@ColorInt color: Int) {
        moduleView.srl_advance_recycler_view_lib.setProgressBackgroundColorSchemeColor(color)
    }

    /*
    * pause endless listener
    * */
    fun pauseEndlessListener() {
        isEnableEndless = false
    }

    /*
    * start endless listener
    * */
    fun startEndlessListener() {
        isEnableEndless = true
    }

    /*
    * add footer layout res
    * */
    fun setFooterLoadingView(@LayoutRes layoutRes: Int) {
        try {

            val view = LayoutInflater.from(context).inflate(layoutRes, getBase(), false)
            footerView = view

        } catch (e: Exception) {
            Logger.e("Error on inflate Footer: $e")
        }
    }

    /*
    *
    * add footer layout view
    * */
    fun setFooterLoadingView(view: View) {
        footerView = view
    }

    /*
    * show footer loading
    * */
    fun showFooterLoading() {
        if (getBase().adapter is AdvanceRecyclerViewAdapter<*, *>) {
            if (footerView != null) {
                val adapter = getBase().adapter as AdvanceRecyclerViewAdapter<*, *>
                adapter.setFooterView(footerView)
            }
        } else {
            throw RuntimeException("first i'm sorry for crash :) , But if you want use Footer Loading Option " +
                    "your adapter must be extends 'AdvanceRecyclerViewAdapter'")
        }
    }


    /*
    * show footer loading
    * */
    fun cancelFooterLoading() {
        if (getBase().adapter is AdvanceRecyclerViewAdapter<*, *>) {
            if (footerView != null) {
                val adapter = getBase().adapter as AdvanceRecyclerViewAdapter<*, *>
                adapter.removeFooter()
            }
        }
    }

    /*
    * set item animator
    * */

    fun setItemAnimation(animation: ItemAnimations) {

        when (animation) {

            ItemAnimations.FADE_IN_AND_FROM_RIGHT -> {
                getBase().layoutAnimation = LayoutAnimationController(Animations().fadeInAndFromRight())
            }

            else -> {
                getBase().itemAnimator = null
            }
        }

    }


    /*
    * set set NestedScrolling
    */
    override fun setNestedScrollingEnabled(isEnable: Boolean) {
        getBase().isNestedScrollingEnabled = isEnable
    }

    /*
    * show content message
    * */

    fun showContentMessage(title: String, @DrawableRes logo: Int? = null) {
        getBase().visibility = View.GONE
        moduleView.ll_advance_recycler_view_content_loading_lib.visibility = View.GONE
        moduleView.ll_advance_recycler_view_message_content_lib.visibility = View.VISIBLE
        moduleView.tv_advance_recycler_view_message_content_lib.text = title
        if (logo != null) {
            moduleView.iv_advance_recycler_view_message_content_lib.setImageResource(logo)
        }
    }

    fun showContentMessage(view: View) {
        moduleView.ll_advance_recycler_view_message_content_lib.removeAllViews()
        moduleView.ll_advance_recycler_view_message_content_lib.addView(view)
        contentMessageView = moduleView.ll_advance_recycler_view_message_content_lib

        getBase().visibility = View.GONE
        moduleView.ll_advance_recycler_view_content_loading_lib.visibility = View.GONE
        moduleView.ll_advance_recycler_view_message_content_lib.visibility = View.VISIBLE
    }

    fun showContentMessage(@LayoutRes layoutRes: Int) {
        try {

            moduleView.ll_advance_recycler_view_message_content_lib.removeAllViews()
            val view = LayoutInflater.from(context).inflate(layoutRes, moduleView.ll_advance_recycler_view_message_content_lib, true)
            contentMessageView = view

            getBase().visibility = View.GONE
            moduleView.ll_advance_recycler_view_content_loading_lib.visibility = View.GONE
            moduleView.ll_advance_recycler_view_message_content_lib.visibility = View.VISIBLE
        } catch (e: Exception) {
            Logger.e("Error on inflate content message layout")
        }
    }

    fun getContentMessageView() = contentMessageView

    /*
    * show list
    * */

    fun showList() {
        getBase().visibility = View.VISIBLE
        moduleView.ll_advance_recycler_view_message_content_lib.visibility = View.GONE
        moduleView.ll_advance_recycler_view_content_loading_lib.visibility = View.GONE
    }

    /*
    * show content loading
    * */

    fun showContentLoading() {
        moduleView.ll_advance_recycler_view_content_loading_lib.visibility = View.VISIBLE
        getBase().visibility = View.GONE
        moduleView.ll_advance_recycler_view_message_content_lib.visibility = View.GONE
    }

    fun showContentLoading(view: View) {
        moduleView.ll_advance_recycler_view_content_loading_lib.removeAllViews()
        moduleView.ll_advance_recycler_view_content_loading_lib.addView(view)
        contentLoadingView = moduleView.ll_advance_recycler_view_content_loading_lib

        moduleView.ll_advance_recycler_view_content_loading_lib.visibility = View.VISIBLE
        getBase().visibility = View.GONE
        moduleView.ll_advance_recycler_view_message_content_lib.visibility = View.GONE
    }

    fun showContentLoading(@LayoutRes layoutRes: Int) {
        moduleView.ll_advance_recycler_view_content_loading_lib.removeAllViews()
        try {
            contentLoadingView = LayoutInflater.from(context).inflate(layoutRes, moduleView.ll_advance_recycler_view_content_loading_lib, true)
            moduleView.ll_advance_recycler_view_content_loading_lib.visibility = View.VISIBLE
            getBase().visibility = View.GONE
            moduleView.ll_advance_recycler_view_message_content_lib.visibility = View.GONE

        } catch (e: Exception) {
            Logger.e("Error on inflate content loading layout: $e")
        }
    }

    fun getContentLoadingView() = contentLoadingView

    /*
    *
    *
    * swipe to dismiss
    *
    * recommended for shortcut option like delete item
    *
    *
    * */

    fun setToRightSwipeItem(@DrawableRes icon: Int, @ColorInt backgroundColor: Int,
                            callback: SwipeToDismiss.SwipetoDismissCallBack) {

        val swipeRight = SwipeToDismiss(context, ItemTouchHelper.RIGHT)
        swipeRight.swipeRightBackColor = backgroundColor
        swipeRight.swipeLeftBackColor = backgroundColor
        swipeRight.rightImg = icon
        swipeRight.leftImg = icon


        swipeRight.setSwipetoDismissCallBack(callback)
        swipeRight.requestInit()

        val touchHelper = ItemTouchHelper(swipeRight)
        touchHelper.attachToRecyclerView(getBase())

        toRightSwipeIcon = icon
        toRightSwipeBackColor = backgroundColor

    }

    fun setToLeftSwipeItem(@DrawableRes icon: Int, @ColorInt backgroundColor: Int,
                           callback: SwipeToDismiss.SwipetoDismissCallBack) {

        val leftSwipe = SwipeToDismiss(context, ItemTouchHelper.LEFT)
        leftSwipe.swipeLeftBackColor = backgroundColor
        leftSwipe.swipeRightBackColor = backgroundColor
        leftSwipe.leftImg = icon
        leftSwipe.rightImg = icon
        leftSwipe.setSwipetoDismissCallBack(callback)
        leftSwipe.requestInit()

        val touchHelper = ItemTouchHelper(leftSwipe)
        touchHelper.attachToRecyclerView(getBase())

        toLeftSwipeIcon = icon
        toLeftSwipeBackColor = backgroundColor

    }


}

