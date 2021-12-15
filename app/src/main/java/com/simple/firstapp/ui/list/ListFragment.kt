package com.simple.firstapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.simple.firstapp.MainActivityViewModel
import com.simple.firstapp.R
import com.simple.firstapp.base.BaseFragment
import com.simple.firstapp.databinding.ListFragmentBinding
import com.simple.firstapp.databinding.MainActivityBinding
import com.simple.firstapp.ui.main.MainViewModel

class ListFragment : BaseFragment() {

    private lateinit var binding: ListFragmentBinding

    private var username = ""

    companion object {
        fun newInstance() = ListFragment()
        const val USERNAME = "username"
    }

    private val viewModel by viewModels<ListViewModel>()
    private val activityViewModel by activityViewModels<MainActivityViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.list_fragment, container, false)
        this.binding.lifecycleOwner = this
        return binding.root
    }

    private fun getBundleArguments(){
        requireArguments().run {
            username = getString(USERNAME).toString()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundleArguments()
        binding.fragmentName = "ListFragment1"
        binding.viewModel = viewModel
        activityViewModel.toastValueMLD.value = "listFragment"
        binding.message3.text = username
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                requireActivity().onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun getTitle(): String {
        return "List Fragment"
    }

    override fun hideBackButton(): Boolean {
        return false
    }
}
