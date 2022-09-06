package com.example.ibrasaloonapp.network.model

import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.util.DomainMapper
import javax.inject.Inject

class AppointmentDtoMapper
@Inject
constructor(
    private val customerMapper: UserDtoMapper
) : DomainMapper<AppointmentDto, Appointment> {
    override fun mapToDomainModel(model: AppointmentDto): Appointment {
        return Appointment(
            id = model.id ?: "",
            customer = model.customer,
            isActive = model.isActive,
            service = model.service,
            startTime = model.startTime,
            endTime = model.endTime
        )
    }

    override fun mapFromDomainModel(domainModel: Appointment): AppointmentDto {
        return AppointmentDto(
            id = domainModel.id,
            customer = domainModel.customer,
            isActive = domainModel.isActive,
            service = domainModel.service,
            startTime = domainModel.startTime,
            endTime = domainModel.endTime
        )
    }

    fun toDomainList(initial: List<AppointmentDto>): List<Appointment> {
        return initial.map { appointmentDto -> mapToDomainModel(appointmentDto) }
    }

    fun fromDomainList(initial: List<Appointment>): List<AppointmentDto> {
        return initial.map { appointment -> mapFromDomainModel(appointment) }
    }
}