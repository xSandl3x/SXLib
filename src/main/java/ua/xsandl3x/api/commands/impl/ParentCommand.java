package ua.xsandl3x.api.commands.impl;

import com.google.common.collect.ImmutableMap;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.bukkit.command.CommandSender;
import ua.xsandl3x.api.commands.IExecutable;
import ua.xsandl3x.api.commands.accessibility.IAccessibility;
import ua.xsandl3x.api.commands.annotation.Completer;
import ua.xsandl3x.api.commands.annotation.SubCommand;
import ua.xsandl3x.api.commands.context.ICommandContext;
import ua.xsandl3x.api.commands.context.impl.SimpleCommandContext;;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true)
public abstract class ParentCommand<S extends CommandSender> extends SandCommand<S>{

    // Minimum argument from which subcommands and their suggestions start working.
    @NonFinal
    @Setter(AccessLevel.PROTECTED)
    private int minArg = 1;
    private SubCommandNode subCommandNode;
    private Map<String, Method> subCommandMap = new HashMap<>();
    private ImmutableMap<Integer, TabCompletionStrategy> tabCompletionMap = ImmutableMap.<Integer, TabCompletionStrategy>builder()
            .put(0, new SubCommandTabCompletionStrategy())
            .put(1, new ArgumentTabCompletionStrategy())
            .build();

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
        int comparison = Integer.compare(args.length, minArg);
        return tabCompletionMap.entrySet().stream()
                .filter(entry -> hasAccess(sender))
                .filter(entry -> entry.getKey() == comparison)
                .map(Map.Entry::getValue)
                .map(value -> value.tabComplete(sender, label, args))
                .findFirst()
                .orElseGet(() -> super.tryTabComplete(sender, label, args));
    }

    @Override
    protected List<String[]> getCompletions() {
        return Arrays.stream(getClass().getDeclaredMethods())
                .filter(method -> !method.isAnnotationPresent(SubCommand.class))
                .filter(method -> method.isAnnotationPresent(Completer.class))
                .map(method -> method.getDeclaredAnnotation(Completer.class))
                .map(Completer::value)
                .collect(Collectors.toList());
    }

    private class SubCommandNode implements IExecutable<S> {

        private final ParentCommand<S> parentCommand = ParentCommand.this;

        private SubCommandNode() {
            getSubCommands().forEach(method -> {
                SubCommand subCommand = method.getDeclaredAnnotation(SubCommand.class);
                for (String alias : subCommand.aliases()) {
                    subCommandMap.put(alias.toLowerCase(), method);
                }
            });
        }

        @Override
        public void execute(ICommandContext<S> context) {
            String argument = context.getRawArgument(minArg - 1);
            Method subCommand = subCommandMap.get(argument);
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

        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
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

    private interface TabCompletionStrategy {

        List<String> tabComplete(CommandSender sender, String label, String[] args);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private class ArgumentTabCompletionStrategy implements TabCompletionStrategy {

        @Override
        public List<String> tabComplete(CommandSender sender, String label, String[] args) {
            ParameterSession session = getParameterSession();
            IParameterTransformer<String> transformer = session.getTransformer();
            String subCommandName = args[minArg - 1].toLowerCase();
            Method subCommandMethod = subCommandMap.get(subCommandName);

            return Optional.ofNullable(subCommandMethod)
                    .map(method -> method.getDeclaredAnnotation(Completer.class))
                    .map(Completer::value)
                    .map(value -> {
                        int argumentIndex = args.length - minArg;
                        if (argumentIndex <= value.length) {
                            return transformer.transform(value[argumentIndex - 1]);
                        }
                        return null;
                    })
                    .orElse(Collections.emptyList());
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private class SubCommandTabCompletionStrategy implements TabCompletionStrategy {

        @Override
        public List<String> tabComplete(CommandSender sender, String label, String[] args) {
            String subCommandName = args[minArg - 1].toLowerCase();
            return subCommandMap.keySet().stream()
                    .filter(key -> key.startsWith(subCommandName))
                    .collect(Collectors.toList());
        }
    }
}
