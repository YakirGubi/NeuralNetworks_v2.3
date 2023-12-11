import java.util.Arrays;
import java.util.Random;

public class NeuralNetworkClass {

    Random random = new Random();
    private double[] inputs;
    private Wait[][][] waits;
    private Bias[][] bias;
    private ActivationFunction[][] activationFunctions;
    private final double learningRate = 0.1;
    private double SSR; // (Sum of the Squared Residuals), the SSR told as how far the AI from completion

    // Make a NeuralNetworkClass (constructor)
    // input: size: index 0 is the size of inputs, the last index is the size of outputs and the mid are the size of
    //              the hidden layers, and the waits connects between a layer to the next layer
    public NeuralNetworkClass(int[] size){
        if(size.length > 1){
            this.inputs = new double[size[0]];

            this.waits = new Wait[size.length-1][][];
            this.bias = new Bias[size.length-1][];
            this.activationFunctions = new ActivationFunction[size.length-1][];

            for(int i = 1 ; i < size.length ; i++){
                this.waits[i-1] = newWaits(size[i-1], size[i]);
                this.bias[i-1] = newBias(size[i]);
                if(i == size.length-1){
                    this.activationFunctions[i-1] = newOutput(size[i]);
                }else {
                    this.activationFunctions[i-1] = newActivationFunction(size[i]);
                }
            }
        }else {
            System.out.println("SIZE LENGTH MUST BE 2 OR HIGHER");
        }
    }

    // Make a copy of NeuralNetworkClass (constructor)
    // input: network: copy the network info to the new class (and thy don't sher the same pinter)
    // return: the copy of the network
    public NeuralNetworkClass(NeuralNetworkClass network) {
        // Copy inputs array
        this.inputs = Arrays.copyOf(network.inputs, network.inputs.length);

        // Deep copy waits array
        this.waits = new Wait[network.waits.length][][];
        for (int i = 0; i < network.waits.length; i++) {
            this.waits[i] = new Wait[network.waits[i].length][];
            for (int j = 0; j < network.waits[i].length; j++) {
                this.waits[i][j] = Arrays.copyOf(network.waits[i][j], network.waits[i][j].length);
                for (int k = 0; k < network.waits[i][j].length; k++) {
                    this.waits[i][j][k] = new Wait(network.waits[i][j][k]);
                }
            }
        }

        this.bias = new Bias[network.bias.length][];
        for (int i = 0; i < network.bias.length; i++) {
            this.bias[i] = Arrays.copyOf(network.bias[i], network.bias[i].length);
            for (int j = 0; j < network.bias[i].length; j++) {
                this.bias[i][j] = new Bias(network.bias[i][j]);
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


    // Make 2D array of wait, all the waits are a random number from -1 to 1
    // inputs: columns: make the size of columns
    //         rows: make the size of rows
    // return: the new Wait 2D array
    private Wait[][] newWaits(int columns, int rows){
        Wait[][] waits = new Wait[columns][rows];

        for(int i = 0 ; i < columns ; i++){
            for(int j = 0 ; j < rows ; j++){
                waits[i][j] = new Wait(this.random.nextDouble(-1,1));
            }
        }

        return waits;
    }

    // Make an array of Bias, all bias is equal to 0
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
        if(this.inputs.length == inputs.length) {
            this.inputs = inputs;
        }else {
            System.out.println("INPUT SIZE NOT EQUAL");
        }
    }

    // this function calculates the NeuralNetwork
    // return: the array of outputs
    public ActivationFunction[] getOutputs() {

        for(int i = 0 ; i < this.waits.length ; i++){

            if(i == 0){
                for(int j = 0 ; j < this.waits[i].length ; j++){
                    for(int k = 0 ; k < this.waits[i][j].length ; k++){
                        this.waits[i][j][k].setInput(this.inputs[j]);
                    }
                }
            }else {
                for(int j = 0 ; j < this.waits[i].length ; j++){
                    for(int k = 0 ; k < this.waits[i][j].length ; k++){
                        this.waits[i][j][k].setInput(this.activationFunctions[i-1][j].getOutput());
                    }
                }
            }

            for(int j = 0 ; j < this.bias[i].length ; j++){
                this.bias[i][j].setInput(getSumOfWaits(this.waits[i], j));
            }

            for(int j = 0 ; j < this.activationFunctions[i].length ; j++){
                this.activationFunctions[i][j].setInput(this.bias[i][j].getOutput());
            }
        }

        return this.activationFunctions[this.activationFunctions.length-1];
    }

    public void learning(double[] inputs, double[] observed){
        this.setInputs(inputs);
        this.getOutputs();
        this.setSSR(observed);

        NeuralNetworkClass original = new NeuralNetworkClass(this);

        for(int i = 0 ; i < this.activationFunctions[this.activationFunctions.length-1].length ; i++){
            this.activationFunctions[this.activationFunctions.length-1][i].setLoss(-2 * (observed[i] - this.activationFunctions[this.activationFunctions.length-1][i].getOutput()));
        }

        for(int i = this.waits.length-1 ; i >= 0 ; i--){
            for(int j = 0 ; j < this.waits[i].length ; j++){
                for(int k = 0 ; k < this.waits[i][j].length ; k++){
//                    System.out.println("\\\\\\\\\\\\");
//                    System.out.println(original.waits.length);
//                    System.out.println(original.waits[i].length);
//                    System.out.println(original.waits[i][j].length);
//                    System.out.println(i);
//                    System.out.println(j);
//                    System.out.println(k);
                    this.waits[i][j][k].setWait(original.waits[i][j][k].getWait() -
                                                original.waits[i][j][k].d() *
                                                this.activationFunctions[i][j].d() *
                                                this.activationFunctions[i][j].getLoss() * this.learningRate);
                }
                this.bias[i][j].setBias(original.bias[i][j].getBias() - this.activationFunctions[i][j].getLoss() *
                                                                        this.activationFunctions[i][j].d() *
                                                                        this.learningRate);
                int sum = 0;
                for(int k = 0 ; k < this.waits[i][j].length ; k++){
                    sum += original.waits[i][j][k].getWait() * this.activationFunctions[i][k].getLoss();
                }
                if(i != 0) {
                    this.activationFunctions[i - 1][j].setLoss(sum);
                }
            }
        }
    }


    private double getSumOfWaits(Wait[][] waits, int x){
        double sum = 0;

        for(int i = 0 ; i < waits.length ; i++){
            sum += waits[i][x].getOutput();
        }

        return sum;
    }

    // calculates the SSR (Sum of the Squared Residuals), the SSR told as how far the AI from completion
    // inputs: observed: the observed it what we want the output will be
    public void setSSR(double[] observed) {
        double sum = 0;

        for(int i = 0 ; i < observed.length ; i++){
            sum += Math.pow(observed[i] - this.activationFunctions[this.activationFunctions.length-1][i].getOutput(),2);
        }

        this.SSR = sum;
    }

    public double getSSR() {
        return SSR;
    }
}
