package com.fit_up.health.capacitor

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.aggregate.AggregateMetric
import androidx.health.connect.client.aggregate.AggregationResult
import androidx.health.connect.client.aggregate.AggregationResultGroupedByPeriod
import androidx.health.connect.client.records.*
import androidx.health.connect.client.request.AggregateGroupByPeriodRequest
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.getcapacitor.JSArray
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.annotation.Permission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.Period
import java.time.ZoneId
import java.util.Optional
import java.util.concurrent.atomic.AtomicReference
import kotlin.jvm.optionals.getOrDefault

enum class CapHealthPermission {
    READ_STEPS, READ_WORKOUTS, READ_HEART_RATE, READ_ROUTE,READ_ACTIVITY_INTENSITY, READ_ACTIVE_CALORIES, READ_TOTAL_CALORIES, READ_DISTANCE, READ_BLOOD_GLUCOSE, READ_BLOOD_PRESSURE, READ_BODY_FAT, READ_BODY_TEMPERATURE, READ_BODY_WATER_MASS, READ_BODY_BONE_MASS, READ_BASAL_BODY_TEMPERATURE, READ_BASAL_METABOLIC_RATE, READ_CERVICAL_MUCUS, READ_ELEVATION_GAINED, READ_FLOORS_CLIMBED, READ_HEART_RATE_VARIABILITY, READ_HEIGHT, READ_HYDRATION, READ_INTERMENSTRUAL_BLEEDING, READ_LEAN_BODY_MASS, READ_MENSTRUATION, READ_MINDFULNESS, READ_NUTRITION, READ_OVULATION_TEST, READ_OXYGEN_SATURATION, READ_PLANNED_EXERCISE, READ_POWER, READ_RESPIRATORY_RATE, READ_RESTING_HEART_RATE, READ_SLEEP, READ_SPEED, READ_STEPS_CADENCE, READ_VO2_MAX, READ_WEIGHT, READ_WHEELCHAIR_PUSHES;

    companion object {
        fun from(s: String): CapHealthPermission? {
            return try {
                CapHealthPermission.valueOf(s)
            } catch (e: Exception) {
                null
            }
        }
    }
}


@CapacitorPlugin(
    name = "HealthPlugin",
    permissions = [
        Permission(
            alias = "READ_STEPS",
            strings = ["android.permission.health.READ_STEPS"]
        ),
        Permission(
            alias = "READ_WORKOUTS",
            strings = ["android.permission.health.READ_EXERCISE"]
        ),
        Permission(
            alias = "READ_DISTANCE",
            strings = ["android.permission.health.READ_DISTANCE"]
        ),
        Permission(
            alias = "READ_ACTIVE_CALORIES",
            strings = ["android.permission.health.READ_ACTIVE_CALORIES_BURNED"]
        ),
        Permission(
            alias = "READ_TOTAL_CALORIES",
            strings = ["android.permission.health.READ_TOTAL_CALORIES_BURNED"]
        ),
        Permission(
            alias = "READ_HEART_RATE",
            strings = ["android.permission.health.READ_HEART_RATE"]
        ),
        Permission(
            alias = "READ_ROUTE",
            strings = ["android.permission.health.READ_EXERCISE_ROUTE"]
        ),
        Permission(
            alias = "READ_ACTIVITY_INTENSITY",
            strings = ["android.permission.READ_ACTIVITY_INTENSITY"]
        ),
        Permission(
            alias = "READ_BASAL_BODY_TEMPERATURE",
            strings = ["android.permission.READ_BASAL_BODY_TEMPERATURE"]
        ),
        Permission(
            alias = "READ_BASAL_METABOLIC_RATE",
            strings = ["android.permission.READ_BASAL_METABOLIC_RATE"]
        ),
        Permission(
            alias = "READ_BLOOD_GLUCOSE",
            strings = ["android.permission.READ_BLOOD_GLUCOSE"]
        ),
        Permission(
            alias = "READ_BLOOD_PRESSURE",
            strings = ["android.permission.READ_BLOOD_PRESSURE"]
        ),
        Permission(
            alias = "READ_BODY_FAT",
            strings = ["android.permission.READ_BODY_FAT"]
        ),
        Permission(
            alias = "READ_BODY_TEMPERATURE",
            strings = ["android.permission.READ_BODY_TEMPERATURE"]
        ),
        Permission(
            alias = "READ_BODY_WATER_MASS",
            strings = ["android.permission.READ_BODY_WATER_MASS"]
        ),
        Permission(
            alias = "READ_BODY_BONE_MASS",
            strings = ["android.permission.READ_BODY_BONE_MASS"]
        ),
        Permission(
            alias = "READ_CERVICAL_MUCUS",
            strings = ["android.permission.READ_CERVICAL_MUCUS"]
        ),
        Permission(
            alias = "READ_ELEVATION_GAINED",
            strings = ["android.permission.READ_ELEVATION_GAINED"]
        ),
        Permission(
            alias = "READ_FLOORS_CLIMBED",
            strings = ["android.permission.READ_FLOORS_CLIMBED"]
        ),
        Permission(
            alias = "READ_HEART_RATE_VARIABILITY",
            strings = ["android.permission.READ_HEART_RATE_VARIABILITY"]
        ),
        Permission(
            alias = "READ_HEIGHT",
            strings = ["android.permission.READ_HEIGHT"]
        ),
        Permission(
            alias = "READ_HYDRATION",
            strings = ["android.permission.READ_HYDRATION"]
        ),
        Permission(
            alias = "READ_INTERMENSTRUAL_BLEEDING",
            strings = ["android.permission.READ_INTERMENSTRUAL_BLEEDING"]
        ),
        Permission(
            alias = "READ_LEAN_BODY_MASS",
            strings = ["android.permission.READ_LEAN_BODY_MASS"]
        ),
        Permission(
            alias = "READ_MENSTRUATION",
            strings = ["android.permission.READ_MENSTRUATION"]
        ),
        Permission(
            alias = "READ_MINDFULNESS",
            strings = ["android.permission.READ_MINDFULNESS"]
        ),
        Permission(
            alias = "READ_NUTRITION",
            strings = ["android.permission.READ_NUTRITION"]
        ),
        Permission(
            alias = "READ_OVULATION_TEST",
            strings = ["android.permission.READ_OVULATION_TEST"]
        ),
        Permission(
            alias = "READ_OXYGEN_SATURATION",
            strings = ["android.permission.READ_OXYGEN_SATURATION"]
        ),
        Permission(
            alias = "READ_PLANNED_EXERCISE",
            strings = ["android.permission.READ_PLANNED_EXERCISE"]
        ),
        Permission(
            alias = "READ_POWER",
            strings = ["android.permission.READ_POWER"]
        ),
        Permission(
            alias = "READ_RESPIRATORY_RATE",
            strings = ["android.permission.READ_RESPIRATORY_RATE"]
        ),
        Permission(
            alias = "READ_RESTING_HEART_RATE",
            strings = ["android.permission.READ_RESTING_HEART_RATE"]
        ),
        Permission(
            alias = "READ_SLEEP",
            strings = ["android.permission.READ_SLEEP"]
        ),
        Permission(
            alias = "READ_SPEED",
            strings = ["android.permission.READ_SPEED"]
        ),        
        Permission(
            alias = "READ_STEPS_CADENCE",
            strings = ["android.permission.READ_STEPS_CADENCE"]
        ),
        Permission(
            alias = "READ_VO2_MAX",
            strings = ["android.permission.READ_VO2_MAX"]
        ),
        Permission(
            alias = "READ_WEIGHT",
            strings = ["android.permission.READ_WEIGHT"]
        ),
        Permission(
            alias = "READ_WHEELCHAIR_PUSHES",
            strings = ["android.permission.health.READ_WHEELCHAIR_PUSHES"]
        )
    ]
)

class HealthPlugin : Plugin() {


    private val tag = "CapHealth"

    private lateinit var healthConnectClient: HealthConnectClient
    private var available: Boolean = false

    private lateinit var permissionsLauncher: ActivityResultLauncher<Set<String>>
    override fun load() {
        super.load()

        val contract: ActivityResultContract<Set<String>, Set<String>> =
            PermissionController.createRequestPermissionResultContract()

        val callback: ActivityResultCallback<Set<String>> = ActivityResultCallback { grantedPermissions ->
            val context = requestPermissionContext.get()
            if (context != null) {
                val result = grantedPermissionResult(context.requestedPermissions, grantedPermissions)
                context.pluginCal.resolve(result)
            }
        }
        permissionsLauncher = activity.registerForActivityResult(contract, callback)
    }

    // Check if Google Health Connect is available. Must be called before anything else
    @PluginMethod
    fun isHealthAvailable(call: PluginCall) {

        if (!available) {
            try {
                healthConnectClient = HealthConnectClient.getOrCreate(context)
                available = true
            } catch (e: Exception) {
                Log.e("CAP-HEALTH", "error health connect client", e)
                available = false
            }
        }


        val result = JSObject()
        result.put("available", available)
        call.resolve(result)
    }


    private val permissionMapping = mapOf(
    Pair(CapHealthPermission.READ_WORKOUTS, "android.permission.health.READ_EXERCISE"),
    Pair(CapHealthPermission.READ_ROUTE, "android.permission.health.READ_EXERCISE_ROUTE"),
    Pair(CapHealthPermission.READ_HEART_RATE, "android.permission.health.READ_HEART_RATE"),
    Pair(CapHealthPermission.READ_ACTIVE_CALORIES, "android.permission.health.READ_ACTIVE_CALORIES_BURNED"),
    Pair(CapHealthPermission.READ_TOTAL_CALORIES, "android.permission.health.READ_TOTAL_CALORIES_BURNED"),
    Pair(CapHealthPermission.READ_DISTANCE, "android.permission.health.READ_DISTANCE"),
    Pair(CapHealthPermission.READ_STEPS, "android.permission.health.READ_STEPS"),
    Pair(CapHealthPermission.READ_ACTIVITY_INTENSITY, "android.permission.health.READ_ACTIVITY_INTENSITY"),
    Pair(CapHealthPermission.READ_BLOOD_GLUCOSE, "android.permission.health.READ_BLOOD_GLUCOSE"),
    Pair(CapHealthPermission.READ_BLOOD_PRESSURE, "android.permission.health.READ_BLOOD_PRESSURE"),
    Pair(CapHealthPermission.READ_BODY_FAT, "android.permission.health.READ_BODY_FAT"),
    Pair(CapHealthPermission.READ_BODY_TEMPERATURE, "android.permission.health.READ_BODY_TEMPERATURE"),
    Pair(CapHealthPermission.READ_BODY_WATER_MASS, "android.permission.health.READ_BODY_WATER_MASS"),
    Pair(CapHealthPermission.READ_BODY_BONE_MASS, "android.permission.health.READ_BONE_MASS"),
    Pair(CapHealthPermission.READ_BASAL_BODY_TEMPERATURE, "android.permission.health.READ_BASAL_BODY_TEMPERATURE"),
    Pair(CapHealthPermission.READ_BASAL_METABOLIC_RATE, "android.permission.health.READ_BASAL_METABOLIC_RATE"),
    Pair(CapHealthPermission.READ_CERVICAL_MUCUS, "android.permission.health.READ_CERVICAL_MUCUS"),
    Pair(CapHealthPermission.READ_ELEVATION_GAINED, "android.permission.health.READ_ELEVATION_GAINED"),
    Pair(CapHealthPermission.READ_FLOORS_CLIMBED, "android.permission.health.READ_FLOORS_CLIMBED"),
    Pair(CapHealthPermission.READ_HEART_RATE_VARIABILITY, "android.permission.health.READ_HEART_RATE_VARIABILITY"),
    Pair(CapHealthPermission.READ_HEIGHT, "android.permission.health.READ_HEIGHT"),
    Pair(CapHealthPermission.READ_HYDRATION, "android.permission.health.READ_HYDRATION"),
    Pair(CapHealthPermission.READ_INTERMENSTRUAL_BLEEDING, "android.permission.health.READ_INTERMENSTRUAL_BLEEDING"),
    Pair(CapHealthPermission.READ_LEAN_BODY_MASS, "android.permission.health.READ_LEAN_BODY_MASS"),
    Pair(CapHealthPermission.READ_MENSTRUATION, "android.permission.health.READ_MENSTRUATION"),
    Pair(CapHealthPermission.READ_MINDFULNESS, "android.permission.health.READ_MINDFULNESS"),
    Pair(CapHealthPermission.READ_NUTRITION, "android.permission.health.READ_NUTRITION"),
    Pair(CapHealthPermission.READ_OVULATION_TEST, "android.permission.health.READ_OVULATION_TEST"),
    Pair(CapHealthPermission.READ_OXYGEN_SATURATION, "android.permission.health.READ_OXYGEN_SATURATION"),
    Pair(CapHealthPermission.READ_PLANNED_EXERCISE, "android.permission.health.READ_PLANNED_EXERCISE"),
    Pair(CapHealthPermission.READ_POWER, "android.permission.health.READ_POWER"),
    Pair(CapHealthPermission.READ_RESPIRATORY_RATE, "android.permission.health.READ_RESPIRATORY_RATE"),
    Pair(CapHealthPermission.READ_RESTING_HEART_RATE, "android.permission.health.READ_RESTING_HEART_RATE"),
    Pair(CapHealthPermission.READ_SLEEP, "android.permission.health.READ_SLEEP"),
    Pair(CapHealthPermission.READ_SPEED, "android.permission.health.READ_SPEED"),
    Pair(CapHealthPermission.READ_STEPS_CADENCE, "android.permission.health.READ_STEPS_CADENCE"),
    Pair(CapHealthPermission.READ_VO2_MAX, "android.permission.health.READ_VO2_MAX"),
    Pair(CapHealthPermission.READ_WEIGHT, "android.permission.health.READ_WEIGHT"),
    Pair(CapHealthPermission.READ_WHEELCHAIR_PUSHES, "android.permission.health.READ_WHEELCHAIR_PUSHES"),
        
    )

    // Check if a set of permissions are granted
    @PluginMethod
    fun checkHealthPermissions(call: PluginCall) {
        val permissionsToCheck = call.getArray("permissions")
        if (permissionsToCheck == null) {
            call.reject("Must provide permissions to check")
            return
        }


        val permissions =
            permissionsToCheck.toList<String>().mapNotNull { CapHealthPermission.from(it) }.toSet()


        CoroutineScope(Dispatchers.IO).launch {
            try {

                val grantedPermissions = healthConnectClient.permissionController.getGrantedPermissions()
                val result = grantedPermissionResult(permissions, grantedPermissions)

                call.resolve(result)
            } catch (e: Exception) {
                call.reject("Checking permissions failed: ${e.message}")
            }
        }
    }

    private fun grantedPermissionResult(requestPermissions: Set<CapHealthPermission>, grantedPermissions: Set<String>): JSObject {
        val readPermissions = JSObject()
        val grantedPermissionsWithoutPrefix = grantedPermissions.map { it.substringAfterLast('.') }
        for (permission in requestPermissions) {

            readPermissions.put(
                permission.name,
                grantedPermissionsWithoutPrefix.contains(permissionMapping[permission]?.substringAfterLast('.'))
            )
        }

        val result = JSObject()
        result.put("permissions", readPermissions)
        return result

    }

    data class RequestPermissionContext(val requestedPermissions: Set<CapHealthPermission>, val pluginCal: PluginCall)

    private val requestPermissionContext = AtomicReference<RequestPermissionContext>()

    // Request a set of permissions from the user
    @PluginMethod
    fun requestHealthPermissions(call: PluginCall) {
        val permissionsToRequest = call.getArray("permissions")
        if (permissionsToRequest == null) {
            call.reject("Must provide permissions to request")
            return
        }

        val permissions = permissionsToRequest.toList<String>().mapNotNull { CapHealthPermission.from(it) }.toSet()
        val healthConnectPermissions = permissions.mapNotNull { permissionMapping[it] }.toSet()


        CoroutineScope(Dispatchers.IO).launch {
            try {
                requestPermissionContext.set(RequestPermissionContext(permissions, call))
                permissionsLauncher.launch(healthConnectPermissions)
            } catch (e: Exception) {
                call.reject("Permission request failed: ${e.message}")
                requestPermissionContext.set(null)
            }
        }
    }

    // Open Google Health Connect app settings
    @PluginMethod
    fun openHealthConnectSettings(call: PluginCall) {
        try {
            val intent = Intent().apply {
                action = HealthConnectClient.ACTION_HEALTH_CONNECT_SETTINGS
            }
            context.startActivity(intent)
            call.resolve()
        } catch(e: Exception) {
            call.reject(e.message)
        }
    }

    // Open the Google Play Store to install Health Connect
    @PluginMethod
    fun showHealthConnectInPlayStore(call: PluginCall) {
        val uri =
            Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        call.resolve()
    }

    private fun getMetricAndMapper(dataType: String): MetricAndMapper {
        return when (dataType) {
            
            "steps" -> metricAndMapper("steps", CapHealthPermission.READ_STEPS, StepsRecord.COUNT_TOTAL) { it?.toDouble() }
            "basal-calories" -> metricAndMapper(
                "calories",
                CapHealthPermission.READ_BASAL_METABOLIC_RATE,
                BasalMetabolicRateRecord.BASAL_CALORIES_TOTAL
            ) { it?.inKilocalories }
            "active-calories" -> metricAndMapper(
                "calories",
                CapHealthPermission.READ_ACTIVE_CALORIES,
                ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL
            ) { it?.inKilocalories }
            "total-calories" -> metricAndMapper(
                "calories",
                CapHealthPermission.READ_TOTAL_CALORIES,
                TotalCaloriesBurnedRecord.ENERGY_TOTAL
            ) { it?.inKilocalories }
            "distance" -> metricAndMapper("distance", CapHealthPermission.READ_DISTANCE, DistanceRecord.DISTANCE_TOTAL) { it?.inMeters }
            else -> throw RuntimeException("Unsupported dataType: $dataType")
        }
    }

    @PluginMethod
    fun queryAggregated(call: PluginCall) {
        try {
            val startDate = call.getString("startDate")
            val endDate = call.getString("endDate")
            val dataType = call.getString("dataType")
            val bucket = call.getString("bucket")

            if (startDate == null || endDate == null || dataType == null || bucket == null) {
                call.reject("Missing required parameters: startDate, endDate, dataType, or bucket")
                return
            }

            val startDateTime = Instant.parse(startDate).atZone(ZoneId.systemDefault()).toLocalDateTime()
            val endDateTime = Instant.parse(endDate).atZone(ZoneId.systemDefault()).toLocalDateTime()

            val metricAndMapper = getMetricAndMapper(dataType)

            val period = when (bucket) {
                "day" -> Period.ofDays(1)
                else -> throw RuntimeException("Unsupported bucket: $bucket")
            }


            CoroutineScope(Dispatchers.IO).launch {
                try {

                    val r = queryAggregatedMetric(metricAndMapper, TimeRangeFilter.between(startDateTime, endDateTime), period)

                    val aggregatedList = JSArray()
                    r.forEach { aggregatedList.put(it.toJs()) }

                    val finalResult = JSObject()
                    finalResult.put("aggregatedData", aggregatedList)
                    call.resolve(finalResult)
                } catch (e: Exception) {
                    call.reject("Error querying aggregated data: ${e.message}")
                }
            }
        } catch (e: Exception) {
            call.reject(e.message)
            return
        }
    }


    private fun <M : Any> metricAndMapper(
        name: String,
        permission: CapHealthPermission,
        metric: AggregateMetric<M>,
        mapper: (M?) -> Double?
    ): MetricAndMapper {
        @Suppress("UNCHECKED_CAST")
        return MetricAndMapper(name, permission, metric, mapper as (Any?) -> Double?)
    }

    data class MetricAndMapper(
        val name: String,
        val permission: CapHealthPermission,
        val metric: AggregateMetric<Any>,
        val mapper: (Any?) -> Double?
    ) {
        fun getValue(a: AggregationResult): Double? {
            return mapper(a[metric])
        }
    }

    data class AggregatedSample(val startDate: LocalDateTime, val endDate: LocalDateTime, val value: Double?) {
        fun toJs(): JSObject {
            val o = JSObject()
            o.put("startDate", startDate)
            o.put("endDate", endDate)
            o.put("value", value)

            return o

        }
    }

    private suspend fun queryAggregatedMetric(
        metricAndMapper: MetricAndMapper, timeRange: TimeRangeFilter, period: Period,
    ): List<AggregatedSample> {
        if (!hasPermission(metricAndMapper.permission)) {
            return emptyList()
        }

        val response: List<AggregationResultGroupedByPeriod> = healthConnectClient.aggregateGroupByPeriod(
            AggregateGroupByPeriodRequest(
                metrics = setOf(metricAndMapper.metric),
                timeRangeFilter = timeRange,
                timeRangeSlicer = period
            )
        )

        return response.map {
            val mappedValue = metricAndMapper.getValue(it.result)
            AggregatedSample(it.startTime, it.endTime, mappedValue)
        }

    }

    private suspend fun hasPermission(p: CapHealthPermission): Boolean {
        return healthConnectClient.permissionController.getGrantedPermissions().map { it.substringAfterLast('.') }.toSet()
            .contains(permissionMapping[p]?.substringAfterLast('.'))
    }


    @PluginMethod
    fun queryWorkouts(call: PluginCall) {
        val startDate = call.getString("startDate")
        val endDate = call.getString("endDate")
        val includeHeartRate: Boolean = call.getBoolean("includeHeartRate", false) == true
        val includeRoute: Boolean = call.getBoolean("includeRoute", false) == true
        val includeSteps: Boolean = call.getBoolean("includeSteps", false) == true
        if (startDate == null || endDate == null) {
            call.reject("Missing required parameters: startDate or endDate")
            return
        }

        val startDateTime = Instant.parse(startDate).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val endDateTime = Instant.parse(endDate).atZone(ZoneId.systemDefault()).toLocalDateTime()

        val timeRange = TimeRangeFilter.between(startDateTime, endDateTime)
        val request =
            ReadRecordsRequest(ExerciseSessionRecord::class, timeRange, emptySet(), true, 1000)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Query workouts (exercise sessions)
                val response = healthConnectClient.readRecords(request)

                val workoutsArray = JSArray()

                for (workout in response.records) {
                    val workoutObject = JSObject()
                    workoutObject.put("id", workout.metadata.id)
                    workoutObject.put(
                        "sourceName",
                        Optional.ofNullable(workout.metadata.device?.model).getOrDefault("") +
                                Optional.ofNullable(workout.metadata.device?.model).getOrDefault("")
                    )
                    workoutObject.put("sourceBundleId", workout.metadata.dataOrigin.packageName)
                    workoutObject.put("startDate", workout.startTime.toString())
                    workoutObject.put("endDate", workout.endTime.toString())
                    workoutObject.put("workoutType", exerciseTypeMapping.getOrDefault(workout.exerciseType, "OTHER"))
                    workoutObject.put("title", workout.title)
                    val duration = if (workout.segments.isEmpty()) {
                        workout.endTime.epochSecond - workout.startTime.epochSecond
                    } else {
                        workout.segments.map { it.endTime.epochSecond - it.startTime.epochSecond }
                            .stream().mapToLong { it }.sum()
                    }
                    workoutObject.put("duration", duration)

                    if (includeSteps) {
                        addWorkoutMetric(workout, workoutObject, getMetricAndMapper("steps"))
                    }

                    val readTotalCaloriesResult = addWorkoutMetric(workout, workoutObject, getMetricAndMapper("total-calories"))
                    if(!readTotalCaloriesResult) {
                        addWorkoutMetric(workout, workoutObject, getMetricAndMapper("active-calories"))
                    }

                    addWorkoutMetric(workout, workoutObject, getMetricAndMapper("distance"))

                    if (includeHeartRate && hasPermission(CapHealthPermission.READ_HEART_RATE)) {
                        // Query and add heart rate data if requested
                        val heartRates =
                            queryHeartRateForWorkout(workout.startTime, workout.endTime)
                        workoutObject.put("heartRate", heartRates)
                    }

                    if (includeRoute && workout.exerciseRouteResult is ExerciseRouteResult.Data) {
                        val route =
                            queryRouteForWorkout(workout.exerciseRouteResult as ExerciseRouteResult.Data)
                        workoutObject.put("route", route)
                    }

                    workoutsArray.put(workoutObject)
                }

                val result = JSObject()
                result.put("workouts", workoutsArray)
                call.resolve(result)

            } catch (e: Exception) {
                call.reject("Error querying workouts: ${e.message}")
            }
        }
    }

    private suspend fun addWorkoutMetric(
        workout: ExerciseSessionRecord,
        jsWorkout: JSObject,
        metricAndMapper: MetricAndMapper,
    ): Boolean {

        if (hasPermission(metricAndMapper.permission)) {
            try {
                val request = AggregateRequest(
                    setOf(metricAndMapper.metric),
                    TimeRangeFilter.Companion.between(workout.startTime, workout.endTime),
                    emptySet()
                )
                val aggregation = healthConnectClient.aggregate(request)
                val value = metricAndMapper.getValue(aggregation)
                if(value != null) {
                    jsWorkout.put(metricAndMapper.name, value)
                    return true
                }
            } catch (e: Exception) {
                Log.e(tag, "Error", e)
            }
        }
        return false;
    }


    private suspend fun queryHeartRateForWorkout(startTime: Instant, endTime: Instant): JSArray {
        val request =
            ReadRecordsRequest(HeartRateRecord::class, TimeRangeFilter.between(startTime, endTime))
        val heartRateRecords = healthConnectClient.readRecords(request)

        val heartRateArray = JSArray()
        val samples = heartRateRecords.records.flatMap { it.samples }
        for (sample in samples) {
            val heartRateObject = JSObject()
            heartRateObject.put("timestamp", sample.time.toString())
            heartRateObject.put("bpm", sample.beatsPerMinute)
            heartRateArray.put(heartRateObject)
        }
        return heartRateArray
    }

    private fun queryRouteForWorkout(routeResult: ExerciseRouteResult.Data): JSArray {

        val routeArray = JSArray()
        for (record in routeResult.exerciseRoute.route) {
            val routeObject = JSObject()
            routeObject.put("timestamp", record.time.toString())
            routeObject.put("lat", record.latitude)
            routeObject.put("lng", record.longitude)
            routeObject.put("alt", record.altitude)
            routeArray.put(routeObject)
        }
        return routeArray
    }


    private val exerciseTypeMapping = mapOf(
        0 to "OTHER",
        2 to "BADMINTON",
        4 to "BASEBALL",
        5 to "BASKETBALL",
        8 to "BIKING",
        9 to "BIKING_STATIONARY",
        10 to "BOOT_CAMP",
        11 to "BOXING",
        13 to "CALISTHENICS",
        14 to "CRICKET",
        16 to "DANCING",
        25 to "ELLIPTICAL",
        26 to "EXERCISE_CLASS",
        27 to "FENCING",
        28 to "FOOTBALL_AMERICAN",
        29 to "FOOTBALL_AUSTRALIAN",
        31 to "FRISBEE_DISC",
        32 to "GOLF",
        33 to "GUIDED_BREATHING",
        34 to "GYMNASTICS",
        35 to "HANDBALL",
        36 to "HIGH_INTENSITY_INTERVAL_TRAINING",
        37 to "HIKING",
        38 to "ICE_HOCKEY",
        39 to "ICE_SKATING",
        44 to "MARTIAL_ARTS",
        46 to "PADDLING",
        47 to "PARAGLIDING",
        48 to "PILATES",
        50 to "RACQUETBALL",
        51 to "ROCK_CLIMBING",
        52 to "ROLLER_HOCKEY",
        53 to "ROWING",
        54 to "ROWING_MACHINE",
        55 to "RUGBY",
        56 to "RUNNING",
        57 to "RUNNING_TREADMILL",
        58 to "SAILING",
        59 to "SCUBA_DIVING",
        60 to "SKATING",
        61 to "SKIING",
        62 to "SNOWBOARDING",
        63 to "SNOWSHOEING",
        64 to "SOCCER",
        65 to "SOFTBALL",
        66 to "SQUASH",
        68 to "STAIR_CLIMBING",
        69 to "STAIR_CLIMBING_MACHINE",
        70 to "STRENGTH_TRAINING",
        71 to "STRETCHING",
        72 to "SURFING",
        73 to "SWIMMING_OPEN_WATER",
        74 to "SWIMMING_POOL",
        75 to "TABLE_TENNIS",
        76 to "TENNIS",
        78 to "VOLLEYBALL",
        79 to "WALKING",
        80 to "WATER_POLO",
        81 to "WEIGHTLIFTING",
        82 to "WHEELCHAIR",
        83 to "YOGA"
    )

    @PluginMethod
    fun querySleep(call: PluginCall) {
        val startDate = call.getString("startDate") 
        val endDate = call.getString("endDate") 
        if (startDate == null || endDate == null) {
            call.reject("Missing required parameters: startDate or endDate")
            return
        }
        val startDateTime = Instant.parse(startDate).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val endDateTime = Instant.parse(endDate).atZone(ZoneId.systemDefault()).toLocalDateTime()

        val timeRange = TimeRangeFilter.between(startDateTime, endDateTime)
        val request =
            ReadRecordsRequest(SleepSessionRecord::class, timeRange, emptySet(), true, 1000)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = healthConnectClient.readRecords(request)
                val arr = JSArray()
                for (s in response.records) {
                    val obj = JSObject()
                    obj.put("id", s.metadata.id)
                    obj.put("startDate", s.startTime.toString())
                    obj.put("endDate", s.endTime.toString())
                    //obj.put("stage", s.stage?.name ?: "") // example field; check SDK for actual fields
                    
                    val stagesArr = JSArray()
                    for (instance in s.stages) {
                        val stageObj = JSObject()
                        stageObj.put("startDate", instance.startTime.toString())
                        stageObj.put("endDate", instance.endTime.toString())
                        stageObj.put("stage", sleepTypeMapping.getOrDefault(instance.stage, "UNKNOWN"))
                        stagesArr.put(stageObj)
                    }
                    obj.put("stages", stagesArr)
                    arr.put(obj)
                }
                val res = JSObject()
                res.put("sleepSessions", arr)
                call.resolve(res)
            } catch (e: Exception) {
                call.reject("Error querying sleep sessions: ${e.message}")
            }
        }
    }
    private val sleepTypeMapping = mapOf(
        0 to "UNKNOWN",
        1 to "AWAKE",
        2 to "SLEEPING",
        3 to "OUT_OF_BED",
        4 to "LIGHT",
        5 to "DEEP",
        6 to "REM",
        7 to "AWAKE_IN_BED"
    )
    // @PluginMethod
    // fun queryActivityIntensity(call: PluginCall) {
    //     val startDate = call.getString("startDate") 
    //     val endDate = call.getString("endDate") 
    //     if (startDate == null || endDate == null) {
    //         call.reject("Missing required parameters: startDate or endDate")
    //         return
    //     }
    //     val startDateTime = Instant.parse(startDate).atZone(ZoneId.systemDefault()).toLocalDateTime()
    //     val endDateTime = Instant.parse(endDate).atZone(ZoneId.systemDefault()).toLocalDateTime()

    //     val timeRange = TimeRangeFilter.between(startDateTime, endDateTime)
    //     val request =
    //         ReadRecordsRequest(ActivityIntensityRecord::class, timeRange, emptySet(), true, 1000)
    //     CoroutineScope(Dispatchers.IO).launch {
    //         try {
    //             val response = healthConnectClient.readRecords(request)
    //             val arr = JSArray()
    //             for (s in response.records) {
    //                 val obj = JSObject()
    //                 obj.put("id", s.metadata.id)
    //                 obj.put("startDate", s.startTime.toString())
    //                 obj.put("endDate", s.endTime.toString())
    //                 obj.put("activityIntensityType", activityIntensityMapping.getOrDefault(s.activityIntensityType, "UNKNOWN"))
    //                 arr.put(obj)
    //             }
    //             val res = JSObject()
    //             res.put("activityIntensitySessions", arr)
    //             call.resolve(res)
    //         } catch (e: Exception) {
    //             call.reject("Error querying activity intensity: ${e.message}")
    //         }
    //     }
    // }
    // private val activityIntensityMapping = mapOf(
    //     0 to "MODERATE",
    //     1 to "VIGOROUS"
    // )

    @PluginMethod
    fun queryBasalBodyTemperature(call: PluginCall) {
        val startDate = call.getString("startDate") 
        val endDate = call.getString("endDate") 
        if (startDate == null || endDate == null) {
            call.reject("Missing required parameters: startDate or endDate")
            return
        }
        val startDateTime = Instant.parse(startDate).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val endDateTime = Instant.parse(endDate).atZone(ZoneId.systemDefault()).toLocalDateTime()

        val timeRange = TimeRangeFilter.between(startDateTime, endDateTime)
        val request =
            ReadRecordsRequest(BasalBodyTemperatureRecord::class, timeRange, emptySet(), true, 1000)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = healthConnectClient.readRecords(request)
                val arr = JSArray()
                for (s in response.records) {
                    val obj = JSObject()
                    obj.put("id", s.metadata.id)
                    obj.put("sampleDate", s.time.toString())
                    obj.put("temperatureCelsius", s.temperature.inCelsius)
                    arr.put(obj)
                }
                val res = JSObject()
                res.put("basalBodyTemperatureSessions", arr)
                call.resolve(res)
            } catch (e: Exception) {
                call.reject("Error querying basal body temperature: ${e.message}")
            }
        }
    }
    @PluginMethod
    fun queryBloodGlucose(call: PluginCall) {
        val startDate = call.getString("startDate") 
        val endDate = call.getString("endDate") 
        if (startDate == null || endDate == null) {
            call.reject("Missing required parameters: startDate or endDate")
            return
        }
        val startDateTime = Instant.parse(startDate).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val endDateTime = Instant.parse(endDate).atZone(ZoneId.systemDefault()).toLocalDateTime()

        val timeRange = TimeRangeFilter.between(startDateTime, endDateTime)
        val request =
            ReadRecordsRequest(BloodGlucoseRecord::class, timeRange, emptySet(), true, 1000)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = healthConnectClient.readRecords(request)
                val arr = JSArray()
                for (s in response.records) {
                    val obj = JSObject()
                    obj.put("id", s.metadata.id)
                    obj.put("sampleDate", s.time.toString())
                    obj.put("level", s.level.inMilligramsPerDeciliter)
                    obj.put("specimenSource", specimmenSourceMapping.getOrDefault(s.specimenSource, "UNKNOWN"))
                    obj.put("mealType", mealTypeMapping.getOrDefault(s.mealType, "UNKNOWN"))
                    obj.put("relationToMeal", relationToMealMapping.getOrDefault(s.relationToMeal, "UNKNOWN"))
                    arr.put(obj)
                }
                val res = JSObject()
                res.put("bloodGlucoseSessions", arr)
                call.resolve(res)
            } catch (e: Exception) {
                call.reject("Error querying blood glucose: ${e.message}")
            }
        }
    }
    private val specimmenSourceMapping = mapOf(
        0 to "UNKNOWN",
        1 to "INTERSTITIAL_FLUID",
        2 to "CAPILLARY_BLOOD",
        3 to "PLASMA",
        4 to "SERUM",
        5 to "TEARS",
        6 to "WHOLE_BLOOD"
    )
    private val mealTypeMapping = mapOf(
        0 to "UNKNOWN",
        1 to "BREAKFAST",
        2 to "LUNCH",
        3 to "DINNER",
        4 to "SNACK",
    )
    private val relationToMealMapping = mapOf(
        0 to "UNKNOWN",
        1 to "GENERAL",
        2 to "FASTING",
        3 to "BEFORE_MEAL",
        4 to "AFTER_MEAL",
    )

    @PluginMethod
    fun queryOxygenSaturation(call: PluginCall) {
        val startDate = call.getString("startDate") 
        val endDate = call.getString("endDate") 
        if (startDate == null || endDate == null) {
            call.reject("Missing required parameters: startDate or endDate")
            return
        }
        val startDateTime = Instant.parse(startDate).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val endDateTime = Instant.parse(endDate).atZone(ZoneId.systemDefault()).toLocalDateTime()

        val timeRange = TimeRangeFilter.between(startDateTime, endDateTime)
        val request =
            ReadRecordsRequest(OxygenSaturationRecord::class, timeRange, emptySet(), true, 1000)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = healthConnectClient.readRecords(request)
                val arr = JSArray()
                for (s in response.records) {
                    val obj = JSObject()
                    obj.put("id", s.metadata.id)
                    obj.put("sampleDate", s.time.toString())
                    obj.put("percentage", s.percentage)
                    arr.put(obj)
                }
                val res = JSObject()
                res.put("oxygenSaturationSessions", arr)
                call.resolve(res)
            } catch (e: Exception) {
                call.reject("Error querying oxygen saturation: ${e.message}")
            }
        }
    }

        @PluginMethod
    fun queryHeartRate(call: PluginCall) {
        val startDate = call.getString("startDate") 
        val endDate = call.getString("endDate") 
        if (startDate == null || endDate == null) {
            call.reject("Missing required parameters: startDate or endDate")
            return
        }
        val startDateTime = Instant.parse(startDate).atZone(ZoneId.systemDefault()).toLocalDateTime()
        val endDateTime = Instant.parse(endDate).atZone(ZoneId.systemDefault()).toLocalDateTime()

        val timeRange = TimeRangeFilter.between(startDateTime, endDateTime)
        val request =
            ReadRecordsRequest(HeartRateRecord::class, timeRange, emptySet(), true, 1000)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = healthConnectClient.readRecords(request)
                val arr = JSArray()
                for (s in response.records) {
                    val obj = JSObject()
                    obj.put("id", s.metadata.id)
                    obj.put("startDate", s.startTime.toString())
                    obj.put("endDate", s.endTime.toString())
                    arr.put(obj)
                    val stagesArr = JSArray()
                    for (instance in s.samples) {
                        val stageObj = JSObject()
                        stageObj.put("timestamp", instance.time.toString())
                        stageObj.put("bpm",instance.beatsPerMinute)
                        stagesArr.put(stageObj)
                    }
                    obj.put("HeartRateSamples", stagesArr)
                    arr.put(obj)
                }
                val res = JSObject()
                res.put("heartRateMeasurements", arr)
                call.resolve(res)
            } catch (e: Exception) {
                call.reject("Error querying heart rate: ${e.message}")
            }
        }
    }
}


