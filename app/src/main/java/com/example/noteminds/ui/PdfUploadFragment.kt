package com.example.noteminds.ui

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.noteminds.MainActivity
import com.example.noteminds.NavigationListener
import com.example.noteminds.R
import com.example.noteminds.Resource
import com.example.noteminds.TokenManager
import com.example.noteminds.data.api.ApiService
import com.example.noteminds.data.api.RetrofitInstance
import com.example.noteminds.data.model.PdfUploadResponse
import com.example.noteminds.databinding.FragmentPdfUploadBinding
import com.example.noteminds.vm.PdfUploadViewModel
import com.github.dhaval2404.imagepicker.ImagePicker

class PdfUploadFragment : Fragment() {
    private var _binding: FragmentPdfUploadBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PdfUploadViewModel
    private lateinit var apiService: ApiService
    private var pdfUri: Uri? = null

    private val pdfPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                pdfUri = uri
                showSelectedFile(getFileName(uri))
                binding.processButton.isEnabled = true
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        apiService = RetrofitInstance.api
        viewModel = PdfUploadViewModel(apiService)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPdfUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        setupObservers()
        setupInitialState()
    }

    private fun setupInitialState() {
        // Hide selected file card initially
        binding.selectedFileCard.visibility = View.GONE
        binding.progressSection.visibility = View.GONE
    }

    private fun setupClickListeners() {
        // Both upload area and button can trigger file selection
        binding.uploadArea.setOnClickListener {
            selectPdf()
        }

        binding.selectPdfButton.setOnClickListener {
            selectPdf()
        }

        binding.processButton.setOnClickListener {
            pdfUri?.let { uri ->
                val token = TokenManager.getToken(requireContext()) ?: ""
                viewModel.processPdf(requireContext(), uri, token)
            } ?: run {
                Toast.makeText(requireContext(), "Please select a PDF first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun selectPdf() {
        ImagePicker.with(this)
            .galleryOnly()
            .galleryMimeTypes(arrayOf("application/pdf"))
            .createIntent { intent ->
                pdfPickerLauncher.launch(intent)
            }
    }

    private fun showSelectedFile(fileName: String) {
        // Update file name
        binding.pdfName.text = fileName

        // Show selected file card with animation
        binding.selectedFileCard.visibility = View.VISIBLE
        binding.selectedFileCard.alpha = 0f
        binding.selectedFileCard.animate()
            .alpha(1f)
            .setDuration(300)
            .start()

        // Update upload area appearance
        binding.uploadIcon.setImageResource(R.drawable.ic_check_circle)
        binding.uploadText.text = "PDF Selected Successfully!"

        // Change upload area background to success state
        binding.uploadArea.setBackgroundResource(R.drawable.success_border_background)
    }

    private fun setupObservers() {
        viewModel.uploadState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    showLoadingState()
                }
                is Resource.Success -> {
                    hideLoadingState()
                    state.data?.let { response ->
                        navigateToResults(response)
                    }
                }
                is Resource.Error -> {
                    hideLoadingState()
                    showError(state.message ?: "Unknown error occurred")
                }
            }
        }
    }

    private fun showLoadingState() {
        binding.progressSection.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
        binding.processButton.isEnabled = false

        // Animate progress section
        binding.progressSection.alpha = 0f
        binding.progressSection.animate()
            .alpha(1f)
            .setDuration(300)
            .start()
    }

    private fun hideLoadingState() {
        binding.progressSection.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.processButton.isEnabled = true
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_LONG).show()

        // Reset upload area to error state
        binding.uploadIcon.setImageResource(R.drawable.ic_error)
        binding.uploadText.text = "Upload Failed - Try Again"
        binding.uploadArea.setBackgroundResource(R.drawable.error_border_background)
    }

    private fun navigateToResults(response: PdfUploadResponse) {
        System.out.println("Summary: ")
        System.out.println(response.summary)
        System.out.println("Quiz: ")
        System.out.println(ArrayList(response.quiz))
        showResults(response)

        val bundle = Bundle().apply {
            putSerializable("question_list", ArrayList(response.quiz)) // Convert to ArrayList to ensure it's Serializable
        }

        (activity as MainActivity).SummaryText = response.summary
        (activity as? NavigationListener)?.navigateToFragment(
            SummaryFragment.newInstance("android.pdf").apply { arguments = bundle }
        )

    }

    private fun showResults(response: PdfUploadResponse) {
        // Your existing navigation logic here
    }

    private fun getFileName(uri: Uri): String {
        var result = ""
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex("_display_name")
                if (displayNameIndex != -1) {
                    result = it.getString(displayNameIndex)
                }
            }
        }
        return result.ifEmpty { "document.pdf" }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


//
//import android.app.Activity
//import android.content.Context
//import android.net.Uri
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import com.example.noteminds.R
//import com.example.noteminds.Resource
//import com.example.noteminds.TokenManager
//import com.example.noteminds.data.api.ApiService
//import com.example.noteminds.data.api.RetrofitInstance
//import com.example.noteminds.data.model.PdfUploadResponse
//import com.example.noteminds.databinding.FragmentPdfUploadBinding
//import com.example.noteminds.vm.PdfUploadViewModel
//import com.github.dhaval2404.imagepicker.ImagePicker
//
//class PdfUploadFragment : Fragment() {
//    private var _binding: FragmentPdfUploadBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var viewModel: PdfUploadViewModel
//    private lateinit var apiService: ApiService
//    private var pdfUri: Uri? = null
//
//    private val pdfPickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            result.data?.data?.let { uri ->
//                pdfUri = uri
//                binding.pdfName.text = getFileName(uri)
//                binding.processButton.isEnabled = true
//            }
//        }
//    }
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        apiService = RetrofitInstance.api
//        viewModel = PdfUploadViewModel(apiService)
//        //viewModel = ViewModelProvider(this, PdfUploadViewModelFactory(apiService)).get(PdfUploadViewModel::class.java)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentPdfUploadBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        setupClickListeners()
//        setupObservers()
//    }
//
//    private fun setupClickListeners() {
//        binding.selectPdfButton.setOnClickListener {
//            ImagePicker.with(this)
//                .galleryOnly()
//                .galleryMimeTypes(arrayOf("application/pdf"))
//                .createIntent { intent ->
//                    pdfPickerLauncher.launch(intent)
//                }
//        }
//
//        binding.processButton.setOnClickListener {
//            pdfUri?.let { uri ->
//                val token = TokenManager.getToken(requireContext()) ?: ""
//                viewModel.processPdf(requireContext(), uri, token)
//            } ?: run {
//                Toast.makeText(requireContext(), "Please select a PDF first", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    // PdfUploadFragment.kt
//    private fun setupObservers() {
//        viewModel.uploadState.observe(viewLifecycleOwner) { state ->
//            when (state) {
//                is Resource.Loading -> {
//                    binding.progressBar.visibility = View.VISIBLE
//                    binding.processButton.isEnabled = false
//                }
//                is Resource.Success -> {
//                    binding.progressBar.visibility = View.GONE
//                    binding.processButton.isEnabled = true
//                    state.data?.let { response ->
//                        navigateToResults(response)
//                    }
//                }
//                is Resource.Error -> {
//                    binding.progressBar.visibility = View.GONE
//                    binding.processButton.isEnabled = true
//                    showError(state.message ?: "Unknown error occurred")
//                }
//            }
//        }
//    }
//    private fun showError(message: String) {
//        System.out.println("Error")
//    }
//
//    private fun navigateToResults(response: PdfUploadResponse) {
////        val bundle = Bundle().apply {
////            putString("summary", response.summary)
////            putParcelableArrayList("quiz", ArrayList(response.quiz))
////        }
//        System.out.println("Summary: ")
//        System.out.println(response.summary)
//        System.out.println("Quiz: ")
//        System.out.println(ArrayList(response.quiz))
//        showResults(response)
//    }
//
//    private fun showResults(response: PdfUploadResponse) {
////        val bundle = Bundle().apply {
////            putString("summary", response.summary)
////            putSerializable("quiz", ArrayList(response.quiz))
////        }
////        val resultsFragment = ResultsFragment(arguments = Bundle().apply {
////            putSerializable("quizResponses", ArrayList(response.quiz))
////        })
////
////        (activity as? MainActivity)?.navigateToFragment(resultsFragment)
//
//        // Navigate to results screen
////        (activity as? NavigationListener)?.navigateToFragment(
////            PdfResultsFragment().apply { arguments = bundle }
////        )
//    }
//
//    private fun getFileName(uri: Uri): String {
//        var result = ""
//        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
//        cursor?.use {
//            if (it.moveToFirst()) {
//                val displayNameIndex = it.getColumnIndex("_display_name")
//                if (displayNameIndex != -1) {
//                    result = it.getString(displayNameIndex)
//                }
//            }
//        }
//        return result.ifEmpty { "document.pdf" }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}