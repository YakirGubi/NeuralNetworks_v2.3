public class Main {
    public static void main(String[] args) {
        // making the NeuralNetwork in the size of 'size' index 0 it is the size of inputs and the last index is the
        // size of outputs and the mid are the size of the hidden layers
        int[] size = {1,5,2};
        NeuralNetworkClass neuralNetworkClass = new NeuralNetworkClass(size);

        //make a numbers for the training
        double[][] input = {{0}, {0.5}, {1}};
        double[][] observed = {{0,1}, {1,0}, {0,1}};

        // train the AI until is SSR is low or the AI train num of times
//        double SSR = 10;
//        for(int i = 0 ; i < 100000 && SSR > 0.00001 ; i++){
//            SSR = 0;
//            for(int k = 0 ; k < neuralNetworkClass.getOutputs().length ; k++){
//                for(int j = 0 ; j < input.length ; j++){
//                    neuralNetworkClass.setInputs(input[j]);
//                    System.out.println(neuralNetworkClass.getOutputs()[k].toString());
//                }
//                neuralNetworkClass.setSSR(input,observed);
//                System.out.println(neuralNetworkClass.getOutputs()[k].toString());
//                SSR += neuralNetworkClass.getAccuracy();
//            }
//            System.out.println("**********");
//            System.out.println(SSR);
//            System.out.println(i);
//        }

        double SSR = 10;
        for(int i = 0 ; i < 100000 && SSR > 0.00001 ; i++){
            SSR = 0;
                for(int k = 0 ; k < neuralNetworkClass.getOutputs().length ; k++){
                    neuralNetworkClass.setCE(input,observed);
                    System.out.println(k + ": " + neuralNetworkClass.getOutputs()[k].toString());
                    SSR += neuralNetworkClass.getAccuracy();
                System.out.println("**********");
            }
            System.out.println(SSR);
            System.out.println(i);
        }
    }
}