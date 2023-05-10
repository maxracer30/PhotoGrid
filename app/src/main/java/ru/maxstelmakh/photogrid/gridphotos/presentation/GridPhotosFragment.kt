package ru.maxstelmakh.photogrid.gridphotos.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
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

    private val itemsAdapter = ItemsAdapter { click ->
        Toast.makeText(context, click.view.id.toString(), Toast.LENGTH_SHORT).show()
        LookPhotoFragment().apply {
            arguments = Bundle().apply {
                putParcelable(
                    LookPhotoFragment.PHOTO_MODEL,
                    click.item as Image
                )
            }
        }
        // todo transition animation to lookFragment
    }

    private var mainLoadStateHolder: DefaultLoadStateAdapter.Holder? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpItems()
        setupSearchInput()
        setupSwipeToRefresh()
        //todo
    }

    private fun setUpItems() {
        with(binding) {
            val tryAgainAction: TryAgainAction = { itemsAdapter.retry() }
//            itemsAdapter.withLoadStateFooter(DefaultLoadStateAdapter(tryAgainAction))

            val manager = StaggeredGridLayoutManager(
                currentSpanCount(),
                StaggeredGridLayoutManager.VERTICAL
            )

//            manager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            // todo shimmer starting
            rvGridPhotos.apply {
                adapter = itemsAdapter
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

    private fun setupSearchInput() {
//            binding.searchEditText.addTextChangedListener {
//                viewModel.
//            }
    }

    private fun setupSwipeToRefresh() {
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
        itemsAdapter.addLoadStateListener {
            mainLoadStateHolder?.bind(it.refresh)
        }
    }

    private fun handleScrollingToTopWhenSearching() =
        lifecycleScope.launch {
            getRefreshLoadStateFlow()
                .scan(List<LoadState?>(2) { null }) { prev, value ->
                    prev.drop(1) + value
                }
                .collectLatest { (prevState, lastState) ->
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
            .collectLatest { (befPrevState, prevState, lastState) ->
                with(
                    lastState is LoadState.Error ||
                            prevState is LoadState.Error ||
                            (befPrevState is LoadState.Error &&
                                    prevState is LoadState.NotLoading &&
                                    lastState is LoadState.Loading)
                ) {
                    binding.rvGridPhotos.isInvisible = this
                }
            }
    }

    private fun getRefreshLoadStateFlow(): Flow<LoadState> {
        return itemsAdapter.loadStateFlow
            .map { it.refresh }
    }

    private fun currentSpanCount() = when (resources.configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> PORTRAIT_SPANS
        Configuration.ORIENTATION_LANDSCAPE -> LANDSCAPE_SPANS
        else -> UNDEFINED_SPANS
    }

    private companion object {
        const val PORTRAIT_SPANS = 3
        const val LANDSCAPE_SPANS = 6
        const val UNDEFINED_SPANS = 5
    }
}