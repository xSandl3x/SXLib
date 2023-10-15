package ua.xsandl3x.api.commands.impl;

import org.bukkit.command.CommandSender;
import ua.xsandl3x.api.commands.ICommand;
import ua.xsandl3x.api.commands.context.meta.impl.SimpleCommandMeta;

import java.util.List;

public abstract class SandCommand<S extends CommandSender> extends SimpleCommandMeta implements ICommand<S> {

    public SandCommand(String label, String description, List<String> aliases) {
        super(label, description, aliases);
    }
}
