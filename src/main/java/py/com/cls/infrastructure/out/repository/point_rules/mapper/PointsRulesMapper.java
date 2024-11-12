package py.com.cls.infrastructure.out.repository.point_rules.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import py.com.cls.domain.models.point_rule.PointRule;
import py.com.cls.domain.models.point_rule.PointRuleResponse;
import py.com.cls.infrastructure.out.repository.point_rules.entity.PointsRules;

import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi")
public interface PointsRulesMapper {
    PointsRulesMapper INSTANCE = Mappers.getMapper(PointsRulesMapper.class);
    PointRule toDomain(PointsRules pointsRules);
    PointsRules toEntity(PointRule pointRule);
    void mapDomainToEntity(PointRule pointRule, @MappingTarget PointsRules pointsRules);
    default PointRuleResponse<Page<PointRule>> mapToDomainList(Page<PointsRules> pointsRules){
        return Optional.of(pointsRules).map(this::toDomainList)
                .map(response -> PointRuleResponse.<Page<PointRule>>builder().data(response).build())
                .orElseGet(PointRuleResponse::new);
    }
    private Page<PointRule> toDomainList(Page<PointsRules> pointsRules){
        return new PageImpl<>(pointsRules.stream().map(this::toDomain)
                .collect(Collectors.toList()), pointsRules.getPageable(), pointsRules.getTotalElements());
    }
}
