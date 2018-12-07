package ir.ncbox.libarary.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper

class SwipeToDismiss(/*
     * Context instance used for access resources.
     */
        private val context: Context, swipeDirs: Int) : ItemTouchHelper.SimpleCallback(0, swipeDirs) {

    /*
     * Callback for left and right swipe.
     */
    private var swipetoDismissCallBack: SwipetoDismissCallBack? = null

    private val paintLeft = Paint()
    private val paintRight = Paint()
    private val paintNoAction = Paint()

    private var bitmapLeft: Bitmap? = null
    private var bitmapRight: Bitmap? = null

    private val MARGIN = 50f
    private val THRESHOLD = 0.3f
    private val ICON_SIZE = 40f

    private val SWIPE_LEFT = 8

    var swipeLeftBackColor: Int? = null
    var swipeRightBackColor: Int? = null
    var leftImg: Int? = null
    var rightImg: Int? = null

    init {

        /*
         * Default colors
         */
        paintLeft.color = Color.BLUE
        paintRight.color = Color.CYAN
        paintNoAction.color = Color.TRANSPARENT
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        return false
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                             dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

            try {
                val itemView = viewHolder.itemView


                val threshold = itemView.width * THRESHOLD

                val moraThanThreshold = Math.abs(dX) > threshold

                if (dX > 0) {
                    val height = (itemView.height / 2 - bitmapLeft!!.height / 2).toFloat()

                    c.drawRect(itemView.left.toFloat(), itemView.top.toFloat(), dX,
                            itemView.bottom.toFloat(), if (!moraThanThreshold) paintNoAction else paintLeft)

                    if (bitmapLeft != null) {
                        c.drawBitmap(bitmapLeft!!, MARGIN, itemView.top.toFloat() + height, null)
                    }

                } else if (dX < 0) {
                    val height = (itemView.height / 2 - bitmapRight!!.height / 2).toFloat()
                    val bitmapWidth = bitmapRight!!.width.toFloat()

                    c.drawRect(itemView.right.toFloat() + dX, itemView.top.toFloat(),
                            itemView.right.toFloat(), itemView.bottom.toFloat(), if (!moraThanThreshold) paintNoAction else paintLeft)

                    if (bitmapRight != null) {
                        c.drawBitmap(bitmapRight!!, itemView.right.toFloat() - bitmapWidth - MARGIN,
                                itemView.top.toFloat() + height, null)
                    }
                }
            } catch (e: NullPointerException) {
                Logger.i("Error on swipe: $e")
            }

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    private fun resize(bitmap: Bitmap): Bitmap {
        val maxHeight = dpToPx(context, ICON_SIZE)
        val maxWidth = dpToPx(context, ICON_SIZE)
        val scale = Math.min(maxHeight / bitmap.width, maxWidth / bitmap.height)

        val matrix = Matrix()
        matrix.postScale(scale, scale)

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (swipetoDismissCallBack != null) {
            if (direction == SWIPE_LEFT) {
                swipetoDismissCallBack!!.onSwipedToRight(viewHolder)
            } else {
                swipetoDismissCallBack!!.onSwipedToLeft(viewHolder)
            }
        }
    }



    fun setSwipetoDismissCallBack(swipetoDismissCallBack: SwipetoDismissCallBack) {
        this.swipetoDismissCallBack = swipetoDismissCallBack
    }


    fun requestInit() {
        if (swipeRightBackColor != null) {
            paintRight.color = swipeRightBackColor as Int
            paintNoAction.color = swipeRightBackColor as Int
        }

        if (rightImg != null) {
            bitmapRight = resize(drawableToBitmap(getDrawable(rightImg as Int)))
        }

        if (leftImg != null) {
            bitmapLeft = resize(drawableToBitmap(getDrawable(leftImg as Int)))
        }

        if (swipeLeftBackColor != null) {
            paintLeft.color = swipeLeftBackColor as Int
            paintNoAction.color = swipeLeftBackColor as Int
        }


    }


    private fun getDrawable(id: Int): Drawable? {
        return ContextCompat.getDrawable(context, id)
    }

    private fun dpToPx(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder?): Float {
        return THRESHOLD
    }

    interface SwipetoDismissCallBack {
        fun onSwipedToRight(viewHolder: RecyclerView.ViewHolder)

        fun onSwipedToLeft(viewHolder: RecyclerView.ViewHolder)
    }

    private fun drawableToBitmap(drawable: Drawable?): Bitmap {

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }
}
