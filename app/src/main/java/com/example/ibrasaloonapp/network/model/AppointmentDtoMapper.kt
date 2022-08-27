package com.example.ibrasaloonapp.network.model

import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.util.DomainMapper
import javax.inject.Inject

class AppointmentDtoMapper
@Inject
constructor(
    private val customerMapper: CustomerDtoMapper
) : DomainMapper<AppointmentDto, Appointment> {
    override fun mapToDomainModel(model: AppointmentDto): Appointment {
        return Appointment(
            type = model.type,
            date = model.date,
            id = model.id ?: "",
            customer = model.customer,
            time = model.time,
            isActive = model.isActive,
            createdAt = model.createdAt
        )
    }

    override fun mapFromDomainModel(domainModel: Appointment): AppointmentDto {
        return AppointmentDto(
            type = domainModel.type,
            date = domainModel.date,
            id = domainModel.id,
            customer = domainModel.customer,
            time = domainModel.time,
            isActive = domainModel.isActive,
            createdAt = domainModel.createdAt
        )
    }

    fun toDomainList(initial: List<AppointmentDto>): List<Appointment> {
        return initial.map { appointmentDto -> mapToDomainModel(appointmentDto) }
    }

    fun fromDomainList(initial: List<Appointment>): List<AppointmentDto> {
        return initial.map { appointment -> mapFromDomainModel(appointment) }
    }
}