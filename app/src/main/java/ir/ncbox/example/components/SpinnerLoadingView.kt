package ir.ncbox.example.components

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import android.widget.ImageView
import ir.ncbox.example.R

class SpinnerLoadingView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    init {
        setImageResource(R.drawable.spinner_large)
        val rotationAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate)
        startAnimation(rotationAnim)
    }


}