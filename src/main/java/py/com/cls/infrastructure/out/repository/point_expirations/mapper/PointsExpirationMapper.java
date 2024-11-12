package py.com.cls.infrastructure.out.repository.point_expirations.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import py.com.cls.domain.models.point_expiration.PointExpiration;
import py.com.cls.domain.models.point_expiration.PointExpirationResponse;
import py.com.cls.infrastructure.out.repository.point_expirations.entity.PointsExpiration;

import java.util.Optional;
import java.util.stream.Collectors;

@Mapper()
public interface PointsExpirationMapper {
    PointsExpirationMapper INSTANCE = Mappers.getMapper(PointsExpirationMapper.class);
    PointExpiration toDomain(PointsExpiration pointsExpiration);
    PointsExpiration toEntity(PointExpiration pointExpiration);
    void mapDomainToEntity(PointExpiration pointExpiration, @MappingTarget PointsExpiration pointsExpiration);
    default PointExpirationResponse<Page<PointExpiration>> mapToDomainList(Page<PointsExpiration> pointsExpirations){
        return Optional.of(pointsExpirations).map(this::toDomainList)
                .map(response -> PointExpirationResponse.<Page<PointExpiration>>builder().data(response).build())
                .orElseGet(PointExpirationResponse::new);
    }
    private Page<PointExpiration> toDomainList(Page<PointsExpiration> pointsExpirations){
        return new PageImpl<>(pointsExpirations.stream().map(this::toDomain)
                .collect(Collectors.toList()), pointsExpirations.getPageable(), pointsExpirations.getTotalElements());
    }
}
