package py.com.cls.infrastructure.out.repository.point_wallets.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import py.com.cls.domain.models.point_wallets.PointWallet;
import py.com.cls.infrastructure.out.repository.customers.mapper.CustomerMapper;
import py.com.cls.infrastructure.out.repository.point_expirations.mapper.PointsExpirationMapper;
import py.com.cls.infrastructure.out.repository.point_rules.mapper.PointsRuleMapper;
import py.com.cls.infrastructure.out.repository.point_wallets.entity.PointsWallet;

@Mapper(uses = {CustomerMapper.class, PointsExpirationMapper.class, PointsRuleMapper.class})
public interface PointsWalletMapper {
    PointsWalletMapper INSTANCE = Mappers.getMapper(PointsWalletMapper.class);
    PointWallet toDomain(PointsWallet pointsWallet);
    PointsWallet toEntity(PointWallet pointWallet);
    void mapDomainToEntity(PointWallet pointWallet, @MappingTarget PointsWallet pointsWallet);
}
