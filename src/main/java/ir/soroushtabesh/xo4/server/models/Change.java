package ir.soroushtabesh.xo4.server.models;

public class Change {
    private Integer ci, cj, cval;
    private Boolean active;
    private Integer turn;
    private Integer winner;
    private Boolean forfeited;

    public Integer getCi() {
        return ci;
    }

    public Change setCi(Integer ci) {
        this.ci = ci;
        return this;
    }

    public Integer getCj() {
        return cj;
    }

    public Change setCj(Integer cj) {
        this.cj = cj;
        return this;
    }

    public Integer getCval() {
        return cval;
    }

    public Change setCval(Integer cval) {
        this.cval = cval;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public Change setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Integer getTurn() {
        return turn;
    }

    public Change setTurn(Integer turn) {
        this.turn = turn;
        return this;
    }

    public Integer getWinner() {
        return winner;
    }

    public Change setWinner(Integer winner) {
        this.winner = winner;
        return this;
    }

    public Boolean getForfeited() {
        return forfeited;
    }

    public Change setForfeited(Boolean forfeited) {
        this.forfeited = forfeited;
        return this;
    }
}
