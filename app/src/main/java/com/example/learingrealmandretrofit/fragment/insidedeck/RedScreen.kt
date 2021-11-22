package com.example.learingrealmandretrofit.fragment.insidedeck

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.learingrealmandretrofit.databinding.RedScreenBinding

class RedScreen : Fragment() {

    private lateinit var binding: RedScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RedScreenBinding.inflate(layoutInflater)
        return binding.root
    }
}
