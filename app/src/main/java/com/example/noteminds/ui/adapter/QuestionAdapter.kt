package com.example.noteminds.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteminds.R
import com.example.noteminds.databinding.ItemQuestionBinding
import com.example.noteminds.ui.model.Question

/**
Created by Abdul Mueez, 04/24/2025
 */
class QuestionAdapter(
    private val onAnswerSelected: (Question, Int) -> Unit
) : ListAdapter<Question, QuestionAdapter.QuestionViewHolder>(DiffCallback()) {

    inner class QuestionViewHolder(private val binding: ItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Question) {
            binding.tvQuestionNumber.text = "${question.number}."
            binding.tvQuestionTitle.text = question.title
            binding.tvQuestionText.text = question.questionText
            binding.tvQuestionText.visibility = View.VISIBLE

            // Setup radio buttons
            binding.rgAnswers.visibility = View.VISIBLE
            binding.layoutToggle.visibility = View.GONE

            question.options.forEachIndexed { index, option ->
                when (index) {
                    0 -> {
                        binding.rbAnswer1.text = option
                        binding.rbAnswer1.visibility = View.VISIBLE
                    }
                    1 -> {
                        binding.rbAnswer2.text = option
                        binding.rbAnswer2.visibility = View.VISIBLE
                    }
                    2 -> {
                        binding.rbAnswer3.text = option
                        binding.rbAnswer3.visibility = View.VISIBLE
                    }
                }
            }

            binding.rgAnswers.setOnCheckedChangeListener { _, checkedId ->
                val selectedIndex = when (checkedId) {
                    R.id.rb_answer1 -> 0
                    R.id.rb_answer2 -> 1
                    R.id.rb_answer3 -> 2
                    else -> -1
                }
                System.out.println(selectedIndex);
                if (selectedIndex != -1) {
                    question.selectedOption = selectedIndex;
                    onAnswerSelected(question, selectedIndex)
                }
            }
            binding.rgAnswers.visibility = View.GONE
            binding.tvQuestionText.visibility = View.GONE

            binding.ivQuestionStatus.setOnClickListener(View.OnClickListener {
                binding.rgAnswers.visibility = View.VISIBLE
                binding.tvQuestionText.visibility = View.VISIBLE
            });


        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Question>() {
        override fun areItemsTheSame(oldItem: Question, newItem: Question) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Question, newItem: Question) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return QuestionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}