package ua.xsandl3x.api.commands;

import org.bukkit.command.CommandSender;
import java.util.List;

/**
 * Represents a command and suggestions.
 *
 * @param <S> The type of the command sender.
 */
public interface ICommand<S extends CommandSender> extends IExecutable<S> {

    /**
     * Attempt to execute command.
     *
     * @param sender The command sender.
     * @param label  The command label.
     * @param args   The command arguments.
     */
    void tryExecute(CommandSender sender, String label, String[] args);

    /**
     * Attempt to provide command suggestions.
     *
     * @param sender The command sender.
     * @param label  The command label.
     * @param args   The command arguments.
     * @return A list of suggestions.
     */
    List<String> tryTabComplete(CommandSender sender, String label, String[] args);

    /**
     * Nested interface for subcommands. Subcommands are parts of a larger command.
     *
     * @param <S> The type of the command sender.
     */
    interface ISubCommand<S extends CommandSender> extends IExecutable<S> {

        /**
         * Returns the id of the argument.
         *
         * @return The ID of the argument.
         */
        int assignedArgument();
    }
}
