# capacitor-health

Capacitor plugin to query data from Apple Health and Google Health Connect

## Thanks and attribution

Some parts, concepts and ideas are borrowed from [cordova-plugin-health](https://github.com/dariosalvi78/cordova-plugin-health/). Big thanks to [@dariosalvi78](https://github.com/dariosalvi78) for the support.

## Install

```bash
npm install capacitor-health
npx cap sync
```

## Setup

### iOS

* Make sure your app id has the 'HealthKit' entitlement when this plugin is installed (see iOS dev center).
* Also, make sure your app and App Store description comply with the Apple review guidelines.
* There are two keys to be added to the info.plist file: NSHealthShareUsageDescription and NSHealthUpdateUsageDescription. 

### Android

* Android Manifest in application tag
```xml
        <!-- For supported versions through Android 13, create an activity to show the rationale
    of Health Connect permissions once users click the privacy policy link. -->
        <activity
            android:name="com.fit_up.health.capacitor.PermissionsRationaleActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE" />
            </intent-filter>
        </activity>

        <!-- For versions starting Android 14, create an activity alias to show the rationale
         of Health Connect permissions once users click the privacy policy link. -->
        <activity-alias
            android:name="ViewPermissionUsageActivity"
            android:exported="true"
            android:targetActivity="com.fit_up.health.capacitor.PermissionsRationaleActivity"
            android:permission="android.permission.START_VIEW_PERMISSION_USAGE">
            <intent-filter>
                <action android:name="android.intent.action.VIEW_PERMISSION_USAGE" />
                <category android:name="android.intent.category.HEALTH_PERMISSIONS" />
            </intent-filter>
        </activity-alias>
```

* Android Manifest in root tag
```xml
    <queries>
        <package android:name="com.google.android.apps.healthdata" />
    </queries>
    
    <uses-permission android:name="android.permission.health.READ_STEPS" />
    <uses-permission android:name="android.permission.health.READ_ACTIVE_CALORIES_BURNED" />
    <uses-permission android:name="android.permission.health.READ_DISTANCE" />
    <uses-permission android:name="android.permission.health.READ_EXERCISE" />
    <uses-permission android:name="android.permission.health.READ_EXERCISE_ROUTE" />
    <uses-permission android:name="android.permission.health.READ_HEART_RATE" />
```

## API

<docgen-index>

* [`isHealthAvailable()`](#ishealthavailable)
* [`checkHealthPermissions(...)`](#checkhealthpermissions)
* [`requestHealthPermissions(...)`](#requesthealthpermissions)
* [`openAppleHealthSettings()`](#openapplehealthsettings)
* [`openHealthConnectSettings()`](#openhealthconnectsettings)
* [`showHealthConnectInPlayStore()`](#showhealthconnectinplaystore)
* [`queryAggregated(...)`](#queryaggregated)
* [`queryWorkouts(...)`](#queryworkouts)
* [`querySleep(...)`](#querysleep)
* [`queryBasalBodyTemperature(...)`](#querybasalbodytemperature)
* [`queryBloodGlucose(...)`](#querybloodglucose)
* [`queryOxygenSaturation(...)`](#queryoxygensaturation)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### isHealthAvailable()

```typescript
isHealthAvailable() => Promise<{ available: boolean; }>
```

Checks if health API is available.
Android: If false is returned, the Google Health Connect app is probably not installed.
See showHealthConnectInPlayStore()

**Returns:** <code>Promise&lt;{ available: boolean; }&gt;</code>

--------------------


### checkHealthPermissions(...)

```typescript
checkHealthPermissions(permissions: PermissionsRequest) => Promise<PermissionResponse>
```

Android only: Returns for each given permission, if it was granted by the underlying health API

| Param             | Type                                                              | Description          |
| ----------------- | ----------------------------------------------------------------- | -------------------- |
| **`permissions`** | <code><a href="#permissionsrequest">PermissionsRequest</a></code> | permissions to query |

**Returns:** <code>Promise&lt;<a href="#permissionresponse">PermissionResponse</a>&gt;</code>

--------------------


### requestHealthPermissions(...)

```typescript
requestHealthPermissions(permissions: PermissionsRequest) => Promise<PermissionResponse>
```

Requests the permissions from the user.

Android: Apps can ask only a few times for permissions, after that the user has to grant them manually in
the Health Connect app. See openHealthConnectSettings()

iOS: If the permissions are already granted or denied, this method will just return without asking the user. In iOS
we can't really detect if a user granted or denied a permission. The return value reflects the assumption that all
permissions were granted.

| Param             | Type                                                              | Description            |
| ----------------- | ----------------------------------------------------------------- | ---------------------- |
| **`permissions`** | <code><a href="#permissionsrequest">PermissionsRequest</a></code> | permissions to request |

**Returns:** <code>Promise&lt;<a href="#permissionresponse">PermissionResponse</a>&gt;</code>

--------------------


### openAppleHealthSettings()

```typescript
openAppleHealthSettings() => Promise<void>
```

Opens the apps settings, which is kind of wrong, because health permissions are configured under:
Settings &gt; Apps &gt; (Apple) Health &gt; Access and Devices &gt; [app-name]
But we can't go there directly.

--------------------


### openHealthConnectSettings()

```typescript
openHealthConnectSettings() => Promise<void>
```

Opens the Google Health Connect app

--------------------


### showHealthConnectInPlayStore()

```typescript
showHealthConnectInPlayStore() => Promise<void>
```

Opens the Google Health Connect app in PlayStore

--------------------


### queryAggregated(...)

```typescript
queryAggregated(request: QueryAggregatedRequest) => Promise<QueryAggregatedResponse>
```

Query aggregated data

| Param         | Type                                                                      |
| ------------- | ------------------------------------------------------------------------- |
| **`request`** | <code><a href="#queryaggregatedrequest">QueryAggregatedRequest</a></code> |

**Returns:** <code>Promise&lt;<a href="#queryaggregatedresponse">QueryAggregatedResponse</a>&gt;</code>

--------------------


### queryWorkouts(...)

```typescript
queryWorkouts(request: QueryWorkoutRequest) => Promise<QueryWorkoutResponse>
```

Query workouts

| Param         | Type                                                                |
| ------------- | ------------------------------------------------------------------- |
| **`request`** | <code><a href="#queryworkoutrequest">QueryWorkoutRequest</a></code> |

**Returns:** <code>Promise&lt;<a href="#queryworkoutresponse">QueryWorkoutResponse</a>&gt;</code>

--------------------


### querySleep(...)

```typescript
querySleep(request: QuerySleepRequest) => Promise<QuerySleepResponse>
```

Query sleep

| Param         | Type                                                            |
| ------------- | --------------------------------------------------------------- |
| **`request`** | <code><a href="#querysleeprequest">QuerySleepRequest</a></code> |

**Returns:** <code>Promise&lt;<a href="#querysleepresponse">QuerySleepResponse</a>&gt;</code>

--------------------


### queryBasalBodyTemperature(...)

```typescript
queryBasalBodyTemperature(request: QueryBasalBodyTemperatureRequest) => Promise<QueryBasalBodyTemperatureResponse>
```

Query basal body temperature

| Param         | Type                                                                                          |
| ------------- | --------------------------------------------------------------------------------------------- |
| **`request`** | <code><a href="#querybasalbodytemperaturerequest">QueryBasalBodyTemperatureRequest</a></code> |

**Returns:** <code>Promise&lt;<a href="#querybasalbodytemperatureresponse">QueryBasalBodyTemperatureResponse</a>&gt;</code>

--------------------


### queryBloodGlucose(...)

```typescript
queryBloodGlucose(request: QueryBloodGlucoseRequest) => Promise<QueryBloodGlucoseResponse>
```

Query blood glucose

| Param         | Type                                                                          |
| ------------- | ----------------------------------------------------------------------------- |
| **`request`** | <code><a href="#querybloodglucoserequest">QueryBloodGlucoseRequest</a></code> |

**Returns:** <code>Promise&lt;<a href="#querybloodglucoseresponse">QueryBloodGlucoseResponse</a>&gt;</code>

--------------------


### queryOxygenSaturation(...)

```typescript
queryOxygenSaturation(request: QueryOxygenSaturationRequest) => Promise<QueryOxygenSaturationResponse>
```

Query oxygen saturation

| Param         | Type                                                                                  |
| ------------- | ------------------------------------------------------------------------------------- |
| **`request`** | <code><a href="#queryoxygensaturationrequest">QueryOxygenSaturationRequest</a></code> |

**Returns:** <code>Promise&lt;<a href="#queryoxygensaturationresponse">QueryOxygenSaturationResponse</a>&gt;</code>

--------------------


### Interfaces


#### PermissionResponse

| Prop              | Type                                       |
| ----------------- | ------------------------------------------ |
| **`permissions`** | <code>{ [key: string]: boolean; }[]</code> |


#### PermissionsRequest

| Prop              | Type                            |
| ----------------- | ------------------------------- |
| **`permissions`** | <code>HealthPermission[]</code> |


#### QueryAggregatedResponse

| Prop                 | Type                            |
| -------------------- | ------------------------------- |
| **`aggregatedData`** | <code>AggregatedSample[]</code> |


#### AggregatedSample

| Prop            | Type                |
| --------------- | ------------------- |
| **`startDate`** | <code>string</code> |
| **`endDate`**   | <code>string</code> |
| **`value`**     | <code>number</code> |


#### QueryAggregatedRequest

| Prop            | Type                                                                                                             |
| --------------- | ---------------------------------------------------------------------------------------------------------------- |
| **`startDate`** | <code>string</code>                                                                                              |
| **`endDate`**   | <code>string</code>                                                                                              |
| **`dataType`**  | <code>'steps' \| 'basal-calories' \| 'active-calories' \| 'total-calories' \| 'distance' \| 'mindfulness'</code> |
| **`bucket`**    | <code>string</code>                                                                                              |


#### QueryWorkoutResponse

| Prop           | Type                   |
| -------------- | ---------------------- |
| **`workouts`** | <code>Workout[]</code> |


#### Workout

| Prop                 | Type                           |
| -------------------- | ------------------------------ |
| **`startDate`**      | <code>string</code>            |
| **`endDate`**        | <code>string</code>            |
| **`workoutType`**    | <code>string</code>            |
| **`sourceName`**     | <code>string</code>            |
| **`id`**             | <code>string</code>            |
| **`duration`**       | <code>number</code>            |
| **`distance`**       | <code>number</code>            |
| **`steps`**          | <code>number</code>            |
| **`calories`**       | <code>number</code>            |
| **`sourceBundleId`** | <code>string</code>            |
| **`route`**          | <code>RouteSample[]</code>     |
| **`heartRate`**      | <code>HeartRateSample[]</code> |


#### RouteSample

| Prop            | Type                |
| --------------- | ------------------- |
| **`timestamp`** | <code>string</code> |
| **`lat`**       | <code>number</code> |
| **`lng`**       | <code>number</code> |
| **`alt`**       | <code>number</code> |


#### HeartRateSample

| Prop            | Type                |
| --------------- | ------------------- |
| **`timestamp`** | <code>string</code> |
| **`bpm`**       | <code>number</code> |


#### QueryWorkoutRequest

| Prop                   | Type                 |
| ---------------------- | -------------------- |
| **`startDate`**        | <code>string</code>  |
| **`endDate`**          | <code>string</code>  |
| **`includeHeartRate`** | <code>boolean</code> |
| **`includeRoute`**     | <code>boolean</code> |
| **`includeSteps`**     | <code>boolean</code> |


#### QuerySleepResponse

| Prop                | Type                       |
| ------------------- | -------------------------- |
| **`sleepSessions`** | <code>SleepSample[]</code> |


#### SleepSample

| Prop            | Type                            |
| --------------- | ------------------------------- |
| **`startDate`** | <code>string</code>             |
| **`endDate`**   | <code>string</code>             |
| **`id`**        | <code>string</code>             |
| **`stages`**    | <code>SleepStageSample[]</code> |


#### SleepStageSample

| Prop            | Type                |
| --------------- | ------------------- |
| **`startDate`** | <code>string</code> |
| **`endDate`**   | <code>string</code> |
| **`stage`**     | <code>string</code> |


#### QuerySleepRequest

| Prop            | Type                |
| --------------- | ------------------- |
| **`startDate`** | <code>string</code> |
| **`endDate`**   | <code>string</code> |


#### QueryBasalBodyTemperatureResponse

| Prop                               | Type                                      |
| ---------------------------------- | ----------------------------------------- |
| **`basalBodyTemperatureSessions`** | <code>BasalBodyTemperatureSample[]</code> |


#### BasalBodyTemperatureSample

| Prop                     | Type                |
| ------------------------ | ------------------- |
| **`sampleDate`**         | <code>string</code> |
| **`id`**                 | <code>string</code> |
| **`temperatureCelsius`** | <code>number</code> |


#### QueryBasalBodyTemperatureRequest

| Prop            | Type                |
| --------------- | ------------------- |
| **`startDate`** | <code>string</code> |
| **`endDate`**   | <code>string</code> |


#### QueryBloodGlucoseResponse

| Prop                       | Type                              |
| -------------------------- | --------------------------------- |
| **`bloodGlucoseSessions`** | <code>BloodGlucoseSample[]</code> |


#### BloodGlucoseSample

| Prop                 | Type                |
| -------------------- | ------------------- |
| **`sampleDate`**     | <code>string</code> |
| **`id`**             | <code>string</code> |
| **`level`**          | <code>number</code> |
| **`specimenSource`** | <code>string</code> |
| **`mealType`**       | <code>string</code> |
| **`relationToMeal`** | <code>string</code> |


#### QueryBloodGlucoseRequest

| Prop            | Type                |
| --------------- | ------------------- |
| **`startDate`** | <code>string</code> |
| **`endDate`**   | <code>string</code> |


#### QueryOxygenSaturationResponse

| Prop                           | Type                                  |
| ------------------------------ | ------------------------------------- |
| **`oxygenSaturationSessions`** | <code>OxygenSaturationSample[]</code> |


#### OxygenSaturationSample

| Prop             | Type                |
| ---------------- | ------------------- |
| **`sampleDate`** | <code>string</code> |
| **`id`**         | <code>string</code> |
| **`percentage`** | <code>number</code> |


#### QueryOxygenSaturationRequest

| Prop            | Type                |
| --------------- | ------------------- |
| **`startDate`** | <code>string</code> |
| **`endDate`**   | <code>string</code> |


### Type Aliases


#### HealthPermission

<code>'READ_STEPS' | 'READ_WORKOUTS' | 'READ_HEART_RATE' | 'READ_ROUTE' | 'READ_ACTIVE_CALORIES' | 'READ_TOTAL_CALORIES' | 'READ_DISTANCE' | 'READ_ACTIVITY_INTENSITY' | 'READ_BLOOD_GLUCOSE' | 'READ_BLOOD_PRESSURE' | 'READ_BODY_FAT' | 'READ_BODY_TEMPERATURE' | 'READ_BODY_WATER_MASS' | 'READ_BODY_BONE_MASS' | 'READ_BASAL_BODY_TEMPERATURE' | 'READ_BASAL_METABOLIC_RATE' | 'READ_CERVICAL_MUCUS' | 'READ_ELEVATION_GAINED' | 'READ_FLOORS_CLIMBED' | 'READ_HEART_RATE_VARIABILITY' | 'READ_HEIGHT' | 'READ_HYDRATION' | 'READ_INTERMENSTRUAL_BLEEDING' | 'READ_LEAN_BODY_MASS' | 'READ_MENSTRUATION' | 'READ_MINDFULNESS' | 'READ_NUTRITION' | 'READ_OVULATION_TEST' | 'READ_OXYGEN_SATURATION' | 'READ_PLANNED_EXERCISE' | 'READ_POWER' | 'READ_RESPIRATORY_RATE' | 'READ_RESTING_HEART_RATE' | 'READ_SLEEP' | 'READ_SPEED' | 'READ_STEPS_CADENCE' | 'READ_TOTAL_CALORIES_BURNED' | 'READ_VO2_MAX' | 'READ_WEIGHT' | 'READ_WHEELCHAIR_PUSHES'</code>

</docgen-api>
