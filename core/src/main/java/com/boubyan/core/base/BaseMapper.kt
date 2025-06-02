package com.boubyan.core.base

/**
 * Interface for mapping between DTO (Data Transfer Object) and Domain models.
 * @param DTO The data transfer object type
 * @param DOMAIN The domain model type
 */
interface BaseMapper<DTO, DOMAIN> {
    /**
     * Maps a DTO object to a Domain model.
     * @param dto The DTO object to map
     * @return The mapped Domain model
     */
    fun mapToDomain(dto: DTO): DOMAIN

    /**
     * Maps a Domain model to a DTO object.
     * @param domain The Domain model to map
     * @return The mapped DTO object
     */
    fun mapToDto(domain: DOMAIN): DTO
}

/**
 * Interface for mapping lists between DTO and Domain models.
 * @param DTO The data transfer object type
 * @param DOMAIN The domain model type
 */
interface BaseListMapper<DTO, DOMAIN> {
    /**
     * Maps a list of DTO objects to a list of Domain models.
     * @param dtoList The list of DTO objects to map
     * @return The list of mapped Domain models
     */
    fun mapToDomainList(dtoList: List<DTO>): List<DOMAIN>

    /**
     * Maps a list of Domain models to a list of DTO objects.
     * @param domainList The list of Domain models to map
     * @return The list of mapped DTO objects
     */
    fun mapToDtoList(domainList: List<DOMAIN>): List<DTO>
}

/**
 * Abstract base implementation of both BaseMapper and BaseListMapper interfaces.
 * Provides default implementations for list mapping operations.
 * @param DTO The data transfer object type
 * @param DOMAIN The domain model type
 */
abstract class BaseMapperImpl<DTO, DOMAIN> : BaseMapper<DTO, DOMAIN>, BaseListMapper<DTO, DOMAIN> {
    
    /**
     * Default implementation for mapping a list of DTOs to Domain models.
     * Uses the single object mapper for each item in the list.
     */
    override fun mapToDomainList(dtoList: List<DTO>): List<DOMAIN> {
        return dtoList.map { mapToDomain(it) }
    }
    
    /**
     * Default implementation for mapping a list of Domain models to DTOs.
     * Uses the single object mapper for each item in the list.
     */
    override fun mapToDtoList(domainList: List<DOMAIN>): List<DTO> {
        return domainList.map { mapToDto(it) }
    }
}