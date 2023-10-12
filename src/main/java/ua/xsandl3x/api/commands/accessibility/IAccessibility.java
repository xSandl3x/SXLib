package ua.xsandl3x.api.commands.accessibility;

import org.bukkit.command.CommandSender;
import java.util.function.Consumer;

/**
 * Represents a contract for working with the accessibility of objects.
 *
 * @param <S> The type of the command sender.
 */
public interface IAccessibility<S extends CommandSender> {

    /**
     * Sets the action to be taken after an unsuccessful availability check.
     *
     * @param sourceConsumer The function to be executed in case of an error.
     */
    void failAction(Consumer<S> sourceConsumer);

    /**
     * Executes the code in case of availability check error.
     *
     * @param source The sender of the command.
     */
    void apply(S source);

    /**
     * Checks the availability of the object.
     *
     * @param source The sender of the command.
     * @return if the object is available, otherwise false.
     */
    boolean checkAccessibility(S source);
}
