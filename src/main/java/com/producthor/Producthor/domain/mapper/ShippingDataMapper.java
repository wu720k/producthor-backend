package com.producthor.Producthor.domain.mapper;


import com.producthor.Producthor.domain.dao.ShippingDataDao;
import com.producthor.Producthor.domain.dto.ShippingDataDto;
import com.producthor.Producthor.domain.model.ShippingData;

public final class ShippingDataMapper {

    private ShippingDataMapper(){}

    public static ShippingData toEntity(ShippingDataDto dto) {
        if (dto == null) return null;
        return ShippingData.builder()
                .postalCode(dto.getPostalCode())
                .city(dto.getCity())
                .street(dto.getStreet())
                .houseNumber(dto.getHouseNumber())
                .additionalInfo(dto.getAdditionalInfo())
                .build();
    }

    public static void updateEntityFromDto(ShippingDataDto dto, ShippingData entity) {
        if (dto == null || entity == null) return;
        if (dto.getPostalCode() != null)   entity.setPostalCode(dto.getPostalCode());
        if (dto.getCity() != null)         entity.setCity(dto.getCity());
        if (dto.getStreet() != null)       entity.setStreet(dto.getStreet());
        if (dto.getHouseNumber() != null)  entity.setHouseNumber(dto.getHouseNumber());
        if (dto.getAdditionalInfo() != null) entity.setAdditionalInfo(dto.getAdditionalInfo());
    }

    public static ShippingDataDao toDao(ShippingData entity) {
        if (entity == null) return null;
        return ShippingDataDao.builder()
                .id(entity.getId())
                .postalCode(entity.getPostalCode())
                .city(entity.getCity())
                .street(entity.getStreet())
                .houseNumber(entity.getHouseNumber())
                .additionalInfo(entity.getAdditionalInfo())
                .build();
    }
}