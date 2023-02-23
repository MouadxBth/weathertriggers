package me.hanane.trigger;

import me.hanane.data.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static me.hanane.utilities.Utilities.defaultValue;

@Entity
@Table(name = "triggers")
public class Trigger extends AbstractEntity {

    @NotNull
    @NotEmpty
    private String name;
    private String description;
    @Lob
    private String message;
    private String date;
    private boolean runOnce;

    private boolean executed;
    private double temperature = defaultValue();
    private double temp_feels_like = defaultValue();
    private double minimumTemperature = defaultValue();
    private double maximumTemperature = defaultValue();
    private double pressure = defaultValue();
    private double humidity = defaultValue();
    private double rainLastHour = defaultValue();
    private double cloudiness = defaultValue();
    private double visibility = defaultValue();
    private double windSpeed = defaultValue();
    private double windDegree = defaultValue();
    private double windGust = defaultValue();

    public Trigger(String name, String description, String message, String date, boolean runOnce, boolean executed, double temperature, double temp_feels_like, double minimumTemperature, double maximumTemperature, double pressure, double humidity, double rainLastHour, double cloudiness, double visibility, double windSpeed, double windDegree, double windGust) {
        this.name = name;
        this.description = description;
        this.message = message;
        this.date = date;
        this.runOnce = runOnce;
        this.executed = executed;
        this.temperature = temperature;
        this.temp_feels_like = temp_feels_like;
        this.minimumTemperature = minimumTemperature;
        this.maximumTemperature = maximumTemperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.rainLastHour = rainLastHour;
        this.cloudiness = cloudiness;
        this.visibility = visibility;
        this.windSpeed = windSpeed;
        this.windDegree = windDegree;
        this.windGust = windGust;
    }

    public Trigger() {
        this.name = "";
        this.description = "";
        this.message = "";
        this.date = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRunOnce() {
        return runOnce;
    }

    public void setRunOnce(boolean runOnce) {
        this.runOnce = runOnce;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTemp_feels_like() {
        return temp_feels_like;
    }

    public void setTemp_feels_like(double temp_feels_like) {
        this.temp_feels_like = temp_feels_like;
    }

    public double getMinimumTemperature() {
        return minimumTemperature;
    }

    public void setMinimumTemperature(double minimumTemperature) {
        this.minimumTemperature = minimumTemperature;
    }

    public double getMaximumTemperature() {
        return maximumTemperature;
    }

    public void setMaximumTemperature(double maximumTemperature) {
        this.maximumTemperature = maximumTemperature;
    }



    public double getRainLastHour() {
        return rainLastHour;
    }

    public void setRainLastHour(double rainLastHour) {
        this.rainLastHour = rainLastHour;
    }


    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }


    public double getWindGust() {
        return windGust;
    }

    public void setWindGust(double windGust) {
        this.windGust = windGust;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(double cloudiness) {
        this.cloudiness = cloudiness;
    }

    public double getVisibility() {
        return visibility;
    }

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }

    public double getWindDegree() {
        return windDegree;
    }

    public void setWindDegree(double windDegree) {
        this.windDegree = windDegree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Trigger trigger = (Trigger) o;
        return runOnce == trigger.runOnce
                && Double.compare(trigger.temperature, temperature) == 0
                && Double.compare(trigger.temp_feels_like, temp_feels_like) == 0
                && Double.compare(trigger.minimumTemperature, minimumTemperature) == 0
                && Double.compare(trigger.maximumTemperature, maximumTemperature) == 0
                && Double.compare(pressure, trigger.pressure) == 0
                && Double.compare(humidity, trigger.humidity) == 0
                && Double.compare(trigger.rainLastHour, rainLastHour) == 0
                && Double.compare(cloudiness, trigger.cloudiness) == 0
                && Double.compare(visibility, trigger.visibility) == 0
                && Double.compare(trigger.windSpeed, windSpeed) == 0
                && Double.compare(windDegree, trigger.windDegree) == 0
                && Double.compare(trigger.windGust, windGust) == 0
                && Objects.equals(name, trigger.name)
                && Objects.equals(description, trigger.description)
                && Objects.equals(message, trigger.message)
                && Objects.equals(date, trigger.date);
    }

    @Override
    public String toString() {
        return "Trigger{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                ", runOnce=" + runOnce +
                ", executed=" + executed +
                ", temperature=" + temperature +
                ", temp_feels_like=" + temp_feels_like +
                ", minimumTemperature=" + minimumTemperature +
                ", maximumTemperature=" + maximumTemperature +
                ", pressure=" + pressure +
                ", humidity=" + humidity +
                ", rainLastHour=" + rainLastHour +
                ", cloudiness=" + cloudiness +
                ", visibility=" + visibility +
                ", windSpeed=" + windSpeed +
                ", windDegree=" + windDegree +
                ", windGust=" + windGust +
                '}';
    }
}
