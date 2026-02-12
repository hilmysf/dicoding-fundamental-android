package com.hilmysf.fundamental.presentation.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.hilmysf.fundamental.R
import com.hilmysf.fundamental.databinding.ActivityBookmarkBinding
import com.hilmysf.fundamental.domain.model.Event
import com.hilmysf.fundamental.domain.model.ResultState
import com.hilmysf.fundamental.presentation.adapter.OnEventClickListener
import com.hilmysf.fundamental.presentation.adapter.VerticalEventAdapter
import com.hilmysf.fundamental.presentation.detail.DetailEventActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkActivity : AppCompatActivity(), OnEventClickListener {
    private lateinit var binding: ActivityBookmarkBinding
    private val viewModel: BookmarkViewModel by viewModels()
    private val eventAdapter: VerticalEventAdapter by lazy {
        VerticalEventAdapter(
            onEventClickListener = this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = getString(R.string.bookmark)
            setDisplayHomeAsUpEnabled(true)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupAdapter()
    }

    private fun setupAdapter() {
        binding.rvEvents.adapter = eventAdapter
        viewModel.getBookmarkedEvents()
        viewModel.events.observe(this) { result ->
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

    private fun navigateToDetail(eventId: Int) {
        val intent = Intent(this, DetailEventActivity::class.java)
        intent.putExtra(DetailEventActivity.EXTRA_EVENT_ID, eventId)
        startActivity(intent)
    }

    override fun onEventClick(event: Event) {
        navigateToDetail(
            event.id
        )
    }

    override fun onBookmarkClick(event: Event) {
        viewModel.deleteEvent(event)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}