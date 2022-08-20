package com.example.ibrasaloonapp.network.model

import com.example.ibrasaloonapp.domain.model.Session
import com.example.ibrasaloonapp.domain.util.DomainMapper

class SessionDtoMapper : DomainMapper<SessionDto, Session> {
    override fun mapToDomainModel(model: SessionDto): Session {
        return Session(
            type = model.type,
            date = model.date,
            id = model.id ?: "1"
        )
    }

    override fun mapFromDomainModel(domainModel: Session): SessionDto {
        return SessionDto(
            type = domainModel.type,
            date = domainModel.date,
            id = domainModel.id
        )
    }

    fun toDomainList(initial: List<SessionDto>): List<Session> {
        return initial.map { sessionDto -> mapToDomainModel(sessionDto) }
    }

    fun fromDomainList(initial: List<Session>): List<SessionDto> {
        return initial.map { session -> mapFromDomainModel(session) }
    }
}