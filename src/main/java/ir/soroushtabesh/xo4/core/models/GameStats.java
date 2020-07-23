package ir.soroushtabesh.xo4.core.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GameStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
