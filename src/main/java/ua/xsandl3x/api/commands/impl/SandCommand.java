package ua.xsandl3x.api.commands.impl;

import com.google.common.collect.ImmutableList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ua.xsandl3x.api.commands.ICommand;
import ua.xsandl3x.api.commands.accessibility.IAccessibility;
import ua.xsandl3x.api.commands.accessibility.impl.PermissionAccessibility;
import ua.xsandl3x.api.commands.accessibility.impl.SourceAccessibility;
import ua.xsandl3x.api.commands.context.meta.impl.SimpleCommandMeta;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter(AccessLevel.PROTECTED)
@FieldDefaults(makeFinal = true)
public abstract class SandCommand<S extends CommandSender> extends SimpleCommandMeta implements ICommand<S> {

    private ParameterSession parameterSession;
    private ImmutableList<IAccessibility<S>> accessibilityList = ImmutableList.<IAccessibility<S>>builder()
            .add(new SourceAccessibility<>(this))
            .add(new PermissionAccessibility<>(this))
            .build();

    public SandCommand(String label, String description, List<String> aliases) {
        super(label, description, aliases);

        parameterSession = new ParameterSession();
    }

    @Override
    public List<String> tryTabComplete(CommandSender sender, String label, String[] args) {
        ParameterSession session = getParameterSession();
        IParameterTransformer<String> transformer = session.getTransformer();

        return getCompletions().stream()
                .filter(values -> hasAccess(sender))
                .filter(values -> (args.length - 1) < values.length)
                .map(values -> transformer.transform(values[args.length - 1]))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    protected boolean hasAccess(CommandSender sender) {
        return getAccessibilityList().stream()
                .noneMatch(accessibility -> accessibility.checkAccessibility((S) sender));
    }

    protected abstract List<String[]> getCompletions();

    protected static class ParameterSession {

        private final Map<String, IParameter> parameterMap = new HashMap<>();

        protected ParameterSession() {
            addParameterType("@boolean", argument -> Arrays.asList("true", "false"));
            addParameterType("@players", argument -> Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .collect(Collectors.toList()));
        }

        public void addParameterType(String argument, IParameter parameter) {
            if (!argument.startsWith("@")) {
                throw new IllegalArgumentException("Argument should start with '@'");
            }

            parameterMap.put(argument, parameter);
        }

        public void removeParameterType(String argument) {
            if (!argument.startsWith("@")) {
                throw new IllegalArgumentException("Argument should start with '@'");
            }

            parameterMap.remove(argument);
        }

        public IParameterTransformer<String> getTransformer() {
            return new ParameterTransformer();
        }

        private final class ParameterTransformer implements IParameterTransformer<String> {

            @Override
            public List<String> transform(@NonNull String type) {
                return parameterMap.entrySet().stream()
                        .filter(entry -> type.startsWith(entry.getKey()))
                        .map(entry -> {
                            String key = entry.getKey();
                            String remainder = type.substring(key.length());
                            IParameter parameter = entry.getValue();
                            return parameter.apply(remainder);
                        })
                        .findFirst()
                        .orElseGet(() -> Collections.singletonList(type));
            }
        }
    }

    public interface IParameter extends Function<String, List<String>> { }

    public interface IParameterTransformer<T> {

        List<String> transform(T type);
    }
}
