package ua.xsandl3x.api.commands.context.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.command.CommandSender;
import ua.xsandl3x.api.commands.context.ICommandContext;
import ua.xsandl3x.api.commands.context.argument.IArgument;
import ua.xsandl3x.api.commands.context.argument.impl.SimpleArgument;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class SimpleCommandContext<S extends CommandSender> implements ICommandContext<S> {

    @Getter
    private S source;
    @Getter
    private String label;
    private String[] arguments;

    @Override
    public String getRawArgument(int index) {
        if (index < 0 || index >= arguments.length) {
            return null;
        }
        return arguments[index];
    }

    @Override
    public IArgument getArgument(int index) {
        return new SimpleArgument(index, getRawArgument(index));
    }

    @Override
    public boolean isEmptyArguments() {
        return (arguments.length == 0);
    }
}
