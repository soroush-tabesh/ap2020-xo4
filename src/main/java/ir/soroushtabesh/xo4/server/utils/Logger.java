package ir.soroushtabesh.xo4.server.utils;

import ir.soroushtabesh.xo4.server.models.Log;

import java.util.Date;

public class Logger {
    private Logger() {
    }

    public static void log(String event, String desc) {
        log(event, desc, Log.Severity.INFO);
    }

    public static void log(String event, String desc, Log.Severity severity) {
        Log log = new Log();
//        if (PlayerManager.getInstance().getPlayer() != null) {
//            Player player = PlayerManager.getInstance().getPlayer();
//            log.setUser_id(player.getId());
//            log.setUsername(player.getUsername());
//        } else {
        log.setUser_id(-1);
//        }
        log.setDate(new Date());
        log.setEvent(event);
        log.setDescription(desc);
        log.setSeverity(severity);
        DBUtil.doInJPATemp(session -> {
            session.saveOrUpdate(log);
            return null;
        });
    }
}
