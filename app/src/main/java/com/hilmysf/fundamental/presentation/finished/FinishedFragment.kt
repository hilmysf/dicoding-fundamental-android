package com.hilmysf.fundamental.presentation.finished

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.hilmysf.fundamental.databinding.FragmentFinishedBinding
import com.hilmysf.fundamental.domain.model.Event
import com.hilmysf.fundamental.domain.model.ResultState
import com.hilmysf.fundamental.presentation.adapter.OnEventClickListener
import com.hilmysf.fundamental.presentation.adapter.VerticalEventAdapter
import com.hilmysf.fundamental.presentation.detail.DetailEventActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinishedFragment : Fragment(), OnEventClickListener {
    private lateinit var binding: FragmentFinishedBinding
    private val viewModel: FinishediewModel by activityViewModels()
    private val eventAdapter: VerticalEventAdapter by lazy {
        VerticalEventAdapter(
            onEventClickListener = this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeFinishedEvents()
        observeSearchQuery()
    }

    private fun observeSearchQuery() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getFinishedEvents(query = query, forceLoad = true)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.getFinishedEvents(query = newText, forceLoad = true)
                return true
            }

        })
    }

    private fun setupAdapter() {
        binding.apply {
            rvEvents.adapter = eventAdapter
        }
    }

    private fun observeFinishedEvents() {
        val eventsObserver = Observer<ResultState<List<Event>>> { result ->
            when (result) {
                is ResultState.Success -> {
                    showVerticalLoading(false)
                    showVerticalError(false)
                    eventAdapter.submitList(result.data)
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

    private fun showVerticalLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                rvEvents.visibility = View.GONE
                shimmerVerticalEvents.visibility = View.VISIBLE
            } else {
                shimmerVerticalEvents.visibility = View.GONE
                rvEvents.visibility = View.VISIBLE
            }
        }
    }

    private fun showVerticalError(isError: Boolean, errorMessage: String = "") {
        binding.errorStateVerticalEvents.isVisible = isError
        binding.errorStateVerticalEvents.text = errorMessage
    }

    override fun onEventClick(event: Event) {
        val intent = Intent(requireContext(), DetailEventActivity::class.java)
        intent.putExtra(DetailEventActivity.EXTRA_EVENT_ID, event.id)
        startActivity(intent)
    }

    override fun onBookmarkClick(event: Event) {
        viewModel.onBookmarkClick(event)
    }
}