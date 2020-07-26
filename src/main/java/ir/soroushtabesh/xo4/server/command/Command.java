package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;

import java.io.DataOutputStream;

public interface Command {
    void visit(IServer server, DataOutputStream outputStream);
}
