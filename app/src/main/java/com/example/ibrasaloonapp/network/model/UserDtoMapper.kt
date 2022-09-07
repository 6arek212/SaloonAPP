package com.example.ibrasaloonapp.network.model

import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.domain.util.DomainMapper

class UserDtoMapper : DomainMapper<UserDto, User> {

    override fun mapToDomainModel(model: UserDto): User {
        return User(
            id = model.id,
            firstName = model.firstName,
            lastName = model.lastName,
            phone = model.phone
        )
    }

    override fun mapFromDomainModel(domainModel: User): UserDto {
        return UserDto(
            id = domainModel.id,
            firstName = domainModel.firstName,
            lastName = domainModel.lastName,
            phone = domainModel.phone
        )
    }

    fun toDomainList(initial: List<UserDto>): List<User> {
        return initial.map { userDto -> mapToDomainModel(userDto) }
    }

    fun fromDomainList(initial: List<User>): List<UserDto> {
        return initial.map { user -> mapFromDomainModel(user) }
    }
}