package me.hanane.weather;

import me.hanane.trigger.Trigger;
import me.hanane.weather.info.MainInfo;
import me.hanane.weather.info.WeatherInfo;
import me.hanane.weather.info.WindInfo;

public record Weather(WeatherInfo weatherInfo, MainInfo mainInfo, WindInfo windInfo) {
    public boolean testTrigger(Trigger trigger) {
        System.out.println("TESTING " + trigger);
        boolean result;

        result = testTriggerMain(trigger);
        System.out.println("MAIN TEST " + result);

        result = testTriggerTemp(trigger);
        System.out.println("TEMP TEST " + result);

        result = testTriggerWind(trigger);
        System.out.println("WIND TEST " + result);

        return result;
    }

    private boolean testTriggerTemp(Trigger trigger) {
        boolean result = true;
        if (trigger.getTemperature() != null)
            result = Double.compare(trigger.getTemperature(), mainInfo().temp()) == 0;

        if (trigger.getMaximumTemperature() != null)
            result = Double.compare(trigger.getMaximumTemperature(), mainInfo().temp_max()) == 0;

        if (trigger.getMinimumTemperature() != null)
            result = Double.compare(trigger.getMinimumTemperature(), mainInfo().temp_min()) == 0;

        if (trigger.getTemp_feels_like() != null)
            result = Double.compare(trigger.getTemp_feels_like(), mainInfo().feels_like()) == 0;
        return result;
    }

    private boolean testTriggerWind(Trigger trigger) {
        boolean result = true;
        if (trigger.getWindSpeed() != null)
            result = Double.compare(trigger.getWindSpeed(), windInfo().speed()) == 0;

        if (trigger.getWindDegree() != null)
            result = trigger.getWindDegree() == windInfo().deg();

        if (trigger.getWindGust() != null)
            result = Double.compare(trigger.getWindGust(), windInfo().gust()) == 0;

        return result;
    }

    private boolean testTriggerMain(Trigger trigger) {
        boolean result = true;
        if (trigger.getPressure() != null)
            result = trigger.getPressure() == mainInfo.pressure();

        if (trigger.getHumidity() != null)
            result = trigger.getHumidity() == mainInfo().humidity();

        if (trigger.getCloudiness() != null)
            result = trigger.getCloudiness() == weatherInfo().cloudiness();

        if (trigger.getVisibility() != null)
            result = trigger.getVisibility() == weatherInfo().visibility();

        if (trigger.getRainLastHour() != null)
            result = Double.compare(trigger.getRainLastHour(), weatherInfo().rainLastHour()) == 0;

        return result;
    }

}
