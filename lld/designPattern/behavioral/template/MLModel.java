package behavioral.template;

public class MLModel extends ModelTrainer {

    @Override
    public String loadData() {
        System.out.println("load ml model");
        return "data.csv";
    }

    @Override
    public String preprocessData() {
        System.out.println("preprocess data");
        return "preprocessedData.csv";
    }

    @Override
    public String trainData() {
        System.out.println("train data");
        return "trained.csv";
    }

    @Override
    public String evaluateModel() {
        System.out.println("finding ml accuracy 99%");
        return "99% accuracy";
    }
	
}
