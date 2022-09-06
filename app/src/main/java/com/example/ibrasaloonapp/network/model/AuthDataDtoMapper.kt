package com.example.ibrasaloonapp.network.model

import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.domain.util.DomainMapper
import javax.inject.Inject

class AuthDataDtoMapper
@Inject
constructor(val userDtoMapper: UserDtoMapper) : DomainMapper<AuthDataDto, AuthData> {

    override fun mapToDomainModel(model: AuthDataDto): AuthData {
        return AuthData(
            user = userDtoMapper.mapToDomainModel(model.customer),
            token = model.token,
            expiresIn = model.expiresIn
        )
    }

    override fun mapFromDomainModel(domainModel: AuthData): AuthDataDto {
        return AuthDataDto(
            customer = userDtoMapper.mapFromDomainModel(domainModel.user),
            token = domainModel.token,
            expiresIn = domainModel.expiresIn
        )
    }
}

