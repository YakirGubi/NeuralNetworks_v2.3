public class Main {
    public static void main(String[] args) {
        int[] size = {1,2,1};
        NeuralNetworkClass neuralNetworkClass = new NeuralNetworkClass(size);

        double[][] input = {{0}, {0.5}, {1}};
        double[][] observed = {{0}, {1}, {0}};

        int i = 0;
        do{
            neuralNetworkClass.setInputs(input[0]);
            System.out.println(neuralNetworkClass.getOutputs()[0].toString());

            neuralNetworkClass.setInputs(input[1]);
            System.out.println(neuralNetworkClass.getOutputs()[0].toString());

            neuralNetworkClass.setInputs(input[2]);
            System.out.println(neuralNetworkClass.getOutputs()[0].toString());

            neuralNetworkClass.learning(input[0],observed[0]);
            neuralNetworkClass.learning(input[1],observed[1]);
            neuralNetworkClass.learning(input[2],observed[2]);

            i++;
            System.out.println(neuralNetworkClass.getSSR());
        }while (neuralNetworkClass.getSSR() > 0.1 && i < 1000);
    }
}