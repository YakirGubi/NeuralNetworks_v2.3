public class Weight implements NNPart{

    private double input;
    private double weight;
    private double originalWait;
    private double output;
    private double loss = 0;

    public Weight(double wait) {
        this.weight = wait;
    }
    public Weight(Weight weight) {
        this.input = weight.input;
        this.weight = weight.weight;
        this.output = weight.output;
    }

    @Override
    public void setInput(double input) {
        this.input = input;
        this.output = this.input * this.weight;
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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getOriginalWait() {
        return originalWait;
    }

    public void setOriginalWait() {
        this.originalWait = this.weight;
    }
    public void addLoss(double loss){
        this.loss += loss;
    }
    public void applyLoss(){
        this.weight -= this.loss;
        this.loss = 0;
    }
}