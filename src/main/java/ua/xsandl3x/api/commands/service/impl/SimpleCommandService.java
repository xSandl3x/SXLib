package ua.xsandl3x.api.commands.service.impl;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import ua.xsandl3x.api.commands.impl.SimpleCommand;
import ua.xsandl3x.api.commands.service.ICommandService;
import ua.xsandl3x.api.utils.CommandMapUtil;
import java.util.List;
import java.util.Optional;

public class SimpleCommandService implements ICommandService {

    private static final CommandMap COMMAND_MAP = CommandMapUtil.getCommandMap();

    @Override
    public void register(SimpleCommand<?> command) {
        Command simpleCommand = new Command(
                command.getLabel(), command.getDescription(),
                command.getSyntax(), command.getAliases()
        ) {
            @Override
            public boolean execute(@NonNull CommandSender sender, @NonNull String label, @NonNull String[] args) {
                command.tryExecute(sender, label, args);
                return false;
            }

            @SneakyThrows(IllegalArgumentException.class)
            @Override
            public @NonNull List<String> tabComplete(@NonNull CommandSender sender, @NonNull String alias, @NonNull String[] args) {
                return command.tryTabComplete(sender, alias, args);
            }
        };

        COMMAND_MAP.register(simpleCommand.getDescription(), simpleCommand);
    }

    @Override
    public void unregister(SimpleCommand<?> command) {
        Command simpleCommand = COMMAND_MAP.getCommand(command.toString());
        Optional.ofNullable(simpleCommand).ifPresent(checkedCommand -> {
            checkedCommand.unregister(COMMAND_MAP);
        });
    }
}