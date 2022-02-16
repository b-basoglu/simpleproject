package com.simple.firstapp.ui.listlogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.log.filelogger.FileLogger
import com.simple.firstapp.MainActivityViewModel
import com.simple.firstapp.base.BaseFragment
import com.simple.firstapp.databinding.ListFragmentBinding
import com.simple.firstapp.databinding.ListLogsFragmentBinding
import com.simple.firstapp.databinding.MainFragmentBinding
import com.simple.firstapp.ui.list.ListFragment
import com.simple.firstapp.ui.list.ListViewModel
import java.io.File

class ListLogsFragment : BaseFragment() {

    private lateinit var binding: ListLogsFragmentBinding


    companion object {
        fun newInstance() = ListLogsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = ListLogsFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter: ListLogAdapter = ListLogAdapter(
            object : ListLogAdapter.LogSelectedListener {
                override fun logSelected(fileName: String?) {
                    val fileLocation: File = File(FileLogger.pathToLog, fileName)
                    FileLogger.sendMail(
                        requireActivity(),
                        FileProvider.getUriForFile(
                            requireActivity().applicationContext,
                            requireActivity().applicationContext.packageName + ".provider",
                            fileLocation
                        ), "We have successfully created our local logs"
                    )
                }
            }
        )
        binding.run {
            tvPath.text = FileLogger.pathToLog
            rvFiles.layoutManager = LinearLayoutManager(requireContext())
            rvFiles.adapter = adapter
        }
        adapter.submitList(FileLogger.getLogsList())
    }

    override fun getTitle(): String {
        return "Log List"
    }

    override fun hideBackButton(): Boolean {
        return false
    }

}