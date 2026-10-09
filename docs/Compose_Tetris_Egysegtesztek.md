# Compose Tetris – Egység- és integrációs tesztek

**Azonosító:** ALPHA-TETRIS-UT-002 · **Verzió:** 1.0 · **Dátum:** 2026-10-09

## Hatókör és eredmény

A meglévő játéklogikát vizsgáló JUnit 4 tesztek az [app tesztkönyvtárában](../software/compose-tetris/app/src/test/java/com/jetgame/tetris/logic/) találhatók. A program játékszabályait nem módosítottuk. **87 futás, 87 sikeres, 0 sikertelen, 0 kihagyott.** Ebből 86 projektellenőrzés, 1 az eredeti `ExampleUnitTest` (2+2) infrastruktúrapróba.

| Osztály | Futások | Mit ellenőriz? |
|---|---:|---|
| SpiritTest | 11 | Mozgatás, forgatás, pályahatár, ütközés, üres alakzat, hét alakzatot tartalmazó tartalék |
| ShapeContractTest | 28 | Mind a hét alakzat 4 egyedi cellája, négy forgatás, érvényes orientáció, Brick-konverzió |
| BrickTest | 5 | Téglalap, üres tartomány, eltolás, alakzatkonverzió |
| ScoreTest | 5 | 0–4 törölt sor pontértékei: 0/100/300/700/1500 |
| ViewStateLevelTest | 11 | 0, 19, 20, 21, 39, 40, 179, 180, 199, 200, 999 sor |
| ViewStateTest | 4 | Kezdeti állapot, tartalék, következő elem, állapotjelzők |
| LineClearingIntegrationTest | 9 | A valódi ViewModel sortörlése: 0–4 sor, egymástól távoli sorok, felette/alatta maradó elemek |
| GameActionIntegrationTest | 13 | Indítás, szünet/folytatás, némítás, mozgatás, forgatás, ejtés, tick, lerakás, sortörlés, game-over/reset |
| ExampleUnitTest | 1 | Eredeti infrastruktúraellenőrzés |
| **Összesen** | **87** | **64 egység + 22 integráció + 1 infrastruktúra** |

A parametrizált sorok külön futások, nem 87 külön tesztmetódus. A szint 10-es korlátja és a 12 pontos lerakási bónusz a forrásból származó, még csapat által elfogadandó tesztbázis. A README-ben megadott 100/300/700/1500 sortörlési pontok ettől elkülönülnek.

## Fixture, technika és korlátok

A tiszta logikai tesztek előkészített koordinátalistákat használnak. A ViewModel-tesztek a valódi `dispatch` és `updateBricks` végrehajtását ellenőrzik; a privát állapothoz és sortörlő metódushoz reflectiont használnak, hogy a termékkód változatlan maradjon. Egy belső átnevezés ezért a tesztsegéd módosítását igényli. Az alakzattartalék ellenőrzése nem statisztikai egyenletességmérés.

Az integrációs tesztek a Main dispatchert tesztdispatcherre cserélik, majd visszaállítják; a valódi Default dispatcher eredményére legfeljebb 5 másodpercet várnak. A teszt végén a ViewModel coroutine scope-ját leállítják. A sound fixture némított, az Android stubok `returnDefaultValues` beállítással futnak. **A hang, UI, Android-életciklus és emulátor sebessége ezzel nem igazolt.**

Az első futás egy hibás tesztassertiont talált: a forgatásból kapott -0.0 és +0.0 az Offset bit szerinti egyenlőségében különbözött. Az ellenőrzést koordinátánként, numerikusan javítottuk; a játék forrását nem változtattuk emiatt. A megőrzött végső nyers eredmény a javított csomag sikeres futása.

## Futtatás és bizonyíték

Szükséges: JDK 17, Android SDK platform 32 és build-tools 30.0.3; a projekt wrapperje Gradle 7.3-rc-1, Kotlin 1.6.10, AGP 7.1.2, Compose 1.1.1. JUnit 4.13.2 és kotlinx-coroutines-test 1.6.0 rögzített tesztfüggőség.

```powershell
$env:JAVA_HOME = 'a JDK 17 mappája'
$env:ANDROID_HOME = 'az Android SDK mappája'
.\software\compose-tetris\gradlew.bat --project-dir software\compose-tetris :app:testDebugUnitTest :app:lintDebug :app:assembleDebug --no-daemon --console=plain
```

Az összegyűjtött [JUnit XML](../reports/unit/xml/), [HTML eredmény](../reports/unit/html/index.html), [gépileg összesített eredmény](../reports/run-summary.json) és [buildnapló](../reports/environment/gradle-verification.log) együtt igazolja a futást. A [Verify-Mk2.ps1](../tools/Verify-Mk2.ps1) a Detektet és az eredménymentést is indítja. Nem számol coverage-et, és nem hoz létre Git-commitot vagy GitLab-bejegyzést.
