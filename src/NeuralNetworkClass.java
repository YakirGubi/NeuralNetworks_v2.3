import java.util.Random;

public class NeuralNetworkClass {

    Random random = new Random();
    private double[] inputs;
    private Wait[][][] waits;
    private Bias[][] bias;
    private ActivationFunction[][] activationFunctions;
    private double learningRate = 0.1;
    private double SSR;

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
        }
    }

    private Wait[][] newWaits(int columns, int rows){
        Wait[][] waits = new Wait[columns][rows];

        for(int i = 0 ; i < columns ; i++){
            for(int j = 0 ; j < rows ; j++){
                waits[i][j] = new Wait(this.random.nextDouble(-1,1));
            }
        }

        return waits;
    }

    private Bias[] newBias(int size){
        Bias[] bias = new Bias[size];

        for(int i = 0 ; i < bias.length ; i++){
            bias[i] = new Bias();
        }

        return bias;
    }

    private ActivationFunction[] newActivationFunction(int size){
        ActivationFunction[] activationFunctions = new ActivationFunction[size];

        for(int i = 0 ; i < activationFunctions.length ; i++){
            activationFunctions[i] = new ActivationFunction();
        }

        return activationFunctions;
    }
    private ActivationFunction[] newOutput(int size){
        ActivationFunction[] activationFunctions = new ActivationFunction[size];

        for(int i = 0 ; i < activationFunctions.length ; i++){
            activationFunctions[i] = new Output();
        }

        return activationFunctions;
    }

    public void setInputs(double[] inputs) {
        this.inputs = inputs;
    }

    public ActivationFunction[] getOutputs() {

        for(int i = 0 ; i < this.inputs.length ; i++){
            for(int j = 0 ; j < this.waits[0][0].length ; j++){
                this.waits[0][i][j].setInput(this.inputs[i]);
            }
        }

        for(int i = 0 ; i < this.bias[0].length ; i++){
            this.bias[0][i].setInput(getSumOfWaits(this.waits[0], i));
        }

        for(int i = 1 ; i < this.waits.length ; i++){
            for(int j = 0 ; j < this.activationFunctions[i-1].length ; j++){
                this.activationFunctions[i-1][j].setInput(this.bias[i-1][j].getOutput());
            }
            for(int j = 0 ; j < this.waits[i].length ; j++){
                for(int k = 0 ; k < this.waits[i][j].length ; k++){
                    this.waits[i][j][k].setInput(this.activationFunctions[i-1][j].getOutput());
                }
            }
            for(int j = 0 ; j < this.bias[i].length ; j++){
                this.bias[i][j].setInput(getSumOfWaits(this.waits[i], j));
            }
        }

        for(int i = 0 ; i < this.bias[this.bias.length-1].length ; i++){
            this.activationFunctions[this.activationFunctions.length-1][i].setInput(this.bias[this.bias.length-1][i].getOutput());
        }

        return this.activationFunctions[this.activationFunctions.length-1];
    }

    public void learning(double[] inputs, double[] observed){
        NeuralNetworkClass original = this;
        this.setInputs(inputs);
        this.getOutputs();
        this.setSSR(observed);

        for(int i = 0 ; i < this.activationFunctions[this.activationFunctions.length-1].length ; i++){
            this.activationFunctions[this.activationFunctions.length-1][i].setLoss(-2 * (observed[i] - this.activationFunctions[this.activationFunctions.length-1][i].getOutput()));
        }

        for(int i = 0 ; i < this.waits.length ; i++){
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
                                                original.waits[0][0][0].d() *
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
                this.activationFunctions[i][j].setLoss(sum);
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
