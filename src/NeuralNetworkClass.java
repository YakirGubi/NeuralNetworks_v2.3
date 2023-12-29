import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public class NeuralNetworkClass implements Serializable{

    Random random = new Random();
    private double[] inputs;
    private Weight[][][] weights;
    private Bias[][] biases;
    private ActivationFunction[][] activationFunctions;
    private final double learningRate = 0.001;
    private double lost;
    private double accuracy = 0;

    // Make a NeuralNetworkClass (constructor)
    // input: size: index 0 is the size of inputs, the last index is the size of outputs and the mid are the size of
    //              the hidden layers, and the weights connects between a layer to the next layer
    public NeuralNetworkClass(int[] size){
        int minimumSize = 2;
        if(size.length < minimumSize) throw new CustomException("SIZE LENGTH MUST BE 2 OR HIGHER");

        if(size[0] <= 0) throw new CustomException("SIZE IN INDEX 0 MUST BE BIGGER THAN 0");
        this.inputs = new double[size[0]];
        this.weights = new Weight[size.length-1][][];
        this.biases = new Bias[size.length-1][];
        this.activationFunctions = new ActivationFunction[size.length-1][];

        for(int i = 1 ; i < size.length ; i++){
            if(size[i] <= 0) throw new CustomException("SIZE IN INDEX " + i + " MUST BE BIGGER THAN 0");

            this.weights[i-1] = newWeights(size[i-1], size[i]);
            this.biases[i-1] = newBias(size[i]);
            if(i == size.length-1){
                this.activationFunctions[i-1] = newOutput(size[i]);
            }else {
                this.activationFunctions[i-1] = newActivationFunction(size[i]);
            }
        }
    }

    // Make a copy of NeuralNetworkClass (constructor)
    // input: network: copy the network info to the new class (and thy don't sher the same pinter)
    // return: the copy of the network
    public NeuralNetworkClass(NeuralNetworkClass network) {
        // Copy inputs array
        this.inputs = Arrays.copyOf(network.inputs, network.inputs.length);

        // Deep copy weights array
        this.weights = new Weight[network.weights.length][][];
        for (int i = 0; i < network.weights.length; i++) {
            this.weights[i] = new Weight[network.weights[i].length][];
            for (int j = 0; j < network.weights[i].length; j++) {
                this.weights[i][j] = Arrays.copyOf(network.weights[i][j], network.weights[i][j].length);
                for (int k = 0; k < network.weights[i][j].length; k++) {
                    this.weights[i][j][k] = new Weight(network.weights[i][j][k]);
                }
            }
        }

        this.biases = new Bias[network.biases.length][];
        for (int i = 0; i < network.biases.length; i++) {
            this.biases[i] = Arrays.copyOf(network.biases[i], network.biases[i].length);
            for (int j = 0; j < network.biases[i].length; j++) {
                this.biases[i][j] = new Bias(network.biases[i][j]);
            }
        }

        this.activationFunctions = new ActivationFunction[network.activationFunctions.length][];
        for (int i = 0; i < network.activationFunctions.length; i++) {
            this.activationFunctions[i] = Arrays.copyOf(network.activationFunctions[i], network.activationFunctions[i].length);
            for (int j = 0; j < network.activationFunctions[i].length; j++) {
                this.activationFunctions[i][j] = new ActivationFunction(network.activationFunctions[i][j]);
            }
        }
    }


    // Make 2D array of wait, all the weights are a random number from -1 to 1
    // inputs: columns: make the size of columns
    //         rows: make the size of rows
    // return: the new Wait 2D array
    private Weight[][] newWeights(int columns, int rows){
        Weight[][] waits = new Weight[columns][rows];

        for(int i = 0 ; i < columns ; i++){
            for(int j = 0 ; j < rows ; j++){
                waits[i][j] = new Weight(this.random.nextDouble(-1,1));
            }
        }

        return waits;
    }

    // Make an array of Bias, all biases is equal to 0
    // inputs: size: make the size of the array
    // return: the new Bias array
    private Bias[] newBias(int size){
        Bias[] bias = new Bias[size];

        for(int i = 0 ; i < bias.length ; i++){
            bias[i] = new Bias();
        }

        return bias;
    }

    // Make an array of ActivationFunction, all the activationFunction equal to default
    // inputs: size: make the size of the array
    // return: the new ActivationFunction array
    private ActivationFunction[] newActivationFunction(int size){
        ActivationFunction[] activationFunctions = new ActivationFunction[size];

        for(int i = 0 ; i < activationFunctions.length ; i++){
            activationFunctions[i] = new ActivationFunction();
        }

        return activationFunctions;
    }

    // Make an array of Outputs thad extend ActivationFunction, all the outputs equal to default
    // inputs: size: make the size of the array
    // return: the new Output array
    private ActivationFunction[] newOutput(int size){
        ActivationFunction[] activationFunctions = new ActivationFunction[size];

        for(int i = 0 ; i < activationFunctions.length ; i++){
            activationFunctions[i] = new Output();
        }

        return activationFunctions;
    }

    public void setInputs(double[] inputs) {
        if(this.inputs.length != inputs.length) throw new CustomException("INPUT SIZE NOT EQUAL");
        this.inputs = inputs;
    }

    // this function calculates the NeuralNetwork
    // return: the array of outputs
    public void setOutputs(double[] inputs) {
        setInputs(inputs);

        for(int i = 0; i < this.weights.length ; i++){

            for(int j = 0; j < this.weights[i].length ; j++){
                for(int k = 0; k < this.weights[i][j].length ; k++){
                    if(i == 0){
                        this.weights[i][j][k].setInput(this.inputs[j]);
                    }else {
                        this.weights[i][j][k].setInput(this.activationFunctions[i-1][j].getOutput());
                    }
                }
            }

            for(int j = 0; j < this.biases[i].length ; j++){
                this.biases[i][j].setInput(getSumOfWaits(this.weights[i], j));
            }

            for(int j = 0 ; j < this.activationFunctions[i].length ; j++){
                this.activationFunctions[i][j].setInput(this.biases[i][j].getOutput());
            }
        }
    }

    public ActivationFunction[] getOutputs(){
        return this.activationFunctions[this.activationFunctions.length-1];
    }

    private void applyLosses(){
        for(int i = 0 ; i < weights.length ; i++){
            for(int j = 0 ; j < weights[i].length ; j++){
                for(int k = 0 ; k < weights[i][j].length ; k++){
                    this.weights[i][j][k].applyLoss();
                }
            }
            for(int j = 0 ; j < biases[i].length ; j++){
                this.biases[i][j].applyLoss();
                this.activationFunctions[i][j].setLoss(0);
            }
        }
    }

    public void learning(){

        for(int i = this.weights.length-1; i >= 0 ; i--){
            for(int j = 0; j < this.weights[i].length ; j++){
                for(int k = 0; k < this.weights[i][j].length ; k++){
                    this.weights[i][j][k].addLoss(this.weights[i][j][k].d() *
                                                  this.activationFunctions[i][k].getLoss() *
                                                  this.activationFunctions[i][k].d() * this.learningRate);
                }
                if(i != 0) {
                    double sum = 0;
                    for(int k = 0; k < this.weights[i][j].length ; k++){
                        sum += this.weights[i][j][k].getWeight() * this.activationFunctions[i][k].getLoss() * this.activationFunctions[i][k].d();
                    }
                    this.activationFunctions[i - 1][j].setLoss(sum);
                }
            }
            for(int j = 0; j < this.biases[i].length ; j++){
                this.biases[i][j].addLoss(this.activationFunctions[i][j].getLoss() *
                                          this.activationFunctions[i][j].d() *
                                          this.learningRate);
            }
        }
    }

    public void softMax(){
        double sum = 0;
        for(int i = 0 ; i < this.activationFunctions[this.activationFunctions.length - 1].length ; i++){
            sum += Math.exp(this.activationFunctions[this.activationFunctions.length - 1][i].getInput());
        }

        for(int i = 0 ; i < this.activationFunctions[this.activationFunctions.length - 1].length ; i++){
            this.activationFunctions[this.activationFunctions.length - 1][i].setOutput(Math.exp(this.activationFunctions[this.activationFunctions.length - 1][i].getOutput()) / sum);
        }
    }

    public int maxOfSoftMax(){
        int bigger;
        for(int i = 0 ; i < this.activationFunctions[this.activationFunctions.length - 1].length ; i++){
            bigger = i+1;
            for(int j = i+1 ; j < this.activationFunctions[this.activationFunctions.length - 1].length ; j++){
                if(this.activationFunctions[this.activationFunctions.length - 1][i].getOutput() >= this.activationFunctions[this.activationFunctions.length - 1][j].getOutput()) bigger++;
            }
            if (bigger == this.activationFunctions[this.activationFunctions.length - 1].length) return i;
        }
        return -1;
    }

    public void setCE(double[][] inputs, double[][] observed){
        this.lost = 0;
        for(int i = 0 ; i < inputs.length ; i++) {
            this.setCELosses(inputs[i], observed[i]);
        }
        this.applyLosses();
    }

    public void setCE(double[] inputs, double[] observed){
        this.lost = 0;
        this.setCELosses(inputs, observed);
        this.applyLosses();
    }

    private void setCELosses(double[] inputs, double[] observed){
        this.setOutputs(inputs);
        this.softMax();

        for (int j = 0; j < this.activationFunctions[this.activationFunctions.length - 1].length; j++) {
            if(observed[j] == 1) {
                this.lost -= Math.log(this.activationFunctions[this.activationFunctions.length - 1][j].getOutput());
            }
            this.activationFunctions[this.activationFunctions.length - 1][j].setLoss(
                            this.activationFunctions[this.activationFunctions.length - 1][j].getLoss() +
                            this.activationFunctions[this.activationFunctions.length - 1][j].getOutput() - observed[j]);
        }

        this.learning();
    }
    private double getSumOfWaits(Weight[][] waits, int x){
        double sum = 0;

        for(int i = 0 ; i < waits.length ; i++){
            sum += waits[i][x].getOutput();
        }

        return sum;
    }

    // calculates the lost (Sum of the Squared Residuals), the lost told as how far the AI from completion
    // inputs: observed: the observed it what we want the output will be
    public void setSSR(double[][] inputs, double[][] observed) {
        this.lost = 0;
        for(int i = 0 ; i < inputs.length ; i++) {
            setSSRLoss(inputs[i], observed[i]);
        }
        this.applyLosses();
    }

    public void setSSR(double[] inputs, double[] observed) {
        this.lost = 0;
        setSSRLoss(inputs, observed);
        this.applyLosses();
    }

    private void setSSRLoss(double[] inputs, double[] observed){
        this.setOutputs(inputs);

        for (int i = 0; i < observed.length; i++) {
            this.lost += Math.pow(observed[i] - this.activationFunctions[this.activationFunctions.length - 1][i].getOutput(), 2);
            this.activationFunctions[this.activationFunctions.length - 1][i].setLoss(-2 * (observed[i] - this.activationFunctions[this.activationFunctions.length - 1][i].getOutput()));
        }

        this.learning();
    }

    public double getLost() {
        return lost;
    }

    public double getAccuracy() {
        return this.accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
}