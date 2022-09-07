package com.example.ibrasaloonapp.presentation.ui.book_appointment

import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.domain.model.WorkingDate

sealed class BookAppointmentEvent {

    class OnSelectedWorker(val worker: User) : BookAppointmentEvent()
    class OnSelectedWorkingDate(val date: WorkingDate) : BookAppointmentEvent()
    class OnSelectedService(val service: String) : BookAppointmentEvent()
    class OnSelectedAppointment(val appointment: Appointment) : BookAppointmentEvent()


    object GetWorkingDates : BookAppointmentEvent()
    object Book : BookAppointmentEvent()
    object GetServices : BookAppointmentEvent()
    object GetAvailableAppointments : BookAppointmentEvent()
    object GetWorkers : BookAppointmentEvent()
    object OnRemoveHeadFromQueue : BookAppointmentEvent()

}