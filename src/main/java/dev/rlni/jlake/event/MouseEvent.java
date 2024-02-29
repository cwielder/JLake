package dev.rlni.jlake.event;

public abstract class MouseEvent implements IEvent {
    private final int mButton;
    private final int mAction;
    private final int mMods;

    protected MouseEvent(int button, int action, int mods) {
        mButton = button;
        mAction = action;
        mMods = mods;
    }

    public int getButton() {
        return mButton;
    }

    public int getAction() {
        return mAction;
    }

    public int getMods() {
        return mMods;
    }
}
