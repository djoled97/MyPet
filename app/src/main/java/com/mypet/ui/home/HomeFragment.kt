package com.mypet.ui.home

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mypet.MainActivity
import com.mypet.databinding.FragmentHomeBinding
import com.mypet.util.SharedPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private  val viewModel: HomeViewModel by viewModels()

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            (requireActivity() as MainActivity).sharedPreferencesHelper.updateProfileimageUri(uri.toString())
            binding.imageView.setImageURI(uri)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).sharedPreferencesHelper.profileImageUri?.let {
            binding.imageView.setImageURI(Uri.parse(it))
        }
        viewModel.getPet()
        binding.imageView.setOnClickListener { pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
        viewModel.petPairValueList.observe(viewLifecycleOwner){
            binding.recyclerview.layoutManager=LinearLayoutManager(context)
            binding.recyclerview.adapter=HomeAdapter(it)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}