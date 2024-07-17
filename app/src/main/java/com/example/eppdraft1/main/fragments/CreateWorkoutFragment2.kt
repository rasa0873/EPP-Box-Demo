package com.example.eppdraft1.main.fragments

import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.eppdraft1.R
import com.example.eppdraft1.main.activities.BaseActivity
import com.example.eppdraft1.main.activities.CreateWorkoutTabsActivity
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import kotlin.collections.ArrayList

// This fragment handles the calendar and time, for the creation of new workout(s)
// Hosting activity is CreateWorkoutTabsActivity

class CreateWorkoutFragment2(private var myInterface2: OnDateModifiedListener) : Fragment() {

    private lateinit var onDateModifiedListener: OnDateModifiedListener

    private lateinit var buttonPrevMonth: ImageView
    private lateinit var buttonNextMonth: ImageView
    private lateinit var monthYearText: TextView

    private lateinit var selectedDate : LocalDate

    private lateinit var btnSetTime : Button
    private var hour: Int = 0
    private var minute: Int = 0

    // New calendar TableLayout based
    private lateinit var buttonCal01: Button
    private lateinit var buttonCal02: Button
    private lateinit var buttonCal03: Button
    private lateinit var buttonCal04: Button
    private lateinit var buttonCal05: Button
    private lateinit var buttonCal06: Button
    private lateinit var buttonCal07: Button
    private lateinit var buttonCal08: Button
    private lateinit var buttonCal09: Button
    private lateinit var buttonCal10: Button

    private lateinit var buttonCal11: Button
    private lateinit var buttonCal12: Button
    private lateinit var buttonCal13: Button
    private lateinit var buttonCal14: Button
    private lateinit var buttonCal15: Button
    private lateinit var buttonCal16: Button
    private lateinit var buttonCal17: Button
    private lateinit var buttonCal18: Button
    private lateinit var buttonCal19: Button
    private lateinit var buttonCal20: Button

    private lateinit var buttonCal21: Button
    private lateinit var buttonCal22: Button
    private lateinit var buttonCal23: Button
    private lateinit var buttonCal24: Button
    private lateinit var buttonCal25: Button
    private lateinit var buttonCal26: Button
    private lateinit var buttonCal27: Button
    private lateinit var buttonCal28: Button
    private lateinit var buttonCal29: Button
    private lateinit var buttonCal30: Button

    private lateinit var buttonCal31: Button
    private lateinit var buttonCal32: Button
    private lateinit var buttonCal33: Button
    private lateinit var buttonCal34: Button
    private lateinit var buttonCal35: Button
    private lateinit var buttonCal36: Button
    private lateinit var buttonCal37: Button
    private lateinit var buttonCal38: Button
    private lateinit var buttonCal39: Button
    private lateinit var buttonCal40: Button

    private lateinit var buttonCal41: Button
    private lateinit var buttonCal42: Button

    private lateinit var daysInMonth : ArrayList<String>


    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?, ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_create_workout2, container, false)

        // Initialize the interface from viewPager2 setup
        onDateModifiedListener = myInterface2

        buttonPrevMonth = view.findViewById(R.id.button_fragment_prev_month)
        buttonNextMonth = view.findViewById(R.id.button_fragment_next_month)
        monthYearText = view.findViewById(R.id.fragment_monthYearTV)

        btnSetTime = view.findViewById(R.id.btn_select_time_frag2)
        btnSetTime.setOnClickListener {
            popTimePicker()
        }

        // Initialize selectedDay to show the current Month ONLY when the fragment is created
        selectedDate = LocalDate.now()

        buttonPrevMonth.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)

            onDateModifiedListener.monthChanged(
                selectedDate.year, // Int 2023
                convertMonthToNumber(selectedDate.month.toString()), // Int 11
                btnSetTime.text.toString()) // 5:00 PM

        }

        buttonNextMonth.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)

            onDateModifiedListener.monthChanged(
                selectedDate.year, // Int 2023
                convertMonthToNumber(selectedDate.month.toString()), // Int 11
                btnSetTime.text.toString()) // 5:00 PM

        }

        // Initial calendar day buttons wire to layout, onClickListeners and initial invisible status
        initCalendarTable(view)

        // Alert the CreateWorkoutTabsActivity about the initial time set, which is the current time
        // This causes the load of all workouts for the given year and month
        onDateModifiedListener.timePicked(selectedDate.year, // Int 2023
            convertMonthToNumber(selectedDate.month.toString()), // Int 11
            btnSetTime.text.toString() )  // 7:00 AM

        return view
    }


    override fun onResume() {
        super.onResume()
        setMonthView()
    }

    // onClick listeners with conditions from date buttons, are here
    // as well as layout wiring for each button
    private fun initCalendarTable(view: View) {

        buttonCal01 = view.findViewById(R.id.calendar_pos_1)
        buttonCal02 = view.findViewById(R.id.calendar_pos_2)
        buttonCal03 = view.findViewById(R.id.calendar_pos_3)
        buttonCal04 = view.findViewById(R.id.calendar_pos_4)
        buttonCal05 = view.findViewById(R.id.calendar_pos_5)
        buttonCal06 = view.findViewById(R.id.calendar_pos_6)
        buttonCal07 = view.findViewById(R.id.calendar_pos_7)
        buttonCal08 = view.findViewById(R.id.calendar_pos_8)
        buttonCal09 = view.findViewById(R.id.calendar_pos_9)
        buttonCal10 = view.findViewById(R.id.calendar_pos_10)

        buttonCal11 = view.findViewById(R.id.calendar_pos_11)
        buttonCal12 = view.findViewById(R.id.calendar_pos_12)
        buttonCal13 = view.findViewById(R.id.calendar_pos_13)
        buttonCal14 = view.findViewById(R.id.calendar_pos_14)
        buttonCal15 = view.findViewById(R.id.calendar_pos_15)
        buttonCal16 = view.findViewById(R.id.calendar_pos_16)
        buttonCal17 = view.findViewById(R.id.calendar_pos_17)
        buttonCal18 = view.findViewById(R.id.calendar_pos_18)
        buttonCal19 = view.findViewById(R.id.calendar_pos_19)
        buttonCal20 = view.findViewById(R.id.calendar_pos_20)

        buttonCal21 = view.findViewById(R.id.calendar_pos_21)
        buttonCal22 = view.findViewById(R.id.calendar_pos_22)
        buttonCal23 = view.findViewById(R.id.calendar_pos_23)
        buttonCal24 = view.findViewById(R.id.calendar_pos_24)
        buttonCal25 = view.findViewById(R.id.calendar_pos_25)
        buttonCal26 = view.findViewById(R.id.calendar_pos_26)
        buttonCal27 = view.findViewById(R.id.calendar_pos_27)
        buttonCal28 = view.findViewById(R.id.calendar_pos_28)
        buttonCal29 = view.findViewById(R.id.calendar_pos_29)
        buttonCal30 = view.findViewById(R.id.calendar_pos_30)

        buttonCal31 = view.findViewById(R.id.calendar_pos_31)
        buttonCal32 = view.findViewById(R.id.calendar_pos_32)
        buttonCal33 = view.findViewById(R.id.calendar_pos_33)
        buttonCal34 = view.findViewById(R.id.calendar_pos_34)
        buttonCal35 = view.findViewById(R.id.calendar_pos_35)
        buttonCal36 = view.findViewById(R.id.calendar_pos_36)
        buttonCal37 = view.findViewById(R.id.calendar_pos_37)
        buttonCal38 = view.findViewById(R.id.calendar_pos_38)
        buttonCal39 = view.findViewById(R.id.calendar_pos_39)
        buttonCal40 = view.findViewById(R.id.calendar_pos_40)

        buttonCal41 = view.findViewById(R.id.calendar_pos_41)
        buttonCal42 = view.findViewById(R.id.calendar_pos_42)

        // Only initially, set all calendar day buttons as invisible
        setButtonVisibility(false)

        buttonCal01.setOnClickListener {
            if (buttonCal01.text != "" && isButtonTextOnTime(buttonCal01.text.toString())) {
                val dayTouched : Int = buttonCal01.text.toString().toInt()

                when (buttonCal01.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal01.setTextColor(Color.WHITE)
                        buttonCal01.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal01.setTextColor(Color.BLACK)
                        buttonCal01.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal02.setOnClickListener {
            if (buttonCal02.text != "" && isButtonTextOnTime(buttonCal02.text.toString())) {
                val dayTouched : Int = buttonCal02.text.toString().toInt()

                when (buttonCal02.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal02.setTextColor(Color.WHITE)
                        buttonCal02.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal02.setTextColor(Color.BLACK)
                        buttonCal02.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal03.setOnClickListener {
            if (buttonCal03.text != "" && isButtonTextOnTime(buttonCal03.text.toString())) {
                val dayTouched : Int = buttonCal03.text.toString().toInt()

                when (buttonCal03.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal03.setTextColor(Color.WHITE)
                        buttonCal03.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal03.setTextColor(Color.BLACK)
                        buttonCal03.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal04.setOnClickListener {
            if (buttonCal04.text != "" && isButtonTextOnTime(buttonCal04.text.toString())) {
                val dayTouched : Int = buttonCal04.text.toString().toInt()

                when (buttonCal04.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal04.setTextColor(Color.WHITE)
                        buttonCal04.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal04.setTextColor(Color.BLACK)
                        buttonCal04.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal05.setOnClickListener {
            if (buttonCal05.text != "" && isButtonTextOnTime(buttonCal05.text.toString())) {
                val dayTouched : Int = buttonCal05.text.toString().toInt()

                when (buttonCal05.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal05.setTextColor(Color.WHITE)
                        buttonCal05.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal05.setTextColor(Color.BLACK)
                        buttonCal05.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal06.setOnClickListener {
            if (buttonCal06.text != "" && isButtonTextOnTime(buttonCal06.text.toString())) {
                val dayTouched : Int = buttonCal06.text.toString().toInt()

                when (buttonCal06.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal06.setTextColor(Color.WHITE)
                        buttonCal06.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal06.setTextColor(Color.BLACK)
                        buttonCal06.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal07.setOnClickListener {
            if (buttonCal07.text != "" && isButtonTextOnTime(buttonCal07.text.toString())) {

                val dayTouched : Int = buttonCal07.text.toString().toInt()

                when (buttonCal07.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal07.setTextColor(Color.WHITE)
                        buttonCal07.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal07.setTextColor(Color.BLACK)
                        buttonCal07.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal08.setOnClickListener {
            if (buttonCal08.text != "" && isButtonTextOnTime(buttonCal08.text.toString())) {

                val dayTouched : Int = buttonCal08.text.toString().toInt()

                when (buttonCal08.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal08.setTextColor(Color.WHITE)
                        buttonCal08.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal08.setTextColor(Color.BLACK)
                        buttonCal08.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal09.setOnClickListener {
            if (buttonCal09.text != "" && isButtonTextOnTime(buttonCal09.text.toString())) {

                val dayTouched : Int = buttonCal09.text.toString().toInt()

                when (buttonCal09.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal09.setTextColor(Color.WHITE)
                        buttonCal09.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal09.setTextColor(Color.BLACK)
                        buttonCal09.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal10.setOnClickListener {
            if (buttonCal10.text != "" && isButtonTextOnTime(buttonCal10.text.toString())) {

                val dayTouched : Int = buttonCal10.text.toString().toInt()

                when (buttonCal10.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal10.setTextColor(Color.WHITE)
                        buttonCal10.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal10.setTextColor(Color.BLACK)
                        buttonCal10.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal11.setOnClickListener {
            if (buttonCal11.text != "" && isButtonTextOnTime(buttonCal11.text.toString())) {

                val dayTouched : Int = buttonCal11.text.toString().toInt()

                when (buttonCal11.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal11.setTextColor(Color.WHITE)
                        buttonCal11.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal11.setTextColor(Color.BLACK)
                        buttonCal11.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal12.setOnClickListener {
            if (buttonCal12.text != "" && isButtonTextOnTime(buttonCal12.text.toString())) {
                val dayTouched : Int = buttonCal12.text.toString().toInt()

                when (buttonCal12.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal12.setTextColor(Color.WHITE)
                        buttonCal12.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal12.setTextColor(Color.BLACK)
                        buttonCal12.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal13.setOnClickListener {
            if (buttonCal13.text != "" && isButtonTextOnTime(buttonCal13.text.toString())) {
                val dayTouched : Int = buttonCal13.text.toString().toInt()

                when (buttonCal13.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal13.setTextColor(Color.WHITE)
                        buttonCal13.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal13.setTextColor(Color.BLACK)
                        buttonCal13.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal14.setOnClickListener {
            if (buttonCal14.text != "" && isButtonTextOnTime(buttonCal14.text.toString())) {
                val dayTouched : Int = buttonCal14.text.toString().toInt()

                when (buttonCal14.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal14.setTextColor(Color.WHITE)
                        buttonCal14.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal14.setTextColor(Color.BLACK)
                        buttonCal14.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal15.setOnClickListener {
            if (buttonCal15.text != "" && isButtonTextOnTime(buttonCal15.text.toString())) {
                val dayTouched : Int = buttonCal15.text.toString().toInt()

                when (buttonCal15.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal15.setTextColor(Color.WHITE)
                        buttonCal15.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal15.setTextColor(Color.BLACK)
                        buttonCal15.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal16.setOnClickListener {
            if (buttonCal16.text != "" && isButtonTextOnTime(buttonCal16.text.toString())) {
                val dayTouched : Int = buttonCal16.text.toString().toInt()

                when (buttonCal16.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal16.setTextColor(Color.WHITE)
                        buttonCal16.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal16.setTextColor(Color.BLACK)
                        buttonCal16.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal17.setOnClickListener {
            if (buttonCal17.text != "" && isButtonTextOnTime(buttonCal17.text.toString())) {
                val dayTouched : Int = buttonCal17.text.toString().toInt()

                when (buttonCal17.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal17.setTextColor(Color.WHITE)
                        buttonCal17.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal17.setTextColor(Color.BLACK)
                        buttonCal17.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal18.setOnClickListener {
            if (buttonCal18.text != "" && isButtonTextOnTime(buttonCal18.text.toString())) {
                val dayTouched : Int = buttonCal18.text.toString().toInt()

                when (buttonCal18.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal18.setTextColor(Color.WHITE)
                        buttonCal18.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal18.setTextColor(Color.BLACK)
                        buttonCal18.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal19.setOnClickListener {
            if (buttonCal19.text != "" && isButtonTextOnTime(buttonCal19.text.toString())) {
                val dayTouched : Int = buttonCal19.text.toString().toInt()

                when (buttonCal19.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal19.setTextColor(Color.WHITE)
                        buttonCal19.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal19.setTextColor(Color.BLACK)
                        buttonCal19.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal20.setOnClickListener {
            if (buttonCal20.text != "" && isButtonTextOnTime(buttonCal20.text.toString())) {
                val dayTouched : Int = buttonCal20.text.toString().toInt()

                when (buttonCal20.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal20.setTextColor(Color.WHITE)
                        buttonCal20.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal20.setTextColor(Color.BLACK)
                        buttonCal20.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal21.setOnClickListener {
            if (buttonCal21.text != "" && isButtonTextOnTime(buttonCal21.text.toString())) {
                val dayTouched : Int = buttonCal21.text.toString().toInt()

                when (buttonCal21.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal21.setTextColor(Color.WHITE)
                        buttonCal21.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal21.setTextColor(Color.BLACK)
                        buttonCal21.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal22.setOnClickListener {
            if (buttonCal22.text != "" && isButtonTextOnTime(buttonCal22.text.toString())) {
                val dayTouched : Int = buttonCal22.text.toString().toInt()

                when (buttonCal22.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal22.setTextColor(Color.WHITE)
                        buttonCal22.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal22.setTextColor(Color.BLACK)
                        buttonCal22.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal23.setOnClickListener {
            if (buttonCal23.text != "" && isButtonTextOnTime(buttonCal23.text.toString())) {
                val dayTouched : Int = buttonCal23.text.toString().toInt()

                when (buttonCal23.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal23.setTextColor(Color.WHITE)
                        buttonCal23.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal23.setTextColor(Color.BLACK)
                        buttonCal23.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal24.setOnClickListener {
            if (buttonCal24.text != "" && isButtonTextOnTime(buttonCal24.text.toString())) {
                val dayTouched : Int = buttonCal24.text.toString().toInt()

                when (buttonCal24.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal24.setTextColor(Color.WHITE)
                        buttonCal24.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal24.setTextColor(Color.BLACK)
                        buttonCal24.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal25.setOnClickListener {
            if (buttonCal25.text != "" && isButtonTextOnTime(buttonCal25.text.toString())) {
                val dayTouched : Int = buttonCal25.text.toString().toInt()

                when (buttonCal25.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal25.setTextColor(Color.WHITE)
                        buttonCal25.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal25.setTextColor(Color.BLACK)
                        buttonCal25.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal26.setOnClickListener {
            if (buttonCal26.text != "" && isButtonTextOnTime(buttonCal26.text.toString())) {
                val dayTouched : Int = buttonCal26.text.toString().toInt()

                when (buttonCal26.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal26.setTextColor(Color.WHITE)
                        buttonCal26.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal26.setTextColor(Color.BLACK)
                        buttonCal26.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal27.setOnClickListener {
            if (buttonCal27.text != "" && isButtonTextOnTime(buttonCal27.text.toString())) {
                val dayTouched : Int = buttonCal27.text.toString().toInt()

                when (buttonCal27.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal27.setTextColor(Color.WHITE)
                        buttonCal27.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal27.setTextColor(Color.BLACK)
                        buttonCal27.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal28.setOnClickListener {
            if (buttonCal28.text != "" && isButtonTextOnTime(buttonCal28.text.toString())) {
                val dayTouched : Int = buttonCal28.text.toString().toInt()

                when (buttonCal28.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal28.setTextColor(Color.WHITE)
                        buttonCal28.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal28.setTextColor(Color.BLACK)
                        buttonCal28.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal29.setOnClickListener {
            if (buttonCal29.text != "" && isButtonTextOnTime(buttonCal29.text.toString())) {
                val dayTouched : Int = buttonCal29.text.toString().toInt()

                when (buttonCal29.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal29.setTextColor(Color.WHITE)
                        buttonCal29.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal29.setTextColor(Color.BLACK)
                        buttonCal29.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal30.setOnClickListener {
            if (buttonCal30.text != "" && isButtonTextOnTime(buttonCal30.text.toString())) {
                val dayTouched : Int = buttonCal30.text.toString().toInt()

                when (buttonCal30.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal30.setTextColor(Color.WHITE)
                        buttonCal30.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal30.setTextColor(Color.BLACK)
                        buttonCal30.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal31.setOnClickListener {
            if (buttonCal31.text != "" && isButtonTextOnTime(buttonCal31.text.toString())) {
                val dayTouched : Int = buttonCal31.text.toString().toInt()

                when (buttonCal31.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal31.setTextColor(Color.WHITE)
                        buttonCal31.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal31.setTextColor(Color.BLACK)
                        buttonCal31.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal32.setOnClickListener {
            if (buttonCal32.text != "" && isButtonTextOnTime(buttonCal32.text.toString())) {
                val dayTouched : Int = buttonCal32.text.toString().toInt()

                when (buttonCal32.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal32.setTextColor(Color.WHITE)
                        buttonCal32.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal32.setTextColor(Color.BLACK)
                        buttonCal32.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal33.setOnClickListener {
            if (buttonCal33.text != "" && isButtonTextOnTime(buttonCal33.text.toString())) {
                val dayTouched : Int = buttonCal33.text.toString().toInt()

                when (buttonCal33.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal33.setTextColor(Color.WHITE)
                        buttonCal33.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal33.setTextColor(Color.BLACK)
                        buttonCal33.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal34.setOnClickListener {
            if (buttonCal34.text != "" && isButtonTextOnTime(buttonCal34.text.toString())) {
                val dayTouched : Int = buttonCal34.text.toString().toInt()

                when (buttonCal34.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal34.setTextColor(Color.WHITE)
                        buttonCal34.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal34.setTextColor(Color.BLACK)
                        buttonCal34.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal35.setOnClickListener {
            if (buttonCal35.text != "" && isButtonTextOnTime(buttonCal35.text.toString())) {
                val dayTouched : Int = buttonCal35.text.toString().toInt()

                when (buttonCal35.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal35.setTextColor(Color.WHITE)
                        buttonCal35.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal35.setTextColor(Color.BLACK)
                        buttonCal35.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal36.setOnClickListener {
            if (buttonCal36.text != "" && isButtonTextOnTime(buttonCal36.text.toString())) {
                val dayTouched : Int = buttonCal36.text.toString().toInt()

                when (buttonCal36.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal36.setTextColor(Color.WHITE)
                        buttonCal36.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal36.setTextColor(Color.BLACK)
                        buttonCal36.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal37.setOnClickListener {
            if (buttonCal37.text != "" && isButtonTextOnTime(buttonCal37.text.toString())) {
                val dayTouched : Int = buttonCal37.text.toString().toInt()

                when (buttonCal37.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal37.setTextColor(Color.WHITE)
                        buttonCal37.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal37.setTextColor(Color.BLACK)
                        buttonCal37.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal38.setOnClickListener {
            if (buttonCal38.text != "" && isButtonTextOnTime(buttonCal38.text.toString())) {
                val dayTouched : Int = buttonCal38.text.toString().toInt()

                when (buttonCal38.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal38.setTextColor(Color.WHITE)
                        buttonCal38.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal38.setTextColor(Color.BLACK)
                        buttonCal38.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal39.setOnClickListener {
            if (buttonCal39.text != "" && isButtonTextOnTime(buttonCal39.text.toString())) {
                val dayTouched : Int = buttonCal39.text.toString().toInt()

                when (buttonCal39.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal39.setTextColor(Color.WHITE)
                        buttonCal39.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal39.setTextColor(Color.BLACK)
                        buttonCal39.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal40.setOnClickListener {
            if (buttonCal40.text != "" && isButtonTextOnTime(buttonCal40.text.toString())) {
                val dayTouched : Int = buttonCal40.text.toString().toInt()

                when (buttonCal40.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal40.setTextColor(Color.WHITE)
                        buttonCal40.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal40.setTextColor(Color.BLACK)
                        buttonCal40.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal41.setOnClickListener {
            if (buttonCal41.text != "" && isButtonTextOnTime(buttonCal41.text.toString())) {
                val dayTouched : Int = buttonCal41.text.toString().toInt()

                when (buttonCal41.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal41.setTextColor(Color.WHITE)
                        buttonCal41.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal41.setTextColor(Color.BLACK)
                        buttonCal41.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }

        buttonCal42.setOnClickListener {
            if (buttonCal42.text != "" && isButtonTextOnTime(buttonCal42.text.toString())) {
                val dayTouched : Int = buttonCal42.text.toString().toInt()

                when (buttonCal42.currentTextColor) {
                    Color.BLACK -> {
                        // Not selected yet
                        buttonCal42.setTextColor(Color.WHITE)
                        buttonCal42.setBackgroundResource(R.drawable.rounded_corners_blue)
                        onDateModifiedListener.dateAdded(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    Color.WHITE -> {
                        // Already selected
                        buttonCal42.setTextColor(Color.BLACK)
                        buttonCal42.setBackgroundResource(R.drawable.rounded_corners_white)
                        onDateModifiedListener.dateRemoved(getTempDateTouched(dayTouched)) // sending a LocalDate value
                    }
                    else -> {
                        activity?.resources?.let { it1 -> onDateModifiedListener.showSnack(it1.getString(R.string.date_already_reserved), true) }
                    }
                }
            }
        }
    }

    // Set month title and calendar look based on selectedDate
    fun setMonthView() {
        // Month and Year textView heading
        monthYearText.text = monthYearFromDate(selectedDate).replaceFirstChar { it.titlecase() }

        // this is the array of days, including some blank entries. Either 35 or 42 items in the array
        daysInMonth = daysInMonthArray(selectedDate)

        displayCalendarButtons() // day text number to each calendar button
        updateButtonColors() // set each calendar button appearance

        // after buttons are set, switch them to visible
        setButtonVisibility(true)
    }

    private fun monthYearFromDate(date: LocalDate) : String {
        val formatter : DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<String> {

        val daysInMonthArray : ArrayList<String> = ArrayList()
        val yearMonth : YearMonth = YearMonth.from(date)
        val daysInMonth : Int = yearMonth.lengthOfMonth()

        val firstDayOfMonth : LocalDate = selectedDate.withDayOfMonth(1)
        val dayOfWeek : Int = firstDayOfMonth.dayOfWeek.value

        if (dayOfWeek == 7) {
            for (i in 1..42){
                if (i > daysInMonth ){
                    daysInMonthArray.add("")
                } else {
                    daysInMonthArray.add((i).toString())
                }
            }
        } else {
            for (i in 1..42) {
                if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                    daysInMonthArray.add("")
                } else {
                    daysInMonthArray.add((i - dayOfWeek).toString())
                }
            }
        }

        return daysInMonthArray

    }

    // Display day number to each calendar button
    private fun displayCalendarButtons() {
        for (i in daysInMonth.indices){
            when (i){
                0 -> {
                    buttonCal01.text = daysInMonth[i]
                }
                1 -> {
                    buttonCal02.text = daysInMonth[i]
                }
                2 -> {
                    buttonCal03.text = daysInMonth[i]
                }
                3 -> {
                    buttonCal04.text = daysInMonth[i]
                }
                4 -> {
                    buttonCal05.text = daysInMonth[i]
                }
                5 -> {
                    buttonCal06.text = daysInMonth[i]
                }
                6 -> {
                    buttonCal07.text = daysInMonth[i]
                }
                7 -> {
                    buttonCal08.text = daysInMonth[i]
                }
                8 -> {
                    buttonCal09.text = daysInMonth[i]
                }
                9 -> {
                    buttonCal10.text = daysInMonth[i]
                }
                10 -> {
                    buttonCal11.text = daysInMonth[i]
                }
                11 -> {
                    buttonCal12.text = daysInMonth[i]
                }
                12 -> {
                    buttonCal13.text = daysInMonth[i]
                }
                13 -> {
                    buttonCal14.text = daysInMonth[i]
                }
                14 -> {
                    buttonCal15.text = daysInMonth[i]
                }
                15 -> {
                    buttonCal16.text = daysInMonth[i]
                }
                16 -> {
                    buttonCal17.text = daysInMonth[i]
                }
                17 -> {
                    buttonCal18.text = daysInMonth[i]
                }
                18 -> {
                    buttonCal19.text = daysInMonth[i]
                }
                19 -> {
                    buttonCal20.text = daysInMonth[i]
                }
                20 -> {
                    buttonCal21.text = daysInMonth[i]
                }
                21 -> {
                    buttonCal22.text = daysInMonth[i]
                }
                22 -> {
                    buttonCal23.text = daysInMonth[i]
                }
                23 -> {
                    buttonCal24.text = daysInMonth[i]
                }
                24 -> {
                    buttonCal25.text = daysInMonth[i]
                }
                25 -> {
                    buttonCal26.text = daysInMonth[i]
                }
                26 -> {
                    buttonCal27.text = daysInMonth[i]
                }
                27 -> {
                    buttonCal28.text = daysInMonth[i]
                }
                28 -> {
                    buttonCal29.text = daysInMonth[i]
                }
                29 -> {
                    buttonCal30.text = daysInMonth[i]
                }
                30 -> {
                    buttonCal31.text = daysInMonth[i]
                }
                31 -> {
                    buttonCal32.text = daysInMonth[i]
                }
                32 -> {
                    buttonCal33.text = daysInMonth[i]
                }
                33 -> {
                    buttonCal34.text = daysInMonth[i]
                }
                34 -> {
                    buttonCal35.text = daysInMonth[i]
                }
                35 -> {
                    buttonCal36.text = daysInMonth[i]
                }
                36 -> {
                    buttonCal37.text = daysInMonth[i]
                }
                37 -> {
                    buttonCal38.text = daysInMonth[i]
                }
                38 -> {
                    buttonCal39.text = daysInMonth[i]
                }
                39 -> {
                    buttonCal40.text = daysInMonth[i]
                }
                40 -> {
                    buttonCal41.text = daysInMonth[i]
                }
                41 -> {
                    buttonCal42.text = daysInMonth[i]
                }

            }
        }

    }

    private fun updateButtonColors() {
        val datesPicked = CreateWorkoutTabsActivity.datesPicked
        val tempDate = selectedDate

        // Button 01
        if (buttonCal01.text != "") {
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal01.text.toString().toInt()))){
                buttonCal01.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal01.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal01.text.toString())){
                buttonCal01.setTextColor(Color.BLUE)
                buttonCal01.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal01.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal01.setTextColor(Color.WHITE)
                    buttonCal01.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal01.setTextColor(Color.BLACK)
                    buttonCal01.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal01.text.toString())){
                buttonCal01.alpha = 1.0f
            } else {
                buttonCal01.alpha = 0.4f
            }
        } else {
            buttonCal01.setTextColor(Color.BLACK)
            buttonCal01.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 02
        if (buttonCal02.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal02.text.toString().toInt()))){
                buttonCal02.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal02.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal02.text.toString())){
                buttonCal02.setTextColor(Color.BLUE)
                buttonCal02.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal02.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal02.setTextColor(Color.WHITE)
                    buttonCal02.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal02.setTextColor(Color.BLACK)
                    buttonCal02.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal02.text.toString())){
                buttonCal02.alpha = 1.0f
            } else {
                buttonCal02.alpha = 0.4f
            }

        } else {
            buttonCal02.setTextColor(Color.BLACK)
            buttonCal02.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 03
        if (buttonCal03.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal03.text.toString().toInt()))){
                buttonCal03.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal03.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal03.text.toString())){
                buttonCal03.setTextColor(Color.BLUE)
                buttonCal03.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal03.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal03.setTextColor(Color.WHITE)
                    buttonCal03.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal03.setTextColor(Color.BLACK)
                    buttonCal03.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal03.text.toString())){
                buttonCal03.alpha = 1.0f
            } else {
                buttonCal03.alpha = 0.4f
            }
        } else {
            buttonCal03.setTextColor(Color.BLACK)
            buttonCal03.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 04
        if (buttonCal04.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal04.text.toString().toInt()))){
                buttonCal04.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal04.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal04.text.toString())){
                buttonCal04.setTextColor(Color.BLUE)
                buttonCal04.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal04.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal04.setTextColor(Color.WHITE)
                    buttonCal04.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal04.setTextColor(Color.BLACK)
                    buttonCal04.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal04.text.toString())){
                buttonCal04.alpha = 1.0f
            } else {
                buttonCal04.alpha = 0.4f
            }
        } else {
            buttonCal04.setTextColor(Color.BLACK)
            buttonCal04.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 05
        if (buttonCal05.text.toString() != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal05.text.toString().toInt()))){
                buttonCal05.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal05.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal05.text.toString())){
                buttonCal05.setTextColor(Color.BLUE)
                buttonCal05.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal05.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal05.setTextColor(Color.WHITE)
                    buttonCal05.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal05.setTextColor(Color.BLACK)
                    buttonCal05.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal05.text.toString())){
                buttonCal05.alpha = 1.0f
            } else {
                buttonCal05.alpha = 0.4f
            }
        } else {
            buttonCal05.setTextColor(Color.BLACK)
            buttonCal05.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 06
        if (buttonCal06.text != ""){

            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal06.text.toString().toInt()))){
                buttonCal06.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal06.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal06.text.toString())){
                buttonCal06.setTextColor(Color.BLUE)
                buttonCal06.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(tempDate.withDayOfMonth(buttonCal06.text.toString().toInt()))) {
                    buttonCal06.setTextColor(Color.WHITE)
                    buttonCal06.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal06.setTextColor(Color.BLACK)
                    buttonCal06.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal06.text.toString())){
                buttonCal06.alpha = 1.0f
            } else {
                buttonCal06.alpha = 0.4f
            }
        } else {
            buttonCal06.setTextColor(Color.BLACK)
            buttonCal06.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 07
        if (buttonCal07.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal07.text.toString().toInt()))){
                buttonCal07.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal07.setTextAppearance(R.style.calendar_number)
            }

            if (timesOverlap(buttonCal07.text.toString())){
                buttonCal07.setTextColor(Color.BLUE)
                buttonCal07.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal07.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal07.setTextColor(Color.WHITE)
                    buttonCal07.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal07.setTextColor(Color.BLACK)
                    buttonCal07.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal07.text.toString())){
                buttonCal07.alpha = 1.0f
            } else {
                buttonCal07.alpha = 0.4f
            }
        } else {
            buttonCal07.setTextColor(Color.BLACK)
            buttonCal07.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 08
        if (buttonCal08.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal08.text.toString().toInt()))){
                buttonCal08.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal08.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal08.text.toString())){
                buttonCal08.setTextColor(Color.BLUE)
                buttonCal08.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal08.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal08.setTextColor(Color.WHITE)
                    buttonCal08.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal08.setTextColor(Color.BLACK)
                    buttonCal08.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal08.text.toString())){
                buttonCal08.alpha = 1.0f
            } else {
                buttonCal08.alpha = 0.4f
            }
        } else {
            buttonCal08.setTextColor(Color.BLACK)
            buttonCal08.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 09
        if (buttonCal09.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal09.text.toString().toInt()))){
                buttonCal09.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal09.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal09.text.toString())){
                buttonCal09.setTextColor(Color.BLUE)
                buttonCal09.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal09.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal09.setTextColor(Color.WHITE)
                    buttonCal09.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal09.setTextColor(Color.BLACK)
                    buttonCal09.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal09.text.toString())){
                buttonCal09.alpha = 1.0f
            } else {
                buttonCal09.alpha = 0.4f
            }
        } else {
            buttonCal09.setTextColor(Color.BLACK)
            buttonCal09.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 10
        if (buttonCal10.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal10.text.toString().toInt()))){
                buttonCal10.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal10.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal10.text.toString())){
                buttonCal10.setTextColor(Color.BLUE)
                buttonCal10.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        selectedDate.withDayOfMonth(
                            buttonCal10.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal10.setTextColor(Color.WHITE)
                    buttonCal10.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal10.setTextColor(Color.BLACK)
                    buttonCal10.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal10.text.toString())){
                buttonCal10.alpha = 1.0f
            } else {
                buttonCal10.alpha = 0.4f
            }
        } else {
            buttonCal10.setTextColor(Color.BLACK)
            buttonCal10.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 11
        if (buttonCal11.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal11.text.toString().toInt()))){
                buttonCal11.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal11.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal11.text.toString())){
                buttonCal11.setTextColor(Color.BLUE)
                buttonCal11.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal11.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal11.setTextColor(Color.WHITE)
                    buttonCal11.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal11.setTextColor(Color.BLACK)
                    buttonCal11.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal11.text.toString())){
                buttonCal11.alpha = 1.0f
            } else {
                buttonCal11.alpha = 0.4f
            }
        } else {
            buttonCal11.setTextColor(Color.BLACK)
            buttonCal11.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 12
        if (buttonCal12.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal12.text.toString().toInt()))){
                buttonCal12.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal12.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal12.text.toString())){
                buttonCal12.setTextColor(Color.BLUE)
                buttonCal12.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal12.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal12.setTextColor(Color.WHITE)
                    buttonCal12.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal12.setTextColor(Color.BLACK)
                    buttonCal12.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal12.text.toString())){
                buttonCal12.alpha = 1.0f
            } else {
                buttonCal12.alpha = 0.4f
            }
        } else {
            buttonCal12.setTextColor(Color.BLACK)
            buttonCal12.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 13
        if (buttonCal13.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal13.text.toString().toInt()))){
                buttonCal13.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal13.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal13.text.toString())){
                buttonCal13.setTextColor(Color.BLUE)
                buttonCal13.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal13.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal13.setTextColor(Color.WHITE)
                    buttonCal13.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal13.setTextColor(Color.BLACK)
                    buttonCal13.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal13.text.toString())){
                buttonCal13.alpha = 1.0f
            } else {
                buttonCal13.alpha = 0.4f
            }
        } else {
            buttonCal13.setTextColor(Color.BLACK)
            buttonCal13.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 14
        if (buttonCal14.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal14.text.toString().toInt()))){
                buttonCal14.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal14.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal14.text.toString())){
                buttonCal14.setTextColor(Color.BLUE)
                buttonCal14.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal14.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal14.setTextColor(Color.WHITE)
                    buttonCal14.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal14.setTextColor(Color.BLACK)
                    buttonCal14.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal14.text.toString())){
                buttonCal14.alpha = 1.0f
            } else {
                buttonCal14.alpha = 0.4f
            }
        } else {
            buttonCal14.setTextColor(Color.BLACK)
            buttonCal14.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 15
        if (buttonCal15.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal15.text.toString().toInt()))){
                buttonCal15.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal15.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal15.text.toString())){
                buttonCal15.setTextColor(Color.BLUE)
                buttonCal15.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal15.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal15.setTextColor(Color.WHITE)
                    buttonCal15.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal15.setTextColor(Color.BLACK)
                    buttonCal15.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal15.text.toString())){
                buttonCal15.alpha = 1.0f
            } else {
                buttonCal15.alpha = 0.4f
            }
        } else {
            buttonCal15.setTextColor(Color.BLACK)
            buttonCal15.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 16
        if (buttonCal16.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal16.text.toString().toInt()))){
                buttonCal16.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal16.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal16.text.toString())){
                buttonCal16.setTextColor(Color.BLUE)
                buttonCal16.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal16.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal16.setTextColor(Color.WHITE)
                    buttonCal16.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal16.setTextColor(Color.BLACK)
                    buttonCal16.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal16.text.toString())){
                buttonCal16.alpha = 1.0f
            } else {
                buttonCal16.alpha = 0.4f
            }
        } else {
            buttonCal16.setTextColor(Color.BLACK)
            buttonCal16.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 17
        if (buttonCal17.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal17.text.toString().toInt()))){
                buttonCal17.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal17.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal17.text.toString())){
                buttonCal17.setTextColor(Color.BLUE)
                buttonCal17.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal17.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal17.setTextColor(Color.WHITE)
                    buttonCal17.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal17.setTextColor(Color.BLACK)
                    buttonCal17.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal17.text.toString())){
                buttonCal17.alpha = 1.0f
            } else {
                buttonCal17.alpha = 0.4f
            }
        } else {
            buttonCal17.setTextColor(Color.BLACK)
            buttonCal17.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 18
        if (buttonCal18.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal18.text.toString().toInt()))){
                buttonCal18.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal18.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal18.text.toString())){
                buttonCal18.setTextColor(Color.BLUE)
                buttonCal18.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal18.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal18.setTextColor(Color.WHITE)
                    buttonCal18.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal18.setTextColor(Color.BLACK)
                    buttonCal18.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal18.text.toString())){
                buttonCal18.alpha = 1.0f
            } else {
                buttonCal18.alpha = 0.4f
            }
        } else {
            buttonCal18.setTextColor(Color.BLACK)
            buttonCal18.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 19
        if (buttonCal19.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal19.text.toString().toInt()))){
                buttonCal19.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal19.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal19.text.toString())){
                buttonCal19.setTextColor(Color.BLUE)
                buttonCal19.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal19.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal19.setTextColor(Color.WHITE)
                    buttonCal19.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal19.setTextColor(Color.BLACK)
                    buttonCal19.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal19.text.toString())){
                buttonCal19.alpha = 1.0f
            } else {
                buttonCal19.alpha = 0.4f
            }
        } else {
            buttonCal19.setTextColor(Color.BLACK)
            buttonCal19.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 20
        if (buttonCal20.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal20.text.toString().toInt()))){
                buttonCal20.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal20.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal20.text.toString())){
                buttonCal20.setTextColor(Color.BLUE)
                buttonCal20.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal20.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal20.setTextColor(Color.WHITE)
                    buttonCal20.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal20.setTextColor(Color.BLACK)
                    buttonCal20.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal20.text.toString())){
                buttonCal20.alpha = 1.0f
            } else {
                buttonCal20.alpha = 0.4f
            }
        } else {
            buttonCal20.setTextColor(Color.BLACK)
            buttonCal20.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 21
        if (buttonCal21.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal21.text.toString().toInt()))){
                buttonCal21.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal21.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal21.text.toString())){
                buttonCal21.setTextColor(Color.BLUE)
                buttonCal21.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal21.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal21.setTextColor(Color.WHITE)
                    buttonCal21.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal21.setTextColor(Color.BLACK)
                    buttonCal21.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal21.text.toString())){
                buttonCal21.alpha = 1.0f
            } else {
                buttonCal21.alpha = 0.4f
            }
        } else {
            buttonCal21.setTextColor(Color.BLACK)
            buttonCal21.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 22
        if (buttonCal22.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal22.text.toString().toInt()))){
                buttonCal22.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal22.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal22.text.toString())){
                buttonCal22.setTextColor(Color.BLUE)
                buttonCal22.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal22.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal22.setTextColor(Color.WHITE)
                    buttonCal22.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal22.setTextColor(Color.BLACK)
                    buttonCal22.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal22.text.toString())){
                buttonCal22.alpha = 1.0f
            } else {
                buttonCal22.alpha = 0.4f
            }
        } else {
            buttonCal22.setTextColor(Color.BLACK)
            buttonCal22.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 23
        if (buttonCal23.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal23.text.toString().toInt()))){
                buttonCal23.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal23.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal23.text.toString())){
                buttonCal23.setTextColor(Color.BLUE)
                buttonCal23.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal23.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal23.setTextColor(Color.WHITE)
                    buttonCal23.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal23.setTextColor(Color.BLACK)
                    buttonCal23.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal23.text.toString())){
                buttonCal23.alpha = 1.0f
            } else {
                buttonCal23.alpha = 0.4f
            }
        } else {
            buttonCal23.setTextColor(Color.BLACK)
            buttonCal23.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 24
        if (buttonCal24.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal24.text.toString().toInt()))){
                buttonCal24.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal24.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal24.text.toString())){
                buttonCal24.setTextColor(Color.BLUE)
                buttonCal24.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal24.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal24.setTextColor(Color.WHITE)
                    buttonCal24.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal24.setTextColor(Color.BLACK)
                    buttonCal24.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal24.text.toString())){
                buttonCal24.alpha = 1.0f
            } else {
                buttonCal24.alpha = 0.4f
            }
        } else {
            buttonCal24.setTextColor(Color.BLACK)
            buttonCal24.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 25
        if (buttonCal25.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal25.text.toString().toInt()))){
                buttonCal25.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal25.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal25.text.toString())){
                buttonCal25.setTextColor(Color.BLUE)
                buttonCal25.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal25.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal25.setTextColor(Color.WHITE)
                    buttonCal25.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal25.setTextColor(Color.BLACK)
                    buttonCal25.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal25.text.toString())){
                buttonCal25.alpha = 1.0f
            } else {
                buttonCal25.alpha = 0.4f
            }
        } else {
            buttonCal25.setTextColor(Color.BLACK)
            buttonCal25.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 26
        if (buttonCal26.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal26.text.toString().toInt()))){
                buttonCal26.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal26.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal26.text.toString())){
                buttonCal26.setTextColor(Color.BLUE)
                buttonCal26.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal26.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal26.setTextColor(Color.WHITE)
                    buttonCal26.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal26.setTextColor(Color.BLACK)
                    buttonCal26.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal26.text.toString())){
                buttonCal26.alpha = 1.0f
            } else {
                buttonCal26.alpha = 0.4f
            }
        } else {
            buttonCal26.setTextColor(Color.BLACK)
            buttonCal26.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 27
        if (buttonCal27.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal27.text.toString().toInt()))){
                buttonCal27.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal27.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal27.text.toString())){
                buttonCal27.setTextColor(Color.BLUE)
                buttonCal27.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal27.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal27.setTextColor(Color.WHITE)
                    buttonCal27.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal27.setTextColor(Color.BLACK)
                    buttonCal27.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal27.text.toString())){
                buttonCal27.alpha = 1.0f
            } else {
                buttonCal27.alpha = 0.4f
            }
        } else {
            buttonCal27.setTextColor(Color.BLACK)
            buttonCal27.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 28
        if (buttonCal28.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal28.text.toString().toInt()))){
                buttonCal28.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal28.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal28.text.toString())){
                buttonCal28.setTextColor(Color.BLUE)
                buttonCal28.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal28.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal28.setTextColor(Color.WHITE)
                    buttonCal28.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal28.setTextColor(Color.BLACK)
                    buttonCal28.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal28.text.toString())){
                buttonCal28.alpha = 1.0f
            } else {
                buttonCal28.alpha = 0.4f
            }
        } else {
            buttonCal28.setTextColor(Color.BLACK)
            buttonCal28.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 29
        if (buttonCal29.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal29.text.toString().toInt()))){
                buttonCal29.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal29.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal29.text.toString())){
                buttonCal29.setTextColor(Color.BLUE)
                buttonCal29.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal29.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal29.setTextColor(Color.WHITE)
                    buttonCal29.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal29.setTextColor(Color.BLACK)
                    buttonCal29.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal29.text.toString())){
                buttonCal29.alpha = 1.0f
            } else {
                buttonCal29.alpha = 0.4f
            }
        } else {
            buttonCal29.setTextColor(Color.BLACK)
            buttonCal29.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 30
        if (buttonCal30.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal30.text.toString().toInt()))){
                buttonCal30.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal30.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal30.text.toString())){
                buttonCal30.setTextColor(Color.BLUE)
                buttonCal30.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal30.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal30.setTextColor(Color.WHITE)
                    buttonCal30.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal30.setTextColor(Color.BLACK)
                    buttonCal30.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal30.text.toString())){
                buttonCal30.alpha = 1.0f
            } else {
                buttonCal30.alpha = 0.4f
            }
        } else {
            buttonCal30.setTextColor(Color.BLACK)
            buttonCal30.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 31
        if (buttonCal31.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal31.text.toString().toInt()))){
                buttonCal31.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal31.setTextAppearance(R.style.calendar_number)
            }
            timesOverlap(buttonCal31.text.toString())
            if (timesOverlap(buttonCal31.text.toString())){
                buttonCal31.setTextColor(Color.BLUE)
                buttonCal31.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal31.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal31.setTextColor(Color.WHITE)
                    buttonCal31.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal31.setTextColor(Color.BLACK)
                    buttonCal31.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal31.text.toString())){
                buttonCal31.alpha = 1.0f
            } else {
                buttonCal31.alpha = 0.4f
            }
        } else {
            buttonCal31.setTextColor(Color.BLACK)
            buttonCal31.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 32
        if (buttonCal32.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal32.text.toString().toInt()))){
                buttonCal32.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal32.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal32.text.toString())){
                buttonCal32.setTextColor(Color.BLUE)
                buttonCal32.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal32.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal32.setTextColor(Color.WHITE)
                    buttonCal32.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal32.setTextColor(Color.BLACK)
                    buttonCal32.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal32.text.toString())){
                buttonCal32.alpha = 1.0f
            } else {
                buttonCal32.alpha = 0.4f
            }
        } else {
            buttonCal32.setTextColor(Color.BLACK)
            buttonCal32.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 33
        if (buttonCal33.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal33.text.toString().toInt()))){
                buttonCal33.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal33.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal33.text.toString())){
                buttonCal33.setTextColor(Color.BLUE)
                buttonCal33.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal33.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal33.setTextColor(Color.WHITE)
                    buttonCal33.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal33.setTextColor(Color.BLACK)
                    buttonCal33.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal33.text.toString())){
                buttonCal33.alpha = 1.0f
            } else {
                buttonCal33.alpha = 0.4f
            }
        } else {
            buttonCal33.setTextColor(Color.BLACK)
            buttonCal33.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 34
        if (buttonCal34.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal34.text.toString().toInt()))){
                buttonCal34.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal34.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal34.text.toString())){
                buttonCal34.setTextColor(Color.BLUE)
                buttonCal34.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal34.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal34.setTextColor(Color.WHITE)
                    buttonCal34.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal34.setTextColor(Color.BLACK)
                    buttonCal34.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal34.text.toString())){
                buttonCal34.alpha = 1.0f
            } else {
                buttonCal34.alpha = 0.4f
            }
        } else {
            buttonCal34.setTextColor(Color.BLACK)
            buttonCal34.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 35
        if (buttonCal35.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal35.text.toString().toInt()))){
                buttonCal35.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal35.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal35.text.toString())){
                buttonCal35.setTextColor(Color.BLUE)
                buttonCal35.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal35.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal35.setTextColor(Color.WHITE)
                    buttonCal35.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal35.setTextColor(Color.BLACK)
                    buttonCal35.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal35.text.toString())){
                buttonCal35.alpha = 1.0f
            } else {
                buttonCal35.alpha = 0.4f
            }
        } else {
            buttonCal35.setTextColor(Color.BLACK)
            buttonCal35.setBackgroundResource(R.drawable.rounded_corners_white)
        }


        // Button 36
        if (buttonCal36.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal36.text.toString().toInt()))){
                buttonCal36.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal36.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal36.text.toString())){
                buttonCal36.setTextColor(Color.BLUE)
                buttonCal36.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal36.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal36.setTextColor(Color.WHITE)
                    buttonCal36.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal36.setTextColor(Color.BLACK)
                    buttonCal36.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal36.text.toString())){
                buttonCal36.alpha = 1.0f
            } else {
                buttonCal36.alpha = 0.4f
            }
        } else {
            buttonCal36.setTextColor(Color.BLACK)
            buttonCal36.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 37
        if (buttonCal37.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal37.text.toString().toInt()))){
                buttonCal37.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal37.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal37.text.toString())){
                buttonCal37.setTextColor(Color.BLUE)
                buttonCal37.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal37.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal37.setTextColor(Color.WHITE)
                    buttonCal37.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal37.setTextColor(Color.BLACK)
                    buttonCal37.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal37.text.toString())){
                buttonCal37.alpha = 1.0f
            } else {
                buttonCal37.alpha = 0.4f
            }
        } else {
            buttonCal37.setTextColor(Color.BLACK)
            buttonCal37.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 38
        if (buttonCal38.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal38.text.toString().toInt()))){
                buttonCal38.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal38.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal38.text.toString())){
                buttonCal38.setTextColor(Color.BLUE)
                buttonCal38.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal38.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal38.setTextColor(Color.WHITE)
                    buttonCal38.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal38.setTextColor(Color.BLACK)
                    buttonCal38.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal38.text.toString())){
                buttonCal38.alpha = 1.0f
            } else {
                buttonCal38.alpha = 0.4f
            }
        } else {
            buttonCal38.setTextColor(Color.BLACK)
            buttonCal38.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 39
        if (buttonCal39.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal39.text.toString().toInt()))){
                buttonCal39.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal39.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal39.text.toString())){
                buttonCal39.setTextColor(Color.BLUE)
                buttonCal39.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal39.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal39.setTextColor(Color.WHITE)
                    buttonCal39.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal39.setTextColor(Color.BLACK)
                    buttonCal39.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal39.text.toString())){
                buttonCal39.alpha = 1.0f
            } else {
                buttonCal39.alpha = 0.4f
            }
        } else {
            buttonCal39.setTextColor(Color.BLACK)
            buttonCal39.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 40
        if (buttonCal40.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal40.text.toString().toInt()))){
                buttonCal40.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal40.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal40.text.toString())){
                buttonCal40.setTextColor(Color.BLUE)
                buttonCal40.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal40.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal40.setTextColor(Color.WHITE)
                    buttonCal40.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal40.setTextColor(Color.BLACK)
                    buttonCal40.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal40.text.toString())){
                buttonCal40.alpha = 1.0f
            } else {
                buttonCal40.alpha = 0.4f
            }
        } else {
            buttonCal40.setTextColor(Color.BLACK)
            buttonCal40.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 41
        if (buttonCal41.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal41.text.toString().toInt()))){
                buttonCal41.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal41.setTextAppearance(R.style.calendar_number)
            }
            if (timesOverlap(buttonCal41.text.toString())){
                buttonCal41.setTextColor(Color.BLUE)
                buttonCal41.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal41.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal41.setTextColor(Color.WHITE)
                    buttonCal41.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal41.setTextColor(Color.BLACK)
                    buttonCal41.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal41.text.toString())){
                buttonCal41.alpha = 1.0f
            } else {
                buttonCal41.alpha = 0.4f
            }
        } else {
            buttonCal41.setTextColor(Color.BLACK)
            buttonCal41.setBackgroundResource(R.drawable.rounded_corners_white)
        }

        // Button 42
        if (buttonCal42.text != ""){
            if (checkTodayDate(tempDate.withDayOfMonth(buttonCal42.text.toString().toInt()))){
                buttonCal42.setTextAppearance(R.style.calendar_number_bold)
            } else {
                buttonCal42.setTextAppearance(R.style.calendar_number)
            }
            // if (checkAlreadyCreatedWoDays(tempDate.year.toString(), selectedDate.month.toString()).contains(buttonCal42.text))
            if (timesOverlap(buttonCal42.text.toString())){
                buttonCal42.setTextColor(Color.BLUE)
                buttonCal42.setBackgroundResource(R.drawable.rounded_corners_blue_2)
            } else {
                if (datesPicked.contains(
                        tempDate.withDayOfMonth(
                            buttonCal42.text.toString().toInt()
                        )
                    )
                ) {
                    buttonCal42.setTextColor(Color.WHITE)
                    buttonCal42.setBackgroundResource(R.drawable.rounded_corners_blue)
                } else {
                    buttonCal42.setTextColor(Color.BLACK)
                    buttonCal42.setBackgroundResource(R.drawable.rounded_corners_white)
                }
            }
            if (isButtonTextOnTime(buttonCal42.text.toString())){
                buttonCal42.alpha = 1.0f
            } else {
                buttonCal42.alpha = 0.4f
            }
        } else {
            buttonCal42.setTextColor(Color.BLACK)
            buttonCal42.setBackgroundResource(R.drawable.rounded_corners_white)
        }

    }

    private fun getTempDateTouched(dayTouched: Int): LocalDate {
        return selectedDate.withDayOfMonth(dayTouched)
    }

    private fun checkTodayDate(tempDate: LocalDate): Boolean {
        return (tempDate.year == LocalDate.now().year && tempDate.month == LocalDate.now().month
                && tempDate.dayOfMonth == LocalDate.now().dayOfMonth)
    }


    // Check if the time and duration combination is already taken
    // returns an Array of String days (for the month)
    private fun checkAlreadyCreatedWoDays(year: String, monthText: String) : ArrayList<String> {
        val daysFound : ArrayList<String> = ArrayList() // days found per year, month and time
        for (workout in CreateWorkoutTabsActivity.workoutList){
            // This workoutList is already filtered by time

            if (workout.dateYear == year && workout.dateMonth == convertMonthToNumber(monthText).toString()){
                daysFound.add(workout.dateDay)
            }
        }

        return daysFound

    }

    // TODO define this function
    private fun timesOverlap(buttonTextDay: String): Boolean {
        var overlapFound = false

        val year = selectedDate.year // 2023
        val month = convertMonthToNumber(selectedDate.month.toString())  // 8
        val day = buttonTextDay.toInt() // 8
        val timeHourRetrieved = CreateWorkoutTabsActivity.timeHour
        val timeMinuteRetrieved = CreateWorkoutTabsActivity.timeMinute

        val durationRetrieved = CreateWorkoutTabsActivity.duration
        val buttonTimeMillisA = BaseActivity().getTimeInMillisCCS(year, month, day, timeHourRetrieved,
        timeMinuteRetrieved)
        val buttonTimeMillisB = buttonTimeMillisA + (3600000 * durationRetrieved) - 60000

        // for loop of all already filtered workouts for year and month
        val filteredWorkoutList = CreateWorkoutTabsActivity.workoutList
        for (workout in filteredWorkoutList) {
            val workoutDurationCorrection = (workout.duration * 3600000) - 60000 // duration in hours * milliseconds in one hour

            if (buttonTimeMillisA >= workout.timeCreatedMillis.toLong() &&
                buttonTimeMillisA <= workout.timeCreatedMillis.toLong() + workoutDurationCorrection){
                overlapFound = true
                break
            } else if(buttonTimeMillisB >= workout.timeCreatedMillis.toLong() &&
                buttonTimeMillisA <= workout.timeCreatedMillis.toLong() + workoutDurationCorrection) {
                overlapFound = true
                break
            }

        }
        
        return overlapFound
    }

    private fun convertMonthToNumber(monthText: String): Int {
        when(monthText){
            "JANUARY" -> {
                return 1
            }
            "FEBRUARY" -> {
                return 2
            }
            "MARCH" -> {
                return 3
            }
            "APRIL" -> {
                return 4
            }
            "MAY" -> {
                return 5
            }
            "JUNE" -> {
                return 6
            }
            "JULY" -> {
                return 7
            }
            "AUGUST" -> {
                return 8
            }
            "SEPTEMBER" -> {
                return 9
            }
            "OCTOBER" -> {
                return 10
            }
            "NOVEMBER" -> {
                return 11
            }
            "DECEMBER" -> {
                return 12
            }
        }
        return 0
    }


    private fun isButtonTextOnTime(buttonTextDay: String): Boolean {

        // Selected Month - Year - Time -------------------------
        // Time in millis extracted from the selected time
        val yearSelected = selectedDate.year // Int 2023
        val monthSelected = convertMonthToNumber(selectedDate.month.toString()) // Int 11
        val timeSelectedHour = CreateWorkoutTabsActivity.timeHour // Int Format 15
        val timeSelectedMinute = CreateWorkoutTabsActivity.timeMinute // Int Format 0 or 30

        val selectedInMillisGMT: Long = BaseActivity().getTimeInMillisCCS(yearSelected, monthSelected,buttonTextDay.toInt(),
        timeSelectedHour, timeSelectedMinute, 0) + 14400000 // Adjusted back to GMT


        // return : if selected time in millis GMT minus 1 hour > current time in millis GMT
        return selectedInMillisGMT - 3600000 > BaseActivity().getCurrentTimeGMT()

    }

    private fun popTimePicker() {

        // create listener
        val listener = TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
            hour = selectedHour
            minute = selectedMinute
            // Transform hour and minute Int into textTime 5:00 PM format
            val timeText = "${transformHourIntToString(hour)}:${transformMinToString(minute)} ${isAMorPM(hour)}"
            btnSetTime.text = timeText

            onDateModifiedListener.timePicked(selectedDate.year, // Int 2023
                convertMonthToNumber(selectedDate.month.toString()), // Int 11
                timeText )  // 5:00 PM

        }

        // create picker
        val timePicker = TimePickerDialog(context, 2, listener, hour, minute, false)
        timePicker.setTitle(resources.getString(R.string.set_time))

        timePicker.show()
    }

    private fun transformHourIntToString(hour: Int) : String {

        val hourText : String = when (hour) {
            0 -> {
                "12"
            }
            in 1..12 -> {
                hour.toString()
            }
            else -> {
                (hour - 12).toString()
            }
        }

        return hourText
    }

    private fun transformMinToString(minute: Int) : String {
        return if (minute < 10){
            "0$minute"
        } else {
            minute.toString()
        }
    }

    private fun isAMorPM(hour: Int) : String {

        val amPm : String = if (hour < 12){
            "AM"
        } else {
            "PM"
        }

        return amPm
    }

    private fun setButtonVisibility(visible: Boolean){
        if (visible){
            buttonCal01.visibility = View.VISIBLE
            buttonCal02.visibility = View.VISIBLE
            buttonCal03.visibility = View.VISIBLE
            buttonCal04.visibility = View.VISIBLE
            buttonCal05.visibility = View.VISIBLE
            buttonCal06.visibility = View.VISIBLE
            buttonCal07.visibility = View.VISIBLE
            buttonCal08.visibility = View.VISIBLE
            buttonCal09.visibility = View.VISIBLE

            buttonCal10.visibility = View.VISIBLE
            buttonCal11.visibility = View.VISIBLE
            buttonCal12.visibility = View.VISIBLE
            buttonCal13.visibility = View.VISIBLE
            buttonCal14.visibility = View.VISIBLE
            buttonCal15.visibility = View.VISIBLE
            buttonCal16.visibility = View.VISIBLE
            buttonCal17.visibility = View.VISIBLE
            buttonCal18.visibility = View.VISIBLE
            buttonCal19.visibility = View.VISIBLE

            buttonCal20.visibility = View.VISIBLE
            buttonCal21.visibility = View.VISIBLE
            buttonCal22.visibility = View.VISIBLE
            buttonCal23.visibility = View.VISIBLE
            buttonCal24.visibility = View.VISIBLE
            buttonCal25.visibility = View.VISIBLE
            buttonCal26.visibility = View.VISIBLE
            buttonCal27.visibility = View.VISIBLE
            buttonCal28.visibility = View.VISIBLE
            buttonCal29.visibility = View.VISIBLE

            buttonCal30.visibility = View.VISIBLE
            buttonCal31.visibility = View.VISIBLE
            buttonCal32.visibility = View.VISIBLE
            buttonCal33.visibility = View.VISIBLE
            buttonCal34.visibility = View.VISIBLE
            buttonCal35.visibility = View.VISIBLE
            buttonCal36.visibility = View.VISIBLE
            buttonCal37.visibility = View.VISIBLE
            buttonCal38.visibility = View.VISIBLE
            buttonCal39.visibility = View.VISIBLE

            buttonCal40.visibility = View.VISIBLE
            buttonCal41.visibility = View.VISIBLE
            buttonCal42.visibility = View.VISIBLE
        } else {
            buttonCal01.visibility = View.INVISIBLE
            buttonCal02.visibility = View.INVISIBLE
            buttonCal03.visibility = View.INVISIBLE
            buttonCal04.visibility = View.INVISIBLE
            buttonCal05.visibility = View.INVISIBLE
            buttonCal06.visibility = View.INVISIBLE
            buttonCal07.visibility = View.INVISIBLE
            buttonCal08.visibility = View.INVISIBLE
            buttonCal09.visibility = View.INVISIBLE

            buttonCal10.visibility = View.INVISIBLE
            buttonCal11.visibility = View.INVISIBLE
            buttonCal12.visibility = View.INVISIBLE
            buttonCal13.visibility = View.INVISIBLE
            buttonCal14.visibility = View.INVISIBLE
            buttonCal15.visibility = View.INVISIBLE
            buttonCal16.visibility = View.INVISIBLE
            buttonCal17.visibility = View.INVISIBLE
            buttonCal18.visibility = View.INVISIBLE
            buttonCal19.visibility = View.INVISIBLE

            buttonCal20.visibility = View.INVISIBLE
            buttonCal21.visibility = View.INVISIBLE
            buttonCal22.visibility = View.INVISIBLE
            buttonCal23.visibility = View.INVISIBLE
            buttonCal24.visibility = View.INVISIBLE
            buttonCal25.visibility = View.INVISIBLE
            buttonCal26.visibility = View.INVISIBLE
            buttonCal27.visibility = View.INVISIBLE
            buttonCal28.visibility = View.INVISIBLE
            buttonCal29.visibility = View.INVISIBLE

            buttonCal30.visibility = View.INVISIBLE
            buttonCal31.visibility = View.INVISIBLE
            buttonCal32.visibility = View.INVISIBLE
            buttonCal33.visibility = View.INVISIBLE
            buttonCal34.visibility = View.INVISIBLE
            buttonCal35.visibility = View.INVISIBLE
            buttonCal36.visibility = View.INVISIBLE
            buttonCal37.visibility = View.INVISIBLE
            buttonCal38.visibility = View.INVISIBLE
            buttonCal39.visibility = View.INVISIBLE

            buttonCal40.visibility = View.INVISIBLE
            buttonCal41.visibility = View.INVISIBLE
            buttonCal42.visibility = View.INVISIBLE
        }
    }


    // Locally defined interface that is later implemented at CreateWorkoutTabsActivity
    interface OnDateModifiedListener {
        fun dateAdded(date: LocalDate)
        fun dateRemoved(date: LocalDate)
        fun showSnack(text: String, alert: Boolean)

        fun timePicked(year: Int, month: Int, timeText: String)

        fun monthChanged(year: Int, month: Int, time: String)

    }


}