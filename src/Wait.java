public class Wait implements NNPart{

    private double input;
    private double wait;
    private double output;
    private double d;

    public Wait(double wait) {
        this.wait = wait;
    }

    @Override
    public void setInput(double input) {
        this.input = input;
        this.output = this.input * this.wait;
    }

    @Override
    public double getInput() {
        return this.input;
    }

    @Override
    public double getOutput() {
        return this.output;
    }

    @Override
    public double d() {
        return this.input;
    }

    public double getWait() {
        return wait;
    }

    public void setWait(double wait) {
        this.wait = wait;
    }
}