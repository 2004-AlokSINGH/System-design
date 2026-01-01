package behavioral.template;

public abstract class ModelTrainer {

    public void executionTemplate(){
        System.out.println("calling method in sequence");
        loadData();
        preprocessData();
        trainData();
        evaluateModel();
        saveModel("default folder temp");
        System.out.println("all process completed in sequnce as per template");
    }

    
    public abstract String loadData();
    public abstract String preprocessData();
    public abstract String trainData();
    public abstract String evaluateModel();


    public String saveModel(String pathtoSave){
        System.out.println("saved the file successfull");
        return pathtoSave+"";
    }

    
    

}
