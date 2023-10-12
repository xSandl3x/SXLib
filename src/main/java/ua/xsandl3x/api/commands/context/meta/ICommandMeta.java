package ua.xsandl3x.api.commands.context.meta;

import java.util.Collections;
import java.util.List;

/**
 * This type represents the storage mechanism for auxiliary team data.
 */
public interface ICommandMeta {

    /**
     * Gets the label of the command.
     *
     * @return The command label.
     */
    String getLabel();

    /**
     * Gets the description of the command.
     *
     * @return The command description.
     */
    String getDescription();

    /**
     * Gets the syntax of the command.
     *
     * @return The command syntax.
     */
    default String getSyntax() {
        return ("/").concat(getLabel());
    }

    /**
     * Gets a list of aliases for the command.
     *
     * @return The list of aliases for the command.
     */
    default List<String> getAliases() {
        return Collections.emptyList();
    }
}
