package sample;

import com.jfoenix.controls.JFXHamburger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javafx.scene.text.Text;
import weatherapp.Weather;
import weatherapp.WeatherEnum;
import weatherapp.WeatherStructure;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
    private Parent root;

    private static Main instance;

    public Main() {
        instance = this;
    }
    // static method to get instance of view
    public static Main getInstance() {
        return instance;
    }

    public void fillData(WeatherStructure structure, String data){
        Text node = (Text)root.lookup("#"+structure.getXml_id());
        if(node != null) {
            node.setText(data + " " + structure.getSuffix());
        }
    }



    public void changeLocation(String location) throws Exception{
        Text node = (Text)root.lookup("#location-text");
        if(node != null) {
            node.setText(location);
        }

        double lattitude;
        double longtitude;

        switch(location){
            case "Oxford":
                lattitude = 51.7522;
                longtitude = -1.256;
                break;
            case "London":
                lattitude = 51.5085;
                longtitude =  -0.1258;
                break;
            case "Warsaw":
                lattitude = 52.2298;
                longtitude =  21.0118;
                break;
            default: //Cambridge
                lattitude = 52.207148;
                longtitude =  0.122047;
                break;
        }

        loadAll(lattitude,longtitude);

    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        this.root = FXMLLoader.load(getClass().getResource("app_layout.fxml"));
        primaryStage.setTitle("Bike Weather");
        primaryStage.setScene(new Scene(root, 450, 800));
        primaryStage.show();



        loadAll(52.207148,0.122047); //default start location is Cambridge

    }

    private void loadAll(double lattitude, double longitude) throws Exception{

        HashMap<WeatherEnum, WeatherStructure> dataMap = new HashMap<>();
        dataMap.put(WeatherEnum.TEMPERATURE, new WeatherStructure("temperature-text", "°C"));
        dataMap.put(WeatherEnum.DESCRIPTION, new WeatherStructure("description-text", ""));
        dataMap.put(WeatherEnum.WIND_SPEED, new WeatherStructure("wind-speed-text", "m/s"));
        dataMap.put(WeatherEnum.WIND_DIRECTION, new WeatherStructure("wind-direction-text", ""));
        dataMap.put(WeatherEnum.RAIN, new WeatherStructure("rain-text", "mm"));
        dataMap.put(WeatherEnum.HUMIDITY, new WeatherStructure("humidity-text", ""));
        dataMap.put(WeatherEnum.CLOUD_COVER, new WeatherStructure("cloud-cover-text", ""));
        dataMap.put(WeatherEnum.SUNRISE, new WeatherStructure("sunrise-text", ""));
        dataMap.put(WeatherEnum.SUNSET, new WeatherStructure("sunset-text", ""));
        dataMap.put(WeatherEnum.PRESSURE, new WeatherStructure("pressure-text", "hPa"));
        dataMap.put(WeatherEnum.VISIBILITY, new WeatherStructure("visibility-text", "km"));
        dataMap.put(WeatherEnum.DAY, new WeatherStructure("day-text", ""));
        dataMap.put(WeatherEnum.SHORT_DAY, new WeatherStructure("short-day-text", ""));
        dataMap.put(WeatherEnum.MIN_TEMPERATURE, new WeatherStructure("min-temperature-text", "°C"));
        dataMap.put(WeatherEnum.MAX_TEMPERATURE, new WeatherStructure("max-temperature-text", "°C"));


        Weather wdata = new Weather(lattitude,longitude,"now");

        for(Map.Entry<WeatherEnum, WeatherStructure> entry : dataMap.entrySet()){
            WeatherEnum k = entry.getKey();
            if(wdata.nowData.containsKey(k)){
                fillData(entry.getValue(), wdata.nowData.get(k));
            }
        }

        //Setting main icon
        ImageView node = (ImageView)root.lookup("#main-icon1");
        node.setImage( new Image("/sample/icons/main_icons/"+wdata.nowData.get(WeatherEnum.ICON)+".png"));

        //Setting the alert image
        ImageView alertbarimage = (ImageView)root.lookup("#alert-bar-image");
        int im = (int)(Math.random()*6 + 1);
        alertbarimage.setImage(new Image("/sample/alertbar/"+im+".jpg"));

        DataController dataController = new DataController(root,lattitude,longitude);
        dataController.loadDayData();
        dataController.loadRainGraph();
        // dataController.loadTempGraph();
        dataController.loadWeekData();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
