public class ActivationFunction implements NNPart{

    protected double input;
    protected double output;
    protected double d;
    protected double loss;

    public ActivationFunction() {
    }

    @Override
    public void setInput(double input) {
        this.input = input;
    }

    @Override
    public double getInput() {
        return this.input;
    }

    @Override
    public double getOutput() {
        return Math.log(1 + Math.pow(Math.E, this.input));
    }

    @Override
    public double d() {
        return (Math.pow(Math.E, this.input) / (1 + Math.pow(Math.E, this.input)));
    }

    public double getLoss() {
        return loss;
    }

    public void setLoss(double loss) {
        this.loss = loss;
    }
}