package org.example.geoblinker.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.example.geoblinker.core.base.BaseViewModel
import org.example.geoblinker.core.utils.MarkerUtils
import org.example.geoblinker.domain.models.Devices
import org.example.geoblinker.domain.repositories.Repository
import org.example.geoblinker.domain.usecases.SyncDevicesUseCase
import org.example.geoblinker.presentation.features.map_screen.*

/**
 * ViewModel для карты
 * Расширяет DeviceViewModel логикой карты
 */
class MapViewModel(
    private val syncDevicesUseCase: SyncDevicesUseCase,
    private val repository: Repository
) : BaseViewModel<MapState, MapEvent, MapEffect>() {
    
    override val initialState = MapState()
    
    init {
        loadDevices()
    }
    
    override fun handleEvent(event: MapEvent) {
        when (event) {
            is MapEvent.OnDeviceSelected -> {
                updateState { copy(selectedDevice = event.device) }
                event.device?.let { device ->
                    if (MarkerUtils.isValidCoordinates(device.lat, device.lng)) {
                        sendEffect(MapEffect.ZoomToDevice(device))
                    }
                }
            }
            
            is MapEvent.OnUserLocationUpdated -> {
                updateState { copy(userLocation = event.lat to event.lng) }
            }
            
            is MapEvent.OnThemeChanged -> {
                updateState { copy(mapTheme = event.theme) }
            }
            
            is MapEvent.OnSearchDevice -> searchDevice(event.query)
            
            is MapEvent.OnFollowModeToggle -> {
                val newMode = !state.value.isFollowMode
                updateState { copy(isFollowMode = newMode) }
                
                if (newMode) {
                    state.value.selectedDevice?.let { device ->
                        if (MarkerUtils.isValidCoordinates(device.lat, device.lng)) {
                            sendEffect(MapEffect.ZoomToDevice(device))
                        }
                    }
                }
            }
            
            is MapEvent.OnRefreshDevices -> refreshDevices()
        }
    }
    
    /**
     * Загрузка устройств из БД
     */
    private fun loadDevices() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            
            repository.getAllDevices()
                .catch { e ->
                    updateState { copy(isLoading = false, error = e.message) }
                    sendEffect(MapEffect.ShowError(e.message ?: "Ошибка загрузки"))
                }
                .collect { devices ->
                    updateState { 
                        copy(
                            devices = devices.filter { device ->
                                MarkerUtils.isValidCoordinates(device.lat, device.lng)
                            },
                            isLoading = false
                        ) 
                    }
                }
        }
    }
    
    /**
     * Синхронизация устройств
     */
    private fun refreshDevices() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            
            val result = syncDevicesUseCase.execute()
            
            updateState { copy(isLoading = false) }
            
            if (result.isFailure) {
                val error = result.exceptionOrNull()?.message ?: "Ошибка синхронизации"
                sendEffect(MapEffect.ShowError(error))
            }
        }
    }
    
    /**
     * Поиск устройства
     */
    private fun searchDevice(query: String) {
        val found = state.value.devices.find { device ->
            device.name.contains(query, ignoreCase = true) ||
            device.imei.contains(query) ||
            device.registrationPlate.contains(query, ignoreCase = true)
        }
        
        if (found != null) {
            updateState { copy(selectedDevice = found) }
            sendEffect(MapEffect.ZoomToDevice(found))
        } else {
            sendEffect(MapEffect.ShowError("Устройство не найдено"))
        }
    }
}
