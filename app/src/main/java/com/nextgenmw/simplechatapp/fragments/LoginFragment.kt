package com.nextgenmw.simplechatapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.nextgenmw.simplechatapp.App
import com.nextgenmw.simplechatapp.R
import com.nextgenmw.simplechatapp.databinding.FragmentFirstBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            if (binding.username.text.isNotEmpty()) {

                val user = binding.username.text.toString()

                App.user = user

                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

            } else {
                Toast.makeText(
                    requireContext(),
                    "Username should not be empty",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}