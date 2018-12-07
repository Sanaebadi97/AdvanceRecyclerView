package ir.ncbox.libarary.animations

import android.view.animation.*

class Animations {


    fun fadeInAndFromRight(): Animation {
        val animSet = AnimationSet(false)
        animSet.duration = 700
        animSet.interpolator = LinearInterpolator()

        val alphaAnim = AlphaAnimation(0.5f, 1.0f)
        alphaAnim.duration = 500
        alphaAnim.interpolator = LinearInterpolator()

        val translateAnimation = TranslateAnimation(Animation.RELATIVE_TO_PARENT, 80f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f)
        translateAnimation.duration = 700
        translateAnimation.interpolator = AccelerateDecelerateInterpolator()

        animSet.addAnimation(alphaAnim)
        animSet.addAnimation(translateAnimation)
        return animSet
    }

}
