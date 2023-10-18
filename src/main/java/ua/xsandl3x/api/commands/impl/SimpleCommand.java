package ua.xsandl3x.api.commands.impl;

import org.bukkit.command.CommandSender;
import ua.xsandl3x.api.commands.accessibility.IAccessibility;
import ua.xsandl3x.api.commands.annotation.Completer;
import ua.xsandl3x.api.commands.context.ICommandContext;
import ua.xsandl3x.api.commands.context.impl.SimpleCommandContext;
import java.util.*;
import java.util.stream.Collectors;

public abstract class SimpleCommand<S extends CommandSender> extends SandCommand<S> {

    public SimpleCommand(String label, String description, List<String> aliases) {
        super(label, description, aliases);
    }

    public SimpleCommand(String label, List<String> aliases) {
        this(label, "command by SandAPI", aliases);
    }

    public SimpleCommand(String label, String[] aliases) {
        this(label, Arrays.asList(aliases));
    }

    public SimpleCommand(String label) {
        this(label, new String[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void tryExecute(CommandSender sender, String label, String[] args) {
        ICommandContext<S> context = (ICommandContext<S>) new SimpleCommandContext<>(sender, label, args);
        Optional<IAccessibility<S>> accessibilityOptional = getAccessibilityList().stream()
                .filter(accessibility -> accessibility.checkAccessibility(context.getSource()))
                .findFirst();

        accessibilityOptional.ifPresent(accessibility -> accessibility.apply(context.getSource()));
        if (!accessibilityOptional.isPresent()) {
            execute(context);
        }
    }
}
