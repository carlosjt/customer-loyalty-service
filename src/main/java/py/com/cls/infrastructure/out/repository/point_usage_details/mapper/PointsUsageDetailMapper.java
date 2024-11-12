package py.com.cls.infrastructure.out.repository.point_usage_details.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import py.com.cls.domain.models.point_usage_details.PointUsageDetail;
import py.com.cls.infrastructure.out.repository.point_usage_details.entity.PointsUsageDetail;
import py.com.cls.infrastructure.out.repository.point_usage_headers.mapper.PointsUsageHeaderMapper;
import py.com.cls.infrastructure.out.repository.point_wallets.mapper.PointsWalletMapper;

@Mapper(uses = {PointsUsageHeaderMapper.class, PointsWalletMapper.class})
public interface PointsUsageDetailMapper {
    PointsUsageDetailMapper INSTANCE = Mappers.getMapper(PointsUsageDetailMapper.class);
    PointUsageDetail toDomain(PointsUsageDetail pointsUsageDetail);
    PointsUsageDetail toEntity(PointUsageDetail pointUsageDetail);
    void mapDomainToEntity(PointUsageDetail pointUsageDetail, @MappingTarget PointsUsageDetail pointsUsageDetail);
}
