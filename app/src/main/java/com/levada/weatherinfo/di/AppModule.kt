package com.levada.weatherinfo.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.levada.weatherinfo.database.WeatherInfoDatabase
import com.levada.weatherinfo.database.cities.CityDatabaseDao
import com.levada.weatherinfo.database.weather.WeatherDatabaseDao
import com.levada.weatherinfo.network.WeatherApi
import com.levada.weatherinfo.repository.WeatherRepository
import com.levada.weatherinfo.ui.LocationManager
import com.levada.weatherinfo.ui.forecast.dataStore
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun weatherInfoDatabase(app: Application) = WeatherInfoDatabase.getInstance(app)


    @Provides
    @Singleton
    fun cityDao(db: WeatherInfoDatabase) = db.cityDatabaseDao

    @Provides
    @Singleton
    fun weatherDao(db: WeatherInfoDatabase) = db.weatherDatabaseDao

    @Provides
    @Singleton
    fun moshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun weatherApi(moshi: Moshi) = WeatherApi(moshi)

    @Provides
    @Singleton
    fun prefs(app: Application) = app.applicationContext.dataStore

    @Provides
    @Singleton
    fun weatherRepo(
        weatherDao: WeatherDatabaseDao,
        cityDao: CityDatabaseDao,
        api: WeatherApi,
        lm: LocationManager,
        prefs: DataStore<Preferences>
    ) =
        WeatherRepository(weatherDao, cityDao, api, lm, prefs)

    @Provides
    @Singleton
    fun locationProvider(app: Application): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(app)


    @Provides
    @Singleton
    fun locationManager(
        fusedLocationClient: FusedLocationProviderClient,
    ) = LocationManager(fusedLocationClient)
}
