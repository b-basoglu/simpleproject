package com.simple.firstapp.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.simple.firstapp.MainActivityViewModel
import com.simple.firstapp.R
import com.simple.firstapp.base.BaseFragment
import com.simple.firstapp.databinding.MainFragmentBinding

class MainFragment : BaseFragment() {

    private lateinit var binding : MainFragmentBinding

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel by viewModels<MainViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = MainFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.message.setOnClickListener(
            View.OnClickListener {
                navigateTo(MainFragmentDirections.actionMainToList("mainuser"))
            }
        )
        activityViewModel.toastValueMLD.value = "mainFragment"
    }

    override fun getTitle(): String {
        return "Main Fragment"
    }

    override fun hideBackButton(): Boolean {
        return true
    }

}