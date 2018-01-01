package wekatest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import weka.classifiers.evaluation.NumericPrediction;
import weka.classifiers.functions.SMOreg;
import weka.classifiers.timeseries.WekaForecaster;
import weka.core.Instances;

public class predictor {

    //this is the return object
    private int[] output = new int[29];
    
    public void run(int id) throws FileNotFoundException, IOException, Exception {
        // TODO code application logic here
        
        //FILE PATH
        String file = ".\\wekaFiles\\product"+id+".arff";
        
        //load the file 
        Instances data = new Instances(new BufferedReader(new FileReader(file)));
        
        WekaForecaster forecaster = new WekaForecaster();
        
        try {
            //set field to forecast
            forecaster.setFieldsToForecast("q");
        } catch (Exception ex) {
            System.out.println("Forecasting field not set correctly");
        }
        
        
        SMOreg model = new SMOreg();
        
        String[] opts = model.getOptions();
        opts[1] = "10.0";
        opts[7] = "weka.classifiers.functions.supportVector.RBFKernel -G 0.01 -C 250007";
        
        try {
            //model.setOptions(weka.core.Utils.splitOptions(opts));
            model.setOptions(opts);
        } catch (Exception ex) 
        {
            System.out.println("Problem setting options ....");
        }
        
        //set all the parameters
        forecaster.setBaseForecaster(model);
        forecaster.getTSLagMaker().setMinLag(7);
        forecaster.getTSLagMaker().setMaxLag(7);
        forecaster.getTSLagMaker().setAdjustForVariance(true);
        forecaster.getTSLagMaker().setRemoveLeadingInstancesWithUnknownLagValues(true);
        forecaster.getTSLagMaker().setIncludePowersOfTime(false);
        forecaster.getTSLagMaker().setIncludeTimeLagProducts(false);
        
        try {
            //build the model
            forecaster.buildForecaster(data, System.out);
        } catch (Exception ex) {
            System.out.println("Problem building the model...");
        }
        
        //setting prime forecaster
        try {
            forecaster.primeForecaster(data);
        } catch (Exception ex) {
            System.out.println("Problem setting prime forecaster");
        }
        
        //set the prediction for next 29 days
        List<List<NumericPrediction>> forecast = forecaster.forecast(29, System.out);
        
        for(int i = 0; i < 29;i++)
        {
            List<NumericPrediction> predsAtStep = forecast.get(i);
            for(int j = 0; j < 1; j++)
            {
                NumericPrediction predForTarget = predsAtStep.get(j);
                output[i] = (int) Math.round(predForTarget.predicted());
            }
        }
    }
    public int[] getOuput()
    {
        return output;
    }
}
