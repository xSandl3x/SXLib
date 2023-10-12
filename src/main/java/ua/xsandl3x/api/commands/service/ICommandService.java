package ua.xsandl3x.api.commands.service;

import ua.xsandl3x.api.commands.impl.SimpleCommand;

/**
 * A service which can create {@link SimpleCommand}s.
 */
public interface ICommandService {

    /**
     * Registers a command in the service.
     *
     * @param command The command object to be registered in the service.
     */
    void register(SimpleCommand<?> command);

    /**
     * Unregisters a command from the service.
     *
     * @param command The command object to be removed from the service.
     */
    void unregister(SimpleCommand<?> command);
}

