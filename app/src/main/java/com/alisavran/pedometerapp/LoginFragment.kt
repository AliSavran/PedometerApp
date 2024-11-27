package com.alisavran.pedometerapp

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.alisavran.pedometerapp.AppDatabase
import com.alisavran.pedometerapp.User
import com.alisavran.pedometerapp.UserDao
import com.alisavran.pedometerapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var userDao: UserDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        userDao = AppDatabase.getDatabase(requireContext()).userDao()

        val genders = arrayOf("Erkek", "Kadın", "Diğer")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, genders)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinner.adapter = adapter

        binding.loginButton.setOnClickListener {

            val name = binding.nameText.text.toString().trim()
            val surname = binding.surnameText.text.toString().trim()
            val age = binding.ageText.text.toString().trim()
            val tall = binding.tallText.text.toString().trim()
            val weight = binding.weightText.text.toString().trim()
            val gender = binding.genderSpinner.selectedItem.toString()

            if (validateInputs(name, surname, age, tall, weight)) {

                val user = User(
                    name = name,
                    surname = surname,
                    age = age.toInt(),
                    tall = tall.toDouble(),
                    weight = weight.toDouble(),
                    gender = gender
                )

                lifecycleScope.launch {

                    try {
                        userDao.insertUser(user)
                        Toast.makeText(requireContext(), "Kullanıcı kaydedildi", Toast.LENGTH_SHORT).show()


                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToPedometerFragment())
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Kayıt hatası: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return binding.root
    }

    private fun validateInputs(

        name: String,
        surname: String,
        age: String,
        tall: String,

        weight: String
    ): Boolean {
        return when {
            name.isEmpty() -> {
                Toast.makeText(requireContext(), "Ad boş bırakılamaz", Toast.LENGTH_SHORT).show()
                false
            }
            surname.isEmpty() -> {
                Toast.makeText(requireContext(), "Soyad boş bırakılamaz", Toast.LENGTH_SHORT).show()
                false
            }
            age.isEmpty() -> {
                Toast.makeText(requireContext(), "Yaş boş bırakılamaz", Toast.LENGTH_SHORT).show()
                false
            }
            tall.isEmpty() -> {
                Toast.makeText(requireContext(), "Boy boş bırakılamaz", Toast.LENGTH_SHORT).show()
                false
            }
            weight.isEmpty() -> {
                Toast.makeText(requireContext(), "Kilo boş bırakılamaz", Toast.LENGTH_SHORT).show()
                false
            }
            age.toIntOrNull() == null || age.toInt() < 5 || age.toInt() > 120 -> {
                Toast.makeText(requireContext(), "Geçerli bir yaş girin", Toast.LENGTH_SHORT).show()
                false
            }
            tall.toDoubleOrNull() == null || tall.toDouble() < 50 || tall.toDouble() > 250 -> {
                Toast.makeText(requireContext(), "Geçerli bir boy girin", Toast.LENGTH_SHORT).show()
                false
            }
            weight.toDoubleOrNull() == null || weight.toDouble() < 10 || weight.toDouble() > 300 -> {
                Toast.makeText(requireContext(), "Geçerli bir kilo girin", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}