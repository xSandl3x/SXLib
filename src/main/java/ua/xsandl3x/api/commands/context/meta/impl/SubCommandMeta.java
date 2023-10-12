package ua.xsandl3x.api.commands.context.meta.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import ua.xsandl3x.api.commands.context.meta.ICommandMeta;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class SubCommandMeta implements ICommandMeta {

    private String label;
    private String description;
}
