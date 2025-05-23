package com.example.noteminds.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.noteminds.data.repository.UserRepository
import com.example.noteminds.data.room.AppDatabase
import com.example.noteminds.databinding.FragmentProfileBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.content.FileProvider
import com.example.noteminds.NavigationListener
import java.io.File
import java.io.FileOutputStream

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var userId: Int = -1
    private lateinit var userRepository: UserRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun generateQRCode(content: String): Bitmap {
        val size = 512
        val bits = QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size)
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bmp.setPixel(x, y, if (bits[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }
    fun shareImage(bitmap: Bitmap, context: Context) {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val file = File(cachePath, "profile_qr.png")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/png"
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        context.startActivity(Intent.createChooser(intent, "Share Profile QR Code"))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = arguments?.getInt("userId", -1) ?: -1
        userRepository = UserRepository(AppDatabase.getDatabase(requireContext()).userDao())

        binding.cardViewIncorrectAns1.setOnClickListener{
            val profileFragment = UpgradeAccountFragment().apply {
                arguments = Bundle().apply {
                    putInt("userId", userId)
                }
            }
            (activity as? NavigationListener)?.navigateToFragment(profileFragment)
        }
        binding.materialButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.cardViewIncorrectAns.setOnClickListener {

            val historyFragment = HistoryFragment().apply {
                arguments = Bundle().apply {
                    putInt("userId", userId)
                }
            }
            (activity as? NavigationListener)?.navigateToFragment(historyFragment)
        }

        binding.btnShare.setOnClickListener {
//            lifecycleScope.launch {
//                try {
//                    val user = userRepository.getUserById(userId)
//                    System.out.println(user)
//                    user?.let {
                        val profileInfo = """
    🔹 *My Profile*
    
    👤 Name: Abdul Mueez
    📧 Email: test1@gmail.com
    📞 Phone: 123456
""".trimIndent()
                        val qrBitmap = generateQRCode(profileInfo)
                        shareImage(qrBitmap, requireContext())







//                        val shareIntent = Intent().apply {
//                            action = Intent.ACTION_SEND
//                            putExtra(Intent.EXTRA_TEXT, profileInfo)
//                            type = "text/plain"
//                        }
//                        startActivity(Intent.createChooser(shareIntent, "Share profile via"))
                 //   }

//                } catch (e: Exception) {
//                    Toast.makeText(requireContext(), "Error loading user data", Toast.LENGTH_SHORT).show()
//                }
            //}
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}