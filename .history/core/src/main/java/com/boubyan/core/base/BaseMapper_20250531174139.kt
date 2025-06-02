package com.boubyan.core.base

interface BaseMapper<DTO, DOMAIN> {
    fun mapToDomain(dto: DTO): DOMAIN
    fun mapToDto(domain: DOMAIN): DTO
}

interface BaseListMapper<DTO, DOMAIN> {
    fun mapToDomainList(dtoList: List<DTO>): List<DOMAIN>
    fun mapToDtoList(domainList: List<DOMAIN>): List<DTO>
}

abstract class BaseMapperImpl<DTO, DOMAIN> : BaseMapper<DTO, DOMAIN>, BaseListMapper<DTO, DOMAIN> {
    
    override fun mapToDomainList(dtoList: List<DTO>): List<DOMAIN> {
        return dtoList.map { mapToDomain(it) }
    }
    
    override fun mapToDtoList(domainList: List<DOMAIN>): List<DTO> {
        return domainList.map { mapToDto(it) }
    }
}