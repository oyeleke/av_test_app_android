package com.avtestapp.android.androidbase.av_test.core

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.models.response.Question
import com.avtestapp.android.androidbase.databinding.QuestionSearchItemBinding
import com.avtestapp.android.androidbase.extensions.hide
import timber.log.Timber

class DashboardAdapter (
    private val questionClickedListener: (Question) -> Unit
) : ListAdapter<Question, DashboardAdapter.ViewHolder>(QuestionListDiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            QuestionSearchItemBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.question_search_item, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    inner class ViewHolder(val binding: QuestionSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Question) {
            binding.questionString.text = item.text
            binding.bookmarkIcon.isSelected = item.isBookmarked
            if (item.options.isNotEmpty()) {
                if (item.options[0].isCorrect) {
                    binding.answerOption1.isSelected = true
                    binding.answerOption2.isSelected = false
                    binding.answerOption3.isSelected = false
                }
            }
            if (item.options.size > 1) {
                if (item.options[1].isCorrect) {
                    binding.answerOption1.isSelected = false
                    binding.answerOption2.isSelected = true
                    binding.answerOption3.isSelected = false
                }
            }
            if (item.options.size > 2) {
                if (item.options[2].isCorrect) {
                    binding.answerOption1.isSelected = false
                    binding.answerOption2.isSelected = false
                    binding.answerOption3.isSelected = true
                }
            }

            if (item.options.isNotEmpty()) {
                binding.answerOption1Text.text = item.options[0].text
            } else {
                binding.answerOption1.hide()
            }

            if (item.options.size > 1) {
                binding.answerOption2Text.text = item.options[1].text
            } else {
                binding.answerOption2.hide()
            }

            if (item.options.size > 2) {
                binding.answerOption3Text.text = item.options[2].text
            } else {
                binding.answerOption3.hide()
            }
        }
    }
}

class QuestionListDiffUtilCallback : DiffUtil.ItemCallback<Question>() {
    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem == newItem
    }

}