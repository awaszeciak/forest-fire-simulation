# Forest Fire Simulation
Projekt symulujący rozprzestrzenianie się pożaru lasu w czasie, za pomocą modelu SIR.

## Opis
Symulacja przedstawia las jako siatkę komórek, które mogą znajdować się w jednym z trzech stanów:
- **SUSPECTED** - drzewo zdrowe (niepłonące)
- **BURNING** - drzewo płonące
- **DEAD** - drzewo spalone
  
W każdym kroku symulacji:
- płonące drzewa mogą się wypalić,
- ogień może przenieść się na sąsiednie drzewa,
- wpływ na rozprzestrzenianie ma kierunek i siła wiatru.

## Model wiatru
Wiatr modelowany jest jako:
- kąt (0-359 stopni) - kierunek wiatru,
- prędkość (m/s) - siła wiatru.
  
Wpływ wiatru obliczany jest na podstawie zgodności kierunku wiatru z kierunkiem rozprzestrzeniania się ognia.

## Konfiguracja
Parametry symulacji wczytywane są z pliku: config.properties.

