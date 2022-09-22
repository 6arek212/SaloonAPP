package com.example.ibrasaloonapp.network.model

import android.app.Application
import android.util.Log
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.KeyValueWrapper
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.util.DomainMapper
import javax.inject.Inject


const val HAIR_CUT = "Hair Cut"
const val Wax = "Wax"


private const val TAG = "AppointmentDtoMapper"

class AppointmentDtoMapper
@Inject
constructor(
    private val customerMapper: UserDtoMapper,
    private val serviceDtoMapper: ServiceDtoMapper
) : DomainMapper<AppointmentDto, Appointment> {

    override fun mapToDomainModel(model: AppointmentDto): Appointment {
        return Appointment(
            id = model.id ?: "",
            customer = model.customer?.let { customerMapper.mapToDomainModel(it) },
            status = model.status,
            service = model.service?.let { serviceDtoMapper.mapToDomainModel(it) },
            startTime = model.startTime,
            endTime = model.endTime,
            worker = customerMapper.mapToDomainModel(model.worker)
        )
    }

    override fun mapFromDomainModel(domainModel: Appointment): AppointmentDto {
        return AppointmentDto(
            id = domainModel.id,
            customer = domainModel.customer?.let { customerMapper.mapFromDomainModel(it) },
            status = domainModel.status,
            service = domainModel.service?.let { serviceDtoMapper.mapFromDomainModel(it) },
            startTime = domainModel.startTime,
            endTime = domainModel.endTime,
            worker = customerMapper.mapFromDomainModel(domainModel.worker)
        )
    }

    fun toDomainList(initial: List<AppointmentDto>): List<Appointment> {
        return initial.map { appointmentDto -> mapToDomainModel(appointmentDto) }
    }

    fun fromDomainList(initial: List<Appointment>): List<AppointmentDto> {
        return initial.map { appointment -> mapFromDomainModel(appointment) }
    }
}