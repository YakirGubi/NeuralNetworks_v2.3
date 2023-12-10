public class Main {
    public static void main(String[] args) {
        // making the NeuralNetwork in the size of 'size' index 0 it is the size of inputs and the last index is the
        // size of outputs and the mid are the size of the hidden layers
        int[] size = {1,5,5,5};
        NeuralNetworkClass neuralNetworkClass = new NeuralNetworkClass(size);

        //make a numbers for the training
        double[][] input = {{0}, {0.5}, {1}};
        double[][] observed = {{0}, {1}, {0}};

        // train the AI until is SSR is low or the AI train num of times
        int i = 0;
        do{
            for(int j = 0 ; j < input.length ; j++){
                System.out.println("input " + j);
                for(int k = 0 ; k < neuralNetworkClass.getOutputs().length ; k++){
                    neuralNetworkClass.setInputs(input[j]);
                    System.out.println(neuralNetworkClass.getOutputs()[k].toString());
                }
                System.out.println("**********");
            }

//            neuralNetworkClass.learning(input[0],observed[0]);
//            neuralNetworkClass.learning(input[1],observed[1]);
//            neuralNetworkClass.learning(input[2],observed[2]);

            i++;
            System.out.println(neuralNetworkClass.getSSR());
        }while (neuralNetworkClass.getSSR() > 0.001 && i < 1);
    }
}