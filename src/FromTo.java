public class FromTo{
    private NFA start;
    private NFA end;

    public FromTo(NFA start, NFA end) {
        this.start = start;
        this.end = end;
    }

    public NFA getStart() {
        return start;
    }

    public void setStart(NFA start) {
        this.start = start;
    }

    public NFA getEnd() {
        return end;
    }

    public void setEnd(NFA end) {
        this.end = end;
    }
}

