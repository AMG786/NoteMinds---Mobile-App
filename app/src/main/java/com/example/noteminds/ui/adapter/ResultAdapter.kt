package com.example.noteminds.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteminds.ui.model.Result
import com.example.noteminds.R

/**
Created by Abdul Mueez, 04/24/2025
 */
class ResultAdapter(
    private val results: List<Result>
) : RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_result, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        val result = results[position]
        holder.bind(result)
    }

    override fun getItemCount(): Int = results.size

    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val resultNumber: TextView = itemView.findViewById(R.id.tv_result_number)
        private val resultTitle: TextView = itemView.findViewById(R.id.tv_result_title)
        private val questionText: TextView = itemView.findViewById(R.id.tv_question_text)
        private val correctAnswer: TextView = itemView.findViewById(R.id.tv_correct_answer)
        private val userAnswer: TextView = itemView.findViewById(R.id.tv_user_answer)
        private val statusImageView: ImageView = itemView.findViewById(R.id.iv_result_status)

        fun bind(result: Result) {
            resultNumber.text = "${result.number}."
            resultTitle.text = result.questionTitle
            questionText.text=result.questionText
            correctAnswer.text=result.correctAnswer
            userAnswer.text=result.userAnswer
             // Change text color based on correctness
            if (result.userAnswer.trim().equals(result.correctAnswer.trim(), ignoreCase = true)) {
                userAnswer.setTextColor(itemView.context.getColor(R.color.green)) // Use your actual green color resource
                statusImageView.setImageResource(R.drawable.correct);
            } else {
                userAnswer.setTextColor(itemView.context.getColor(R.color.red)) // Use your actual red color resource
                statusImageView.setImageResource(R.drawable.wrong);
            }
        }
    }
}