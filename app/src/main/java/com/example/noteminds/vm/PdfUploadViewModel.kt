package com.example.noteminds.vm

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteminds.data.api.ApiService
import androidx.lifecycle.viewModelScope
import com.example.noteminds.Resource
import com.example.noteminds.data.model.PdfUploadResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class PdfUploadViewModel(private val apiService: ApiService) : ViewModel() {
    private val _uploadState = MutableLiveData<Resource<PdfUploadResponse>>()
    val uploadState: LiveData<Resource<PdfUploadResponse>> get() = _uploadState

    fun processPdf(context: Context, uri: Uri, token: String) {
        _uploadState.value = Resource.Loading()

        viewModelScope.launch {
            try {
                val file = createTempFileFromUri(context, uri)
                val requestFile = file.asRequestBody("application/pdf".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("pdf", file.name, requestFile)

                val response = apiService.processPdf("Bearer $token", body)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _uploadState.postValue(Resource.Success(it))
                    } ?: run {
                        _uploadState.postValue(Resource.Error("Empty response from server"))
                    }
                } else {
                    _uploadState.postValue(Resource.Error("Server error: ${response.code()}"))
                }
            } catch (e: Exception) {
                _uploadState.postValue(Resource.Error("Network error: ${e.message}"))
            }
        }
    }

    private fun createTempFileFromUri(context: Context, uri: Uri): File {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val file = File.createTempFile("upload", ".pdf", context.cacheDir)
        FileOutputStream(file).use { output ->
            inputStream?.copyTo(output)
        }
        inputStream?.close()
        return file
    }
}