package com.simple.firstapp.ui.listlogs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simple.firstapp.databinding.LogListItemViewBinding

class ListLogAdapter(private val listener: LogSelectedListener) : ListAdapter<String, ListLogAdapter.LogListViewHolder>(
    DIFF_CALLBACK
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogListViewHolder {
        val binding : LogListItemViewBinding = LogListItemViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LogListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LogListViewHolder, position: Int) {
        getItem(position)?.let {
                fileName ->
            holder.bind(fileName)
        }
    }

    inner class LogListViewHolder(val binding : LogListItemViewBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(fileName:String?){
            binding.tvFile.text = fileName
            binding.itemLog.setOnClickListener {
                listener.logSelected(fileName)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<String> =
            object : DiffUtil.ItemCallback<String>(){
                override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                    return oldItem == newItem
                }

            }
    }

    interface LogSelectedListener{
        fun logSelected(fileName: String?)
    }
}