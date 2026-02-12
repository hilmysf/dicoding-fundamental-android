package com.hilmysf.fundamental.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.hilmysf.fundamental.R
import com.hilmysf.fundamental.databinding.FragmentHomeBinding
import com.hilmysf.fundamental.domain.model.Event
import com.hilmysf.fundamental.domain.model.ResultState
import com.hilmysf.fundamental.presentation.adapter.HorizontalEventAdapter
import com.hilmysf.fundamental.presentation.adapter.OnEventClickListener
import com.hilmysf.fundamental.presentation.adapter.VerticalEventAdapter
import com.hilmysf.fundamental.presentation.bookmark.BookmarkActivity
import com.hilmysf.fundamental.presentation.detail.DetailEventActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), OnEventClickListener {
    private lateinit var navController: NavController
    private val horizontalEventAdapter: HorizontalEventAdapter by lazy {
        HorizontalEventAdapter(
            5,
            this
        )
    }
    private val verticalEventAdapter: VerticalEventAdapter by lazy { VerticalEventAdapter(5, this) }
    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        setupAdapter()
        viewModel.getUpcomingEvents()
        viewModel.getFinishedEvents()
        observeUpcomingEvents()
        observeFinishedEvents()
        onClick()
    }

    private fun onClick() {
        binding.apply {
            btnViewAllFinishedEvents.setOnClickListener {
                if (navController.currentDestination?.id == R.id.home_fragment) {
                    navController.navigate(
                        HomeFragmentDirections.actionHomeFragmentToFinishedFragment(
                        )
                    )
                }
            }
            btnViewAllUpcomingEvents.setOnClickListener {
                if (navController.currentDestination?.id == R.id.home_fragment) {
                    navController.navigate(
                        HomeFragmentDirections.actionHomeFragmentToUpcomingFragment()
                    )
                }
            }
            btnBookmarkList.setOnClickListener {
                val intent = Intent(requireActivity(), BookmarkActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showVerticalLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                rvVerticalEvents.visibility = View.GONE
                shimmerVerticalEvents.visibility = View.VISIBLE
            } else {
                shimmerVerticalEvents.visibility = View.GONE
                rvVerticalEvents.visibility = View.VISIBLE
            }
        }

    }

    private fun showHorizontalLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                rvHorizontalEvent.visibility = View.GONE
                shimmerHorizontalEvents.visibility = View.VISIBLE
            } else {
                rvHorizontalEvent.visibility = View.VISIBLE
                shimmerHorizontalEvents.visibility = View.GONE
            }
        }
    }

    private fun showVerticalError(isError: Boolean, errorMessage: String = "") {
        binding.errorStateVerticalEvents.isVisible = isError
        binding.errorStateVerticalEvents.text = errorMessage
    }

    private fun showHorizontalError(isError: Boolean, errorMessage: String = "") {
        binding.errorStateHorizontalEvents.isVisible = isError
        binding.errorStateHorizontalEvents.text = errorMessage
    }


    private fun navigateToDetail(eventId: Int) {
        val intent = Intent(requireContext(), DetailEventActivity::class.java)
        intent.putExtra(DetailEventActivity.EXTRA_EVENT_ID, eventId)
        startActivity(intent)
    }

    private fun setupAdapter() {
        binding.apply {
            rvHorizontalEvent.adapter = horizontalEventAdapter
            rvVerticalEvents.adapter = verticalEventAdapter
        }
    }

    private fun observeUpcomingEvents() {
        val eventsObserver = Observer<ResultState<List<Event>>> { result ->
            when (result) {
                is ResultState.Success -> {
                    showHorizontalLoading(false)
                    showHorizontalError(false)
                    horizontalEventAdapter.submitList(result.data)
                }

                is ResultState.Loading -> {
                    showHorizontalLoading(true)
                    showHorizontalError(false)
                }

                is ResultState.Error -> {
                    showHorizontalLoading(false)
                    showHorizontalError(true, result.error)
                }
            }
        }

        viewModel.upcomingEvents.observe(viewLifecycleOwner, eventsObserver)
    }

    private fun observeFinishedEvents() {
        val eventsObserver = Observer<ResultState<List<Event>>> { result ->
            when (result) {
                is ResultState.Success -> {
                    showVerticalLoading(false)
                    showVerticalError(false)
                    verticalEventAdapter.submitList(result.data)
                }

                is ResultState.Loading -> {
                    showVerticalLoading(true)
                    showVerticalError(false)
                }

                is ResultState.Error -> {
                    showVerticalLoading(false)
                    showVerticalError(true, result.error)
                }
            }
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner, eventsObserver)
    }

    override fun onEventClick(event: Event) {
        navigateToDetail(event.id)
    }

    override fun onBookmarkClick(event: Event) {
        viewModel.onBookmarkClick(event)
    }
}