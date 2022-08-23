package com.example.ibrasaloonapp.network.model

import com.example.ibrasaloonapp.domain.model.Customer
import com.example.ibrasaloonapp.domain.util.DomainMapper

class CustomerDtoMapper : DomainMapper<CustomerDto, Customer> {

    override fun mapToDomainModel(model: CustomerDto): Customer {
        return Customer(
            id = model.id,
            firstName = model.firstName,
            lastName = model.lastName,
            phone = model.phone
        )
    }

    override fun mapFromDomainModel(domainModel: Customer): CustomerDto {
        return CustomerDto(
            id = domainModel.id,
            firstName = domainModel.firstName,
            lastName = domainModel.lastName,
            phone = domainModel.phone
        )
    }
}