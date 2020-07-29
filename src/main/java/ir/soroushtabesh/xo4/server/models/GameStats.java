package ir.soroushtabesh.xo4.server.models;

import ir.soroushtabesh.xo4.server.utils.JSONUtil;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class GameStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Integer gid;

    @Column(columnDefinition = "text")
    private String gameInstanceData;

    @Column
    @Generated(GenerationTime.ALWAYS)
    private LocalDateTime time;

    public GameStats() {
    }

    public GameStats(GameInstance gameInstance) {
        gameInstanceData = JSONUtil.getGson().toJson(gameInstance);
        gid = gameInstance.getGid();
    }

    public GameInstance getGameInstance() {
        return JSONUtil.getGson().fromJson(gameInstanceData, GameInstance.class);
    }

    public int getGid() {
        return gid;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
