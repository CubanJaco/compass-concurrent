package cu.jaco.compassconcurrent.live.coding

import java.util.Date

data class Appointment(
    val startTime: Date,
    val endTime: Date,
    val client: String,
) {

    fun overlapsWith(appointment: Appointment): Boolean {
        if (appointment.startTime == endTime || appointment.endTime == startTime)
            return false

        if (appointment.startTime in startTime..endTime)
            return true

        if (appointment.endTime in startTime..endTime)
            return true

        return false
    }

    fun isInRange(from: Date, to: Date): Boolean {
        if (to == startTime || from == endTime)
            return false

        if (from in startTime..endTime)
            return true

        if (to in startTime..endTime)
            return true

        return false
    }

}