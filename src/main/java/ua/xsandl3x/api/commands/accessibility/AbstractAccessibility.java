package ua.xsandl3x.api.commands.accessibility;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import ua.xsandl3x.api.commands.IExecutable;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class AbstractAccessibility<S extends CommandSender> implements IAccessibility<S> {

    // Getting the commands executable class for retrieving its characteristics.
    @Getter
    private final IExecutable<S> executable;
    private Consumer<S> sourceConsumer = __ -> { };

    @Override
    public void failAction(Consumer<S> sourceConsumer) {
        this.sourceConsumer = sourceConsumer;
    }

    @Override
    public void apply(S source) {
        sourceConsumer.accept(source);
    }
}
