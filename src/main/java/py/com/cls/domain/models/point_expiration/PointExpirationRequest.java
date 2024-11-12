package py.com.cls.domain.models.point_expiration;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import py.com.cls.application.commands.CommandBase;
import py.com.cls.application.commands.CommandPageable;

@Getter
@Setter
@SuperBuilder
public class PointExpirationRequest extends CommandPageable {
    @Override
    public CommandBase validate() {
        return this;
    }
}
