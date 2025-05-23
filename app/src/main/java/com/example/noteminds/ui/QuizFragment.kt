package com.example.noteminds.ui

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteminds.MainActivity
import com.example.noteminds.NavigationListener
import com.example.noteminds.ui.model.Question
import com.example.noteminds.ui.adapter.QuestionAdapter
import com.example.noteminds.Resource
import com.example.noteminds.TokenManager
import com.example.noteminds.data.api.RetrofitInstance
import com.example.noteminds.data.model.QuizQuestion
import com.example.noteminds.data.repository.QuizRepository
import com.example.noteminds.databinding.FragmentQuizBinding
import com.example.noteminds.vm.QuizViewModel
import com.example.noteminds.vm.QuizViewModelFactory
import java.io.Serializable


/**
Created by Abdul Mueez 04/16/2025
 */
class QuizFragment : Fragment() {
    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: QuizViewModel
    private lateinit var adapter: QuestionAdapter
    private var historyId: String="6825f36a3d2b75c378dfa555";

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        setupObservers()

        val topic = arguments?.getString("topic") ?: "ANDROID DEVELOPMENT"

        if((activity as MainActivity).flag ==true){
            viewModel.fetchQuiz(token = TokenManager.getToken(requireContext()) ?: "",topic)
        }else{
            @Suppress("UNCHECKED_CAST")
            val quizList = arguments?.getSerializable("question_list") as? ArrayList<QuizQuestion>
            val questionList: List<Question> = quizList?.map { it.toQuestion() } ?: emptyList()
            showQuestions(questionList)
        }



        binding.tvTaskTitle.text = "Quiz on $topic";
        binding.tvTaskDescription.text = "Test your knowledge of $topic Hope you love it \uD83D\uDE04";


        binding.btnSubmit.setOnClickListener {

            adapter.currentList?.let { questions ->
                viewModel.submitQuiz(questions)
                viewModel.submitQuiz(historyId,questions, TokenManager.getToken(requireContext()) ?: "")
            }

        }
    }

    private fun setupViewModel() {
        val factory = QuizViewModelFactory(QuizRepository(RetrofitInstance.api))
        viewModel = ViewModelProvider(this, factory)[QuizViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = QuestionAdapter { question, selectedOption ->
            // Update question in ViewModel if needed
        }
        binding.rvQuestions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@QuizFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.quizState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    state.data?.let { questions ->
                        // Here you would set the historyId if your API returns it
                        // viewModel.setHistoryId(questions.historyId)
                        showQuestions(questions)
                    } ?: showError("No questions found")
                }

                    //showQuestions(state.data)
                is Resource.Error -> showError(state.message)
            }
        }

        // Observe historyId and set it
        viewModel.historyId.observe(viewLifecycleOwner) { id ->
            historyId = id // ✅ Automatically set from API response
        }

        viewModel.navigateToResults.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                val resultsFragment = ResultsFragment().apply {
                    adapter.currentList?.let { questions ->
                        arguments = Bundle().apply {
                            putSerializable("quizResponses", ArrayList(questions))
                        }}
                }
                (activity as? NavigationListener)?.navigateToFragment(resultsFragment)
                viewModel.doneNavigating()
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.rvQuestions.visibility = View.GONE
    }

    private fun showQuestions(questions: List<Question>?) {
        binding.progressBar.visibility = View.GONE
        questions?.let {
            adapter.submitList(it)
            binding.rvQuestions.visibility = View.VISIBLE
        } ?: showError("No questions found")
    }

    private fun showError(message: String?) {
        binding.progressBar.visibility = View.GONE
        binding.rvQuestions.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}