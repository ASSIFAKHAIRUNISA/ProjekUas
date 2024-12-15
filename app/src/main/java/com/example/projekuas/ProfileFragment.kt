package com.example.projekuas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.projekuas.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inisialisasi ViewBinding
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi PrefManager
        val prefManager = PrefManager.getInstance(requireContext())

        // Ambil username yang tersimpan di SharedPreferences
        val username = prefManager.getUsername()

        // Set teks "Welcome Username" di TextView
        val welcomeText = "Welcome $username"
        binding.textViewWelcome.text = welcomeText

        // Handle tombol logout
        binding.btnLogout.setOnClickListener {
            // Hapus data login dari PrefManager
            prefManager.clear()

            // Kembali ke LoginActivity
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
