package it.emarolab.runnableSITutility.sceneRecustructor.simulation;

import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.runnableSITutility.sceneRecustructor.MonteCarloInterface;
import it.emarolab.fuzzySIT.core.SITTBox;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CarloInterfaceTest {

    public static final int NUMBER_PARTICLES = 15;

    public static void main(String[] args) {

        String  scene = "FPGK";
        String  path = FuzzySITBase.RESOURCES_PATH + "ontologies/learnedComposedTable.fuzzydl";

        for (int i = 7; i < 10; i++){

            if( i > 5){
                scene = "TableSetUp";
                path =  FuzzySITBase.RESOURCES_PATH + "ontology/learnedSimpleTable.fuzzydl";
            }

            try {
                SITTBox h = new SITTBox(path);
                MonteCarloInterface carlo = new CarloImpl(h, scene, NUMBER_PARTICLES);
                carlo.start();

                try {
                    Thread.sleep( 15*60*1000); // 15 min =
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                carlo.kill();
                try {
                    Thread.sleep(140000); // 1.5 min
                    Runtime.getRuntime().freeMemory();
                    carlo.interrupt(); // todo to review !!!!!!!!!
                    h = null;
                    carlo = null;
                    Thread.sleep(140000); // 1.5 min
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (Exception e){
                e.printStackTrace();
                i--;
            }
        }


        //CarloInterface c1 = new CsvCarloListener( csvFilePath)

        /*
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        c1.kill(); // todo killall
        */
    }
}

class CarloImpl
        extends MonteCarloInterface {

    private ListenerImpl listener;

    public CarloImpl(SITTBox tbox, String toGenerateName, int particlesNumber) {
        super(tbox, toGenerateName, particlesNumber);
        listener.appendHeader();
    }

    @Override
    protected CarloListener getListener() { // called in super constructor
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String date = sdf.format(cal.getTime());
        String csvFilePath = FuzzySITBase.RESOURCES_PATH + "sceneReconstructionLog/" + date + ".log";
        listener = new ListenerImpl( csvFilePath);
        return listener;
    }


    class ListenerImpl
            extends MonteCarloInterface.CsvCarloListener{

        public ListenerImpl(String filePath) {
            super(filePath);
        }
    }
}