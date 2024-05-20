public class Qubit {
    // Qubit states: 0 = 0, 1 = 1, 2 = i, 3 = -i
    int state = 0;
    boolean entangled = true;
    public Qubit () {

    }
    public int qubitState() {
        return state;
    }
    public boolean entangled{
        return entangled;
    }
    public void changeQubitState (int st){
        state = st;
    }
    

}
