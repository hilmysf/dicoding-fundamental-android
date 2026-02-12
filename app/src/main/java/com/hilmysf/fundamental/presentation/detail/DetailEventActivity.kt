package com.hilmysf.fundamental.presentation.detail

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.hilmysf.fundamental.R
import com.hilmysf.fundamental.databinding.ActivityDetailEventBinding
import com.hilmysf.fundamental.domain.model.Event
import com.hilmysf.fundamental.domain.model.ResultState
import com.hilmysf.fundamental.helper.DateHelper
import com.hilmysf.fundamental.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailEventActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }

    private lateinit var binding: ActivityDetailEventBinding
    private val viewModel: DetailEventViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, 0)
        viewModel.getEvent(eventId)
        observeEvent()
        onClick()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isTaskRoot) {
                    val intent = Intent(this@DetailEventActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun onClick() {
        binding.btnRegister.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, viewModel.detailEvent.link.toUri())
            startActivity(intent)
        }
    }

    private fun setContent(event: Event) {
        binding.apply {
            Glide.with(this@DetailEventActivity)
                .load(event.mediaCover)
                .into(imgEvent)
            tvEventName.text = event.name
            tvEventDesc.text = event.summary
            tvInformasi.text = HtmlCompat.fromHtml(
                event.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
            tvStart.text = DateHelper.formatDate(event.beginTime)
            tvEnd.text = DateHelper.formatDate(event.endTime)
            tvLocation.text = event.cityName
            val availableQuota = event.quota - event.registrants
            tvQuota.text = getString(R.string.dari, availableQuota, event.quota)
            val spannableOwnerName = SpannableString("• ${event.ownerName}")
            tvOwner.text = spannableOwnerName.apply {
                setSpan(
                    ForegroundColorSpan(Color.RED),
                    0,
                    1,
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                contentContainer.visibility = View.GONE
                imgEvent.visibility = View.GONE

                placeholder.visibility = View.VISIBLE
            } else {
                contentContainer.visibility = View.VISIBLE
                imgEvent.visibility = View.VISIBLE

                placeholder.visibility = View.GONE
            }
        }
    }

    private fun showError(isError: Boolean, errorMessage: String = "") {
        binding.apply {
            if (isError) {
                btnRegister.visibility = View.GONE
                frameLayout.visibility = View.GONE
                errorState.visibility = View.VISIBLE
                errorState.text = errorMessage
            } else {
                errorState.visibility = View.GONE
                btnRegister.visibility = View.VISIBLE
                frameLayout.visibility = View.VISIBLE
            }
        }
    }


    private fun observeEvent() {
        viewModel.eventResponse.observe(this) {
            when (it) {
                is ResultState.Success -> {
                    showLoading(false)
                    showError(false)
                    val event = it.data
                    viewModel.detailEvent = event
                    setContent(event)
                }

                is ResultState.Loading -> {
                    showError(false)
                    showLoading(true)
                }

                is ResultState.Error -> {
                    showError(true, it.error)
                    showLoading(false)
                }
            }
        }
    }
}