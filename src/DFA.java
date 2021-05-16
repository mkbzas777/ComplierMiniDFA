public class DFA {
    private int st;
    private char w;
    private int ed;

    public DFA(int st, char w, int ed) {
        this.st = st;
        this.w = w;
        this.ed = ed;
    }

    public int getSt() {
        return st;
    }

    public void setSt(int st) {
        this.st = st;
    }

    public char getW() {
        return w;
    }

    public void setW(char w) {
        this.w = w;
    }

    public int getEd() {
        return ed;
    }

    public void setEd(int ed) {
        this.ed = ed;
    }

    @Override
    public String toString() {
        return   st + "-" + w + "->" + ed ;
    }
}
