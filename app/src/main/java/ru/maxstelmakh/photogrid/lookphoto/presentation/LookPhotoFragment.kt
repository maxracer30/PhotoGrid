package ru.maxstelmakh.photogrid.lookphoto.presentation

import android.os.Bundle
import android.view.View
import ru.maxstelmakh.photogrid.utils.ViewBindingFragment
import ru.maxstelmakh.photogrid.databinding.FragmentLookPhotoBinding

class LookPhotoFragment : ViewBindingFragment<FragmentLookPhotoBinding>() {
    override fun getVB() = FragmentLookPhotoBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //todo
    }
}