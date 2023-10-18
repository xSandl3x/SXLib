package ua.xsandl3x.api.commands.impl;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import ua.xsandl3x.api.commands.IExecutable;
import ua.xsandl3x.api.commands.accessibility.IAccessibility;
import ua.xsandl3x.api.commands.annotation.Completer;
import ua.xsandl3x.api.commands.annotation.SubCommand;
import ua.xsandl3x.api.commands.context.ICommandContext;
import ua.xsandl3x.api.commands.context.impl.SimpleCommandContext;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public abstract class ParentCommand<S extends CommandSender> extends SandCommand<S>{

    private final int minArg = 2; // test various
    private final SubCommandNode subCommandNode;
    private final Map<String, Method> subCommandMap = new HashMap<>();

    public ParentCommand(String label, String description, List<String> aliases) {
        super(label, description, aliases);

        subCommandNode = new SubCommandNode();
    }

    public ParentCommand(String label, List<String> aliases) {
        this(label, "command by SandAPI", aliases);
    }

    public ParentCommand(String label, String[] aliases) {
        this(label, Arrays.asList(aliases));
    }

    public ParentCommand(String label) {
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
            subCommandNode.execute(context);
        }
    }

    @Override
    public List<String> tryTabComplete(CommandSender sender, String label, String[] args) {
        return null;
    }

    private class SubCommandNode implements IExecutable<S> {

        private final ParentCommand<S> parentCommand = ParentCommand.this;

        private SubCommandNode() {
            getSubCommands().forEach(method -> {
                SubCommand subCommand = method.getDeclaredAnnotation(SubCommand.class);
                for (String alias : subCommand.aliases()) {
                    parentCommand.subCommandMap.put(alias.toLowerCase(), method);
                }
            });
        }

        @Override
        public void execute(ICommandContext<S> context) {
            String argument = context.getRawArgument(minArg - 1);
            Method subCommand = parentCommand.subCommandMap.get(argument);
            Optional<Method> subCommandOptional = Optional.ofNullable(subCommand)
                    .filter(method -> !context.isEmptyArguments());

            subCommandOptional.map(method -> new HandledSubCommand(method, context))
                    .ifPresent(HandledSubCommand::handle);

            if (!subCommandOptional.isPresent()) {
                parentCommand.execute(context);
            }
        }

        private List<Method> getSubCommands() {
            Class<?> clazz = parentCommand.getClass();
            return Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(SubCommand.class))
                    .filter(this::hasSingleParameter)
                    .peek(method -> method.setAccessible(true))
                    .collect(Collectors.toList());
        }

        private boolean hasSingleParameter(Method method) {
            return Arrays.stream(method.getParameterTypes())
                    .filter(parameterType -> parameterType.equals(ICommandContext.class))
                    .count() == 1;
        }

        @RequiredArgsConstructor
        @FieldDefaults(makeFinal = true)
        private class HandledSubCommand {

            private Method method;
            private ICommandContext<S> context;

            private void handle() {
                try {
                    method.invoke(ParentCommand.this, context);
                }
                catch (Throwable throwable) {
                    throw new RuntimeException(throwable);
                }
            }
        }
    }
}
