# Compose Tetris – Környezet és verzióazonosítás

**Azonosító:** ALPHA-TETRIS-ENV-002 · **Verzió:** 1.0 · **Dátum:** 2026-10-09

## Vizsgált program

- Upstream: <https://github.com/vitaviva/compose-tetris>
- Upstream commit: `234416c455cd0b5524b7f2a7e91aaa9f6206457a`.
- ALPHA munkakönyvtár: `software/compose-tetris`; forrása az upstream commit archívuma, nem beágyazott Git-repozitórium.
- Licence: az eredeti [LICENSE](../software/compose-tetris/LICENSE) megőrizve.
- A fő alkalmazáskód és erőforrások változatlanok. Eltérések: `app/build.gradle` tesztbeállításai és JUnit/coroutines-test függőségei, a hozzáadott `app/src/test` tesztek.
- GitLab commitazonosító: **nincs új commit**, a felhasználó kérésére nem commitoltunk/pusholtunk. Az upstream SHA és a forrásfájlok hash-listája azonosítja a most vizsgált állapotot.

A [forrásmanifest](../reports/environment/source-manifest.json) fájlonként azonosítja a vizsgált main forrásokat. Az eredeti repo HEAD-jével összehasonlított main fájlok között nincs eltérés.

## Igazolt eszközök és build

| Eszköz / beállítás | Tényleges érték |
|---|---|
| Gép | Windows, helyi munkakörnyezet |
| JDK | Eclipse Temurin 17.0.20.1+1 |
| Gradle | 7.3-rc-1, a projekt wrapperjének megfelelően |
| Android Gradle Plugin / Lint | 7.1.2 |
| Kotlin | 1.6.10 |
| Compose | 1.1.1 |
| compileSdk / targetSdk / minSdk | 32 / 32 / 21 |
| Android platform / build-tools | API 32 / 30.0.3 |
| ADB platform-tools | 37.0.1 |
| Detekt | CLI 1.23.8; alapkonfiguráció, típusfeloldás nélkül |
| Tesztfüggőségek | JUnit 4.13.2, kotlinx-coroutines-test 1.6.0 |

A hordozható eszközök `.tools/` alatt vannak és nem kerülnek verziókövetésbe. Nem módosítottuk a gép rendszerkörnyezeti beállításait. Beszerzési URL-ek és archívum SHA-256 értékek: [tool-manifest.json](../reports/environment/tool-manifest.json). Az Android Studio és Android Emulator működését nem igazoltuk.

Sikeres feladatok: `:app:testDebugUnitTest`, `:app:lintDebug`, `:app:assembleDebug`. Nyers [buildnapló](../reports/environment/gradle-verification.log), [eredményösszesítő](../reports/run-summary.json).

## APK

Elkészült helyi debug APK: `software/compose-tetris/app/build/outputs/apk/debug/app-debug.apk` (a buildkönyvtár ignorált).

**SHA-256:** `297f5c20cbff2a50392106642d6a79bba883202d9e262fc68f7507c85c93d2ee`

Egy új build hash-e változhat. A csapat által ténylegesen telepített APK hash-ét a funkcionális jegyzőkönyvbe be kell írni. A build sikere nem igazolja a telepítést és indítást.

## Androidos végrehajtási környezet

Az ADB eszközlistája a méréskor üres: [devices.txt](../reports/environment/devices.txt). Emiatt telepítés, indítás, hang, Android-életciklus és kézi funkcionális végrehajtás **nem történt**.

Terv: elsődleges Pixel 2 / API 35, másodlagos API 21. Ezek jelenleg tervezett profilok, nem futtatási bizonyítékok. A virtuális vagy fizikai eszköz tényleges azonosítója, Android-verziója és felbontása a végrehajtás előtt rögzítendő.

```powershell
.\.tools\android-sdk\platform-tools\adb.exe devices -l
.\.tools\android-sdk\platform-tools\adb.exe install -r .\software\compose-tetris\app\build\outputs\apk\debug\app-debug.apk
.\.tools\android-sdk\platform-tools\adb.exe shell am start -n com.jetgame.tetris/.MainActivity
```

Több eszköznél minden parancshoz `-s ESZKOZAZONOSITO` kell. A tesztesetek Androidon való tényleges futtatását a csapat dokumentálja; JUnit-eredményből erre nem következtetünk.
