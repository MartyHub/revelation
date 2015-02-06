package org.sweet.revelation.revelation.core.log.impl;

import org.sweet.revelation.revelation.core.log.Activity;
import org.sweet.revelation.revelation.core.log.PredictableWorkLoad;
import org.sweet.revelation.revelation.core.log.UnknownWorkLoad;

public class CompositeActivity extends AbstractActivity {

    public static Activity main() {
        return new CompositeActivity(null, null, new Activity[]{new ConsoleActivity(), new Slf4jActivity()});
    }

    private final Activity[] activities;

    protected CompositeActivity(String name, Activity parent, Activity[] activities) {
        super(null, name, parent);

        if (activities == null) {
            throw new IllegalArgumentException("Activities is mandatory");
        }

        this.activities = activities;
    }

    public void info(String message) {
        for (Activity activity : activities) {
            activity.info(message);
        }
    }

    public void error(String message) {
        for (Activity activity : activities) {
            activity.error(message);
        }
    }

    public void error(String message, Exception e) {
        for (Activity activity : activities) {
            activity.error(message, e);
        }
    }

    public Activity createSubActivity(String name) {
        final int length = activities.length;
        Activity[] subActivities = new Activity[length];

        for (int i = 0; i < length; ++i) {
            subActivities[i] = activities[i].createSubActivity(name);
        }

        return new CompositeActivity(name, this, subActivities);
    }

    @Override
    protected boolean log() {
        return false;
    }

    @Override
    protected void doCancel() {
        for (Activity activity : activities) {
            activity.cancel();
        }
    }

    @Override
    protected UnknownWorkLoad doStart() {
        final int length = activities.length;
        UnknownWorkLoad[] workLoads = new UnknownWorkLoad[length];

        for (int i = 0; i < length; ++i) {
            workLoads[i] = activities[i].start();
        }

        return new CompositeUnknownWorkLoad(this, workLoads);
    }

    @Override
    protected PredictableWorkLoad doStart(final int totalAmount, final boolean cancelOnFirstError) {
        final int length = activities.length;
        PredictableWorkLoad[] workLoads = new PredictableWorkLoad[length];

        for (int i = 0; i < length; ++i) {
            workLoads[i] = activities[i].start(totalAmount, cancelOnFirstError);
        }

        return new CompositePredictableWorkLoad(this, workLoads);
    }
}
