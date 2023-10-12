package ua.xsandl3x.api.commands.impl;

import org.bukkit.command.CommandSender;
import ua.xsandl3x.api.commands.ICommand;
import ua.xsandl3x.api.commands.IExecutable;
import ua.xsandl3x.api.commands.accessibility.IAccessibility;
import ua.xsandl3x.api.commands.accessibility.impl.PermissionAccessibility;
import ua.xsandl3x.api.commands.accessibility.impl.SourceAccessibility;
import ua.xsandl3x.api.commands.context.ICommandContext;
import ua.xsandl3x.api.commands.context.impl.SimpleCommandContext;
import ua.xsandl3x.api.commands.context.meta.impl.SimpleCommandMeta;

import java.util.*;

public abstract class SimpleCommand<S extends CommandSender> extends SimpleCommandMeta implements ICommand<S> {

    private final AccessibilitySession session = new AccessibilitySession(this);

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
        this(label, Collections.emptyList());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void tryExecute(CommandSender sender, String label, String[] args) {
        ICommandContext<S> context = (ICommandContext<S>) new SimpleCommandContext<>(sender, label, args);
        Optional<IAccessibility<S>> accessibilityOptional = session.accessibilityList.stream()
                .filter(accessibility -> accessibility.checkAccessibility(context.getSource()))
                .findFirst();

        accessibilityOptional.ifPresent(accessibility -> accessibility.apply(context.getSource()));
        if (!accessibilityOptional.isPresent()) {
            execute(context);
        }
    }

    @Override
    public List<String> tryTabComplete(CommandSender sender, String label, String[] args) {
        return Collections.emptyList();
    }

    private class AccessibilitySession {

        private final List<IAccessibility<S>> accessibilityList = new ArrayList<>();

        private AccessibilitySession(IExecutable<S> executable) {
            addAccessibility(new SourceAccessibility<>(executable));
            addAccessibility(new PermissionAccessibility<>(executable));
        }

        private void addAccessibility(IAccessibility<S> accessibility) {
            accessibilityList.add(accessibility);
        }

        private void removeAccessibility(IAccessibility<S> accessibility) {
            accessibilityList.remove(accessibility);
        }
    }
}
