package com.example.ibrasaloonapp.network.model

import android.app.Application
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.KeyValueWrapper
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.util.DomainMapper
import javax.inject.Inject


const val HAIR_CUT = "Hair cut"
const val Wax = "Wax"


class AppointmentDtoMapper
@Inject
constructor(
    private val context: Application,
    private val customerMapper: UserDtoMapper
) : DomainMapper<AppointmentDto, Appointment> {

    override fun mapToDomainModel(model: AppointmentDto): Appointment {

        val service = model.service?.let {
            when (it) {
                HAIR_CUT -> {
                    KeyValueWrapper(model.service, context.getString(R.string.hair_cut))
                }

                Wax -> {
                    KeyValueWrapper(model.service, context.getString(R.string.wax))
                }
                else -> {
                    KeyValueWrapper(it,it)
                }
            }
        }

        return Appointment(
            id = model.id ?: "",
            customer = model.customer?.let { customerMapper.mapToDomainModel(it) },
            isActive = model.isActive,
            service = service,
            startTime = model.startTime,
            endTime = model.endTime,
            worker = customerMapper.mapToDomainModel(model.worker)
        )
    }

    override fun mapFromDomainModel(domainModel: Appointment): AppointmentDto {
        return AppointmentDto(
            id = domainModel.id,
            customer = domainModel.customer?.let { customerMapper.mapFromDomainModel(it) },
            isActive = domainModel.isActive,
            service = domainModel.service?.value,
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