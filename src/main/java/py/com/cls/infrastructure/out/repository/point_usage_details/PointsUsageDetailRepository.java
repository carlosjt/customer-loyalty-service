package py.com.cls.infrastructure.out.repository.point_usage_details;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import py.com.cls.infrastructure.out.repository.point_usage_details.entity.PointsUsageDetail;

@Repository
public interface PointsUsageDetailRepository extends CrudRepository<PointsUsageDetail, Integer> {}
