package com.levada.weatherinfo.ui.forecast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.levada.weatherinfo.domain.Alert
import com.levada.weatherinfo.domain.Current
import coil.compose.rememberImagePainter
import java.time.ZonedDateTime
import com.levada.weatherinfo.R as WeatherInfoR

private val gridPadding = 24.dp

@Composable
fun CurrentForecast(listState: LazyListState, current: Current?, alerts: List<Alert>) {
    if (current == null) {
        NoDataMessage(message = stringResource(com.levada.weatherinfo.R.string.no_data_message_current))
        return
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        current.run {
            item { TimeUpdated(lastUpdated) }
            item {
                Weather(
                    description,
                    icon,
                    temp,
                    feelsLike,
                    uvi
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    GridColumn {
                        Wind(
                            windSpeed,
                            windGust,
                            windDirection
                        )
                        Cloudiness(clouds)
                    }
                    GridColumn {
                        Humidity(humidity)
                        Sunrise(sunrise)

                    }
                    GridColumn {
                        Pressure(pressure)
                        Sunset(sunset)

                    }
                    GridColumn {
                        Visibility(visibility)
                        DewPoint(dewPoint)
                    }

                }
            }
        }
        items(
            items = alerts
        ) { alert ->
            Alert(alert)
        }
    }
}

@Composable
fun TimeUpdated(time: ZonedDateTime) {
    Text(
        text = stringResource(
            com.levada.weatherinfo.R.string.time_updated,
            TimeFormatter.toDate(time)
        ),
        style = MaterialTheme.typography.body2
    )
}

@Composable
fun Weather(description: String, icon: String, temp: Int, feelsLike: Int, uvi: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = rememberImagePainter(
                stringResource(
                    com.levada.weatherinfo.R.string.icon_url,
                    icon
                )
            ),
            contentDescription = null,
            modifier = Modifier.size(128.dp)
        )
        Column {
            Text(
                text = stringResource(com.levada.weatherinfo.R.string.degrees_unit, temp),
                style = MaterialTheme.typography.h3
            )
            Text(text = stringResource(com.levada.weatherinfo.R.string.feels_like, feelsLike))
            Text(description)
            Text(stringResource(com.levada.weatherinfo.R.string.uv_index, uvi))
        }
    }
}

@Composable
fun GridColumn(content: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxHeight()
    ) {
        content()
    }
}

@Composable
fun GridItem(padded: Boolean = false, content: @Composable () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(bottom = if (padded) gridPadding else 0.dp)
    ) {
        content()
    }
}

@Composable
fun Wind(speed: Int, gust: Int, direction: String) {
    GridItem {
        Image(
            painter = painterResource(WeatherInfoR.drawable.wind),
            contentDescription = null
        )
        Text(
            text = stringResource(com.levada.weatherinfo.R.string.wind),
            style = TextStyle(fontWeight = FontWeight.Light)
        )
        Text(text = stringResource(com.levada.weatherinfo.R.string.wind_speed, speed, direction))
        Text(text = stringResource(com.levada.weatherinfo.R.string.wind_speed_gust, gust))
    }
}

@Composable
fun Cloudiness(clouds: Int) {
    GridItem {
        Image(
            painter = painterResource(WeatherInfoR.drawable.cloud),
            contentDescription = null
        )
        Text(
            text = stringResource(com.levada.weatherinfo.R.string.cloudiness),
            style = TextStyle(fontWeight = FontWeight.Light)
        )
        Text(text = stringResource(com.levada.weatherinfo.R.string.int_percent, clouds))
    }
}

@Composable
fun Visibility(visibility: Int) {
    GridItem(padded = true) {
        Image(
            painter = painterResource(WeatherInfoR.drawable.telescope),
            contentDescription = null
        )
        Text(
            text = stringResource(com.levada.weatherinfo.R.string.visibility),
            style = TextStyle(fontWeight = FontWeight.Light)
        )
        Text(text = stringResource(com.levada.weatherinfo.R.string.kilometers, visibility))
    }
}

@Composable
fun Pressure(pressure: Double) {
    GridItem(padded = true) {
        Image(
            painter = painterResource(WeatherInfoR.drawable.barometer),
            contentDescription = null
        )
        Text(
            text = stringResource(com.levada.weatherinfo.R.string.pressure),
            style = TextStyle(fontWeight = FontWeight.Light)
        )
        Text(stringResource(com.levada.weatherinfo.R.string.kpa, "%.1f".format(pressure)))
    }
}

@Composable
fun Humidity(humidity: Int) {
    GridItem(padded = true) {
        Image(
            painter = painterResource(WeatherInfoR.drawable.humidity),
            contentDescription = null
        )
        Text(
            text = stringResource(com.levada.weatherinfo.R.string.humidity),
            style = TextStyle(fontWeight = FontWeight.Light)
        )
        Text(stringResource(com.levada.weatherinfo.R.string.int_percent, humidity))
    }
}

@Composable
fun DewPoint(dewPoint: Int) {
    GridItem {
        Image(
            painter = painterResource(WeatherInfoR.drawable.dewpoint),
            contentDescription = null
        )
        Text(
            text = stringResource(com.levada.weatherinfo.R.string.dew_point),
            style = TextStyle(fontWeight = FontWeight.Light)
        )
        Text(stringResource(com.levada.weatherinfo.R.string.degrees_unit, dewPoint))
    }
}

@Composable
fun Sunrise(sunrise: ZonedDateTime) {
    GridItem {
        Image(
            painter = painterResource(WeatherInfoR.drawable.sunrise),
            contentDescription = null
        )
        Text(
            text = stringResource(com.levada.weatherinfo.R.string.sunrise),
            style = TextStyle(fontWeight = FontWeight.Light)
        )
        Text(text = TimeFormatter.toDayHour(sunrise))
    }
}

@Composable
fun Sunset(sunset: ZonedDateTime) {
    GridItem {
        Image(
            painter = painterResource(WeatherInfoR.drawable.sunset),
            contentDescription = null
        )
        Text(
            text = stringResource(com.levada.weatherinfo.R.string.sunset),
            style = TextStyle(fontWeight = FontWeight.Light)
        )
        Text(text = TimeFormatter.toDayHour(sunset))
    }
}

@Composable
fun Alert(alert: Alert) {
    val caption = MaterialTheme.typography.caption
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.error, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = alert.event, style = MaterialTheme.typography.subtitle2)
        Text(
            text = stringResource(
                com.levada.weatherinfo.R.string.alert_start_time,
                TimeFormatter.toDate(alert.start)
            ), style = caption
        )
        Text(
            text = stringResource(
                com.levada.weatherinfo.R.string.alert_end_time,
                TimeFormatter.toDate(alert.end)
            ), style = caption
        )
        Row {
            Text(
                text = stringResource(com.levada.weatherinfo.R.string.alert_issued_by),
                style = caption
            )
            Text(text = alert.senderName, fontWeight = FontWeight.SemiBold, style = caption)
        }
        Divider(
            color = MaterialTheme.colors.onBackground,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(text = alert.description, style = MaterialTheme.typography.body2)
    }
}