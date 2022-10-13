package com.example.ibrasaloonapp.network.model

import android.app.Application
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.domain.model.Service
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.domain.util.DomainMapper
import javax.inject.Inject

class ServiceDtoMapper
@Inject
constructor(
    private val context: Application
) : DomainMapper<ServiceDto, Service> {


    override fun mapToDomainModel(model: ServiceDto): Service {

        val title = when (model.title) {
            "Hair Cut" -> context.getString(R.string.hair_cut)
            "Wax" -> context.getString(R.string.wax)
            "Face Cut" -> context.getString(R.string.face_cut)
            "Hair Cut + Face Cut" -> context.getString(R.string.hair_cut_and_face_cut)
            "Massage" -> context.getString(R.string.massage)
            else -> model.title
        }

        return Service(
            id = model.id,
            title = title,
            price = model.price
        )
    }

    override fun mapFromDomainModel(domainModel: Service): ServiceDto {
        return ServiceDto(
            id = domainModel.id,
            title = domainModel.title,
            price = domainModel.price
        )
    }

    fun toDomainList(initial: List<ServiceDto>): List<Service> {
        return initial.map { serviceDto -> mapToDomainModel(serviceDto) }
    }

    fun fromDomainList(initial: List<Service>): List<ServiceDto> {
        return initial.map { service -> mapFromDomainModel(service) }
    }
}