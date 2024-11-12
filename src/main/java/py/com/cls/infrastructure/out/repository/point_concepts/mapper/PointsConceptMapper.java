package py.com.cls.infrastructure.out.repository.point_concepts.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import py.com.cls.domain.models.point_concept.PointConcept;
import py.com.cls.domain.models.point_concept.PointConceptResponse;
import py.com.cls.infrastructure.out.repository.point_concepts.entity.PointsConcept;

import java.util.Optional;
import java.util.stream.Collectors;

@Mapper()
public interface PointsConceptMapper {
    PointsConceptMapper INSTANCE = Mappers.getMapper(PointsConceptMapper.class);
    PointConcept toDomain(PointsConcept pointsConcept);
    PointsConcept toEntity(PointConcept pointConcept);
    void mapDomainToEntity(PointConcept pointConcept, @MappingTarget PointsConcept pointsConcept);
    default PointConceptResponse<Page<PointConcept>> mapToDomainList(Page<PointsConcept> pointsConcept){
        return Optional.of(pointsConcept).map(this::toDomainList)
                .map(response -> PointConceptResponse.<Page<PointConcept>>builder().data(response).build())
                .orElseGet(PointConceptResponse::new);
    }
    private Page<PointConcept> toDomainList(Page<PointsConcept> pointsConcept){
        return new PageImpl<>(pointsConcept.stream().map(this::toDomain)
                .collect(Collectors.toList()), pointsConcept.getPageable(), pointsConcept.getTotalElements());
    }
}
