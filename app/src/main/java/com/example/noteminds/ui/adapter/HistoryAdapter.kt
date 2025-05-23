package com.example.noteminds.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteminds.R
import com.example.noteminds.ui.model.Question


class HistoryAdapter(
    private var questions: List<Question>
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    var i=0;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(questions[position])
    }
    fun updateQuestions(newQuestions: List<Question?>) {
        questions = newQuestions as List<Question>
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = questions.size

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvQuestionNumber: TextView = itemView.findViewById(R.id.tv_question_number)
        private val tvQuestionTitle: TextView = itemView.findViewById(R.id.tv_question_title)
        private val tvQuestionText: TextView = itemView.findViewById(R.id.tv_question_text)
        private val ivQuestionStatus: ImageView = itemView.findViewById(R.id.iv_question_status)
        private val answersContainer: LinearLayout = itemView.findViewById(R.id.answers_container)

        // Track expanded state
        private var isExpanded = true

        fun bind(question: Question) {
            // Set basic question information
            tvQuestionNumber.text = "${question.number}."
            tvQuestionTitle.text = question.title
            tvQuestionText.text = question.questionText

            // Setup expand/collapse functionality
            setupExpandCollapse()

            // Clear previous answers and re-add based on the current question
            answersContainer.removeAllViews()

            // Add answers based on question type
            when (question.questionType) {
                Question.QuestionType.MULTIPLE_CHOICE -> {
                    setupMultipleChoiceAnswers(question)
                }
                Question.QuestionType.TOGGLE -> {
//                    setupToggleAnswer(question)
                }

                null -> TODO()
            }

        }

        private fun setupExpandCollapse() {
            // Set initial state
//            ivQuestionStatus.setImageResource(
//                if (isExpanded) R.drawable.arrow_down else R.drawable.arrow_right
//            )

            // Set visibility based on expanded state
            answersContainer.visibility = if (isExpanded) View.VISIBLE else View.GONE

            // Set click listener for expand/collapse
            ivQuestionStatus.setOnClickListener {
                isExpanded = !isExpanded

                // Update UI
                ivQuestionStatus.setImageResource(
                    if (isExpanded) R.drawable.arrow_down else R.drawable.arrow_right
                )
                answersContainer.visibility = if (isExpanded) View.VISIBLE else View.GONE
            }
        }

        private fun setupMultipleChoiceAnswers(question: Question) {
            val inflater = LayoutInflater.from(itemView.context)
            val correctAnswerIndex = question.options.indexOf(question.correctAnswer)
            val userSelectedIndex = question.selectedOption
            val status = question.status;
            val userAnswer = question.userAnswer;

            question.options.forEachIndexed { index, optionText ->
                val answerLayout = LinearLayout(itemView.context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        bottomMargin = if (index < question.options.size - 1)
                            resources.getDimensionPixelSize(R.dimen.answer_item_margin) else 0
                    }
                    gravity = android.view.Gravity.CENTER_VERTICAL
                }

                // Determine if this option is the correct answer
                val isCorrectAnswer = index == correctAnswerIndex
                // Determine if this option was selected by user
                val isUserSelected = index == userSelectedIndex
                System.out.println(question)
                // Create circle indicator
                val circleView = View(itemView.context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        resources.getDimensionPixelSize(R.dimen.answer_circle_size),
                        resources.getDimensionPixelSize(R.dimen.answer_circle_size)
                    ).apply {
                        marginEnd = resources.getDimensionPixelSize(R.dimen.answer_circle_margin)
                    }
                    var index=-1;
                    if(question.correctAnswer=="A"){
                        index=0;
                    }else if(question.correctAnswer=="B"){
                        index=1;
                    } else if (question.correctAnswer == "C") {
                        index=2;
                    }
                    else if (question.correctAnswer == "D") {
                        index=3;
                    }
                    // Set background based on answer status
                    background = when {
                        isCorrectAnswer && status ->
                            resources.getDrawable(R.drawable.circle_green, null)
                        isUserSelected && status ->
                            resources.getDrawable(R.drawable.circle_green, null)
                        isCorrectAnswer ->
                            resources.getDrawable(R.drawable.circle_green, null)

                        userAnswer==questions[index].correctAnswer && isUserSelected->
                            resources.getDrawable(R.drawable.circle_red, null)
                        userAnswer!=questions[index].correctAnswer && isUserSelected->
                            resources.getDrawable(R.drawable.circle_red, null)
                        else ->
                            resources.getDrawable(R.drawable.circle_gray, null)


                    }

                }

                // Create answer text
                val textView = TextView(itemView.context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    text = optionText
                    setTextColor(resources.getColor(R.color.white, null))
                    textSize = 16f
                }

                // Create status label
                val statusLabel = TextView(itemView.context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        marginStart = resources.getDimensionPixelSize(R.dimen.status_label_margin)
                    }
                    alpha = 0.7f
                    setTextColor(resources.getColor(R.color.white, null))
                    textSize = 12f

                    when {
                        isCorrectAnswer && isUserSelected -> {
                            visibility = View.VISIBLE
                            text = "Correct Answer"
                        }
                        isCorrectAnswer -> {
                            visibility = View.VISIBLE
                            text = "Correct Answer"
                        }
                        isUserSelected -> {
                            visibility = View.VISIBLE
                            text = "Your Answer"
                        }
                        else -> {
                            visibility = View.GONE
                        }
                    }
                }

                answerLayout.addView(circleView)
                answerLayout.addView(textView)
                answerLayout.addView(statusLabel)
                answersContainer.addView(answerLayout)

            }
        }

        private fun setupToggleAnswer(question: Question) {
            val options = listOf("Yes", "No")
            options.forEachIndexed { index, optionText ->
                // Create a layout similar to what's in item_history.xml for an answer
                val answerLayout = LinearLayout(itemView.context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        bottomMargin = if (index < options.size - 1)
                            resources.getDimensionPixelSize(R.dimen.answer_item_margin) else 0
                    }
                    gravity = android.view.Gravity.CENTER_VERTICAL
                }

                // Create circle indicator
                val circleView = View(itemView.context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        resources.getDimensionPixelSize(R.dimen.answer_circle_size),
                        resources.getDimensionPixelSize(R.dimen.answer_circle_size)
                    ).apply {
                        marginEnd = resources.getDimensionPixelSize(R.dimen.answer_circle_margin)
                    }

                    // Set background based on answer status
                    background = when {
                        question.status == false ->
                            resources.getDrawable(R.drawable.circle_green, null)
                        question.correctAnswer == options[question.selectedOption] && question.status ->
                            resources.getDrawable(R.drawable.circle_green, null)
                        optionText == question.correctAnswer ->
                            resources.getDrawable(R.drawable.circle_green, null)
                        else ->
                            resources.getDrawable(R.drawable.circle_gray, null)
                    }
                }

                // Create answer text
                val textView = TextView(itemView.context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    text = optionText
                    setTextColor(resources.getColor(R.color.white, null))
                    textSize = 16f
                }

                // Create status label if needed
                val statusLabel = TextView(itemView.context).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        marginStart = resources.getDimensionPixelSize(R.dimen.status_label_margin)
                    }
                    alpha = 0.7f
                    setTextColor(resources.getColor(R.color.white, null))
                    textSize = 12f

                    when {
                        (index == 0 && question.selectedOption == 0) ||
                                (index == 1 && question.selectedOption == 1) -> {
                            visibility = View.VISIBLE
                            text = "Your Answer"
                        }
                        optionText == question.correctAnswer -> {
                            visibility = View.VISIBLE
                            text = "Correct Answer"
                        }
                        else -> {
                            visibility = View.GONE
                        }
                    }
                }

                // Add all views to the answer layout
                answerLayout.addView(circleView)
                answerLayout.addView(textView)
                answerLayout.addView(statusLabel)

                // Add to container
                answersContainer.addView(answerLayout)
            }
        }
    }
}