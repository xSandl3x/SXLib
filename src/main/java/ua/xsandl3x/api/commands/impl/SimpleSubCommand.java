package ua.xsandl3x.api.commands.impl;

import org.bukkit.command.CommandSender;
import ua.xsandl3x.api.commands.ICommand;
import ua.xsandl3x.api.commands.context.meta.impl.SubCommandMeta;

public abstract class SimpleSubCommand<S extends CommandSender> extends SubCommandMeta implements ICommand.ISubCommand<S> {

    public SimpleSubCommand(String label, String description) {
        super(label, description);
    }
}
