package ua.xsandl3x.api.commands.accessibility.impl;

import lombok.experimental.FieldDefaults;
import org.bukkit.command.CommandSender;
import ua.xsandl3x.api.commands.IExecutable;
import ua.xsandl3x.api.commands.accessibility.AbstractAccessibility;
import ua.xsandl3x.api.commands.annotation.Permission;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true)
public class PermissionAccessibility<S extends CommandSender> extends AbstractAccessibility<S> {

    private Class<?> executeClass = getExecutable().getClass();

    public PermissionAccessibility(IExecutable<S> executable) {
        super(executable);

        getAnnotatedMethods().forEach(method -> {
            method.setAccessible(true);

            Permission permission = method.getDeclaredAnnotation(Permission.class);
            failAction(source -> source.sendMessage(permission.message()));
        });
    }

    // Getting a list of methods with the 'Permission' annotation.
    private List<Method> getAnnotatedMethods() {
        return Arrays.stream(executeClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Permission.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkAccessibility(S source) {
        return getAnnotatedMethods().stream()
                .map(method -> {
                    method.setAccessible(true);
                    return method.getDeclaredAnnotation(Permission.class);
                })
                .map(Permission::value)

                .filter(Objects::nonNull)
                .anyMatch(value -> !source.hasPermission(value));
    }
}
