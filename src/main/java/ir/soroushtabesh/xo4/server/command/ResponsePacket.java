package ir.soroushtabesh.xo4.server.command;

import ir.soroushtabesh.xo4.server.IServer;
import ir.soroushtabesh.xo4.server.utils.JSONUtil;

public class ResponsePacket {
    private final int cid;
    private final String clz;
    private final IServer.Message message;
    private final String data;

    public ResponsePacket(int cid, IServer.Message message, Object object) {
        if (object == null)
            object = new Object();
        this.cid = cid;
        this.clz = object.getClass().getName();
        this.message = message;
        this.data = JSONUtil.getGson().toJson(object);
    }

    public int getCid() {
        return cid;
    }

    public String getClz() {
        return clz;
    }

    public IServer.Message getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }
}
