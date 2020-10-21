# Sémantický slovník pojmů

Sémantický slovník pojmů je nástrojem určeným pro popis otevřených datových sad publikovaných veřejnou správou ČR. Architektura slovníku je popsána v této [koncepci](https://opendata.gov.cz/dokumenty:s%C3%A9mantick%C3%BD-slovn%C3%ADk-pojm%C5%AF:start).

## Struktura
Hlavní části projektu jsou složky:
* `content` - samotný obsah slovníku
* `docs` - dokumentace
* `validation` - SHACL validátor korektnosti slovníku, podpůrné skripty

Obsah slovníku se nachází ve složce 'slovník'. Ta obsahuje podsložky s jednotlivými slovníky ve formátu [TTL](https://www.w3.org/TR/turtle/),
dle architektury popsané v [Koncepci sémantického slovníku pojmů](https://opendata.gov.cz/_media/dokumenty:s%C3%A9mantick%C3%BD-slovn%C3%ADk-pojm%C5%AF:c1v2d1_n%C3%A1vrh_koncepce_s%C3%A9mantick%C3%A9ho_slovn%C3%ADku_pojm%C5%AF.pdf))
* `z-sgov` - složka s aktuální verzí základního slovníku
* `v-sgov` - složka s aktuální verzí slovníku veřejného sektoru
* `g-sgov` - kořenová složka pro generické slovníky
* `l-sgov` - kořenová složka pro legislativní slovníky
* `a-sgov` - kořenová složka pro agendové slovníky
* `d-sgov` - kořenová složka pro datové slovníky

## Přístup k slovníku
Tento GIT repozitář je zdrojem pro následující aplikace nad slovníkem:
* [SPARQL Endpoint](https://slovník.gov.cz/sparql)
* [Explorátor propojených dat](https://slovník.gov.cz/veřejný-sektor/pojem/člověk)
* [Prohlížeč](https://slovník.gov.cz/prohlížeč) - fasetový vyhledávač pojmů napříč slovníky

Tento repozitář je udržován v rámci projektu OPZ č. CZ.03.4.74/0.0/0.0/15_025/0004172.
![Evropská unie - Evropský sociální fond - Operační program Zaměstnanost](https://data.gov.cz/images/ozp_logo_cz.jpg)
