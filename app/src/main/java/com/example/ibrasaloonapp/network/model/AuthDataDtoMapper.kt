package com.example.ibrasaloonapp.network.model

import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.domain.util.DomainMapper
import javax.inject.Inject

class AuthDataDtoMapper
@Inject
constructor(val customerDtoMapper: CustomerDtoMapper) : DomainMapper<AuthDataDto, AuthData> {

    override fun mapToDomainModel(model: AuthDataDto): AuthData {
        return AuthData(
            customer = customerDtoMapper.mapToDomainModel(model.customer),
            token = model.token,
            expiresIn = model.expiresIn
        )
    }

    override fun mapFromDomainModel(domainModel: AuthData): AuthDataDto {
        return AuthDataDto(
            customer = customerDtoMapper.mapFromDomainModel(domainModel.customer),
            token = domainModel.token,
            expiresIn = domainModel.expiresIn
        )
    }
}

