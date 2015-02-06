package org.sweet.revelation.revelation.core.event;

import java.util.EventListener;

public interface CommandListener extends EventListener {

    void onCommandStart(CommandStartEvent event);

    void onCommandEnd(CommandEndEvent event);
}
