package py.com.cls.application.commands;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class CommandPageable extends CommandBase {
    private int page;
    private int pageSize;
}

