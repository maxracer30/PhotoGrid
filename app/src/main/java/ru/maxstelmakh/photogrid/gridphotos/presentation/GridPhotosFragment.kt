package ru.maxstelmakh.photogrid.gridphotos.presentation

import android.os.Bundle
import android.view.View
import ru.maxstelmakh.photogrid.utils.ViewBindingFragment
import ru.maxstelmakh.photogrid.databinding.FragmentGridPhotosBinding

class GridPhotosFragment : ViewBindingFragment<FragmentGridPhotosBinding>() {
    override fun getVB() = FragmentGridPhotosBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //todo
    }
}