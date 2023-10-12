package ua.xsandl3x.api.commands.context.meta.impl;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import java.util.List;

@Getter
@FieldDefaults(makeFinal = true)
public class SimpleCommandMeta extends SubCommandMeta {

    private List<String> aliases;

    public SimpleCommandMeta(String label, String description, List<String> aliases) {
        super(label, description);

        this.aliases = aliases;
    }
}
