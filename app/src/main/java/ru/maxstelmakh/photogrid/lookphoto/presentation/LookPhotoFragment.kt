package ru.maxstelmakh.photogrid.lookphoto.presentation

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.navigation.fragment.navArgs
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.transition.MaterialContainerTransform
import ru.maxstelmakh.photogrid.R
import ru.maxstelmakh.photogrid.databinding.FragmentLookPhotoBinding
import ru.maxstelmakh.photogrid.gridphotos.data.model.Image
import ru.maxstelmakh.photogrid.utils.ViewBindingFragment
import ru.maxstelmakh.photogrid.utils.fastAnimLoad

class LookPhotoFragment : ViewBindingFragment<FragmentLookPhotoBinding>() {
    override fun getVB() = FragmentLookPhotoBinding.inflate(layoutInflater)

    var image: Image? = null
    private var imageHeight: Int = 300

    private val args: LookPhotoFragmentArgs by navArgs()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val transformation = MaterialContainerTransform()
        transformation.interpolator = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = 400
            scrimColor = Color.TRANSPARENT
        }
        sharedElementReturnTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = 200
            scrimColor = Color.TRANSPARENT
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        image = args.image
        image?.let {
            binding.root.transitionName = it.id
            binding.bigImageView.fastAnimLoad(it.urls.small)
        }

    }

    override fun onResume() {
        super.onResume()
        imageHeight = binding.bigImageView.measuredHeight

        animateImageViewHeightMatchParent()
    }

    override fun onPause() {
        super.onPause()

        animateImageViewHeightWrapContent()
    }

    private fun animateImageViewHeightMatchParent() {
        val parentHeight: Int = (binding.bigImageView.parent as View).measuredHeight

        val heightAnimator = ValueAnimator.ofInt(binding.bigImageView.height, parentHeight)
        heightAnimator.duration = 500
        heightAnimator.interpolator = DecelerateInterpolator()
        heightAnimator.addUpdateListener { animation ->
            binding.bigImageView.layoutParams.height = animation.animatedValue as Int
            binding.bigImageView.requestLayout()
        }
        heightAnimator.start()
    }

    private fun animateImageViewHeightWrapContent() {
        val heightAnimator = ValueAnimator.ofInt(binding.bigImageView.height, imageHeight)
        heightAnimator.duration = 100
        heightAnimator.interpolator = DecelerateInterpolator()
        heightAnimator.addUpdateListener { animation ->
            binding.bigImageView.layoutParams.height = animation.animatedValue as Int
            binding.bigImageView.requestLayout()
        }
        heightAnimator.start()
    }

    companion object {
        const val PHOTO_MODEL = "image"
    }
}