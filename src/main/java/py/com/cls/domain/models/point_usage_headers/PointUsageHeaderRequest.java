package py.com.cls.domain.models.point_usage_headers;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import py.com.cls.application.commands.CommandBase;
import py.com.cls.application.commands.CommandPageable;

@Getter
@Setter
@SuperBuilder
public class PointUsageHeaderRequest extends CommandPageable {
    @Override
    public CommandBase validate() {
        return this;
    }
}
