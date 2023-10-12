package ua.xsandl3x.api.commands.accessibility.impl;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ua.xsandl3x.api.commands.IExecutable;
import ua.xsandl3x.api.commands.accessibility.AbstractAccessibility;
import java.util.function.Predicate;

public class SourceAccessibility<S extends CommandSender> extends AbstractAccessibility<S> {

    public SourceAccessibility(IExecutable<S> executable) {
        super(executable);

        failAction(source -> source.sendMessage("This command only for players!"));
    }

    @Override
    public boolean checkAccessibility(S source) {
        Predicate<S> available = __ -> !(source instanceof Player);
        available = available.and(__ -> {
            Class<S> sourceType = getExecutable().getSourceType();
            return !(sourceType.isAssignableFrom(CommandSender.class));
        });

        return available.test(source);
    }
}
