package com.example.ibrasaloonapp.network.model

import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.domain.model.WorkingDate
import com.example.ibrasaloonapp.domain.util.DomainMapper

class WorkingDateDtoMapper : DomainMapper<WorkingDateDto, WorkingDate> {
    override fun mapToDomainModel(model: WorkingDateDto): WorkingDate {
        return WorkingDate(
            id = model.id,
            date = model.date
        )
    }

    override fun mapFromDomainModel(domainModel: WorkingDate): WorkingDateDto {
        return WorkingDateDto(
            id = domainModel.id,
            date = domainModel.date
        )
    }


    fun toDomainList(initial: List<WorkingDateDto>): List<WorkingDate> {
        return initial.map { userDto -> mapToDomainModel(userDto) }
    }

    fun fromDomainList(initial: List<WorkingDate>): List<WorkingDateDto> {
        return initial.map { user -> mapFromDomainModel(user) }
    }
}