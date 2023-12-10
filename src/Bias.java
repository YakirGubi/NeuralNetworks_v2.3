public class Bias implements NNPart{

    private double input;
    private double output;
    private double bias;

    public Bias() {
        this.bias = 0;
    }
    public Bias(Bias bias) {
        this.input = bias.input;
        this.output = bias.output;
        this.bias = bias.bias;
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