package entity;

/**
 * 用于构建NFA的数据结构，方便用逆波兰表达式转化为NFA
 */
public class NFA {
    private int id;
    private NFA next1;
    private String state1;
    private NFA next2;
    private String state2;

    public NFA(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public NFA getNext1() {
        return next1;
    }

    public void setNext1(NFA next1) {
        this.next1 = next1;
    }

    public String getState1() {
        return state1;
    }

    public void setState1(String state1) {
        this.state1 = state1;
    }

    public NFA getNext2() {
        return next2;
    }

    public void setNext2(NFA next2) {
        this.next2 = next2;
    }

    public String getState2() {
        return state2;
    }

    public void setState2(String state2) {
        this.state2 = state2;
    }

    @Override
    public String toString() {
        return "FA{" +
                "id=" + id +
                ", next1=" + next1.getId() +
                ", state1='" + state1 + '\'' +
                ", next2=" + next2.getId() +
                ", state2='" + state2 + '\'' +
                '}';
    }
}
