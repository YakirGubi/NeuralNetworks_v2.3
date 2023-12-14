public class Wait implements NNPart{

    private double input;
    private double wait;
    private double originalWait;
    private double output;

    public Wait(double wait) {
        this.wait = wait;
    }
    public Wait(Wait wait) {
        this.input = wait.input;
        this.wait = wait.wait;
        this.output = wait.output;
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

    public double getOriginalWait() {
        return originalWait;
    }

    public void setOriginalWait() {
        this.originalWait = this.wait;
    }
}