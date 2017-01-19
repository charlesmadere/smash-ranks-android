package com.garpr.android.lifecycle;

public enum ActivityState implements Heartbeat {

    CREATED {
        @Override
        public boolean isAlive() {
            return true;
        }
    },

    DESTROYED {
        @Override
        public boolean isAlive() {
            return false;
        }
    },

    PAUSED {
        @Override
        public boolean isAlive() {
            return true;
        }
    },

    RESUMED {
        @Override
        public boolean isAlive() {
            return true;
        }
    },

    STARTED {
        @Override
        public boolean isAlive() {
            return true;
        }
    },

    STOPPED {
        @Override
        public boolean isAlive() {
            return true;
        }
    }

}
