package ua.xsandl3x.api.commands;

import org.bukkit.command.CommandSender;
import ua.xsandl3x.api.commands.context.ICommandContext;
import java.lang.reflect.*;
import java.util.Arrays;

/**
 * Represents a contract for executing a command in a specific context.
 *
 * @param <S> The type of the command sender.
 */
public interface IExecutable<S extends CommandSender> {

    /**
     * Executes the given command.
     *
     * @param context The execution context of the command
     * which may include information about the command and the sender.
     */
    void execute(ICommandContext<S> context);

    /**
     * Returns the class of the command sender type.
     *
     * @return The class of the command sender type.
     * @throws IllegalArgumentException if the sender type cannot be determined.
     */
    @SuppressWarnings("unchecked")
    default Class<S> getSourceType() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        return Arrays.stream(((ParameterizedType) genericSuperclass).getActualTypeArguments())
                .filter(type -> type instanceof Class)
                .findFirst()
                .map(type -> (Class<S>) type)
                .orElseThrow(() -> new IllegalArgumentException("Unable to determine sender type."));
    }
}
