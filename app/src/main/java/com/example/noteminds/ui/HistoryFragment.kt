package com.example.noteminds.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.noteminds.ui.adapter.HistoryAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteminds.Resource
import com.example.noteminds.TokenManager
import com.example.noteminds.data.api.RetrofitInstance
import com.example.noteminds.vm.HistoryViewModel
import com.example.noteminds.databinding.FragmentHistoryBinding
import com.example.noteminds.ui.model.Question


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HistoryViewModel
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = HistoryViewModel(RetrofitInstance.api)

        setupUI()
        setupRecyclerView()
        setupObservers()
        loadHistoryData()

    }

    private fun setupUI() {
        binding.materialButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter(emptyList())
        binding.rvHistory.apply {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObservers() {
        viewModel.historyState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    hideLoading()
                    state.data?.let { historyItems ->
                        println("Received ${historyItems.size} history items")
                        historyItems.forEach { println(it) }
                        var index:Int=0;
                        val allQuestions = historyItems.flatMap { historyItem ->
                            historyItem.questions.map { apiQuestion ->
                                index++;


                                apiQuestion.correctAnswer?.let {
                                    var ind=0;
                                    if(it=="A"){
                                        ind=0
                                    }else if(it=="B"){
                                        ind=1
                                    }else if(it=="C"){
                                        ind=2
                                    }else if(it=="D"){
                                        ind=3
                                    }

                                    apiQuestion.isCorrect?.let { it1 ->
                                        Question(
                                            title = "Question",
                                            questionText = apiQuestion.question,
                                            options = apiQuestion.options,
                                            correctAnswer = it,
                                            id = 1,
                                            number = index,
                                            questionType = Question.QuestionType.MULTIPLE_CHOICE,
                                            selectedOption = ind,
                                            userAnswer = apiQuestion.userAnswer.toString(),
                                            status = it1
                                        )
                                    }
                                }

                            }

                        }

                        if (allQuestions.isEmpty()) {
                            hideLoading()
                        } else {
                            historyAdapter.updateQuestions(allQuestions)
                            hideLoading()
                        }
                    }
                }
                is Resource.Error -> {
                    hideLoading()
                    println("Error occurred: ${state.message}")
                }
            }
        }
    }

    private fun loadHistoryData() {
        val token = TokenManager.getToken(requireContext()) ?: return
        viewModel.fetchUserHistory(token)
    }

    private fun showLoading() {
//        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
//        binding.progressBar.visibility = View.GONE
        System.out.println("Hiding showing")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}