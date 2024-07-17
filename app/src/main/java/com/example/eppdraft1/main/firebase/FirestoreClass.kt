package com.example.eppdraft1.main.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.eppdraft1.main.activities.*
import com.example.eppdraft1.main.models.*
import com.example.eppdraft1.main.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    // register user data to Firestore database
    fun registerUser(activity: CreateUserActivity, userInfo: User){
        mFirestore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener {
                e ->
                activity.hideProgressDialog()
                Log.e(Constants.TAG, "Error while registering an user profile", e)
            }
    }

    // load entire user data from Firestore database
    fun loadUserData(activity: Activity){
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get()
            .addOnSuccessListener {
                    document->
                val loggedInUser = document.toObject(User::class.java)!!
                when (activity) {
                    is PortalActivity -> {
                        activity.updateNavigationUserDetails(loggedInUser)
                    }
                    is ProfileEditActivity -> {
                        activity.displayUserData(loggedInUser)
                    }
                    is ViewWorkoutActivity -> {
                        activity.reserveWithThisUserData(loggedInUser)
                    }
                    is WorkoutListActivity -> {
                        activity.reserveExpress3(loggedInUser)
                    }
                    is MyWorkoutListActivity -> {
                        activity.reserveExpress3(loggedInUser)
                    }
                    is WeekCalendarView -> {
                        activity.reserveExpress3(loggedInUser)
                    }
                }

            }.addOnFailureListener {
                    e ->

               Log.e(Constants.TAG,"Error while loading user data", e )
               Toast.makeText(activity, "Error while loading user data", Toast.LENGTH_LONG).show()
               if (activity is WorkoutListActivity){
                   activity.refreshAdapterOnly()
                   activity.hideProgressDialog()
               } else if (activity is MyWorkoutListActivity){
                   activity.refreshAdapterOnly()
                   activity.hideProgressDialog()
               }
            }
    }

    // load entire user data from Firestore database to construct the Attendee list
    // receiving one (one by one) id data
    fun loadUserDataForAttendee(activity: ViewWorkoutActivity, count: Int, attendeeList: ArrayList<Attendee>){
        mFirestore.collection(Constants.USERS)
            .document(attendeeList[count].id)
            .get()
            .addOnSuccessListener {
                    document->
                val loggedInUser = document.toObject(User::class.java)!!
                activity.userDataToBuildUserAttendee(loggedInUser, attendeeList[count].attended)

            }.addOnFailureListener {
                    e ->
                activity.hideProgressDialog()
                Log.e(Constants.TAG,"Error while loading user data", e )
                Toast.makeText(activity, "Error while loading user data", Toast.LENGTH_LONG).show()

            }
    }


    fun getCurrentUserId(): String {
        return if (FirebaseAuth.getInstance().currentUser!=null) {
            FirebaseAuth.getInstance().currentUser!!.uid
        } else{
            ""
        }
    }

    fun updateUserProfileData(activity: ProfileEditActivity,
                              userHashMap: HashMap<String, Any> ){
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i(Constants.TAG, "Profile data successfully updated")
                activity.profileUpdateSuccess()
            }.addOnFailureListener {
                e ->
                activity.hideProgressDialog()
                Log.e(Constants.TAG, "Error while updating profile data", e)
                Toast.makeText(activity, "Error while updating profile data",
                    Toast.LENGTH_LONG).show()
            }
    }

    fun getAthletesList(activity: AthleteListActivity) {
        mFirestore.collection(Constants.USERS)
            .whereEqualTo(Constants.ROLE, Constants.ATHLETE)
            .orderBy("name")
            .get()
            .addOnSuccessListener {
                    document ->
                val userList : ArrayList<User> = ArrayList()
                for (i in document.documents){
                    val user = i.toObject(User::class.java)!!
                    user.id = i.id
                    userList.add(user)
                }
                activity.populateAthleteListToUI(userList)
            }.addOnFailureListener {
                    e ->
                //activity.hideProgressDialogLight()
                activity.showProgressBarAthleteStandAlone(false, showError = true)
                Log.e("ETIQUETA", "Error while getting the athletes list",e)
            }
    }

    fun getAthletesListCount(activity: PortalActivity){
        mFirestore.collection(Constants.USERS)
            .whereEqualTo(Constants.ROLE, Constants.ATHLETE)
            .get()
            .addOnSuccessListener {
                    document ->
                Log.i(Constants.TAG, "Retrieving user list with role ${Constants.ATHLETE}")
                Log.i(Constants.TAG, document.documents.toString())
                val userList : ArrayList<User> = ArrayList()
                for (i in document.documents){
                    val user = i.toObject(User::class.java)!!
                    user.id = i.id
                    userList.add(user)
                }
                activity.showAthleteListSize(userList.size)
            }.addOnFailureListener {
                    e ->
                activity.hideProgressDialog()
                Log.e("ETIQUETA", "Error while getting the athletes list",e)
            }
    }

    // register Plan data to Firestore database

    fun registerPlan(activity: PlansPortalActivity, plan: Plan) {
        mFirestore.collection(Constants.PLANS)
            .document()
            .set(plan, SetOptions.merge())
            .addOnSuccessListener {
                activity.planCreated()

            }.addOnFailureListener {
                    e ->
                activity.hideProgressDialog()
                Log.e(Constants.TAG, "Error while registering a Plan to Firebase", e)
            }
    }


    // register workout data to Firestore database
    fun registerWorkout(activity: Activity, workout: Workout) {
        mFirestore.collection(Constants.WORKOUTS)
            .document()
            .set(workout, SetOptions.merge())
            .addOnSuccessListener {
                when(activity){
                    is CreateWorkoutActivity -> {
                        activity.workoutCreatedSuccess()
                    }
                    is CreateWorkoutTabsActivity -> {
                        activity.workoutsCreatedSuccess()
                    }
                }

            }.addOnFailureListener {
                    e ->
                when(activity){
                    is CreateWorkoutActivity -> {
                        activity.hideProgressDialog()
                    }
                    is CreateWorkoutTabsActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(Constants.TAG, "Error while registering a workout", e)
            }
    }

    // register/create workout array data to Firestore database
    fun registerWorkoutArray(activity: CreateWorkoutTabsActivity, workoutArray: ArrayList<Workout>) {

        var errorFound = false

        for (workout in workoutArray) {
            mFirestore.collection(Constants.WORKOUTS)
                .document()
                .set(workout, SetOptions.merge())
                .addOnSuccessListener {
                    Log.i(Constants.TAG, "Workout ${workout.description} created")

                }.addOnFailureListener { e ->
                    errorFound = true
                    Log.e(Constants.TAG, "Error while registering workout ${workout.description}", e)

                }

        }

        if (errorFound) {
            activity.hideProgressDialog()
            activity.showCustomSnackBar("Errors found while creating Workouts", true)
        } else {
            activity.workoutsCreatedSuccess()
        }
    }

    fun getWorkoutList(activity: Activity, myList : Boolean = false) {
        mFirestore.collection(Constants.WORKOUTS)
            .orderBy("timeCreatedMillis")
            .get()
            .addOnSuccessListener {
                document ->
                Log.i(Constants.TAG, "Retrieving workout list")
                val workoutList: ArrayList<Workout> = ArrayList()
                for (i in document.documents) {
                     val workout = i.toObject(Workout::class.java)!!
                     if (myList) {
                         for (user in workout.athleteAttendees) {
                             if (user.id == getCurrentUserId()) {
                                 workoutList.add(workout)
                             }
                         }
                     } else {
                         workoutList.add(workout)
                     }
                 }
                 workoutList.reverse()
                when (activity) {
                    is WorkoutListActivity -> {
                        activity.populateWorkoutListToUI(workoutList)
                    }
                    is MyWorkoutListActivity -> {
                        activity.populateWorkoutListToUI(workoutList)
                    }
                    is MyTabsWorkoutListActivity -> {
                        activity.populateWorkoutListToUI(workoutList)
                    }
                    is TabsWorkoutListActivity -> {
                        activity.populateAllWorkoutListToUI(workoutList)
                    }
                    is CalendarMainJava -> {
                        activity.showMyWorkoutsHits(workoutList)
                    }
                    is WeekCalendarView -> {
                        activity.showWeeklyWorkouts(workoutList)
                    }

                }
            }.addOnFailureListener {
                e ->

                when (activity) {
                    is WorkoutListActivity -> {
                        activity.showProgressBarStandAlone(false, showError = true)
                        activity.hideProgressDialog()
                    }
                    is MyWorkoutListActivity -> {
                        activity.showProgressBarStandAlone(false, showError = true)
                        activity.hideProgressDialog()
                    }
                    is MyTabsWorkoutListActivity -> {
                        activity.hideProgressDialog()
                    }
                    is TabsWorkoutListActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(Constants.TAG, "Error while retrieving the workout list", e)
            }
    }

    // load entire user data from Firestore database
    fun loadWorkoutData(activity: Activity, dateTimeMillis: String) {
        mFirestore.collection(Constants.WORKOUTS)
            .whereEqualTo(Constants.TIME_CREATED_MILLIS, dateTimeMillis)
            .get()
            .addOnSuccessListener { querySnapShot->
                when(activity) {
                    is ViewWorkoutActivity -> {
                        for (document in querySnapShot.documents) {
                            val workout = document.toObject(Workout::class.java)!!
                            activity.displayWorkoutData(workout)
                        }
                    }
                    is WorkoutListActivity -> {
                        for (document in querySnapShot.documents) {
                            val workout = document.toObject(Workout::class.java)!!
                            activity.reserveExpress2(workout)
                        }

                    }
                    is MyWorkoutListActivity -> {
                        for (document in querySnapShot.documents) {
                            val workout = document.toObject(Workout::class.java)!!
                            activity.reserveExpress2(workout)
                        }

                    }
                    is WeekCalendarView -> {
                        for (document in querySnapShot.documents) {
                            val workout = document.toObject(Workout::class.java)!!
                            activity.reserveExpress2(workout)
                        }

                    }

                }
            }.addOnFailureListener {
                    e ->
                BaseActivity().hideProgressDialog()
                Log.e(Constants.TAG,"Error while loading workout data", e )
                Toast.makeText(activity, "Error while loading workout data", Toast.LENGTH_LONG).show()
                if (activity is WorkoutListActivity){
                    activity.refreshAdapterOnly()
                } else if (activity is MyWorkoutListActivity) {
                    activity.refreshAdapterOnly()
                }
            }
    }

    fun findWorkout(activity: CreateWorkoutActivity, dateTimeMillis: String)  {

        mFirestore.collection(Constants.WORKOUTS)
            .whereEqualTo(Constants.TIME_CREATED_MILLIS, dateTimeMillis)
            .get()
            .addOnSuccessListener { querySnapShot->
                var workoutFound = Workout()
                for (document in querySnapShot.documents) {
                    workoutFound = document.toObject(Workout::class.java)!!
                }
                if (workoutFound.timeCreatedMillis != ""){
                   activity.workoutFoundVerification(true)
                } else {
                    activity.workoutFoundVerification(false)
                }
            }.addOnFailureListener {
                    e ->
                activity.hideProgressDialog()
                Log.e(Constants.TAG,"Error while locating a workout", e )
                Toast.makeText(activity, "Error while locating a workout", Toast.LENGTH_LONG).show()
            }
    }

    fun updateWorkoutData(activity: Activity, dateTimeMillis: String,
                          workoutAttendeeHashMap: HashMap<String, Any>,
                          updateAttending: Boolean = false) {
        mFirestore.collection(Constants.WORKOUTS)
            .whereEqualTo(Constants.TIME_CREATED_MILLIS, dateTimeMillis)
            .get()
            .addOnSuccessListener { querySnapShot->
                when(activity) {
                    is ViewWorkoutActivity -> {
                        for (document in querySnapShot.documents) {
                            mFirestore.collection(Constants.WORKOUTS).document(document.id)
                                .update(workoutAttendeeHashMap)
                            //val workout = document.toObject(Workout::class.java)!!
                        }
                        if (!updateAttending) {
                            activity.reloadWorkoutData()
                        } else {
                            activity.returnFromUpdatingAttending()
                        }
                    }
                    is WorkoutListActivity -> {
                        for (document in querySnapShot.documents){
                            mFirestore.collection(Constants.WORKOUTS).document(document.id)
                                .update(workoutAttendeeHashMap)
                            activity.reserveExpressResult()
                        }
                    }
                    is MyWorkoutListActivity -> {
                        for (document in querySnapShot.documents){
                            mFirestore.collection(Constants.WORKOUTS).document(document.id)
                                .update(workoutAttendeeHashMap)
                            activity.reserveExpressResult()
                        }
                    }
                    is WeekCalendarView -> {
                        for (document in querySnapShot.documents){
                            mFirestore.collection(Constants.WORKOUTS).document(document.id)
                                .update(workoutAttendeeHashMap)
                            activity.reserveExpressResult()
                        }
                    }
                }
            }.addOnFailureListener {
                    e ->

                Log.e(Constants.TAG,"Error while updating workout data", e )
                Toast.makeText(activity, "Error while updating workout data", Toast.LENGTH_LONG).show()
                if (activity is WorkoutListActivity){
                    activity.refreshAdapterOnly()
                    activity.hideProgressDialog()
                } else if (activity is MyWorkoutListActivity){
                    activity.refreshAdapterOnly()
                    activity.hideProgressDialog()
                }
            }
    }

    // Update Workout Notes, Manager/Trainer or Athlete
    fun updateWorkoutNotes(activity: WorkoutNotes, dateTimeMillis: String,
                          notes: HashMap<String, Any>) {
        mFirestore.collection(Constants.WORKOUTS)
            .whereEqualTo(Constants.TIME_CREATED_MILLIS, dateTimeMillis)
            .get()
            .addOnSuccessListener { querySnapShot->

                    for (document in querySnapShot.documents) {
                        mFirestore.collection(Constants.WORKOUTS).document(document.id)
                            .update(notes)
                    }
                    activity.trainerNotesReturn()

            }.addOnFailureListener {
                    e ->
                activity.hideProgressDialog()
                Log.e(Constants.TAG,"Error while updating workout Notes", e )
                Toast.makeText(activity, "Error while updating workout notes", Toast.LENGTH_LONG).show()
            }
    }


    fun updateWorkoutActive(activity: ViewWorkoutActivity, dateTimeMillis: String,
                          workoutAttendeeHashMap: HashMap<String, Any>) {
        mFirestore.collection(Constants.WORKOUTS)
            .whereEqualTo(Constants.TIME_CREATED_MILLIS, dateTimeMillis)
            .get()
            .addOnSuccessListener { querySnapShot->
                for (document in querySnapShot.documents) {
                    mFirestore.collection(Constants.WORKOUTS).document(document.id)
                        .update(workoutAttendeeHashMap)
                }
                activity.displayWoActiveData()
            }.addOnFailureListener {
                    e ->
                activity.hideProgressDialog()
                Log.e(Constants.TAG,"Error while updating workout data", e )
                Toast.makeText(activity, "Error while updating workout data", Toast.LENGTH_LONG).show()

            }
    }

    fun deleteWorkout(activity: ViewWorkoutActivity, dateTimeMillis: String){
        mFirestore.collection(Constants.WORKOUTS)
            .whereEqualTo(Constants.TIME_CREATED_MILLIS, dateTimeMillis)
            .get()
            .addOnSuccessListener { querySnapShot ->
                for (document in querySnapShot.documents) {
                    mFirestore.collection(Constants.WORKOUTS).document(document.id)
                        .delete()
                }
                activity.reloadWorkoutData()
            }.addOnFailureListener {
                e ->
                activity.hideProgressDialog()
                Log.e(Constants.TAG,"Error while deleting workout data", e )
                Toast.makeText(activity, "Error while deleting workout data", Toast.LENGTH_LONG).show()
            }
    }


    fun listWorkoutInYearMonthTime(activity: CreateWorkoutTabsActivity, year: String,
                                    month: String, time: String)  {

        mFirestore.collection(Constants.WORKOUTS)
            .whereEqualTo(Constants.DATE_YEAR, year)
            .whereEqualTo(Constants.DATE_MONTH, month)
            .whereEqualTo(Constants.DATE_TIME, time)
            .get()
            .addOnSuccessListener {
                    querySnapShot->
                var workoutFound: Workout
                val listWorkoutsForTime: ArrayList<Workout> = ArrayList()
                for (document in querySnapShot.documents) { // document is each Workout with matching data
                    workoutFound = document.toObject(Workout::class.java)!!
                    listWorkoutsForTime.add(workoutFound)
                }
                activity.saveTheWorkoutListForTime(listWorkoutsForTime)
            }.addOnFailureListener {
                    e ->
                activity.hideProgressDialog()
                Log.e(Constants.TAG,"Error while locating the workout list", e )
                Toast.makeText(activity, "Error while locating a workout", Toast.LENGTH_LONG).show()
            }
    }

    fun listWorkoutInYearMonth(activity: CreateWorkoutTabsActivity, year: String,
                                   month: String)  {

        mFirestore.collection(Constants.WORKOUTS)
            .whereEqualTo(Constants.DATE_YEAR, year)
            .whereEqualTo(Constants.DATE_MONTH, month)
            .get()
            .addOnSuccessListener {
                    querySnapShot->
                var workoutFound: Workout
                val listWorkoutsForTime: ArrayList<Workout> = ArrayList()
                for (document in querySnapShot.documents) { // document is each Workout with matching data
                    workoutFound = document.toObject(Workout::class.java)!!
                    listWorkoutsForTime.add(workoutFound)
                }
                activity.saveTheWorkoutListForTime(listWorkoutsForTime)
            }.addOnFailureListener {
                    e ->
                activity.hideProgressDialog()
                Log.e(Constants.TAG,"Error while locating the workout list", e )
                Toast.makeText(activity, "Error while locating a workout", Toast.LENGTH_LONG).show()
            }
    }

    fun listWorkoutForCertainTime(activity: CreateWorkoutTabsActivity, time: String)  {

        mFirestore.collection(Constants.WORKOUTS)
            .whereEqualTo(Constants.DATE_TIME, time)
            .get()
            .addOnSuccessListener {
                    querySnapShot->
                var workoutFound: Workout
                val listDays : ArrayList<String> = ArrayList()
                val listWorkoutsForTime: ArrayList<Workout> = ArrayList()
                for (document in querySnapShot.documents) { // document is each Workout with matching data
                    workoutFound = document.toObject(Workout::class.java)!!
                    listWorkoutsForTime.add(workoutFound)
                }
                activity.saveTheWorkoutListForTime(listWorkoutsForTime)
            }.addOnFailureListener {
                    e ->
                activity.hideProgressDialog()
                Log.e(Constants.TAG,"Error while locating a list of workout by time", e )
                Toast.makeText(activity, "Error while locating a list of workout by time", Toast.LENGTH_LONG).show()
            }
    }

    // Receive a list of Attendee and send back a list of UserAttendee
    fun retrieveUserAttendeeList(activity: ViewWorkoutActivity, attendeeList: ArrayList<Attendee>){
        // Look into Users collections
        val userAttendeeList : ArrayList<UserAttendee> = ArrayList()
        for (eachAttendee in attendeeList){
            mFirestore.collection(Constants.USERS)
                .document(eachAttendee.id)
                .get()
                .addOnSuccessListener {
                    document ->
                    if (document.exists()){
                        val attendee = document.toObject(UserAttendee::class.java)
                        if (attendee != null){
                            attendee.attended = eachAttendee.attended
                            userAttendeeList.add(attendee)
                        }
                    }

                }.addOnFailureListener {
                    e ->
                    activity.hideProgressDialog()
                    Log.e(Constants.TAG, "Error loading attending users", e)
                }
        }


    }




}