package com.example.eppdraft1.main.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import com.example.eppdraft1.R

class CreateWorkoutFragment1(private var myInterface: OnTextEnteredListener) : Fragment() {

    private lateinit var autoCompleteTrainers : AutoCompleteTextView
    private lateinit var autoCompleteCapacity: AutoCompleteTextView
    private lateinit var autoCompleteDuration: AutoCompleteTextView
    private lateinit var editTextCreateWoName : EditText
    private lateinit var editTextCreateWoDetails: EditText

    private lateinit var onTextEnteredListener: OnTextEnteredListener

    private var textWatcherWoName: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {  }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {  }

        override fun afterTextChanged(s: Editable?) {
            onTextEnteredListener.onEditTextWoName(s.toString())
        }
    }

    private var textWatcherWoDetails: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {  }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }

        override fun afterTextChanged(s: Editable?) {
            onTextEnteredListener.onEditTextWoDetails(s.toString())
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment

        // Initializing the interface
        onTextEnteredListener = myInterface

        val view : View = inflater.inflate(R.layout.fragment_create_workout1, container, false)
        autoCompleteCapacity = view.findViewById(R.id.fragment_autocomplete_capacity)
        autoCompleteTrainers = view.findViewById(R.id.fragment_autocomplete_trainers)
        autoCompleteDuration = view.findViewById(R.id.fragment_autocomplete_duration)

        editTextCreateWoName = view.findViewById(R.id.fragment_et_create_workout_name)
        editTextCreateWoName.addTextChangedListener(textWatcherWoName)
        editTextCreateWoName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus){
                val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        editTextCreateWoDetails = view.findViewById(R.id.fragment_et_create_wo_details)
        editTextCreateWoDetails.addTextChangedListener(textWatcherWoDetails)
        editTextCreateWoDetails.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus){
                val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        // Capacity *** *** ***
        val capacities = arrayOf(requireActivity().resources.getString(R.string.athletes_min, "4"),
            requireActivity().resources.getString(R.string.athletes_min, "5"),
            requireActivity().resources.getString(R.string.athletes_min, "6"),
            requireActivity().resources.getString(R.string.athletes_min, "7"),
            requireActivity().resources.getString(R.string.athletes_min, "8"),
            requireActivity().resources.getString(R.string.athletes_min, "9"),
            requireActivity().resources.getString(R.string.athletes_min, "10"),
            requireActivity().resources.getString(R.string.athletes_min, "11"),
            requireActivity().resources.getString(R.string.athletes_min, "12"),
            requireActivity().resources.getString(R.string.athletes_min, "13"),
            requireActivity().resources.getString(R.string.athletes_min, "14"),
            requireActivity().resources.getString(R.string.athletes_min, "15"),
            requireActivity().resources.getString(R.string.athletes_min, "16"),
            "-")


        val adapterCapacities = ArrayAdapter(requireContext(), R.layout.dropdown_item, capacities)
        autoCompleteCapacity.setAdapter(adapterCapacities)
        autoCompleteCapacity.setOnItemClickListener { parent, _, position, _ ->
            hideSoftKeyboard()
            val selectedItem = parent.getItemAtPosition(position) as String
            onTextEnteredListener.onAutoCompleteCapacity(selectedItem)
        }

        // Duration *** *** ***
        val durations = arrayOf(requireActivity().resources.getString(R.string.duration_hours, "1", ""),
            requireActivity().resources.getString(R.string.duration_hours, "2", "s"),
            requireActivity().resources.getString(R.string.duration_hours, "3", "s"),
            requireActivity().resources.getString(R.string.duration_hours, "4", "s"))

        autoCompleteDuration.setText(requireActivity().resources.getString(R.string.duration_hours, "1", ""))

        val adapterDurations = ArrayAdapter(requireContext(), R.layout.dropdown_item, durations)
        autoCompleteDuration.setAdapter(adapterDurations)
        autoCompleteDuration.setOnItemClickListener { parent, _, position, _ ->
            hideSoftKeyboard()
            val selectedItem = parent.getItemAtPosition(position) as String
            onTextEnteredListener.onAutoCompleteDuration(selectedItem)
        }
        

        // Trainer names *** *** ***
        val trainerNames = arrayOf("Ernesto Carrera", "Darvin Castro", "José Salazar", "Gabriela Terán")
        val adapterTrainerNames = ArrayAdapter(requireContext(), R.layout.dropdown_item, trainerNames)
        autoCompleteTrainers.setAdapter(adapterTrainerNames)
        autoCompleteTrainers.setOnItemClickListener { parent, _, position, _ ->
            hideSoftKeyboard()
            val selectedItem = parent.getItemAtPosition(position) as String
            onTextEnteredListener.onAutoCompleteTrainers(selectedItem)
        }

        return view
    }

    private fun hideSoftKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = requireActivity().currentFocus ?: return
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    interface OnTextEnteredListener {
        fun onEditTextWoName(text: String)
        fun onEditTextWoDetails(text: String)
        fun onAutoCompleteDuration(durationFragment1: String)
        fun onAutoCompleteCapacity(capacity: String)
        fun onAutoCompleteTrainers(text: String)
    }


}