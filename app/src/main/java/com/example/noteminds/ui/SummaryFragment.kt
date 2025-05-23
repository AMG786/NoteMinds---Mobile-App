package com.example.noteminds.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.noteminds.MainActivity
import com.example.noteminds.NavigationListener
import com.example.noteminds.R
import com.example.noteminds.databinding.FragmentSummaryBinding
import com.example.noteminds.ui.model.Question
import java.text.SimpleDateFormat
import java.util.*

class SummaryFragment : Fragment() {

    private var _binding: FragmentSummaryBinding? = null
    private val binding get() = _binding!!

    public var summaryText: String? = null
    private var documentName: String? = null
    private var feedbackGiven = false

    companion object {
        const val ARG_SUMMARY_TEXT = "summary_text"
        const val ARG_DOCUMENT_NAME = "document_name"

        fun newInstance(summaryText: String="", documentName: String? = null): SummaryFragment {
            val fragment = SummaryFragment()
            val args = Bundle().apply {
                putString(ARG_SUMMARY_TEXT, summaryText)
                putString(ARG_DOCUMENT_NAME, documentName)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            summaryText = (activity as MainActivity).SummaryText.toString()
//            summaryText = it.getString(ARG_SUMMARY_TEXT)
            documentName = it.getString(ARG_DOCUMENT_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupClickListeners()
        displaySummary()
    }

    private fun setupUI() {
        // Set document name
        documentName?.let {
            binding.documentName.text = "Generated from $it"
        }

        // Set timestamp
        val currentTime = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date())
        binding.timestampText.text = currentTime

        // Calculate and display summary stats
        (activity as MainActivity).SummaryText?.let { text ->
            updateSummaryStats(text)
        }
//        summaryText?.let { text ->
//            updateSummaryStats(text)
//        }
//        summaryText?.let { text ->
//            updateSummaryStats(text)
//        }
    }

    private fun updateSummaryStats(text: String) {
        // Calculate word count

        val wordCount = text.trim().split("\\s+".toRegex()).size
        binding.wordCount.text = wordCount.toString()

        // Estimate reading time (average 200 words per minute)
        val readingTimeMinutes = Math.ceil(wordCount / 200.0).toInt()
        binding.readingTime.text = "${readingTimeMinutes} min"
    }

    private fun displaySummary() {
        summaryText?.let { text ->
            binding.summaryText.text = text

            // Add animation for text appearance
            binding.summaryText.alpha = 0f
            binding.summaryText.animate()
                .alpha(1f)
                .setDuration(500)
                .start()
        } ?: run {
            binding.summaryText.text = "No summary available"
        }
    }

    private fun setupClickListeners() {
        // Back button
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Share button
        binding.shareButton.setOnClickListener {
            shareSummary()
        }

        // Copy button
        binding.copyButton.setOnClickListener {
            copySummaryToClipboard()
        }

        // Save button
        binding.saveButton.setOnClickListener {
            saveSummary()
        }

        // Generate Quiz button
        binding.generateQuizButton.setOnClickListener {
            generateQuiz()
        }

        // Feedback buttons
        binding.thumbsUpButton.setOnClickListener {
            giveFeedback(true)
        }

        binding.thumbsDownButton.setOnClickListener {
            giveFeedback(false)
        }
    }

    private fun shareSummary() {
        summaryText?.let { text ->
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Document Summary:\n\n$text")
                putExtra(Intent.EXTRA_SUBJECT, "PDF Summary - ${documentName ?: "Document"}")
            }

            try {
                startActivity(Intent.createChooser(shareIntent, "Share Summary"))
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Unable to share summary", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun copySummaryToClipboard() {
        summaryText?.let { text ->
            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Summary", text)
            clipboard.setPrimaryClip(clip)

            Toast.makeText(requireContext(), "Summary copied to clipboard", Toast.LENGTH_SHORT).show()

            // Visual feedback
            binding.copyButton.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(100)
                .withEndAction {
                    binding.copyButton.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()
                }
                .start()
        }
    }

    private fun saveSummary() {
        // TODO: Implement save functionality (database/file storage)
        Toast.makeText(requireContext(), "Summary saved to library", Toast.LENGTH_SHORT).show()

        // Visual feedback
        binding.saveButton.text = "Saved!"
        binding.saveButton.setIconResource(R.drawable.ic_check_circle)
        (activity as? NavigationListener)?.navigateToFragment(
            DashboardFragment()
        )
        // Reset button after 2 seconds
//        binding.saveButton.postDelayed({
//            binding.saveButton.text = "Save Summary"
//            binding.saveButton.setIconResource(R.drawable.ic_bookmark)
//        }, 2000)
    }

    private fun generateQuiz() {
        // TODO: Navigate to quiz generation or pass data to quiz fragment
        Toast.makeText(requireContext(), "Generating quiz...", Toast.LENGTH_SHORT).show()



        @Suppress("UNCHECKED_CAST")
        val questionList = arguments?.getSerializable("question_list") as? ArrayList<Question>

        val bundle = Bundle().apply {
            putSerializable("question_list", questionList) // Convert to ArrayList to ensure it's Serializable
        }
        (activity as? MainActivity)?.flag = false;
        (activity as? NavigationListener)?.navigateToFragment(
            QuizFragment().apply { arguments = bundle }
        )

        //findNavController().navigate(R.id.action_fragmentA_to_fragmentB, bundle)

        // You can navigate to quiz fragment here
        // Example:
        // val quizFragment = QuizFragment.newInstance(summaryText ?: "")
        // parentFragmentManager.beginTransaction()
        //     .replace(R.id.fragment_container, quizFragment)
        //     .addToBackStack(null)
        //     .commit()
    }

    private fun giveFeedback(isPositive: Boolean) {
        if (feedbackGiven) {
            Toast.makeText(requireContext(), "Feedback already submitted", Toast.LENGTH_SHORT).show()
            return
        }

        feedbackGiven = true

        val message = if (isPositive) "Thanks for your positive feedback!" else "Thanks for your feedback. We'll improve!"
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

        // Visual feedback
        val button = if (isPositive) binding.thumbsUpButton else binding.thumbsDownButton
        button.animate()
            .scaleX(1.3f)
            .scaleY(1.3f)
            .setDuration(200)
            .withEndAction {
                button.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .start()
            }
            .start()

        // Disable both buttons after feedback
        binding.thumbsUpButton.alpha = 0.5f
        binding.thumbsDownButton.alpha = 0.5f
        binding.thumbsUpButton.isEnabled = false
        binding.thumbsDownButton.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}