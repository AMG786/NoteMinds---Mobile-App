package com.example.noteminds.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.noteminds.databinding.FragmentResultsBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteminds.MainActivity
import com.example.noteminds.NavigationListener
import com.example.noteminds.ui.model.Question
import com.example.noteminds.ui.model.Result
import com.example.noteminds.ui.adapter.ResultAdapter

/**
Craeted by Abdul Mueez 04/16/2025
 */

class ResultsFragment : Fragment() {

    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!
    private lateinit var resultAdapter: ResultAdapter
    private val resultsList :ArrayList<Result> = ArrayList();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // For demonstration, we'll use a hardcoded task ID
        val taskId = arguments?.getLong("taskId") ?: 1L

        loadResults(taskId)
        setupRecyclerView()
        setupContinueButton()
    }

    private fun loadResults(taskId: Long) {
        // In a real app, fetch results from repository or API
        resultsList.clear()
        val questions = arguments?.getSerializable("quizResponses") as? ArrayList<Question>
        System.out.println(questions)
        questions?.forEachIndexed { index, question ->
            if(question.correctAnswer == "A" || question.correctAnswer =="a"){
                question.correctAnswer = question.options[0];
            }else if(question.correctAnswer == "B" || question.correctAnswer =="b"){
                question.correctAnswer = question.options[1];
            }else if(question.correctAnswer == "C" || question.correctAnswer =="c"){
                question.correctAnswer = question.options[2];
            }else if(question.correctAnswer == "D" || question.correctAnswer =="d"){
                question.correctAnswer = question.options[3];
            }
            resultsList.add(
                Result(
                id = question.id,
                number = question.number,
                questionTitle = question.title,
                questionText = question.questionText,
                correctAnswer = question.correctAnswer,
                userAnswer = question.options[question.selectedOption]
            )
            )
        }

    }

    private fun setupRecyclerView() {
        resultAdapter = ResultAdapter(resultsList)

        binding.rvResults.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = resultAdapter
        }
    }

    private fun setupContinueButton() {
        binding.btnContinue.setOnClickListener {
            // Navigate back to dashboard
            (activity as? MainActivity)?.flag = true;
            (activity as? NavigationListener)?.navigateToFragment(DashboardFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
