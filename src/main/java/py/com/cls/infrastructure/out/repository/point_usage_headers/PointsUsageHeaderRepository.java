package py.com.cls.infrastructure.out.repository.point_usage_headers;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import py.com.cls.infrastructure.out.repository.point_usage_headers.entity.PointsUsageHeader;

@Repository
public interface PointsUsageHeaderRepository extends CrudRepository<PointsUsageHeader, Integer> {}
