package ua.xsandl3x.api.commands.service;

import org.bukkit.plugin.Plugin;
import ua.xsandl3x.api.commands.impl.SandCommand;

/**
 * A service which can create {@link SandCommand}s.
 */
public interface ICommandService {

    /**
     * Registers a command in the service.
     *
     * @param command The command object to be registered in the service.
     */
    void register(SandCommand<?> command, Plugin plugin);

    /**
     * Unregisters a command from the service.
     *
     * @param command The command object to be removed from the service.
     */
    void unregister(SandCommand<?> command);
}

