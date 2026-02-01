package com.hilmysf.fundamental.presentation.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hilmysf.fundamental.databinding.FragmentSettingBinding
import com.hilmysf.fundamental.presentation.adapter.SettingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : Fragment() {
    lateinit var binding: FragmentSettingBinding
    val settingAdapter: SettingAdapter by lazy { SettingAdapter() }
    val viewModel: SettingViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        observeState()
    }

    private fun setupAdapter(){
        binding.rvSetting.adapter = settingAdapter
        settingAdapter.setOnCheckChanged {
            when(it.id){
                "dark-mode" -> viewModel.saveThemeSetting(it.isChecked)
                "daily-reminder" -> viewModel.saveReminderSetting(it.isChecked)
            }
        }
    }
    
    private fun observeState(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.settingItems.collect {
                    settingAdapter.submitList(it)
                }
            }
        }
    }
}