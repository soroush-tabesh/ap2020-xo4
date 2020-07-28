package ir.soroushtabesh.xo4.server.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CommandPacket {
    private final String clazz;
    private final String data;
    private static final Gson gson = new GsonBuilder().create();

    public CommandPacket(String clazz, String data) {
        this.clazz = clazz;
        this.data = data;
    }

    public CommandPacket(Class<? extends Command<?>> clazz, String data) {
        this.clazz = clazz.getName();
        this.data = data;
    }

    public CommandPacket(Command<?> command) {
        clazz = command.getClass().getName();
        data = gson.toJson(command);
    }

    public Command<?> build() {
        Command<?> command = null;
        try {
            Class<? extends Command<?>> loadClass = (Class<? extends Command<?>>) getClass().getClassLoader().loadClass(clazz);
            command = gson.fromJson(data, loadClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return command;
    }

    public static Command<?> fromJson(String data) {
        try {
            return gson.fromJson(data, CommandPacket.class).build();
        } catch (Exception e) {
            return null;
        }
    }

    public static String toJson(Command<?> command) {
        return gson.toJson(new CommandPacket(command));
    }
}
