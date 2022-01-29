package com.Viktor.Vano.Reminder;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

import static com.Viktor.Vano.Reminder.FileManager.*;

public class GUI extends Application {
    private final String version = "20220129";
    private String stringIP = "";
    private int port = 8080;
    private final int width = 550;
    private final int height = 400;
    private Label labelIP, labelPort, labelTextToSpeech;
    private TextField textFieldIP, textFieldPort;
    private Label labelTime, labelTimeCountdown;
    private TextField textFieldDays, textFieldHours, textFieldMinutes, textFieldSeconds;
    private Label labelReminderMessage;
    private TextField textFieldReminderMessage;
    private Button buttonStart, buttonStop;
    private Pane pane;
    private String stringDays, stringHours, stringMinutes, stringSeconds;
    private int days, hours, minutes, seconds;
    private String stringReminderMessage;
    private Timeline timelineOneSecondUpdate, timelineReminderPeriod;
    private boolean countdown = false, remind = true;
    private int countdownSeconds = 9999;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage){
        pane = new Pane();

        Scene scene = new Scene(pane, width, height);

        stage.setTitle("Reminder " + version);
        stage.setScene(scene);
        stage.show();
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
        stage.setMaxWidth(stage.getWidth());
        stage.setMaxHeight(stage.getHeight());
        stage.setResizable(false);

        createDirectoryIfNotExist("res");
        try{
            port = Integer.parseInt(Objects.requireNonNull(readOrCreateFile("port.dat")));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        stringIP = readOrCreateFile("IP.dat");
        stringReminderMessage = readOrCreateFile("message.dat");
        stringDays = readOrCreateFile("days.dat");
        stringHours = readOrCreateFile("hours.dat");
        stringMinutes = readOrCreateFile("minutes.dat");
        stringSeconds = readOrCreateFile("seconds.dat");
        try{
            days = Integer.parseInt(stringDays);
            hours = Integer.parseInt(stringHours);
            minutes = Integer.parseInt(stringMinutes);
            seconds = Integer.parseInt(stringSeconds);
        }catch (Exception e){
            e.printStackTrace();
        }

        labelTime = new Label("Days         Hours      Minutes    Seconds");
        labelTime.setFont(Font.font("Arial", 24));
        labelTime.setLayoutX(50);
        labelTime.setLayoutY(50);
        pane.getChildren().add(labelTime);

        textFieldDays = new TextField();
        textFieldDays.setLayoutX(55);
        textFieldDays.setLayoutY(80);
        textFieldDays.setPrefWidth(50);
        textFieldDays.setText(stringDays);
        pane.getChildren().add(textFieldDays);
        textFieldDays.textProperty().addListener(observable -> {
            if(textFieldDays.getText().length() > 0)
                try{
                    int value = Integer.parseInt(textFieldDays.getText());
                    if(value >= 0)
                    {
                        writeToFile("days.dat", String.valueOf(value));
                        days = value;
                        updateLabelCountdown();
                    }else
                        textFieldDays.setText("");
                }catch (Exception e)
                {
                    textFieldDays.setText("");
                }
        });

        textFieldHours = new TextField();
        textFieldHours.setLayoutX(170);
        textFieldHours.setLayoutY(80);
        textFieldHours.setPrefWidth(50);
        textFieldHours.setText(stringHours);
        pane.getChildren().add(textFieldHours);
        textFieldHours.textProperty().addListener(observable -> {
            if(textFieldHours.getText().length() > 0)
                try{
                    int value = Integer.parseInt(textFieldHours.getText());
                    if(value >= 0)
                    {
                        writeToFile("hours.dat", String.valueOf(value));
                        hours = value;
                        updateLabelCountdown();
                    }else
                        textFieldHours.setText("");
                }catch (Exception e)
                {
                    textFieldHours.setText("");
                }
        });

        textFieldMinutes = new TextField();
        textFieldMinutes.setLayoutX(280);
        textFieldMinutes.setLayoutY(80);
        textFieldMinutes.setPrefWidth(50);
        textFieldMinutes.setText(stringMinutes);
        pane.getChildren().add(textFieldMinutes);
        textFieldMinutes.textProperty().addListener(observable -> {
            if(textFieldMinutes.getText().length() > 0)
                try{
                    int value = Integer.parseInt(textFieldMinutes.getText());
                    if(value >= 0)
                    {
                        writeToFile("minutes.dat", String.valueOf(value));
                        minutes = value;
                        updateLabelCountdown();
                    }else
                        textFieldMinutes.setText("");
                }catch (Exception e)
                {
                    textFieldMinutes.setText("");
                }
        });

        textFieldSeconds = new TextField();
        textFieldSeconds.setLayoutX(390);
        textFieldSeconds.setLayoutY(80);
        textFieldSeconds.setPrefWidth(50);
        textFieldSeconds.setText(stringSeconds);
        pane.getChildren().add(textFieldSeconds);
        textFieldSeconds.textProperty().addListener(observable -> {
            if(textFieldSeconds.getText().length() > 0)
                try{
                    int value = Integer.parseInt(textFieldSeconds.getText());
                    if(value >= 0)
                    {
                        writeToFile("seconds.dat", String.valueOf(value));
                        seconds = value;
                        updateLabelCountdown();
                    }else
                        textFieldSeconds.setText("");
                }catch (Exception e)
                {
                    textFieldSeconds.setText("");
                }
        });

        labelTimeCountdown = new Label("0 Days, 0 Hours, 0 Minutes, 0 Seconds");
        labelTimeCountdown.setFont(Font.font("Arial", 24));
        labelTimeCountdown.setLayoutX(50);
        labelTimeCountdown.setLayoutY(120);
        pane.getChildren().add(labelTimeCountdown);
        updateLabelCountdown();

        labelReminderMessage = new Label("Message");
        labelReminderMessage.setFont(Font.font("Arial", 24));
        labelReminderMessage.setLayoutX(50);
        labelReminderMessage.setLayoutY(180);
        pane.getChildren().add(labelReminderMessage);

        textFieldReminderMessage = new TextField();
        textFieldReminderMessage.setLayoutX(160);
        textFieldReminderMessage.setLayoutY(180);
        textFieldReminderMessage.setPrefWidth(360);
        textFieldReminderMessage.setText(stringReminderMessage);
        pane.getChildren().add(textFieldReminderMessage);
        textFieldReminderMessage.textProperty().addListener(observable -> {
            if(textFieldReminderMessage.getText().length() > 0)
                try{
                    String value = textFieldReminderMessage.getText();
                    writeToFile("message.dat", value);
                    stringReminderMessage = value;
                }catch (Exception e)
                {
                    textFieldReminderMessage.setText("");
                }
        });

        labelTextToSpeech = new Label("Text To Speech");
        labelTextToSpeech.setFont(Font.font("Arial", 24));
        labelTextToSpeech.setLayoutX(50);
        labelTextToSpeech.setLayoutY(250);
        pane.getChildren().add(labelTextToSpeech);

        labelIP = new Label("IP");
        labelIP.setFont(Font.font("Arial", 24));
        labelIP.setLayoutX(50);
        labelIP.setLayoutY(300);
        pane.getChildren().add(labelIP);

        textFieldIP = new TextField();
        textFieldIP.setLayoutX(100);
        textFieldIP.setLayoutY(300);
        textFieldIP.setPrefWidth(120);
        textFieldIP.setText(stringIP);
        pane.getChildren().add(textFieldIP);
        textFieldIP.textProperty().addListener(observable -> {
            if(textFieldIP.getText().length() > 0)
                try{
                    String value = textFieldIP.getText();
                    writeToFile("IP.dat", value);
                    stringIP = value;
                }catch (Exception e)
                {
                    textFieldIP.setText("");
                }
        });

        labelPort = new Label("Port");
        labelPort.setFont(Font.font("Arial", 24));
        labelPort.setLayoutX(50);
        labelPort.setLayoutY(350);
        pane.getChildren().add(labelPort);
        labelPort.textProperty().addListener(observable -> {
            if(labelPort.getText().length() > 0)
                try{
                    int value = Integer.parseInt(labelPort.getText());
                    if(value >= 0)
                    {
                        writeToFile("port.dat", String.valueOf(value));
                        port = value;
                    }else
                        labelPort.setText("");
                }catch (Exception e)
                {
                    labelPort.setText("");
                }
        });

        textFieldPort = new TextField();
        textFieldPort.setLayoutX(100);
        textFieldPort.setLayoutY(350);
        textFieldPort.setPrefWidth(80);
        textFieldPort.setText(String.valueOf(port));
        pane.getChildren().add(textFieldPort);

        try
        {
            Image icon = new Image(getClass().getResourceAsStream("reminder.png"));
            stage.getIcons().add(icon);
            System.out.println("Icon loaded on the first attempt...");
        }catch(Exception e)
        {
            try
            {
                Image icon = new Image("reminder.png");
                stage.getIcons().add(icon);
                System.out.println("Icon loaded on the second attempt...");
            }catch(Exception e1)
            {
                System.out.println("Icon failed to load...");
            }
        }

        buttonStart = new Button("Start");
        buttonStart.setLayoutX(300);
        buttonStart.setLayoutY(300);
        buttonStart.setPrefSize(80, 80);
        buttonStart.setOnAction(event -> {
            buttonStop.setDisable(false);
            buttonStart.setDisable(true);
            textFieldIP.setDisable(true);
            textFieldPort.setDisable(true);
            textFieldDays.setDisable(true);
            textFieldHours.setDisable(true);
            textFieldMinutes.setDisable(true);
            textFieldSeconds.setDisable(true);
            textFieldReminderMessage.setDisable(true);
            setCountdownSecondsFromInput();
            countdown = true;
            remind = true;
        });
        pane.getChildren().add(buttonStart);

        buttonStop = new Button("Stop");
        buttonStop.setLayoutX(400);
        buttonStop.setLayoutY(300);
        buttonStop.setPrefSize(80, 80);
        buttonStop.setDisable(true);
        buttonStop.setOnAction(event -> {
            buttonStop.setDisable(true);
            buttonStart.setDisable(false);
            textFieldIP.setDisable(false);
            textFieldPort.setDisable(false);
            textFieldDays.setDisable(false);
            textFieldHours.setDisable(false);
            textFieldMinutes.setDisable(false);
            textFieldSeconds.setDisable(false);
            textFieldReminderMessage.setDisable(false);
            updateIntegersFromInput();
            updateLabelCountdown();
            countdown = false;
            remind = false;
        });
        pane.getChildren().add(buttonStop);

        timelineOneSecondUpdate = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if(countdown)
            {
                countdownSeconds--;
                updateLabelFromCountdownSeconds();
            }

            if(countdownSeconds <= 0 && remind)
            {
                remind = false;
                new Client(stringIP, port, stringReminderMessage);
            }
        }));
        timelineOneSecondUpdate.setCycleCount(Animation.INDEFINITE);
        timelineOneSecondUpdate.play();

        timelineReminderPeriod = new Timeline(new KeyFrame(Duration.seconds(30), event -> {
            remind = true;
        }));
        timelineReminderPeriod.setCycleCount(Animation.INDEFINITE);
        timelineReminderPeriod.play();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    private void updateLabelCountdown()
    {
        labelTimeCountdown.setText(days + " Days, "
                                + hours + " Hours, "
                                + minutes + " Minutes, "
                                + seconds + " Seconds");
    }

    private void setCountdownSecondsFromInput()
    {
        countdownSeconds = days*86400 + hours*3600 + minutes*60 + seconds;
    }

    private void updateLabelFromCountdownSeconds()
    {
        days = countdownSeconds / 86400;
        hours = (countdownSeconds - days*86400) / 3600;
        minutes = (countdownSeconds - days*86400 - hours*3600) / 60;
        seconds = countdownSeconds%60;

        updateLabelCountdown();
    }

    private void updateIntegersFromInput()
    {
        if(textFieldDays.getText().length() > 0)
            try{
                int value = Integer.parseInt(textFieldDays.getText());
                if(value >= 0)
                {
                    days = value;
                }
            }catch (Exception e)
            {
                textFieldDays.setText("");
            }

        if(textFieldHours.getText().length() > 0)
            try{
                int value = Integer.parseInt(textFieldHours.getText());
                if(value >= 0)
                {
                    hours = value;
                }
            }catch (Exception e)
            {
                textFieldHours.setText("");
            }

        if(textFieldMinutes.getText().length() > 0)
            try{
                int value = Integer.parseInt(textFieldMinutes.getText());
                if(value >= 0)
                {
                    minutes = value;
                }
            }catch (Exception e)
            {
                textFieldMinutes.setText("");
            }

        if(textFieldSeconds.getText().length() > 0)
            try{
                int value = Integer.parseInt(textFieldSeconds.getText());
                if(value >= 0)
                {
                    seconds = value;
                }
            }catch (Exception e)
            {
                textFieldSeconds.setText("");
            }
    }
}
