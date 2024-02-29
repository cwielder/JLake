package dev.rlni.jlake.event;

public abstract class KeyEvent implements IEvent {
    private final int mKey;
    private final int mScancode;
    private final int mAction;
    private final int mMods;

    protected KeyEvent(int key, int scancode, int action, int mods) {
        mKey = key;
        mScancode = scancode;
        mAction = action;
        mMods = mods;
    }

    public int getKey() {
        return mKey;
    }

    public int getScancode() {
        return mScancode;
    }

    public int getAction() {
        return mAction;
    }

    public int getMods() {
        return mMods;
    }
}
