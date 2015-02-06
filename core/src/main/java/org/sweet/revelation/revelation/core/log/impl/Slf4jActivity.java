package org.sweet.revelation.revelation.core.log.impl;

import org.sweet.revelation.revelation.core.log.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jActivity extends AbstractActivity {

    private static final Logger LOGGER = LoggerFactory.getLogger(Slf4jActivity.class);

    Slf4jActivity() {
        this(null);
    }

    public Slf4jActivity(String name) {
        this(name, null);
    }

    protected Slf4jActivity(String name, Activity parent) {
        super(AnsiFactory.disabled(), name, parent);
    }

    public void info(String message) {
        if (LOGGER.isInfoEnabled()) {
            if (name == null) {
                LOGGER.info(message);
            } else {
                LOGGER.info("<{}> {}", name, message);
            }
        }
    }

    public void error(String message) {
        if (LOGGER.isErrorEnabled()) {
            if (name == null) {
                LOGGER.error(message);
            } else {
                LOGGER.error("<{}> {}", name, message);
            }
        }
    }

    public void error(String message, Exception e) {
        if (LOGGER.isErrorEnabled()) {
            if (name == null) {
                LOGGER.error(message, e);
            } else {
                LOGGER.error(String.format("<%s> %s", name, message), e);
            }
        }
    }

    public Activity createSubActivity(String name) {
        return new Slf4jActivity(subActivityName(name), this);
    }
}
