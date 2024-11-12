package py.com.cls.domain.models.point_usage_summary;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import py.com.cls.domain.models.point_usage_details.PointUsageDetail;
import py.com.cls.domain.models.point_usage_headers.PointUsageHeader;

import java.util.List;

@Getter
@Setter
@Builder
public class PointUsageSummary {
    private PointUsageHeader header;
    private List<PointUsageDetail> details;
}
