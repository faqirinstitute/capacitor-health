export interface HealthPlugin {
  /**
   * Checks if health API is available.
   * Android: If false is returned, the Google Health Connect app is probably not installed.
   * See showHealthConnectInPlayStore()
   *
   */
  isHealthAvailable(): Promise<{ available: boolean }>;

  /**
   * Android only: Returns for each given permission, if it was granted by the underlying health API
   * @param permissions permissions to query
   */
  checkHealthPermissions(permissions: PermissionsRequest): Promise<PermissionResponse>;

  /**
   * Requests the permissions from the user.
   *
   * Android: Apps can ask only a few times for permissions, after that the user has to grant them manually in
   * the Health Connect app. See openHealthConnectSettings()
   *
   * iOS: If the permissions are already granted or denied, this method will just return without asking the user. In iOS
   * we can't really detect if a user granted or denied a permission. The return value reflects the assumption that all
   * permissions were granted.
   *
   * @param permissions permissions to request
   */
  requestHealthPermissions(permissions: PermissionsRequest): Promise<PermissionResponse>;

  /**
   * Opens the apps settings, which is kind of wrong, because health permissions are configured under:
   * Settings > Apps > (Apple) Health > Access and Devices > [app-name]
   * But we can't go there directly.
   */
  openAppleHealthSettings(): Promise<void>;

  /**
   * Opens the Google Health Connect app
   */
  openHealthConnectSettings(): Promise<void>;

  /**
   * Opens the Google Health Connect app in PlayStore
   */
  showHealthConnectInPlayStore(): Promise<void>;

  /**
   * Query aggregated data
   * @param request
   */
  queryAggregated(request: QueryAggregatedRequest): Promise<QueryAggregatedResponse>;

  /**
   * Query workouts
   * @param request
   */
  queryWorkouts(request: QueryWorkoutRequest): Promise<QueryWorkoutResponse>;

  /**
   * Query sleep
   * @param request
   */
  querySleep(request: QuerySleepRequest): Promise<QuerySleepResponse>;

  // /**
  //  * Query activity intensity
  //  * @param request
  //  */
  // queryActivityIntensity(request: QueryActivityIntensityRequest): Promise<QueryActivityIntensityResponse>;

  /**
   * Query basal body temperature
   * @param request
   */
  queryBasalBodyTemperature(request: QueryBasalBodyTemperatureRequest): Promise<QueryBasalBodyTemperatureResponse>;

  /**
   * Query blood glucose
   * @param request
   */
  queryBloodGlucose(request: QueryBloodGlucoseRequest): Promise<QueryBloodGlucoseResponse>;
  /**
   * Query oxygen saturation
   * @param request
   */
  queryOxygenSaturation(request: QueryOxygenSaturationRequest): Promise<QueryOxygenSaturationResponse>;
  /**
   * Query heart rate 
   * @param request
   */
  queryHeartRate(request: QueryHeartRateRequest): Promise<QueryHeartRateResponse>;

}

  
// --- Permissions ---
export declare type HealthPermission =
  | 'READ_STEPS'
  | 'READ_WORKOUTS'
  | 'READ_HEART_RATE'
  | 'READ_ROUTE'
  | 'READ_ACTIVE_CALORIES'
  | 'READ_TOTAL_CALORIES'
  | 'READ_DISTANCE'
  | 'READ_ACTIVITY_INTENSITY'
  | 'READ_BLOOD_GLUCOSE'
  | 'READ_BLOOD_PRESSURE'
  | 'READ_BODY_FAT'
  | 'READ_BODY_TEMPERATURE'
  | 'READ_BODY_WATER_MASS'
  | 'READ_BODY_BONE_MASS'
  | 'READ_BASAL_BODY_TEMPERATURE'
  | 'READ_BASAL_METABOLIC_RATE'
  | 'READ_CERVICAL_MUCUS'
  | 'READ_ELEVATION_GAINED'
  | 'READ_FLOORS_CLIMBED'
  | 'READ_HEART_RATE_VARIABILITY'
  | 'READ_HEIGHT'
  | 'READ_HYDRATION'
  | 'READ_INTERMENSTRUAL_BLEEDING'
  | 'READ_LEAN_BODY_MASS'
  | 'READ_MENSTRUATION'
  | 'READ_MINDFULNESS'
  | 'READ_NUTRITION'
  | 'READ_OVULATION_TEST'
  | 'READ_OXYGEN_SATURATION'
  | 'READ_PLANNED_EXERCISE'
  | 'READ_POWER'
  | 'READ_RESPIRATORY_RATE'
  | 'READ_RESTING_HEART_RATE'
  | 'READ_SLEEP'
  | 'READ_SPEED'
  | 'READ_STEPS_CADENCE'
  | 'READ_TOTAL_CALORIES_BURNED'
  | 'READ_VO2_MAX'
  | 'READ_WEIGHT'
  | 'READ_WHEELCHAIR_PUSHES';


export interface PermissionsRequest {
  permissions: HealthPermission[];
}

export interface PermissionResponse {
  permissions: { [key: string]: boolean }[];
}

// --- Data Types ---
// every data type has a query request, a query response and a sample type (or session type) and sometimes with sub types
// the doculmentation of the availabe data types can be found here: https://developer.android.com/health-and-fitness/guides/health-connect/plan/data-types#alpha10
// also dont forget to check the HealthPermission list and add the query method to HealthPlugin

// Workouts
export interface QueryWorkoutRequest {
    startDate: string;
    endDate: string;
    includeHeartRate: boolean;
    includeRoute: boolean;
    includeSteps: boolean;
}
export interface QueryWorkoutResponse {
    workouts: Workout[];
}
export interface Workout {
    startDate: string;
    endDate: string;
    workoutType: string;
    sourceName: string;
    id?: string;
    duration: number;
    distance?: number;
    steps?: number;
    calories: number;
    sourceBundleId: string;
    route?: RouteSample[];
    heartRate?: HeartRateSample[];
}

export interface RouteSample {
    timestamp: string;
    lat: number;
    lng: number;
    alt?: number;
}

// Sleep
export interface QuerySleepRequest {
    startDate: string;
    endDate: string;
}
export interface QuerySleepResponse {
    sleepSessions: SleepSample[];
}
export interface SleepSample {
    startDate: string;
    endDate: string;
    id?: string;
    stages: SleepStageSample[];
}
export interface SleepStageSample {
    startDate: string;
    endDate: string;
    stage: string;
}

// Basal Body Temperature
export interface QueryBasalBodyTemperatureRequest {
    startDate: string;
    endDate: string;
}
export interface QueryBasalBodyTemperatureResponse {
    basalBodyTemperatureSessions: BasalBodyTemperatureSample[];
}
export interface BasalBodyTemperatureSample {
    sampleDate: string;
    id?: string;
    temperatureCelsius: number;
}

// Blood Glucose
export interface QueryBloodGlucoseRequest {
    startDate: string;
    endDate: string;
}
export interface QueryBloodGlucoseResponse {
    bloodGlucoseSessions: BloodGlucoseSample[];
}
export interface BloodGlucoseSample {
    sampleDate: string;
    id?: string;
    level: number;
    specimenSource: string;
    mealType: string;
    relationToMeal: string;
}

// Oxygen Saturation
export interface QueryOxygenSaturationRequest {
    startDate: string;
    endDate: string;
}
export interface QueryOxygenSaturationResponse {
    oxygenSaturationSessions: OxygenSaturationSample[];
}
export interface OxygenSaturationSample {
    sampleDate: string;
    id?: string;
    percentage: number;
}

// Heart rate
export interface QueryHeartRateRequest {
    startDate: string;
    endDate: string;
}
export interface QueryHeartRateResponse {
    heartRateMeasurements: HeartRateMeasurement[];
}
export interface HeartRateMeasurement {
    startDate: string;
    endDate: string;
    id?: string;
    HeartRateSamples: HeartRateSample[];
}

export interface HeartRateSample {
    timestamp: string;
    bpm: number;
}

// Aggregated Data
export interface QueryAggregatedRequest {
    startDate: string;
    endDate: string;
    dataType: 'steps' | 'basal-calories' | 'active-calories' | 'total-calories' | 'distance' | 'mindfulness';
    bucket: string;
}
export interface QueryAggregatedResponse {
    aggregatedData: AggregatedSample[];
}
export interface AggregatedSample {
    startDate: string;
    endDate: string;
    value: number;
}

