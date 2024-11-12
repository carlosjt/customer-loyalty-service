package py.com.cls.domain.models.point_usage_details;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import py.com.cls.application.commands.CommandBase;
import py.com.cls.application.commands.CommandPageable;

@Getter
@Setter
@SuperBuilder
public class PointUsageDetailRequest extends CommandPageable {
    @Override
    public CommandBase validate() {
        return this;
    }
}
