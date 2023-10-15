package ua.xsandl3x.api.commands.impl;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;
import ua.xsandl3x.api.commands.ICommand;
import ua.xsandl3x.api.commands.accessibility.IAccessibility;
import ua.xsandl3x.api.commands.accessibility.impl.PermissionAccessibility;
import ua.xsandl3x.api.commands.accessibility.impl.SourceAccessibility;
import ua.xsandl3x.api.commands.annotation.Completer;
import ua.xsandl3x.api.commands.context.ICommandContext;
import ua.xsandl3x.api.commands.context.impl.SimpleCommandContext;
import ua.xsandl3x.api.commands.context.meta.impl.SimpleCommandMeta;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public abstract class SimpleCommand<S extends CommandSender> extends SandCommand<S> {

    private final ImmutableList<IAccessibility<S>> accessibilityList = ImmutableList.<IAccessibility<S>>builder()
            .add(new SourceAccessibility<>(this))
            .add(new PermissionAccessibility<>(this))
            .build();

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
        Optional<IAccessibility<S>> accessibilityOptional = accessibilityList.stream()
                .filter(accessibility -> accessibility.checkAccessibility(context.getSource()))
                .findFirst();

        accessibilityOptional.ifPresent(accessibility -> accessibility.apply(context.getSource()));
        if (!accessibilityOptional.isPresent()) {
            execute(context);
        }
    }

    @Override
    public List<String> tryTabComplete(CommandSender sender, String label, String[] args) {
        return getAnnotatedMethods().stream()
                .map(method -> method.getDeclaredAnnotation(Completer.class))
                .map(completer -> {
                    int argumentIndex = args.length - 1;
                    String[] completions = completer.value();
                    return (argumentIndex < completions.length) ? completions[argumentIndex] : "";
                })
                .collect(Collectors.toList());
    }

    private List<Method> getAnnotatedMethods() {
        return Arrays.stream(getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Completer.class))
                .collect(Collectors.toList());
    }
}
