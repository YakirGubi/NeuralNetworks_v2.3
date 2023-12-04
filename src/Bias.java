public class Bias implements NNPart{

    private double input;
    private double bias;
    private double output;
    private double d;

    public Bias() {
        this.bias = 0;
    }

    @Override
    public void setInput(double input) {
        this.input = input;
        this.output = this.input + this.bias;
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
        return 1;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }
}