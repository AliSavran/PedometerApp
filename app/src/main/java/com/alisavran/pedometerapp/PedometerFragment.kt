package com.alisavran.pedometerapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.alisavran.pedometerapp.AppDatabase
import com.alisavran.pedometerapp.User
import com.alisavran.pedometerapp.UserDao
import com.alisavran.pedometerapp.databinding.FragmentPedometerBinding
import kotlinx.coroutines.launch

class PedometerFragment : Fragment(), SensorEventListener {

    private var _binding: FragmentPedometerBinding? = null
    private val binding get() = _binding!!

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var totalSteps = 0f
    private var previousTotalSteps = 0f

    private lateinit var userDao: UserDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPedometerBinding.inflate(inflater, container, false)
        userDao = AppDatabase.getDatabase(requireContext()).userDao()

        setupStepCounter()
        setupSimulateButton()
        loadUserDetails()

        return binding.root
    }

    private fun setupStepCounter() {
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Toast.makeText(requireContext(), "Adım sayar sensör bulunamadı!", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupSimulateButton() {
        binding.simulateStepButton.setOnClickListener {
            totalSteps++
            updateStepCountAndCalories()
        }
    }

    private fun loadUserDetails() {
        lifecycleScope.launch {

            val user = userDao.getLatestUser()
            user?.let {
                updateStepCountAndCalories(it)
            }
        }
    }

    private fun updateStepCountAndCalories(user: User? = null) {

        binding.stepCountTextView.text = totalSteps.toInt().toString()

        val calories = calculateCalories(

            user?.weight ?: 70.0,
            user?.tall ?: 170.0,
            user?.gender ?: "Diğer",
            totalSteps.toInt()
        )

        binding.calorieTextView.text = String.format("%.2f kcal", calories)
    }

    private fun calculateCalories(

        weight: Double,
        height: Double,
        gender: String,
        steps: Int

    ): Double {
        val calorieFactorPerStep = when (gender) {
            "Erkek" -> weight * 0.5 / 2000
            "Kadın" -> weight * 0.4 / 2000
            else -> weight * 0.45 / 2000
        }
        return steps * calorieFactorPerStep
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {

            if (it.sensor.type == Sensor.TYPE_STEP_COUNTER) {

                if (previousTotalSteps == 0f) {
                    previousTotalSteps = it.values[0]
                }
                totalSteps = it.values[0] - previousTotalSteps
                updateStepCountAndCalories()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onResume() {
        super.onResume()
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}