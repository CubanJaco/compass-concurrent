package cu.jaco.compassconcurrent.live.coding

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import java.util.Date

// Create a new file within the test folder of your project, Calendar.kt. You can paste this entire comment into
// that file so you can easily refer to it throughout the exercise.
//
// Implement a class `Calendar` that is responsible for maintaining a
// collection of appointments. Each Appointment should contain:
//
// - an appointment name
// - a start time
// - an end time
// - a client (the person with whom the agent will meet)
//
// The calendar should define two methods:
//
// 1. a method to add new appointments. Appointments may not overlap.
//    Examples:
//    - We try to add the appointments 10am-11am and 11am-12pm:
//      -> Both are added successfully
//    - We try to add the appointments 10am-11am and 10:30am-11:30pm
//      -> Only the first one is added successfully
// 2. a method to return a list of appointments between a given start and
// end time, sorted by start time
//    Example:
//      3 appointments have been added already:
//      - 10am-11am
//      - 11am-12pm
//      - 12pm-1pm
//      If the date range given is 10:30am to 12pm, the following appointment
//      should be returned:
//        - 10am-11am
//        - 11am-12pm
//
// You should validate your implementation by writing unit tests.
//
// Hint: you can easily create fake dates for your unit tests like this:
//
//   val date1 = Date(0)
//   val date2 = Date(100)

class Calendar {

    private val appointments: ArrayList<Appointment> = arrayListOf()

    fun scheduleAppointment(appointment: Appointment): Boolean {
        val hasOverlapping = appointments.find { it.overlapsWith(appointment) } != null
        if (!hasOverlapping) {
            appointments.add(appointment)
        }
        return !hasOverlapping
    }

    fun getAppointmentsOnRange(from: Date, to: Date): List<Appointment> = appointments
        .filter { it.isInRange(from, to) }
        .sortedBy { it.startTime }

}

class CalendarTest {

    private lateinit var systemUnderTest: Calendar

    @Before
    fun setUp() {
        systemUnderTest = Calendar()
    }

    @Test
    fun `add single appointment scheduled`() {

        val startTime = Date(500)
        val endTime = Date(1000)

        val appointment = Appointment(
            startTime = startTime,
            endTime = endTime,
            client = "Juan",
        )

        val result = systemUnderTest.scheduleAppointment(appointment)

        assertThat(result, `is`(true))
    }

    @Test
    fun `add overlapping start time appointments scheduled`() {
        val firstAppointment = Appointment(
            startTime = Date(500),
            endTime = Date(1000),
            client = "Juan",
        )

        val secondAppointment = Appointment(
            startTime = Date(750),
            endTime = Date(1250),
            client = "Pedro",
        )

        var result = systemUnderTest.scheduleAppointment(firstAppointment)

        assertThat(result, `is`(true))

        result = systemUnderTest.scheduleAppointment(secondAppointment)

        assertThat(result, `is`(false))
    }

    @Test
    fun `add overlapping end time appointments scheduled`() {
        val firstAppointment = Appointment(
            startTime = Date(500),
            endTime = Date(1000),
            client = "Juan",
        )

        val secondAppointment = Appointment(
            startTime = Date(250),
            endTime = Date(750),
            client = "Pedro",
        )

        var result = systemUnderTest.scheduleAppointment(firstAppointment)

        assertThat(result, `is`(true))

        result = systemUnderTest.scheduleAppointment(secondAppointment)

        assertThat(result, `is`(false))
    }

    @Test
    fun `add overlapping appointments scheduled`() {
        val firstAppointment = Appointment(
            startTime = Date(500),
            endTime = Date(1000),
            client = "Juan",
        )

        val secondAppointment = Appointment(
            startTime = Date(700),
            endTime = Date(900),
            client = "Pedro",
        )

        var result = systemUnderTest.scheduleAppointment(firstAppointment)

        assertThat(result, `is`(true))

        result = systemUnderTest.scheduleAppointment(secondAppointment)

        assertThat(result, `is`(false))
    }

    @Test
    fun `add consecutive appointments scheduled`() {
        val firstAppointment = Appointment(
            startTime = Date(500),
            endTime = Date(1000),
            client = "Juan",
        )

        val secondAppointment = Appointment(
            startTime = Date(1000),
            endTime = Date(1500),
            client = "Pedro",
        )

        var result = systemUnderTest.scheduleAppointment(firstAppointment)

        assertThat(result, `is`(true))

        result = systemUnderTest.scheduleAppointment(secondAppointment)

        assertThat(result, `is`(true))
    }

    @Test
    fun `get appointments on range`() {

        systemUnderTest.scheduleAppointment(
            Appointment(
                startTime = Date(1000),
                endTime = Date(2000),
                client = "Juan",
            )
        )

        systemUnderTest.scheduleAppointment(
            Appointment(
                startTime = Date(2000),
                endTime = Date(3000),
                client = "Juan",
            )
        )

        systemUnderTest.scheduleAppointment(
            Appointment(
                startTime = Date(3000),
                endTime = Date(4000),
                client = "Juan",
            )
        )


        val result = systemUnderTest.getAppointmentsOnRange(Date(1500), Date(3000))

        assertThat(
            result, `is`(
                listOf(
                    Appointment(
                        startTime = Date(1000),
                        endTime = Date(2000),
                        client = "Juan",
                    ),
                    Appointment(
                        startTime = Date(2000),
                        endTime = Date(3000),
                        client = "Juan",
                    )
                )
            )
        )

    }
}








