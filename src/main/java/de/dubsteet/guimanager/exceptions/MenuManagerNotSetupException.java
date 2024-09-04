package de.dubsteet.guimanager.exceptions;

public class MenuManagerNotSetupException extends Exception{
    public MenuManagerNotSetupException() {
        super("MenuManager has not been setup yet");
    }
}
