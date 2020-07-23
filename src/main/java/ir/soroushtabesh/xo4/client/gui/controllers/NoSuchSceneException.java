package ir.soroushtabesh.xo4.client.gui.controllers;

public class NoSuchSceneException extends RuntimeException {
    public NoSuchSceneException() {
        super("Add scene before starting");
    }
}
