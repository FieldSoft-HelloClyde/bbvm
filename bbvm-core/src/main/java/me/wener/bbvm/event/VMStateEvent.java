package me.wener.bbvm.event;

public class VMStateEvent extends BBVMEvent
{
    VMState state;

    public enum VMState
    {
        START, PAUSE, INIT, RESET, RESUME, STOP
    }
}
