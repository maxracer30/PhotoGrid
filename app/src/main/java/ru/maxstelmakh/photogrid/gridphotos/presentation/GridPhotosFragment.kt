@file:OptIn(FlowPreview::class)

package ru.maxstelmakh.photogrid.gridphotos.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isInvisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import ru.maxstelmakh.photogrid.R
import ru.maxstelmakh.photogrid.databinding.FragmentGridPhotosBinding
import ru.maxstelmakh.photogrid.gridphotos.data.model.Image
import ru.maxstelmakh.photogrid.lookphoto.presentation.LookPhotoFragment
import ru.maxstelmakh.photogrid.utils.ViewBindingFragment
import ru.maxstelmakh.photogrid.utils.adapters.itemsadapter.ItemsAdapter
import ru.maxstelmakh.photogrid.utils.adapters.loadstateadapter.DefaultLoadStateAdapter
import ru.maxstelmakh.photogrid.utils.adapters.loadstateadapter.TryAgainAction

@AndroidEntryPoint
class GridPhotosFragment : ViewBindingFragment<FragmentGridPhotosBinding>() {
    override fun getVB() = FragmentGridPhotosBinding.inflate(layoutInflater)

    private val viewModel: GridPhotosViewModel by viewModels()

    private var mainLoadStateHolder: DefaultLoadStateAdapter.Holder? = null

    private val itemsAdapter = ItemsAdapter { click ->
        val model = click.item.getItem() as Image
        val extras: FragmentNavigator.Extras = FragmentNavigatorExtras(
            click.view.root to click.view.root.transitionName
        )
        findNavController().navigate(
            LookPhotoNavDirections(model),
            extras
        )
        exitTransition = MaterialElevationScale(false).apply {
            duration = 100
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 100
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        setItems()
        setSearchInput()
        setSwipeToRefresh()
    }

    private fun setItems() {
        with(binding) {

            val tryAgainAction: TryAgainAction = { itemsAdapter.retry() }
            val footerAdapter = DefaultLoadStateAdapter(tryAgainAction)
            val concatAdapter = itemsAdapter.withLoadStateFooter(footerAdapter)

            val manager = StaggeredGridLayoutManager(
                currentSpanCount(),
                StaggeredGridLayoutManager.VERTICAL
            )

            rvGridPhotos.apply {
                adapter = concatAdapter
                layoutManager = manager
                setHasFixedSize(true)
            }

            mainLoadStateHolder = DefaultLoadStateAdapter.Holder(
                loadStateView,
                swipeRefreshLayout,
                tryAgainAction
            )

            observePhotos()
            observeLoadState()

            handleScrollingToTopWhenSearching()
            handleListVisibility()
        }
    }

    private fun setSearchInput() {
        binding.searchEditText.addTextChangedListener {
            viewModel.setSearchBy(it.toString())
        }
    }

    private fun setSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun observePhotos() {
        viewModel.viewModelScope.launch {
            viewModel.items.collect {
                itemsAdapter.submitData(it)
            }
        }
    }

    private fun observeLoadState() {
        lifecycleScope.launch {
            itemsAdapter.loadStateFlow.debounce(300).collectLatest { loadState ->
                mainLoadStateHolder?.bind(loadState.refresh)
            }
        }
    }

    private fun handleScrollingToTopWhenSearching() =
        lifecycleScope.launch {
            getRefreshLoadStateFlow()
                .scan(List<LoadState?>(2) { null }) { prev, value ->
                    prev.drop(1) + value
                }
                .collectLatest { (
                                     prevState,
                                     lastState,
                                 ) ->
                    if (prevState is LoadState.Loading &&
                        lastState is LoadState.NotLoading
                    ) {
                        binding.rvGridPhotos.scrollToPosition(0)
                    }
                }
        }

    private fun handleListVisibility() = lifecycleScope.launch {
        getRefreshLoadStateFlow()
            .scan(List<LoadState?>(3) { null }) { prev, value ->
                prev.drop(1) + value
            }
            .collectLatest { (
                                 beforePrevState,
                                 prevState,
                                 lastState,
                             ) ->
                with(
                    lastState is LoadState.Error ||
                            prevState is LoadState.Error ||
                            (beforePrevState is LoadState.Error &&
                                    prevState is LoadState.NotLoading &&
                                    lastState is LoadState.Loading)
                ) {
                    binding.rvGridPhotos.isInvisible = this
                }
            }
    }

    private fun getRefreshLoadStateFlow(): Flow<LoadState> {
        return itemsAdapter.loadStateFlow.map { it.refresh }
    }

    private fun currentSpanCount() = when (resources.configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> PORTRAIT_SPANS
        Configuration.ORIENTATION_LANDSCAPE -> LANDSCAPE_SPANS
        else -> UNDEFINED_SPANS
    }

    inner class LookPhotoNavDirections(private val image: Image) : NavDirections {
        override val actionId: Int
            get() = R.id.action_gridPhotosFragment_to_lookPhotoFragment
        override val arguments: Bundle
            get() {
                val res = Bundle()
                res.putParcelable(LookPhotoFragment.PHOTO_MODEL, image)
                return res
            }
    }

    private companion object {
        private const val PORTRAIT_SPANS = 3
        private const val LANDSCAPE_SPANS = 6
        private const val UNDEFINED_SPANS = 5
    }
}