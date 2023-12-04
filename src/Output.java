public class Output extends ActivationFunction{

    public Output(){

    }

    @Override
    public void setInput(double input) {
        super.setInput(input);
        this.output = input;
    }

    @Override
    public double getOutput(){
        return this.output;
    }
    @Override
    public double d(){
        return 1;
    }

    @Override
    public String toString() {
        return "Output{" +
                "output=" + output +
                '}';
    }
}
