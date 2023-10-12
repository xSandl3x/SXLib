package ua.xsandl3x.api.commands.context;

import org.bukkit.command.CommandSender;
import ua.xsandl3x.api.commands.context.argument.IArgument;

/**
 * Represents a context with owned properties.
 *
 * @param <S> The type of the command sender.
 */
public interface ICommandContext<S extends CommandSender> {

    /**
     * Retrieve the sender of the command
     *
     * @return the command sender
     */
    S getSource();

    /**
     * Gets the command label used for executing this command.
     *
     * @return The command label that was employed to invoke this command.
     */
    String getLabel();

    /**
     * Gets the argument at the specified index.
     *
     * @param index The ID of the argument.
     * @return The name of the argument or the default value
     */
    String getRawArgument(int index);

    /**
     * Gets the argument object at the specified index.
     *
     * @param index The ID of the argument.
     * @return The argument object.
     */
    IArgument getArgument(int index);

    /**
     * Checks whether the objects arguments are empty.
     *
     * @return if the object has no arguments, otherwise false.
     */
    boolean isEmptyArguments();
}
