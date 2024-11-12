package py.com.cls.infrastructure.out.repository.point_usage_headers.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import py.com.cls.domain.models.point_usage_headers.PointUsageHeader;
import py.com.cls.infrastructure.out.repository.customers.mapper.CustomerMapper;
import py.com.cls.infrastructure.out.repository.point_concepts.mapper.PointsConceptMapper;
import py.com.cls.infrastructure.out.repository.point_usage_headers.entity.PointsUsageHeader;

@Mapper(uses = {CustomerMapper.class, PointsConceptMapper.class})
public interface PointsUsageHeaderMapper {
    PointsUsageHeaderMapper INSTANCE = Mappers.getMapper(PointsUsageHeaderMapper.class);
    PointUsageHeader toDomain(PointsUsageHeader pointsUsageHeader);
    PointsUsageHeader toEntity(PointUsageHeader pointUsageHeader);
    void mapDomainToEntity(PointUsageHeader pointUsageHeader, @MappingTarget PointsUsageHeader pointsUsageHeader);
}
