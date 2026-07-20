# s8089387Assignment2 — NIT3213 Final Assessment

A 3-screen Android app built with Kotlin, Hilt, and Retrofit that authenticates against the `nit3213api`, displays a list of animal entities, and shows full details for a selected entity.

## Screens

1. **Login** — enter your student ID and password to authenticate against the API.
2. **Dashboard** — lists the animal entities returned for your assigned topic (`animals`), showing key fields for each.
3. **Details** — shows full information for a selected entity, including its description.

## Architecture

The app follows an MVVM structure with a Repository layer, wired together with Hilt for dependency injection:

```
UI (Activity)  →  ViewModel  →  Repository  →  Retrofit service  →  nit3213api
```

- **`data.model`** — data classes for API requests/responses (`LoginRequest`, `LoginResponse`, `AnimalEntity`, `DashboardResponse`)
- **`data.remote`** — `NitApiService`, the Retrofit interface defining the API endpoints
- **`data.repository`** — `LoginRepository` and `DashboardRepository`, each wrapping one API call
- **`di`** — `NetworkModule`, a Hilt module providing the Retrofit/OkHttp instances
- **`ui.login`** — `LoginViewModel`, exposing login state as a `StateFlow`
- **`ui.dashboard`** — `DashboardViewModel`, `DashboardAdapter`/`DashboardViewHolder` for the RecyclerView
- **`ui.details`** — `DetailsActivity`, displaying the selected entity's full data

Each ViewModel exposes a sealed `UiState` (`Loading` / `Success` / `Error`) that the Activity collects via `StateFlow`, scoped to the Activity's lifecycle with `repeatOnLifecycle`.

## API details

- Base URL: `https://nit3213api.onrender.com/`
- Login: `POST /sydney/auth` — body `{ "username": "<student id>", "password": "<first name>" }`, returns `{ "keypass": "<topic>" }`
- Dashboard: `GET /dashboard/{keypass}` — returns `{ "entities": [...], "entityTotal": N }`

## Tech stack

- Kotlin, single-Activity-per-screen architecture (3 Activities, `Intent`-based navigation)
- Hilt for dependency injection
- Retrofit + Gson for networking
- Kotlin Coroutines + `StateFlow` for async state management
- Material 3 components (`TextInputLayout`, `MaterialButton`, `ShapeableImageView`)
- MockK + `kotlinx-coroutines-test` for ViewModel unit tests

## Building and running

1. Clone the repository:
   ```
   git clone https://github.com/Fredrick-Nathan/s8089387Assignment2.git
   ```
2. Open the project in Android Studio (tested on a version supporting AGP 9.2.1 and built-in Kotlin support — no separate Kotlin Gradle plugin is applied).
3. Let Gradle sync. No manual configuration is required — API base URL and dependencies are already set up.
4. Run on an emulator or device with **minSdk 24** or higher.
5. Log in with your student ID and password to reach the Dashboard.

## Running unit tests

Unit tests cover `LoginViewModel` and `DashboardViewModel`, using MockK to fake the repository layer and `kotlinx-coroutines-test` to control coroutine execution.

Run from the terminal:
```
./gradlew testDebugUnitTest
```

Or right-click a test class/method in Android Studio and select **Run**.

## Notes

- Dashboard entities use a typed `AnimalEntity` model (`species`, `scientificName`, `habitat`, `diet`, `conservationStatus`, `averageLifespan`, `description`), confirmed against the live API response for the `animals` topic.
- Navigation between screens passes data via `Intent` extras rather than a shared `Parcelable` object, kept simple given the app's scope.
