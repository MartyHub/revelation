package org.sweet.revelation.revelation.job.admin;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.sweet.revelation.revelation.core.Description;
import org.sweet.revelation.revelation.core.ProcessorComponent;
import org.sweet.revelation.revelation.core.command.EmptyCommand;

@ProcessorComponent(EmptyCommand.class)
@Description("Interactive shell")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Shell extends BaseShell<EmptyCommand> {
}
