package com.example.eppdraft1.main.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.example.eppdraft1.R;
import com.example.eppdraft1.main.firebase.FirestoreClass;
import com.example.eppdraft1.main.models.Attendee;
import com.example.eppdraft1.main.models.User;
import com.example.eppdraft1.main.models.Workout;
import com.example.eppdraft1.main.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarMainJava extends BaseActivity {

    private Toolbar appBarCalendarMain;
    private CalendarView calendarView;

    private ActivityResultLauncher<Intent> newWorkoutReservedCreateResult;

    FloatingActionButton fabNewWorkout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_calendar_main);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //show the activity in full screen

        appBarCalendarMain = findViewById(R.id.toolbar_calendar_main);
        setupActionBar();

        fabNewWorkout = findViewById(R.id.fab_activity_main_calendar);
        if (isManager()){
            fabNewWorkout.setVisibility(View.VISIBLE);
        } else {
            fabNewWorkout.setVisibility(View.INVISIBLE);
        }
        fabNewWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline(CalendarMainJava.this)){
                    Intent intentCreateWorkout = new Intent(CalendarMainJava.this,
                            CreateWorkoutActivity.class);
                    //startActivity(intentCreateWorkout);
                    newWorkoutReservedCreateResult.launch(intentCreateWorkout);
                } else {
                    showCustomNotificationAlertDialog(getString(R.string.connectivity),
                            getString(R.string.no_access_to_internet));
                }
            }
        });

        calendarView = (CalendarView) findViewById(R.id.calendarView);

        calendarView.setOnDayClickListener(eventDay -> {

            Calendar clickedDayCalendar = eventDay.getCalendar();
            // clickedDayCalendar.getTime() --> getTime: Wed May 17 00:00:00 GMT-04:00 2023
            // getTime().getTime() --> 1684296000000
            String[] dateInfo = clickedDayCalendar.getTime().toString().split(" ");
            String monthText = convertToMonthNumber(dateInfo[1]); // Format 5 (MAY)
            String day = dateInfo[2]; // Format 17
            String year = dateInfo[5]; // Format 2023
            Intent intentToFilteredWorkoutList = new Intent(CalendarMainJava.this, WorkoutListActivity.class);
            intentToFilteredWorkoutList.putExtra(Constants.DATE_DAY, day);
            intentToFilteredWorkoutList.putExtra(Constants.DATE_MONTH, monthText);
            intentToFilteredWorkoutList.putExtra(Constants.DATE_YEAR, year);
            //startActivity(intentToFilteredWorkoutList);
            newWorkoutReservedCreateResult.launch(intentToFilteredWorkoutList);
        });

        newWorkoutReservedCreateResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK){
                        refreshCalendarHits();
                    }
                }
                
        );

        refreshCalendarHits();

    }

    private String convertToMonthNumber(String monthText) {
        String monthNumber = "";
        switch (monthText) {
            case "Jan":
                monthNumber = "1";
                break;
            case "Feb":
                monthNumber = "2";
                break;
            case "Mar":
                monthNumber = "3";
                break;
            case "Apr":
                monthNumber = "4";
                break;
            case "May":
                monthNumber = "5";
                break;
            case "Jun":
                monthNumber = "6";
                break;
            case "Jul":
                monthNumber = "7";
                break;
            case "Aug":
                monthNumber = "8";
                break;
            case "Sep":
                monthNumber = "9";
                break;
            case "Oct":
                monthNumber = "10";
                break;
            case "Nov":
                monthNumber = "11";
                break;
            case "Dic":
                monthNumber = "12";
                break;
            default:
                monthNumber = "";
                break;
        }
        return monthNumber;
    }

    private void setupActionBar(){
        setSupportActionBar(appBarCalendarMain);
        appBarCalendarMain.setNavigationIcon(R.drawable.white_arrow_back_ios_24);
        appBarCalendarMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void refreshCalendarHits(){
        FirestoreClass firestoreClass = new FirestoreClass();
        showProgressDialog();
        // if the currently user isn't Manager, retrieve My Workouts
        firestoreClass.getWorkoutList(CalendarMainJava.this, false);
    }
    public void showMyWorkoutsHits(ArrayList<Workout> workoutList){
        // Here we receive the list of workouts we have reserved
        hideProgressDialog();
        List<EventDay> events = new ArrayList<>();

        for (Workout workout : workoutList){
            String currentUserId = getCurrentUserId();
            Calendar calendarNew = Calendar.getInstance();
            calendarNew.set(Integer.parseInt(workout.getDateYear()), Integer.parseInt(workout.getDateMonth()) - 1,
                    Integer.parseInt(workout.getDateDay()));
            if (isManager()) { // Manager
                events.add(new EventDay(calendarNew, R.drawable.blue_workout_day_24));
            } else { // Athlete
                Boolean iHaveReservation = false;
                for (Attendee attendee : workout.getAthleteAttendees()){
                    if (attendee.getId().equals(currentUserId) ){
                        iHaveReservation = true;
                    }
                }
                if (iHaveReservation){
                    events.add(new EventDay(calendarNew, R.drawable.blue_check_circle_24));
                } else {
                    events.add(new EventDay(calendarNew, R.drawable.blue_workout_day_24));
                }
            }
        }

        calendarView.setEvents(events);

    }


}
