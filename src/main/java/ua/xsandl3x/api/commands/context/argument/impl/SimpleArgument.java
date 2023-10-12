package ua.xsandl3x.api.commands.context.argument.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ua.xsandl3x.api.commands.context.argument.IArgument;

import java.util.Optional;
import java.util.function.Function;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class SimpleArgument implements IArgument {

    private int index;
    private String argumentValue;

    @Override
    public <T> Optional<T> as(Function<String, T> mapper) {
        try {
            return Optional.ofNullable(mapper.apply(argumentValue));
        }
        catch (Exception ex) {
            return Optional.empty();
        }
    }
}

