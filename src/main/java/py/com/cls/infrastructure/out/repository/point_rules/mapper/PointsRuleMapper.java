package py.com.cls.infrastructure.out.repository.point_rules.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import py.com.cls.domain.models.point_rule.PointRule;
import py.com.cls.domain.models.point_rule.PointRuleResponse;
import py.com.cls.infrastructure.out.repository.point_rules.entity.PointsRule;

import java.util.Optional;
import java.util.stream.Collectors;

@Mapper()
public interface PointsRuleMapper {
    PointsRuleMapper INSTANCE = Mappers.getMapper(PointsRuleMapper.class);
    PointRule toDomain(PointsRule pointsRule);
    PointsRule toEntity(PointRule pointRule);
    void mapDomainToEntity(PointRule pointRule, @MappingTarget PointsRule pointsRule);
    default PointRuleResponse<Page<PointRule>> mapToDomainList(Page<PointsRule> pointsRules){
        return Optional.of(pointsRules).map(this::toDomainList)
                .map(response -> PointRuleResponse.<Page<PointRule>>builder().data(response).build())
                .orElseGet(PointRuleResponse::new);
    }
    private Page<PointRule> toDomainList(Page<PointsRule> pointsRules){
        return new PageImpl<>(pointsRules.stream().map(this::toDomain)
                .collect(Collectors.toList()), pointsRules.getPageable(), pointsRules.getTotalElements());
    }
}
